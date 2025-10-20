package com.soldesk.festival.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.soldesk.festival.dao.BoardDAO;
import com.soldesk.festival.dto.BoardDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
    
    private final BoardDAO boardDAO;

    public List<BoardDTO> AllBoards(){
        return boardDAO.AllBoards();
    }
}
