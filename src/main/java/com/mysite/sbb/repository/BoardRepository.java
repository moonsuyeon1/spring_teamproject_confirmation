package com.mysite.sbb.repository;

import com.mysite.sbb.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    // 최신순으로 모든 항목을 가져오기
    Page<Board> findAllByOrderByCreateDateDesc(Pageable pageable);

    // 키워드로 검색할 때도 최신순으로 가져오기
    Page<Board> findByTitleContainingOrderByCreateDateDesc(String keyword, Pageable pageable);
}
