package com.todolist.user;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="tb_users")
@NoArgsConstructor
@AllArgsConstructor
public class UserModel implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

   @Column(unique=true)
    private String username;
    private String name;
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at", unique=true)
    private LocalDateTime createdAt = LocalDateTime.now();
}
