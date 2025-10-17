package com.soldesk.festival.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OpenAiRestController {
    
    private final OpenAiChatModel chatModel;
    private final OpenAiAudioSpeechModel speechModel;
    private final OpenAiAudioTranscriptionModel transcriptionModel;
    
    // #3 어시스턴트 : 대화 이력 유지하며 대화
    @PostMapping("/assistant")
    public ResponseEntity<Map<String, String>> assistantAi(@RequestBody Map<String, String> chat, HttpSession session){
        SystemMessage system = new SystemMessage(
            "공손하게 대답해라" + "시작단어는 오예로 시작한다" + "답변은 성의있게 전 질문과 연관성 파악해서 연관있으면 조금 더 상세하게"
            + "오류 3번이상 체크해서 사실인 내용으로 판단되는것만 가져오도록" + "반말에는 반말로 대답해도 괜찮지만 존댓말엔 존댓말로 대답하도록"
        ); //시스템 메시지
        
        //AssistantMessage assistantMessage = new AssistantMessage(시스템메시지, 질문1, 답변1, 질문2, 답변2);

        List<Message> history = (List<Message>) session.getAttribute("history");

        if(history == null){ 
            history = new ArrayList<>();
            session.setAttribute("history", history);
            history.add(system);
        }//최초요청시

        UserMessage userMessage = new UserMessage(chat.get("message")); //사용자 메세지
        history.add(userMessage);
        
        ChatOptions chatOptions = OpenAiChatOptions.builder()
            .model(OpenAiApi.ChatModel.GPT_5) //지피티 버전설정
            .N(1) //생성할 응답 개수
            .temperature(1.0) // 창의성 조절(0~1) 버전별 디폴드값이 다름
            .build(); //지피티 상세설정

        Prompt prompt = new Prompt(history, chatOptions); //Prompt : 여러 메시지를 묶어서 전달 (history에 담기는 여러 메세지를 묶음)
        String aiResponse = chatModel.call(prompt).getResult().getOutput().getText();
        // getResult : AI 응답 객체
        // getOutput : 출력 메시지 객체
        // getText : String 반환 메서드
        
        AssistantMessage assistant = new AssistantMessage(aiResponse); //AI 응답 메세지
        history.add(assistant); //첫질문시 시스템 메시지, 사용자메시지1, ai응답메시지1 history에 add 두번쨰 질문시 동일하게 list에 추가되며 첫 질문과 답변이 등록

        Map<String, String> response = new HashMap<>();
        response.put("response", aiResponse);

        return ResponseEntity.ok(response);
    }
}
