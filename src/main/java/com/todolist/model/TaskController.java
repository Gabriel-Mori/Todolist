package com.todolist.model;

import com.todolist.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;


    @PostMapping("users/tasks")
    public ResponseEntity createTask(@RequestBody TaskModel task, HttpServletRequest request){
        var userId = request.getAttribute("userId");
        task.setUserId((UUID) userId);

        var currentDate = LocalDateTime.now();
        if(currentDate.isAfter((task.getStartsAt())) || currentDate.isAfter(task.getEndsAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data deve ser maior que a data atual");
        }

        if(task.getStartsAt().isAfter((task.getEndsAt())) || task.getEndsAt().isBefore(task.getStartsAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data inválida");
        }

        TaskModel createTask = this.taskRepository.save(task);

        return ResponseEntity.ok(createTask);
    }


    @GetMapping("users/tasks")
    public ResponseEntity<List<TaskModel>> getAllTasks(HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        List<TaskModel> tasks = this.taskRepository.findByUserId((UUID) userId);
        return ResponseEntity.ok(tasks);
    }


    @PutMapping("users/tasks/{id}")
    public ResponseEntity updateTask(@PathVariable  UUID id, @RequestBody TaskModel taskModel, HttpServletRequest request){
        var task = this.taskRepository.findById(id).orElse(null);
        var userId = request.getAttribute("userId");

        if(task == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task nao encontrado");
        }

        if(!task.getUserId().equals(userId)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Utils.copyNullProperties(taskModel, task);
        var taskUpdated = this.taskRepository.save(task);
        return ResponseEntity.ok(taskUpdated);
    }

}
