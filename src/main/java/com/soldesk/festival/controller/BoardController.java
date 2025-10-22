package com.soldesk.festival.controller;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.soldesk.festival.dto.BoardDTO;
import com.soldesk.festival.dto.BoardPageDTO;
import com.soldesk.festival.service.BoardService;
import com.soldesk.festival.service.FileUploadService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    
    private final BoardService boardService;
    private final FileUploadService fileUploadService;

    @GetMapping("/list")
    public String listForm(Model model, @RequestParam(name = "page" , defaultValue = "1") int page , @RequestParam(name = "board_category" ,defaultValue = "") String board_category ){
        List<String> category = createCategoryList();
        model.addAttribute("categories", category);
        if (page < 1) page = 1;
        List<BoardDTO> boardList;
        BoardPageDTO pageDTO;    
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
    public String writeFrom(Model model , BoardDTO boardDTO){
        boardDTO.setBoard_category(""); 
        model.addAttribute("categories", createCategoryList());
        model.addAttribute("writeBoard", boardDTO);
        return"/board/write";
    }

    @PostMapping("/write")
    public String writeSubmit(@ModelAttribute("writeBoard")BoardDTO boardDTO ,@RequestParam("upload_file")MultipartFile file){
        if(!file.isEmpty()){
            String imageUrl;
            try {
            System.out.println("파일 이름: " + file.getOriginalFilename());
            System.out.println("파일 크기: " + file.getSize());
            imageUrl = fileUploadService.uploadToFirebase(file);
            boardDTO.setBoard_img_path(imageUrl);
            
            } catch (IOException e) {
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
    public String modifySubmit(@ModelAttribute("board_now")BoardDTO boardDTO, @RequestParam("upload_file")MultipartFile file ){      
        BoardDTO modifyBoard = boardService.infoProcess(boardDTO.getBoard_idx());
        if (modifyBoard.getBoard_img_path() != null && !modifyBoard.getBoard_img_path().isBlank()) {
           String imgPath = fileUploadService.extractPathFromUrl(modifyBoard.getBoard_img_path());
        fileUploadService.deleteFromFirebase(imgPath);
        } 
        try {
        if (file != null && !file.isEmpty()) {
            String updateImage = fileUploadService.uploadToFirebase(file);
            boardDTO.setBoard_img_path(updateImage);
        }   
        else{
        boardDTO.setBoard_img_path(modifyBoard.getBoard_img_path());
        }
        
        boardService.modifyProcess(boardDTO);
        } 
        catch (IOException e) {
        e.printStackTrace();
        }
        return"redirect:/board/info?board_idx="+boardDTO.getBoard_idx();
    }

    @PostMapping("/delete")
    public String deleteSubmit(@RequestParam("board_idx")int board_idx){ //int SessionUser -> 로그인 객체랑 연동 예정
        int SessionUser = 1;
        BoardDTO deleteBaord = boardService.infoProcess(board_idx);
        if(deleteBaord.getMember_idx() == SessionUser){
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
