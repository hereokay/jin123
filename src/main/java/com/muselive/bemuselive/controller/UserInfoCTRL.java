package com.muselive.bemuselive.controller;


import com.muselive.bemuselive.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserInfoCTRL {

    @Autowired
    UserMapper userMapper;

    @PostMapping(value = "/userInfo")
    ResponseEntity<?> getUserInfo(@RequestBody Map map){

        return ResponseEntity.ok().body("");


    }




}
