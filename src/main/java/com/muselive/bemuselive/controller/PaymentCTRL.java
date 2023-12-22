package com.muselive.bemuselive.controller;

import com.muselive.bemuselive.service.EthereumService;
import com.muselive.bemuselive.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
public class PaymentCTRL {

    @Autowired
    PayService payService;

    @Autowired
    EthereumService ethereumService;


    @PostMapping(value = "/payment")
    ResponseEntity<?> test(@RequestBody Map body) throws Exception {

        if(!body.containsKey("school_id")
                || !body.containsKey("service_id")
                || !body.containsKey("payment_amount")){
            return ResponseEntity.badRequest().body(
                    "wrong param"
            );
        }

        // Blockchain
        TransactionReceipt receipt = ethereumService.reqPay(
                (int) body.get("school_id"),
                (int) body.get("service_id"),
                (int) body.get("payment_amount")
        );

        if (!receipt.isStatusOK()){
            return ResponseEntity.badRequest().body("transaction fail");
        }

        // DB
        Map paymentSuccess =  payService.GeneralPayment(body);
        paymentSuccess.put("txHash",receipt.getTransactionHash());

        log.info("paymentSuccess : {}",paymentSuccess);
        return ResponseEntity.ok().body(paymentSuccess);
    }

    @PostMapping("/deposit")

    ResponseEntity<?> deposit(@RequestBody Map paymentInfo){

        int res = payService.Deposit(paymentInfo);

        Map ret = new HashMap();

        if(res == 1){
            ret.put("message","성공");
            return ResponseEntity.ok().body(ret);
        }
        else {
            ret.put("message","실패");
            return ResponseEntity.badRequest().body(ret);
        }
    }


    @PostMapping("/withdraw")
    ResponseEntity<?> withdraw(@RequestBody Map paymentInfo){

        int res = payService.Withdraw(paymentInfo);

        Map ret = new HashMap();

        if(res == 1){
            ret.put("message","성공");
            return ResponseEntity.ok().body(ret);
        }
        else {
            ret.put("message","실패");
            return ResponseEntity.badRequest().body(ret);
        }
    }


}
