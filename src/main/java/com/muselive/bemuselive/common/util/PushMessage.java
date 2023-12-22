package com.muselive.bemuselive.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PushMessage {
    LIBRARY_LATE_FEE("연체된 도서 ", " 원이 출금되었습니다.!");

    private String title;
    private String body;
}
