package com.soldesk.festival.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.soldesk.festival.dao.CommentDAO;
import com.soldesk.festival.dto.CommentDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentDAO commentDAO;

    public void writeProcess(CommentDTO commentDTO){
        commentDAO.writeProcess(commentDTO);
    }

    public List<CommentDTO> listProcess(int review_idx){
        return commentDAO.listProcess(review_idx);
    }

    public CommentDTO infoProcess(int comment_idx){
        return commentDAO.infoProcess(comment_idx);
    }

    public void modifyProcess(CommentDTO commentDTO){
        commentDAO.modifyProcess(commentDTO);
    }
    
    public void deleteProcess(int comment_idx){
        commentDAO.deleteProcess(comment_idx);
    }
    
    public List<CommentDTO> countCommentByMember(int idx){
        return commentDAO.countCommentByMember(idx);
    }
}
