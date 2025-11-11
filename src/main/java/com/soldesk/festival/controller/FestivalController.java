package com.soldesk.festival.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.soldesk.festival.dto.FestivalDTO;
import com.soldesk.festival.dto.ReviewDTO;
import com.soldesk.festival.service.FestivalService;
import com.soldesk.festival.service.ReviewService;
import com.soldesk.festival.dto.ChatRoomDTO;
import com.soldesk.festival.dto.CompanyDTO;
import com.soldesk.festival.dto.FestivalCategoryDTO;
import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.dto.RegionDTO;
import com.soldesk.festival.service.ChatService;
import com.soldesk.festival.service.FavoriteService;
import com.soldesk.festival.service.FileUploadService;
import com.soldesk.festival.service.SegFestivalService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FestivalController {

    private final SegFestivalService segFestivalService;
    private final FestivalService festivalService;
    private final ReviewService reviewService;
    private final ChatService chatService;
    private final FileUploadService fileUploadService;
    private final FavoriteService favoriteService;

    @GetMapping("/festivalInfo")
    public String info(@RequestParam("id") int id, Model model, HttpSession session){
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        CompanyDTO companyMember = (CompanyDTO) session.getAttribute("companyMember");

        FestivalDTO festival = festivalService.getFestival(id);
        model.addAttribute("festival", festival);

        //리뷰 추가 
        int festivalIdx = festival.getFestival_idx();
        List<ReviewDTO> reviewList = reviewService.selectAllReviews(festivalIdx);
        model.addAttribute("reviews", reviewList);
        
        ChatRoomDTO chatRoom = chatService.getChatRoomById(id);
        model.addAttribute("chatRoom", chatRoom);

        boolean exist = false;
        if (loginMember != null) {
            exist = favoriteService.existsFavoriteByMember(loginMember.getMember_idx(), festivalIdx);
            
            model.addAttribute("loginMember", loginMember);
            model.addAttribute("loggedIn", true);
        }else if (companyMember != null) {
            exist = favoriteService.existsFavoriteByCompany(festivalIdx, companyMember.getCompany_idx());
            loginMember = new MemberDTO();
            model.addAttribute("loginMember", loginMember);
            model.addAttribute("loggedIn", false);
        } else {
            loginMember = new MemberDTO();
            model.addAttribute("loginMember", loginMember);
            model.addAttribute("loggedIn", false);
        }
        System.out.println(exist);
        model.addAttribute("isFavorite", exist);

        return "festival/info";
    }

    @GetMapping("/festival/search")
    public String searchFestival(){

        return "festival/search";
    }

    @GetMapping("/festival/registe")
    public String registe(@ModelAttribute("festival") FestivalDTO festivalDTO,Model model){
        List<FestivalCategoryDTO> category = festivalService.getCategory();
        List<RegionDTO> regions = festivalService.getRegion();
        
        model.addAttribute("regions", regions);
        model.addAttribute("category", category);

        return "festival/segFestival";
    }

    @GetMapping("/festival/permit")
    public String permit(@RequestParam("festival_idx") int festival_idx){
        
        FestivalDTO festival = segFestivalService.selectFestival(festival_idx);
        festivalService.insertFestival(festival);

        segFestivalService.deleteFestival(festival_idx);
        return "redirect:/admin/proposal";
    }


    @PostMapping("/festival/regSubData")
    public String regFestival(@ModelAttribute("festival") FestivalDTO festivalDTO, @RequestParam("upload_file") MultipartFile file, @SessionAttribute("companyMember") CompanyDTO companyMember){
        
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

        
        segFestivalService.insertSegFestival(festivalDTO, companyMember);
        return "redirect:/";
    }


    @GetMapping("/festival/delete")
    public String deleteFestival(@RequestParam("festival_idx") int festival_idx){
        
        festivalService.deleteFestival(festival_idx);

        return "redirect:/admin/festival";
    }
}
