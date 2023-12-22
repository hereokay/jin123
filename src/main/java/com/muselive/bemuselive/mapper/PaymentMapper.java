package com.muselive.bemuselive.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PaymentMapper {

    int userGeneralPayment(Map dto);
    List<Map> getPaymentInfo(Map dto);
    Integer getDonatePayment(Map dto);
    List<Map> getPaymentInfoByMonth(Map dto);
    Integer getDonatePaymentByMonth(Map dto);



}
