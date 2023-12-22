package com.muselive.bemuselive.service;


import com.muselive.bemuselive.VO.ServiceDTO;
import com.muselive.bemuselive.VO.User;
import com.muselive.bemuselive.mapper.PaymentMapper;
import com.muselive.bemuselive.mapper.ServiceMapper;
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
    
    @Autowired
    ServiceMapper serviceMapper;


    @Override
    public Map GeneralPayment(Map payment_info) { // payment_info -> school_id , service_id,payment_amount, payment_type



        User user = null;
        ServiceDTO serviceDTO = null;
        Integer pay_amount = null;

        Map returnMap =new HashMap<>();

        
        

        try {
            pay_amount = (Integer) payment_info.get("payment_amount");
        }catch (Exception e){
            returnMap.put("status",0);
            returnMap.put("message","payment_amount ERROR");

            log.error("payment_amount ERROR :",e);
            return returnMap;

        }

        try {
            serviceDTO = serviceMapper.getServiceInfo(payment_info);
            user = userMapper.getUserInfo(payment_info);
        }catch (Exception e){
            returnMap.put("status",0);
            returnMap.put("message","sql ERROR");
            log.error("sql ERROR :",e);
            return returnMap;
        }

        if(user == null || pay_amount== null ){

            returnMap.put("status",0);
            returnMap.put("message","null값 오류");

            return returnMap;
        }
        else if(  user.getBalance() < pay_amount){
            returnMap.put("status",0);
            returnMap.put("message","잔액 부족 오류");
            return returnMap;
        }
        else {
            payment_info.put("user_id",user.getSchool_id());
            payment_info.put("payment_type",serviceDTO.getPayment_type());
        }


        int paymentSuccess = paymentMapper.userGeneralPayment(payment_info);


        if(paymentSuccess == 1){
            returnMap.put("status",1);
            returnMap.put("message","성공");
        }else {
            returnMap.put("status",0);
            returnMap.put("message","입력 실패");
        }
        return returnMap;
    }


    public int Deposit(Map userInfo){
        return userMapper.depositCoin(userInfo);
    }




}
