package com.soldesk.festival.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.soldesk.festival.dto.CommentDTO;
import com.soldesk.festival.mapper.CommentMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentDAO {
    
    private final CommentMapper commentMapper;

    public void writeProcess(CommentDTO commentDTO){
        commentMapper.writeProcess(commentDTO);
    }

    public List<CommentDTO> listProcess(int review_idx){
        return commentMapper.listProcess(review_idx);
    }

    public CommentDTO infoProcess(int comment_idx){
        return commentMapper.infoProcess(comment_idx);
    }

    public void modifyProcess(CommentDTO commentDTO){
        commentMapper.modifyProcess(commentDTO);
    }

    public void deleteProcess(int comment_idx){
        commentMapper.deleteProcess(comment_idx);
    }

    public int countCommentByMember(int idx){
        return commentMapper.countCommentByMember(idx);
    }
}
