package com.soldesk.festival.service;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.soldesk.festival.config.FirebaseInitializer;

@Service
public class FileUploadService {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    public String uploadToFirebase(MultipartFile file) throws IOException {
    String ext = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
    if (!List.of("jpg", "jpeg", "png", "gif").contains(ext)) {
        throw new IOException("허용되지 않는 파일 형식입니다");
    }

    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
    String blobPath = "images/" + fileName;

    FirebaseApp app = firebaseInitializer.getFirebaseApp();
    Bucket bucket = StorageClient.getInstance(app).bucket();

    // 토큰 생성
    String token = UUID.randomUUID().toString();

    // metadata 포함해서 BlobInfo 생성
    BlobInfo blobInfo = BlobInfo.newBuilder(bucket.getName(), blobPath)
        .setContentType(file.getContentType())
        .setMetadata(Map.of("firebaseStorageDownloadTokens", token))
        .build();

    // 파일 업로드
    Blob blob = bucket.getStorage().create(blobInfo, file.getBytes());

    // 토큰 포함된 다운로드 URL 생성
    String downloadUrl = String.format(
        "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media&token=%s",
        bucket.getName(),
        URLEncoder.encode(blob.getName(), StandardCharsets.UTF_8),
        token
    );
    return downloadUrl;
    }
    
    public String extractPathFromUrl(String downloadUrl) {
    String encodedPath = downloadUrl.split("/o/")[1].split("\\?")[0];
    return URLDecoder.decode(encodedPath, StandardCharsets.UTF_8);
    }


    public void deleteFromFirebase(String imagePath) {
    FirebaseApp app = firebaseInitializer.getFirebaseApp();
    Bucket bucket = StorageClient.getInstance(app).bucket();

    Blob blob = bucket.getStorage().get(bucket.getName(), imagePath);
    if (blob != null && blob.exists()) {
        blob.delete();
        System.out.println("FireBase 이미지 삭제 완료");
    } else {
        System.out.println("파일이 존재하지 않음");
    }
    }


}
