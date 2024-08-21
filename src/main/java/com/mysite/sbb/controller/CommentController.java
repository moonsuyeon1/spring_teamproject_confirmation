package com.mysite.sbb.controller;

import com.mysite.sbb.dto.CommentDto;
import com.mysite.sbb.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/create/{boardId}")
    public String createComment(@PathVariable("boardId") Long boardId, @ModelAttribute CommentDto commentDto) {
        commentDto.setBoardId(boardId);
        commentService.saveComment(commentDto);
        return "redirect:/item/" + boardId;
    }

    @GetMapping("/comment/edit/{commentId}")
    public String editCommentForm(@PathVariable("commentId") Long commentId, Model model) {
        CommentDto commentDto = commentService.getCommentById(commentId);
        model.addAttribute("commentDto", commentDto);
        return "modifyComment"; // Changed to a more descriptive name
    }

    @PostMapping("/comment/update/{commentId}")
    public String updateComment(@PathVariable("commentId") Long commentId, @ModelAttribute CommentDto commentDto) {
        commentService.updateComment(commentId, commentDto);
        return "redirect:/item/" + commentDto.getBoardId();
    }

    @GetMapping("/comment/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId) {
        CommentDto commentDto = commentService.getCommentById(commentId);
        commentService.deleteComment(commentId);
        return "redirect:/item/" + commentDto.getBoardId();
    }
}
