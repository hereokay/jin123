package com.muselive.bemuselive.service;

import com.muselive.bemuselive.VO.User;
import com.muselive.bemuselive.common.util.PushMessage;
import com.muselive.bemuselive.mapper.LibraryMapper;
import com.muselive.bemuselive.mapper.PaymentMapper;
import com.muselive.bemuselive.mapper.UserMapper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

@Slf4j
@Service
public class LibraryBatchService {
    @Autowired
    LibraryMapper libraryMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PaymentMapper paymentMapper;

    @Autowired
    private EthereumService ethereumService;

    @Autowired
    NotificationService notificationService;

    public final Integer PAYMENT_TYPE = 2;
    public final Integer SERVICE_ID = 2;

    public void LibraryLateFee(){
        User user = null;
        List<Map> map = libraryMapper.getLibraryTotalFee();

        for(int i=0; i<map.size(); i++){
            Map payment_info = new HashMap();
            Map user_info = new HashMap();

            Map late_fee_info = map.get(i);
            Integer total_fees = Integer.parseInt(String.valueOf(late_fee_info.get("total_fees")));
            user_info.put("school_id",(Integer) late_fee_info.get("borrower_school_id"));

            try {
                user = userMapper.getUserInfo(user_info);

            }catch (Exception e){
                log.error("payment_amount ERROR :",e);
                continue;
            }

            payment_info.put("school_id",user.getSchool_id());
            payment_info.put("service_id",SERVICE_ID);
            payment_info.put("payment_amount",total_fees);
            payment_info.put("payment_type",PAYMENT_TYPE);

            if(user == null){
                log.error("null값 오류");
                continue;
            }
            else if( user.getBalance() < total_fees){
                notificationService.Notification(payment_info,PushMessage.NO_BALANCE_NOTIFICATION);
                continue;
            }

            //Blockchain
            // `ethereumService.reqPay`를 비동기적으로 실행
            User finalUser = user;
            CompletableFuture<TransactionReceipt> future = CompletableFuture.supplyAsync(() ->
                    {
                        try {
                            return ethereumService.reqPayment(
                                    finalUser.getSchool_id(),
                                    0,
                                    total_fees
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


            int paymentSuccess = paymentMapper.userGeneralPayment(payment_info);

            if(paymentSuccess != 1) {
                log.error("입력 실패");
                continue;
            }
            notificationService.Notification(payment_info, PushMessage.LIBRARY_LATE_FEE_NOTIFICATION);

            int updateSuccess = libraryMapper.updateComplete(late_fee_info);
            if(updateSuccess == 1){
                log.info("입력 성공");
            }
        }
    }
}
