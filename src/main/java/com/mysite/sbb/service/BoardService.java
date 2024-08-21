package com.mysite.sbb.service;

import com.mysite.sbb.dto.BoardDto;
import com.mysite.sbb.entity.Board;
import com.mysite.sbb.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    // Save or update an board
    public BoardDto saveOrUpdateItem(BoardDto boardDto) {
        Board board;

        if (boardDto.getId() != null) {
            Optional<Board> boardOpt = boardRepository.findById(boardDto.getId());
            if (boardOpt.isPresent()) {
                board = boardOpt.get();
                board.setModifyDate(LocalDateTime.now()); // Update modifyDate if the board exists

                // Update other fields
                board.setTitle(boardDto.getTitle());
                board.setWriter(boardDto.getWriter());
                board.setBoardContent(boardDto.getBoardContent());
                board.setFilename(boardDto.getFilename());
                board.setFilepath(boardDto.getFilepath());

                // Keep existing createDate
                board.setCreateDate(board.getCreateDate());
            } else {
                throw new RuntimeException("Item not found for update");
            }
        } else {
            board = boardDto.toEntity();
            board.setCreateDate(LocalDateTime.now()); // Set createDate for new board
        }

        Board savedItem = boardRepository.save(board);
        return BoardDto.fromEntity(savedItem);
    }

    // Save or update an board with a file
    public BoardDto saveOrUpdateItemWithFile(BoardDto boardDto, MultipartFile file) throws IOException {
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);

        Board board;
        if (boardDto.getId() != null) {
            Optional<Board> boardOpt = boardRepository.findById(boardDto.getId());
            if (boardOpt.isPresent()) {
                board = boardOpt.get();
                board.setModifyDate(LocalDateTime.now()); // Update modifyDate if the board exists

                // Update other fields
                board.setTitle(boardDto.getTitle());
                board.setWriter(boardDto.getWriter());
                board.setBoardContent(boardDto.getBoardContent());
                board.setFilename(fileName);
                board.setFilepath("/files/" + fileName);

                // Keep existing createDate
                board.setCreateDate(board.getCreateDate());
            } else {
                throw new RuntimeException("Item not found for update");
            }
        } else {
            board = boardDto.toEntity();
            board.setCreateDate(LocalDateTime.now()); // Set createDate for new board
            board.setFilename(fileName);
            board.setFilepath("/files/" + fileName);
        }

        Board savedItem = boardRepository.save(board);
        return BoardDto.fromEntity(savedItem);
    }

    // Find board by ID
    public BoardDto findItemById(Long id) {
        return boardRepository.findById(id)
                .map(BoardDto::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board ID: " + id));
    }

    // 최신순으로 모든 아이템 가져오기
    public Page<BoardDto> findAllItems(Pageable pageable) {
        Page<Board> boardPage = boardRepository.findAllByOrderByCreateDateDesc(pageable);
        List<BoardDto> boardDtoList = boardPage.stream()
                .map(BoardDto::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(boardDtoList, pageable, boardPage.getTotalElements());
    }

    // 키워드로 검색하고 최신순으로 정렬된 결과 가져오기
    public Page<BoardDto> findAllItemsByKeyword(String keyword, Pageable pageable) {
        Page<Board> boards = boardRepository.findByTitleContainingOrderByCreateDateDesc(keyword, pageable);
        return boards.map(BoardDto::fromEntity);
    }

    // Delete an board by ID
    public void deleteItemById(Long id) {
        boardRepository.deleteById(id);
    }

    // Get board by ID
    public Optional<Board> getItem(Long id) {
        return boardRepository.findById(id);
    }
}

