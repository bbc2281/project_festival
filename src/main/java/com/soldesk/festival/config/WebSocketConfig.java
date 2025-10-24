package com.soldesk.festival.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker//웹소켓 메세지 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("ws").setAllowedOriginPatterns("*").withSockJS();
    }//클라이언트와 서버간 웹소켓 연결 설정

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        //구독(subscribe): 서버가 메시지를 구독한 클라이언트 들에게 발송할 경로
        registry.setApplicationDestinationPrefixes("/app");
        //발신(prefix): 클라이언트가 서버로 메시지를 보낼 경로
    }
}