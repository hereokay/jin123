package com.muselive.bemuselive.mapper;

import com.muselive.bemuselive.VO.ServiceDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface ServiceMapper {

    ServiceDTO getServiceInfo(Map map);

}
