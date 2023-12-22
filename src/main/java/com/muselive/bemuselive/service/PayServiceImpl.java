package com.muselive.bemuselive.service;


import com.muselive.bemuselive.VO.User;
import com.muselive.bemuselive.mapper.PaymentMapper;
import com.muselive.bemuselive.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class PayServiceImpl implements PayService {


    @Autowired
    PaymentMapper paymentMapper;

    @Autowired
    UserMapper userMapper;


    @Override
    public Map GeneralPayment(Map userInfo) { // userinfo -> school_id , service_id,payment_amount, payment_type



        User user = null;
        Integer pay_amount = null;

        Map returnMap =new HashMap<>();

        try {
            pay_amount = (Integer) userInfo.get("payment_amount");
        }catch (Exception e){
            returnMap.put("status",0);
            returnMap.put("message","payment_amount ERROR");

            log.error("payment_amount ERROR :",e);
            return returnMap;

        }

        try {
            user = userMapper.getUserInfo(userInfo);
            userInfo.put("user_id",user.getId());
        }catch (Exception e){
            returnMap.put("status",0);
            returnMap.put("message","sql ERROR");
            log.error("sql ERROR :",e);
            return returnMap;

        }

        if(user == null || pay_amount== null || user.getBalance() < pay_amount){

            returnMap.put("status",0);
            returnMap.put("message","잔액 부족 오류");

            return returnMap;
        }

        int paymentSuccess = paymentMapper.userGeneralPayment(userInfo);


        if(paymentSuccess == 1){
            returnMap.put("status",1);
            returnMap.put("message","성공");
        }else {
            returnMap.put("status",0);
            returnMap.put("message","입력 실패");
        }


        return returnMap;



    }



}
