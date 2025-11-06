package com.soldesk.festival.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.soldesk.festival.dto.CommentDTO;
import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.service.CommentService;

import lombok.RequiredArgsConstructor;

/* 
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;    

    @PostMapping("/write")
    public ResponseEntity<Map<String,Object>>writeSubmit(@SessionAttribute("loginMember")MemberDTO memberDTO ,@RequestBody CommentDTO commentDTO){
        
        commentDTO.setMember_idx(memberDTO.getMember_idx());
        
        Map<String,Object> response = new HashMap<String,Object>();
        try {
            commentService.writeProcess(commentDTO);
            response.put("success", true);
            
        } catch (Exception e) {
            response.put("suceess", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CommentDTO>>listForm(@RequestParam("review_idx")int review_idx){
        List<CommentDTO> comments = commentService.listProcess(review_idx);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/modify")
    public ResponseEntity<Map<String,Object>>modifySubmit(@SessionAttribute("loginMember")MemberDTO memberDTO , @RequestBody CommentDTO commentDTO){
        Map<String,Object> response = new HashMap<String,Object>();
        try {
            commentService.modifyProcess(commentDTO);    
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message",e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String,Object>> deleteSubmit(@RequestBody CommentDTO commentDTO){
        Map<String,Object> response = new HashMap<String,Object>();
        try {
            commentService.deleteProcess(commentDTO.getComment_idx());
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

}
*/