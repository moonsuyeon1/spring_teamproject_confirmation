package com.mysite.sbb.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String writer;

    private LocalDateTime createDate; // Added createDate field
    private LocalDateTime modifyDate; // Existing modifyDate field
    private String boardContent;
    private String filename;
    private String filepath;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments;
}
