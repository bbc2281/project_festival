package com.soldesk.festival.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.soldesk.festival.dto.BoardDTO;
import com.soldesk.festival.mapper.BoardMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardDAO {
    
    private final BoardMapper boardMapper;

    public List<BoardDTO> AllBoards(){
        return boardMapper.selectAllBoard();
    }

}
