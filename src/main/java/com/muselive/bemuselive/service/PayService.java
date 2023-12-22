package com.muselive.bemuselive.service;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface PayService {

    Map GeneralPayment(Map userInfo);
    int Deposit(Map userInfo);
    int Withdraw(Map userInfo);
    Map getPaymentInfo(Map info);


}
