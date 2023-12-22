package com.muselive.bemuselive.service;

import com.muselive.bemuselive.mapper.LibraryMapper;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LibraryBatchService {
    @Autowired
    LibraryMapper libraryMapper;

    public void LibraryLateFee(){
        List<Map> map = libraryMapper.getLibrary();
    }

}
