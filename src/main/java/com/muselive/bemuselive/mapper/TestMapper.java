package com.muselive.bemuselive.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Component
@Mapper
public interface TestMapper {

    List<Map> selectTest();

}



