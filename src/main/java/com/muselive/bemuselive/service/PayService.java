package com.muselive.bemuselive.service;


import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public interface PayService {

    Map GeneralPayment(Map userInfo);

}
