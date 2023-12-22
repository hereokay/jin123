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
import java.util.concurrent.CompletableFuture;


@Slf4j
@RestController
public class PaymentCTRL {

    @Autowired
    PayService payService;

    @Autowired
    EthereumService ethereumService;


    @PostMapping(value = "/payment")
    ResponseEntity<?> payment(@RequestBody Map body) throws Exception {

        if(!body.containsKey("school_id")
                || !body.containsKey("service_id")
                || !body.containsKey("payment_amount")){
            return ResponseEntity.badRequest().body(
                    "wrong param"
            );
        }

        // Blockchain
        // `ethereumService.reqPay`를 비동기적으로 실행
        CompletableFuture<TransactionReceipt> future = CompletableFuture.supplyAsync(() ->
                {
                    try {
                        return ethereumService.reqPayment(
                                (int) body.get("school_id"),
                                (int) body.get("service_id"),
                                (int) body.get("payment_amount")
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        // `reqPay`가 완료된 후 실행할 코드
        future.thenAccept(receipt -> {
            // 여기에 TransactionReceipt를 처리하는 코드를 넣습니다.
            // 예를 들어, 영수증 내용을 로깅하거나 확인하는 코드를 구현할 수 있습니다.
            log.info("Payment 완료: " + receipt.getTransactionHash());
        });

        // DB
        Map paymentSuccess =  payService.GeneralPayment(body);

        log.info("paymentSuccess : {}",paymentSuccess);
        return ResponseEntity.ok().body(paymentSuccess);
    }


    @PostMapping("/deposit")
    ResponseEntity<?> deposit(@RequestBody Map paymentInfo) throws Exception {


        CompletableFuture<TransactionReceipt> future = CompletableFuture.supplyAsync(() ->
                {
                    try {
                        return ethereumService.reqMint(
                                (int) paymentInfo.get("school_id"),
                                (int) paymentInfo.get("payment_amount")
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        // `reqPay`가 완료된 후 실행할 코드
        future.thenAccept(receipt -> {
            // 여기에 TransactionReceipt를 처리하는 코드를 넣습니다.
            // 예를 들어, 영수증 내용을 로깅하거나 확인하는 코드를 구현할 수 있습니다.
            log.info("Deposit 완료: " + receipt.getTransactionHash());
        });

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

        CompletableFuture<TransactionReceipt> future = CompletableFuture.supplyAsync(() ->
                {
                    try {
                        return ethereumService.reqPayment(
                                (int) paymentInfo.get("school_id"),
                                0,
                                (int) paymentInfo.get("payment_amount")
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        // `reqPay`가 완료된 후 실행할 코드
        future.thenAccept(receipt -> {
            // 여기에 TransactionReceipt를 처리하는 코드를 넣습니다.
            // 예를 들어, 영수증 내용을 로깅하거나 확인하는 코드를 구현할 수 있습니다.
            log.info("withdraw 완료: " + receipt.getTransactionHash());
        });

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
