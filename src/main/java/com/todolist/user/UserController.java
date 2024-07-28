package com.todolist.user;


import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000/")
public class UserController {

    private final UserRepository repository;

    @PostMapping
    public ResponseEntity registerUser(@RequestBody  UserModel userModel) {
        var userExists = this.repository.findByUsername(userModel.getUsername());

        if(userExists != null ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe");
        }

        var passwordHash =  BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHash);


       var userCreated = this.repository.save(userModel);

       return ResponseEntity.ok(userCreated);
    }
}
