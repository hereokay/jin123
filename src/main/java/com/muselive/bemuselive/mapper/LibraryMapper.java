package com.muselive.bemuselive.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface LibraryMapper {
    List<Map> getLibrary();
    Map getLibraryTotalFee(Integer school_id);
}
