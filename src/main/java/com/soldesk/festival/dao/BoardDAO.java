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
    } //게시판 모든 게시글 조회

    public List<BoardDTO> selectAllBoardByCategory(int start, int limit, String board_category){
        return boardMapper.selectAllBoardByCategory(start, limit,board_category);
    } //특정 게시판 게시글 조회
    
    public int countBoard(){
        return boardMapper.countBoard();
    } //게시글 카운팅

    public int countBoardBycategory(String board_category){
        return boardMapper.countBoardBycategory(board_category);
    } //특정카테고리 게시글 카운팅

    public void writeProcess(BoardDTO writeBoard){
        boardMapper.writeProcess(writeBoard);
    } //게시글 작성

    public BoardDTO infoProcess(int board_idx){
        return boardMapper.infoProcess(board_idx);
    } //게시글 정보

    public void updateViews(int board_idx){
        boardMapper.updateViews(board_idx);
    } //게시글 조회수 증가

    public void modifyProcess(BoardDTO boardDTO){
        boardMapper.modifyProcess(boardDTO);
    } //게시글 수정

    public void deleteProecess(int board_idx){
        boardMapper.deleteProecess(board_idx);
    } //게시글 삭제

    public int countBoardNow(String date){
        return boardMapper.countBoardNow(date);
    }

    public List<BoardDTO> searchProcessCategory(String keyword, String category,int start , int limit){
        return boardMapper.searchProcessCategory(keyword, category,start,limit);
    }

    public List<BoardDTO> searchProcessAll(String keyword, int start , int limit){
        return boardMapper.searchProcessAll(keyword,start,limit);
    }

    public int countSearchBoardCategory(String keyword , String category){
        return boardMapper.countSearchBoardCategory(keyword, category);
    }

    public int countSearchBOardAll(String keyword){
        return boardMapper.countSearchBOardAll(keyword);
    }

}
