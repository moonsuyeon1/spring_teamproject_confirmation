package com.mysite.sbb.dto;

import com.mysite.sbb.entity.Comment;
import com.mysite.sbb.entity.Board;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoardDto {
    private Long id;
    private String title;
    private String writer;

    private LocalDateTime createDate; // Added createDate field
    private LocalDateTime modifyDate; // Existing modifyDate field
    private String boardContent;
    private String filename;
    private String filepath;
    private MultipartFile file;
    private List<Comment> answerList;

    // Convert DTO to entity
    public Board toEntity() {
        Board item = new Board();
        item.setId(this.id);
        item.setTitle(this.title);
        item.setWriter(this.writer);

        item.setCreateDate(this.createDate); // Set createDate in the entity
        item.setModifyDate(this.modifyDate); // Set modifyDate in the entity
        item.setBoardContent(this.boardContent);
        item.setFilename(this.filename);
        item.setFilepath(this.filepath);

        return item;
    }

    // Convert entity to DTO
    public static BoardDto fromEntity(Board item) {
        BoardDto dto = new BoardDto();
        dto.setId(item.getId());
        dto.setTitle(item.getTitle());
        dto.setWriter(item.getWriter());

        dto.setCreateDate(item.getCreateDate()); // Get createDate from the entity
        dto.setModifyDate(item.getModifyDate()); // Get modifyDate from the entity
        dto.setBoardContent(item.getBoardContent());
        dto.setFilename(item.getFilename());
        dto.setFilepath(item.getFilepath());
        dto.setAnswerList(item.getComments());
        return dto;
    }
}
