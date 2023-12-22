package com.muselive.bemuselive.controller;

import com.muselive.bemuselive.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@RestController
public class PaymentCTRL {

    @Autowired
    PayService payService;

    @PostMapping(value = "/payment")
    ResponseEntity<?> test(@RequestBody Map body){



        if(!body.containsKey("school_id")
                || !body.containsKey("service_id")
                || !body.containsKey("payment_amount")){
            return ResponseEntity.badRequest().build();
        }

        Map paymentSuccess =  payService.GeneralPayment(body);

        log.info("paymentSuccess : {}",paymentSuccess);

        return ResponseEntity.ok().body(paymentSuccess);




    }
}
