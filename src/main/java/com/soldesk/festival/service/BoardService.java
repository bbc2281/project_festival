package com.soldesk.festival.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.soldesk.festival.dao.BoardDAO;
import com.soldesk.festival.dto.BoardDTO;
import com.soldesk.festival.dto.BoardPageDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
    
    private final BoardDAO boardDAO;



    public List<BoardDTO> selectAllBoard(int page){
        int start = (page - 1) *10;
		int limit = 10 ; 
        return boardDAO.selectAllBoard(start,limit);
    }

    public List<BoardDTO> selectAllBoardByCategory(int page, String board_category){
        int start = (page - 1) * 10 ;
        int limit = 10;
        return boardDAO.selectAllBoardByCategory(start,limit, board_category);
    }

    public BoardPageDTO getPageDTO(int currentPage) {
		int boardCount = boardDAO.countBoard();
		BoardPageDTO pageDTO = new BoardPageDTO(boardCount , currentPage);
    	return pageDTO;
	}

    public BoardPageDTO getPageDTOByCategory(int currentPage , String board_category) {
		int boardCount = boardDAO.countBoardBycategory(board_category);
		BoardPageDTO pageDTO = new BoardPageDTO(boardCount , currentPage);
    	return pageDTO;
	}

    public void writeProcess(BoardDTO writeBoard){
        
        boardDAO.writeProcess(writeBoard);
    }

    public BoardDTO infoProcess(int board_idx){
        return boardDAO.infoProcess(board_idx);
    }

    public void updateViews(int board_idx){
        boardDAO.updateViews(board_idx);
    }

    public void modifyProcess(BoardDTO boardDTO){
        boardDAO.modifyProcess(boardDTO);
    }

    public void deleteProecess(int board_idx){
        boardDAO.deleteProecess(board_idx);
    }

}
