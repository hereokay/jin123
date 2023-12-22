package com.muselive.bemuselive.VO;


import lombok.Data;

@Data
public class ServiceDTO {

    public Integer id;
    public String service_name;
    public String wallet_address;
    public Integer balance;
    public Integer payment_type;



}
