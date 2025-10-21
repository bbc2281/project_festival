package com.soldesk.festival.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.soldesk.festival.dto.BoardDTO;

@Mapper
public interface BoardMapper {
    
    @Select("select * from board")
    List<BoardDTO> selectAllBoard();
}
