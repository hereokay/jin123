package com.muselive.bemuselive.service;

import com.muselive.bemuselive.VO.ServiceDTO;
import com.muselive.bemuselive.common.util.PushMessage;
import com.muselive.bemuselive.mapper.ServiceMapper;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {
    @Autowired
    ServiceMapper serviceMapper;

    @Autowired
    FCMService fcmService;

    private final String UNIT = " 원";

    public void Notification(Map info, PushMessage pushMessage){
        ServiceDTO dto =  serviceMapper.getServiceInfo(info);
        String title = pushMessage.getTitle() + info.get("payment_amount") + UNIT;
        String body = pushMessage.getLeftBody() + dto.getService_name() + pushMessage.getRightBody();

        try{
            log.info(title);
            log.info(body);
            fcmService.sendMessageTo(title, body);
        }
        catch (IOException e){
            log.error(e.getMessage());
        }
    }

    public void NotificationNoService(Map info, PushMessage pushMessage){
        String title = pushMessage.getTitle() + info.get("payment_amount") + UNIT;
        String body = pushMessage.getLeftBody() + pushMessage.getRightBody();

        try{
            log.info(title);
            log.info(body);
            fcmService.sendMessageTo(title, body);
        }
        catch (IOException e){
            log.error(e.getMessage());
        }
    }
}
