package com.muselive.bemuselive.controller;


import com.muselive.bemuselive.mapper.TestMapper;
import com.muselive.bemuselive.service.EthereumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@RestController
public class testCTRL {

    @Autowired
    TestMapper testMapper;

    @Autowired
    EthereumService ethereumService;


    @GetMapping(value = "/test")
    ResponseEntity<?> test(){

        List<Map> map = testMapper.selectTest();

        return ResponseEntity.ok().body(map);

    }

    @GetMapping(value = "/test2")
    public BigInteger getCurrentBlockNumber() {
        try {
            return ethereumService.getBlockNumber();
        } catch (IOException e) {
            // 오류 처리
            e.printStackTrace();
            return null;
        }
    }



}
