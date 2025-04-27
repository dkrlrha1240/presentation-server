package org.network.netty.header;

public enum SendHeader {
    PONG(0),

    DISCORD_VERSION(10), // 버전 불일치
    CORRECT_VERSION(11), // 버전 일치

    DISCORD_AUTH_CODE(20), // 인증코드 불일치
    CORRECT_AUTH_CODE(21), // 인증코드 일치

    CONNECT_SUCCESS(31), // 접속 성공

    CHANGE_POSITION(101), // 위치 변경 통보
    CHANGE_LAYOUT(102), // 레이아웃 변경 통보


    ;
    private final int value;

    SendHeader(int value) { this.value = value; }

    public int val() { return value; }
}
