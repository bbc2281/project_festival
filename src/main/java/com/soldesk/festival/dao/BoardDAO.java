package com.soldesk.festival.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.soldesk.festival.mapper.BoardMapper;
import com.soldesk.festival.dto.BoardDTO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardDAO {
    
    private final BoardMapper boardMapper;

    public List<BoardDTO> selectAllBoard(int start , int limit){
        return boardMapper.selectAllBoard(start , limit);
    }

    public List<BoardDTO> selectAllBoardByCategory(int start, int limit, String board_category){
        return boardMapper.selectAllBoardByCategory(start, limit,board_category);
    }
    
    public int countBoard(){
        return boardMapper.countBoard();
    }

    public int countBoardBycategory(String board_category){
        return boardMapper.countBoardBycategory(board_category);
    }

    public void writeProcess(BoardDTO writeBoard){
        boardMapper.writeProcess(writeBoard);
    }

    public BoardDTO infoProcess(int board_idx){
        return boardMapper.infoProcess(board_idx);
    }

    public void updateViews(int board_idx){
        boardMapper.updateViews(board_idx);
    }

    public void modifyProcess(BoardDTO boardDTO){
        boardMapper.modifyProcess(boardDTO);
    }

    public void deleteProecess(int board_idx){
        boardMapper.deleteProecess(board_idx);
    }

 

}
