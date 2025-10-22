package com.example1.demo1.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FirebaseInitializer {

    private FirebaseApp firebaseApp; // Firebase (클라우드에 이미지 저장) 초기화 

    @PostConstruct
    public void init() throws IOException {
        InputStream serviceAccount = getClass()
            .getClassLoader()
            .getResourceAsStream("firebase/firebase-key.json");

        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setStorageBucket("festival-website-9cd0f")
            .build();

        if (FirebaseApp.getApps().isEmpty()) {
            firebaseApp = FirebaseApp.initializeApp(options);
            System.out.println("✅ Firebase 초기화 완료");
        } else {
            firebaseApp = FirebaseApp.getInstance(); // 이미 초기화된 앱 가져오기
            System.out.println("⚠️ Firebase 이미 초기화됨");
        }
    }

    public FirebaseApp getFirebaseApp() {
        return firebaseApp;
    }
}
