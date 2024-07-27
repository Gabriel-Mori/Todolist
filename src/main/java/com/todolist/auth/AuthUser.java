package com.todolist.auth;


import at.favre.lib.crypto.bcrypt.BCrypt;
import com.todolist.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AuthUser extends OncePerRequestFilter {

    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var serveletPath = request.getServletPath();

        if(serveletPath.startsWith("/users/tasks")) {
            // pegar autenticação de usuario
            var auth = request.getHeader("Authorization");
            var userAuth =  auth.substring("Basic".length()).trim();

            byte[] decoded = Base64.getDecoder().decode(userAuth);
            var authToken = new String(decoded);

            String[] credentials = authToken.split(":");
            String username = credentials[0];
            String password = credentials[1];


            //Validar usuario
            var user = this.userRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401, "Unauthorized");
            } else{
                //Validar senha
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(),user.getPassword());
                if(passwordVerify.verified){
                    request.setAttribute("userId", user.getId());
                    filterChain.doFilter(request, response);
                } else{
                    response.sendError(401, "Unauthorized");
                }


            }
        } else{
            filterChain.doFilter(request, response);
        }
    }
}
