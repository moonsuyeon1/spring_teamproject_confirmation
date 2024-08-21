package com.mysite.sbb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    private String content2;

    private LocalDateTime createDate = LocalDateTime.now();

    private LocalDateTime modifyDate;

    // No need to add getters and setters explicitly due to @Data
}
