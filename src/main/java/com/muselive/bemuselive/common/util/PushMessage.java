package com.muselive.bemuselive.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PushMessage {
    DEPOSIT_NOTIFICATION("충전 ", "내 계좌 ← ","카카오뱅크"),
    WITHDRAW_NOTIFICATION("출금 ", "내 계좌 → ","카카오뱅크"),
    PAYMENT_NOTIFICATION("결제 ", "내 계좌 → ",""),
    LIBRARY_LATE_FEE_NOTIFICATION("출금 ", ""," 연체료 납부되었습니다."),
    NO_BALANCE_NOTIFICATION("승인 거절 ", ""," 출금 가능 잔액 부족 ");

    private String title;
    private String leftBody;
    private String rightBody;
}
