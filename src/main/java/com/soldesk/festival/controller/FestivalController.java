package com.soldesk.festival.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.soldesk.festival.dto.ChatRoomDTO;
import com.soldesk.festival.dto.FestivalCategoryDTO;
import com.soldesk.festival.dto.FestivalDTO;
import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.dto.RegionDTO;
import com.soldesk.festival.service.ChatService;
import com.soldesk.festival.service.FestivalService;
import com.soldesk.festival.service.FileUploadService;
import com.soldesk.festival.service.SegFestivalService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FestivalController {

    private final SegFestivalService segFestivalService;
    private final FestivalService festivalService;
    private final ChatService chatService;
    private final FileUploadService fileUploadService;
    
    @GetMapping("/festivalInfo")
    public String info(@RequestParam("id") int id, Model model, @SessionAttribute(name = "loginMember", required = false) MemberDTO loginMember){
        
        FestivalDTO festival = festivalService.getFestival(id);
        model.addAttribute("festival", festival);

        ChatRoomDTO chatRoom = chatService.getChatRoomById(id);
        model.addAttribute("chatRoom", chatRoom);

        if(loginMember != null){
            model.addAttribute("loginMember", loginMember);
            model.addAttribute("loggedIn", true);
        }else{
            loginMember = new MemberDTO();
            model.addAttribute("loginMember", loginMember);
            model.addAttribute("loggedIn", false);
        }

        return "festival/festival";
    }

    @GetMapping("/festivalReg")
    public String register(@ModelAttribute("festival") FestivalDTO festivalDTO,Model model){
        List<FestivalCategoryDTO> category = festivalService.getCategory();
        List<RegionDTO> regions = festivalService.getRegion();
        
        model.addAttribute("regions", regions);
        model.addAttribute("category", category);

        return "festival/project_plan";
    }

    @PostMapping("/festival/regSubData")
    public String regFestival(@ModelAttribute("festival") FestivalDTO festivalDTO, @RequestParam("upload_file") MultipartFile file){
        
        if(!file.isEmpty()){
            String imageUrl;
            try {
            System.out.println("파일 이름: " + file.getOriginalFilename());
            System.out.println("파일 크기: " + file.getSize());
            imageUrl = fileUploadService.uploadToFirebase(file);
            festivalDTO.setFestival_img_path(imageUrl);
            
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        segFestivalService.insertSegFestival(festivalDTO);
        return "redirect:/";
    }


    @PostMapping("/festival/delete")
    public String deleteFestival(@RequestParam("seg_festival_idx") int seg_festival_idx){
        
        FestivalDTO festival = segFestivalService.selectFestival(seg_festival_idx);
        String imgPath = festival.getFestival_img_path();
                if (imgPath != null && !imgPath.isBlank()) {
                     String extractedPath = fileUploadService.extractPathFromUrl(imgPath);
                    fileUploadService.deleteFromFirebase(extractedPath);
                     }
            segFestivalService.deleteFestival(seg_festival_idx);
        return "";
    }
}
