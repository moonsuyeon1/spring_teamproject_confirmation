package com.mysite.sbb.service;


import com.mysite.sbb.dto.CommentDto;
import com.mysite.sbb.entity.Comment;
import com.mysite.sbb.entity.Board;
import com.mysite.sbb.repository.CommentRepository;
import com.mysite.sbb.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository itemRepository;

    public void saveComment(CommentDto commentDto) {
        Optional<Board> itemOptional = itemRepository.findById(commentDto.getBoardId());
        if (itemOptional.isPresent()) {
            Comment comment = commentDto.toEntity(itemOptional.get());
            comment.setCreateDate(LocalDateTime.now()); // Set createDate
            commentRepository.save(comment);
        }
    }

//    display
    public List<Comment> getCommentsByBoardId(Long itemId) {
        return commentRepository.findByBoardId(itemId);
    }

//    edit
    public CommentDto getCommentById(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            return CommentDto.fromEntity(commentOptional.get());
        } else {
            throw new RuntimeException("Comment not found");
        }
    }

    public void updateComment(Long commentId, CommentDto commentDto) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            comment.setContent2(commentDto.getContent2());
            comment.setModifyDate(LocalDateTime.now()); // Update modifyDate
            commentRepository.save(comment);
        } else {
            throw new RuntimeException("Comment not found");
        }
    }

    public void deleteComment(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            commentRepository.delete(optionalComment.get());
        } else {
            throw new IllegalArgumentException("Comment not found");
        }
    }

}

