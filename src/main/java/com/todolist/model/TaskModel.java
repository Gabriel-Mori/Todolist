package com.todolist.model;


import com.todolist.user.UserModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="tb_task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskModel implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String priority;

    @Column(nullable = false)
    private Boolean completed;

    private LocalDateTime startsAt;

    private LocalDateTime endsAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();


    private UUID userId;

}
