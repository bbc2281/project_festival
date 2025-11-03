package com.soldesk.festival.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.soldesk.festival.dto.CommentDTO;

@Mapper
public interface CommentMapper {
    
    @Insert("insert into comment (comment_content, comment_regDate, review_idx, member_idx ) values (#{comment_content}, now(), #{review_idx}, #{member_idx})")
    void writeProcess(CommentDTO commentDTO);

    @Select("select c.comment_content, c.comment_regDate, c.comment_idx, c.review_idx, m.member_nickname " +
            "from comment c join member m on c.member_idx = m.member_idx " +
            "where review_idx = #{review_idx} " + 
            "order by c.comment_regDate ")
    List<CommentDTO> listProcess(int review_idx);

    @Select("select comment_content, comment_regDate, comment_idx, review_idx "
            +"from comment " + 
            " where comment_idx = #{comment_idx} ")
    CommentDTO infoProcess(int comment_idx);

    @Update("update comment set comment_content = #{comment_content} "
            +"where comment_idx = #{comment_idx}")
    void modifyProcess(CommentDTO commentDTO);

    @Delete("delete from comment where comment_idx = #{comment_idx}")
    void deleteProcess(int comment_idx);

}
