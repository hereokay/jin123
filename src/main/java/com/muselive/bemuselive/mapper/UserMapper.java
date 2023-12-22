package com.muselive.bemuselive.mapper;

import com.muselive.bemuselive.VO.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface UserMapper {

    User getUserInfo(Map map);
    int depositCoin(Map map);

}
