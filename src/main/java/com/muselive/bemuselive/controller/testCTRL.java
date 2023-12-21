package com.muselive.bemuselive.controller;


import com.muselive.bemuselive.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class testCTRL {

    @Autowired
    TestMapper testMapper;

    @GetMapping(value = "/test")
    ResponseEntity<?> test(){

        List<Map> map = testMapper.selectTest();

        return ResponseEntity.ok().body(map);

    }

}
