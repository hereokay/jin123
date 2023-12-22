package com.muselive.bemuselive.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FCMService {
    @Value("${firebase.auth.scopes}")
    public String FIREBASE_AUTH_SCOPES;

    @Value("${firebase.auth.base-url}")
    public String FIREBASE_BASE_URL;

    @Value("${firebase.endpoint.send}")
    public String FIREBASE_SEND_ENDPOINT;

    @Value("${firebase.auth.sdk}")
    public String FIREBASE_ADMIN_SDK;

    @Value("${firebase.test.registration-token}")
    public String REGISTRATION_TOKEN;

    private final ObjectMapper objectMapper;

    public void sendMessageTo(String title, String body) throws IOException {
        Notification notification = makeNotification(title,body);
        try{
            Message message = Message.builder()
                    .setNotification(notification)
                    .setToken(REGISTRATION_TOKEN)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            //log.info("response : " + response);
        }
        catch (FirebaseMessagingException e){
            e.printStackTrace();
        }
    }

    private Notification makeNotification(String title, String body) throws JsonProcessingException {
        return Notification.builder()
                .setTitle(title)
                .setBody(body)
                .setImage(null)
                .build();
    }
    public String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new FileInputStream(FIREBASE_ADMIN_SDK)) //다운받은 firebase key 넘겨준다.
                .createScoped(Arrays.asList(FIREBASE_AUTH_SCOPES));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
