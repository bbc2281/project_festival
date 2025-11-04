package com.soldesk.festival.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.soldesk.festival.dto.BoardDTO;
@Mapper
public interface BoardMapper {

    @Select("select b.board_idx, m.member_idx, b.board_title, b.board_regDate, b.board_views, b.board_category, b.board_content, m.member_nickname " + 
            "from board b LEFT join member m on b.member_idx = m.member_idx " +  
            "order by board_idx desc " + 
            "limit #{start}, #{limit}")
            //맴버 테이블 연동 후 레프트 조인 풀기
    List<BoardDTO> selectAllBoard(@Param("start") int start ,@Param("limit") int limit);

    @Select("select b.board_idx, m.member_idx, b.board_title, b.board_regDate, b.board_views, b.board_category, b.board_content, m.member_nickname " + 
            "from board b LEFT join member m on b.member_idx = m.member_idx " +  
            "order by board_idx desc ")
    List<BoardDTO> selectAllBoardNoLimits();
    
    @Select("select b.board_idx, m.member_idx, b.board_title, b.board_regDate, b.board_views, b.board_category, b.board_content, m.member_nickname " + 
            "from board b LEFT join member m on b.member_idx = m.member_idx " +
            "where board_category = #{board_category} "+  
            "order by board_idx desc " + 
            "limit #{start}, #{limit} " )
            //맴버 테이블 연동 후 레프트 조인 풀기
    List<BoardDTO> selectAllBoardByCategory(@Param("start") int start ,@Param("limit") int limit , @Param("board_category") String board_category);

    @Select("SELECT COUNT(*) FROM board")
    int countBoard();

    @Select("SELECT COUNT(*) FROM board where board_category=#{board_category}")
    int countBoardBycategory(String board_category);


    @Insert("insert into board (board_title,board_content,board_regDate,board_category,board_img_path,member_idx) values (#{board_title},#{board_content},now(),#{board_category},#{board_img_path},#{member_idx})")
    void writeProcess(BoardDTO writeBoard);


    @Select("select b.board_idx, b.board_title, b.board_content, b.board_views, b.board_category, b.board_regDate, b.board_img_path, m.member_idx, m.member_nickname " +
            "from board b LEFT join member m on b.member_idx = m.member_idx "
            +"where b.board_idx = #{board_idx}") 
    BoardDTO infoProcess(int board_idx);

    @Update("update board set board_views = board_views + 1 where board_idx = #{board_idx}")
    void updateViews(int board_idx);

    @Update("update board set board_title=#{board_title} , board_content=#{board_content} , board_img_path=#{board_img_path} , board_category=#{board_category} where board_idx = #{board_idx}")
    void modifyProcess(BoardDTO boardDTO);

    @Delete("delete from board where board_idx=#{board_idx}")
    void deleteProecess(int board_idx);


    
    
} 
    

