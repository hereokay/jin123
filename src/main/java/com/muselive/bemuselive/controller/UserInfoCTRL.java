package com.muselive.bemuselive.controller;


import com.muselive.bemuselive.mapper.UserMapper;
import com.muselive.bemuselive.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserInfoCTRL {

    @Autowired
    PayService payService;

    @GetMapping(value = "/userInfo")
    ResponseEntity<?> getUserInfo(@RequestParam Integer school_id,@RequestParam String date_info){


        Map params = new HashMap<>();

        params.put("school_id",school_id);
        params.put("date_info",date_info);

        Map ret =  payService.getPaymentInfo(params);

        return ResponseEntity.ok().body(ret);


    }




}
