package com.mysite.sbb.controller;


import com.mysite.sbb.dto.BoardDto;
import com.mysite.sbb.entity.Comment;
import com.mysite.sbb.service.CommentService;
import com.mysite.sbb.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class BoardController {

    @Autowired
    private BoardService itemService;
    @Autowired
    private CommentService commentService;

    // Show the form to create a new item
    @GetMapping("/item/new")
    public String createItemForm(Model model) {
        model.addAttribute("boardDto", new BoardDto());
        return "createBoard";
    }

//    // Handle the form submission for creating a new item
//    @PostMapping("/item")
//    public String createItem(@ModelAttribute boardDto boardDto) {
//        itemService.saveOrUpdateItem(boardDto);
//        return "redirect:/board";
//    }

    @PostMapping("/item")
    public String boardWritePro(@ModelAttribute BoardDto boardDto,
                                @RequestParam("file") MultipartFile file,
                                Model model) throws Exception {

        BoardDto savedItem;
        if (!file.isEmpty()) {
            savedItem = itemService.saveOrUpdateItemWithFile(boardDto, file);
        } else {
            savedItem = itemService.saveOrUpdateItem(boardDto);
        }

        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/item/" + savedItem.getId());

        return "message";
    }


    // Display a single item
    @GetMapping("/item/{id}")
    public String getItem(@PathVariable Long id, Model model) {
        BoardDto boardDto = itemService.findItemById(id);

        // Fetch and set comments
        List<Comment> comments = commentService.getCommentsByBoardId(id);
        boardDto.setAnswerList(comments);

        model.addAttribute("boardDto", boardDto);

        return "boardDetail";
    }

    // Display the list of items
//    @GetMapping("/board")
//    public String getItems(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
//        List<boardDto> boardDtoList = itemService.findAllItems();
//        model.addAttribute("boardDtoList", boardDtoList);
//        return "viewItems";
//    }

    @GetMapping("/board")
    public String getItems(Model model,
                           @RequestParam(name = "searchKeyword", required = false) String searchKeyword,
                           @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {
        Page<BoardDto> boardDtoPage;

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            boardDtoPage = itemService.findAllItemsByKeyword(searchKeyword, pageable);
        } else {
            boardDtoPage = itemService.findAllItems(pageable);
        }

        int nowPage = boardDtoPage.getNumber() + 1;
        int totalPages = boardDtoPage.getTotalPages();
        int startPage = Math.max(1, nowPage - 4);
        int endPage = Math.min(nowPage + 5, totalPages);

        model.addAttribute("boardDtoPage", boardDtoPage);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("searchKeyword", searchKeyword);

        return "toolboard";
    }


    // Show the form to edit an existing item
    @GetMapping("/item/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = itemService.findItemById(id);
        model.addAttribute("boardDto", boardDto);
        return "modifyBoard"; // Reuse the createItem template for editing
    }

    @PostMapping("/item/{id}")
    public String updateItem(@PathVariable("id") Long id,
                             @ModelAttribute BoardDto boardDto,
                             @RequestParam("file") MultipartFile file) throws IOException {

        // 파일 업로드 처리
        if (!file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            // 파일을 서버에 저장하거나 추가적인 처리를 수행할 수 있음
            // 예시: 서비스 계층으로 파일 전달
            itemService.saveOrUpdateItemWithFile(boardDto, file);
        } else {
            boardDto.setId(id); // Ensure the ID is set for the update
            itemService.saveOrUpdateItem(boardDto);
//            itemService.write(boardDto.toEntity());
        }

        return "redirect:/item/" + id;
    }

    // DELETE mapping to handle item deletion
    @GetMapping("/item/{id}/delete")
    public String deleteItem(@PathVariable Long id) {
        itemService.deleteItemById(id);
        return "redirect:/board"; // Redirect to the items list after deletion
    }



}
