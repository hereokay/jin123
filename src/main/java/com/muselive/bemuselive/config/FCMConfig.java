package com.muselive.bemuselive.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FCMConfig {
    @Value("${firebase.auth.sdk}")
    public String FIREBASE_ADMIN_SDK;

    @PostConstruct
    public void init() {
        try{
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(FIREBASE_ADMIN_SDK).getInputStream()))
                    .build();
            FirebaseApp.initializeApp(options);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
