package com.soldesk.festival.controller;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.soldesk.festival.dto.BoardDTO;
import com.soldesk.festival.dto.PageDTO;
import com.soldesk.festival.file.Base64MultipartFile;
import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.service.BoardService;
import com.soldesk.festival.service.FileUploadService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    
    private final BoardService boardService;
    private final FileUploadService fileUploadService;

    @GetMapping("/list")
    public String listForm(Model model, @RequestParam(name = "page" , defaultValue = "1") int page , 
        @RequestParam(name = "board_category" ,defaultValue = "") String board_category , HttpSession session){
        
        //나중에 진짜 맴버 객체랑 연동 할 예정입니다.
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMember_idx(1);
        session.setAttribute("loginMember", memberDTO);
        
        List<String> category = createCategoryList();
        model.addAttribute("categories", category);
        if (page < 1) page = 1;
        List<BoardDTO> boardList;
        PageDTO pageDTO;    
        if (board_category == null || board_category.isBlank()) {
            boardList = boardService.selectAllBoard(page);
            pageDTO = boardService.getPageDTO(page);
        }else{
            boardList = boardService.selectAllBoardByCategory(page,board_category);
            pageDTO = boardService.getPageDTOByCategory(page,board_category);            
        }
        model.addAttribute("boards", boardList);
        model.addAttribute("pageDTO", pageDTO);
        return"board/list";
    }

    @GetMapping("/write")
    public String writeFrom(Model model , BoardDTO boardDTO,  @SessionAttribute("loginMember")MemberDTO memberDTO){   
        boardDTO.setBoard_category(""); 
        model.addAttribute("categories", createCategoryList());
        model.addAttribute("writeBoard", boardDTO);
        model.addAttribute("loginMember", memberDTO);
        return"/board/write";
    }

    @PostMapping("/write")
    public String writeSubmit(@ModelAttribute("writeBoard")BoardDTO boardDTO , @SessionAttribute("loginMember")MemberDTO memberDTO ){
        boardDTO.setMember_idx(memberDTO.getMember_idx());
        Document doc = Jsoup.parse(boardDTO.getBoard_content());
        Element img = doc.selectFirst("img[src^=data:]");
            if(img != null){
                String base64 = img.attr("src").split(",")[1];
                byte[] imaByte = Base64.getDecoder().decode(base64);
                String src = img.attr("src"); 
                String mimeType = src.substring(src.indexOf(":") + 1, src.indexOf(";")); 
                String ext = mimeType.split("/")[1]; 
                String fileName = "editor-image." + ext;
                MultipartFile multipartFile = new Base64MultipartFile(imaByte, fileName, mimeType); 
                    try {
                        String firebaseUrl = fileUploadService.uploadToFirebase(multipartFile);   
                        img.attr("src",firebaseUrl);
                        boardDTO.setBoard_img_path(firebaseUrl);
                        boardDTO.setBoard_content(doc.body().html());
                        System.out.println("파일 이름: " + multipartFile.getOriginalFilename());
                        System.out.println("파일 크기: " + multipartFile.getSize());
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        boardService.writeProcess(boardDTO);     
        return "redirect:/board/list";
        }

    @GetMapping("/info")
    public String infoForm(@RequestParam("board_idx")int board_idx , Model model){
        boardService.updateViews(board_idx);        
        BoardDTO infoBoard = boardService.infoProcess(board_idx);
        model.addAttribute("board", infoBoard);
        return"/board/info";
    }    

    @GetMapping("/modify")
    public String modifyForm(@RequestParam("board_idx")int board_idx , Model model){
        System.out.println("보드 idx" + board_idx);
        BoardDTO boardNow = boardService.infoProcess(board_idx);
        model.addAttribute("board_now", boardNow);
        model.addAttribute("categories", createCategoryList());
        return"/board/modify";
    }

@PostMapping("/modify")
public String modifySubmit(@ModelAttribute("board_now") BoardDTO boardDTO) {
    BoardDTO modifyBoard = boardService.infoProcess(boardDTO.getBoard_idx());

    Document doc = Jsoup.parse(boardDTO.getBoard_content());
    Element base64Img = doc.selectFirst("img[src^=data:]"); // 새로 추가/교체된 이미지가 Base64로 온 경우
    boolean hasAnyImgTag = !doc.select("img").isEmpty();    // 에디터 최종 내용에 img 태그 존재 여부

    try {
        if (base64Img != null && base64Img.hasAttr("src")) {
            // 새 이미지가 추가되거나 교체된 경우
            if (modifyBoard.getBoard_img_path() != null) {
                String existingFile = fileUploadService.extractPathFromUrl(modifyBoard.getBoard_img_path());
                if (existingFile != null) {
                    fileUploadService.deleteFromFirebase(existingFile);
                }
            }
            String src = base64Img.attr("src");
            String base64 = src.split(",")[1];
            byte[] imageBytes = Base64.getDecoder().decode(base64);

            String mimeType = src.substring(src.indexOf(":") + 1, src.indexOf(";")); 
            String ext = mimeType.split("/")[1];                                     
            String fileName = "editor-image." + ext;

            MultipartFile multipartFile = new Base64MultipartFile(imageBytes, fileName, mimeType);
            String firebaseUrl = fileUploadService.uploadToFirebase(multipartFile);

            base64Img.attr("src", firebaseUrl);
            boardDTO.setBoard_img_path(firebaseUrl);
            boardDTO.setBoard_content(doc.body().html());

            boardService.modifyProcess(boardDTO);

        } else if (modifyBoard.getBoard_img_path() != null && !hasAnyImgTag) {
            // 기존 이미지가 있었는데, 최종 내용에서 이미지가 완전히 삭제된 경우
            String firebaseFile = fileUploadService.extractPathFromUrl(modifyBoard.getBoard_img_path());
            if (firebaseFile != null) {
                fileUploadService.deleteFromFirebase(firebaseFile);
            }
            boardDTO.setBoard_img_path(null);
            boardDTO.setBoard_content(doc.body().html());

            boardService.modifyProcess(boardDTO);

        } else if (modifyBoard.getBoard_img_path() != null && hasAnyImgTag) {
            // 기존 이미지를 유지하고 내용만 수정하는 경우 
            boardDTO.setBoard_img_path(modifyBoard.getBoard_img_path());
            boardDTO.setBoard_content(doc.body().html());
            boardService.modifyProcess(boardDTO);

        } else {
            // 이미지가 원래도 없고, 최종 내용에도 없음 → 내용만 수정
            boardDTO.setBoard_img_path(null);
            boardDTO.setBoard_content(doc.body().html());

            boardService.modifyProcess(boardDTO);
        }
    } catch (Exception e) {
        e.printStackTrace();     
    }
    return "redirect:/board/info?board_idx=" + boardDTO.getBoard_idx();
}


    @PostMapping("/delete")
    public String deleteSubmit(@RequestParam("board_idx")int board_idx ,@SessionAttribute("loginMember")MemberDTO MemberDTO){
        BoardDTO deleteBaord = boardService.infoProcess(board_idx);
        if(deleteBaord.getMember_idx() == MemberDTO.getMember_idx()){
            String imgPath = deleteBaord.getBoard_img_path();
                if (imgPath != null && !imgPath.isBlank()) {
                     String extractedPath = fileUploadService.extractPathFromUrl(imgPath);
                    fileUploadService.deleteFromFirebase(extractedPath);
                     }
            boardService.deleteProecess(board_idx);
        }
        return "redirect:/board/list";
    }
      
 public List<String> createCategoryList() {
    return List.of("공지사항", "이벤트", "기타");
}

}
