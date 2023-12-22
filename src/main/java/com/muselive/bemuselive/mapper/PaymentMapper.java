package com.muselive.bemuselive.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PaymentMapper {

    int userGeneralPayment(Map dto);

}
