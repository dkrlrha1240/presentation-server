package org.network.netty.packet;

import org.network.netty.common.PacketWriter;
import org.network.netty.header.SendHeader;
import org.security.Authentication;

public class Packet {
    /*
    PONG(0),

    DISCORD_VERSION(10), // 버전 불일치
    CORRECT_VERSION(11), // 버전 일치

    DISCORD_AUTH_CODE(20), // 인증코드 불일치
    CORRECT_AUTH_CODE(21), // 인증코드 일치

    CONNECT_SUCCESS(31), // 접속 성공

    CHANGE_POSITION(101), // 위치 변경 통보
    CHANGE_LAYOUT(102), // 레이아웃 변경 통보
     */

    public static byte[] pong() {
        PacketWriter writer = new PacketWriter(SendHeader.PONG.val());
        writer.writeLong(System.currentTimeMillis());
        return writer.getPacket();
    }

    public static byte[] discordVersion() {
        PacketWriter writer = new PacketWriter(SendHeader.DISCORD_VERSION.val());
        String latestVersion = Authentication.CLIENT_VERSION;
        writer.writeString(latestVersion);
        return writer.getPacket();
    }

    public static byte[] correctVersion() {
        PacketWriter writer = new PacketWriter(SendHeader.CORRECT_VERSION.val());
        String latestVersion = Authentication.CLIENT_VERSION;
        writer.writeString(latestVersion);
        return writer.getPacket();
    }

    public static byte[] discordAuthCode() {
        PacketWriter writer = new PacketWriter(SendHeader.DISCORD_AUTH_CODE.val());
        return writer.getPacket();
    }

    public static byte[] correctAuthCode() {
        PacketWriter writer = new PacketWriter(SendHeader.CORRECT_AUTH_CODE.val());
        return writer.getPacket();
    }

    public static byte[] connectSuccess() {
        PacketWriter writer = new PacketWriter(SendHeader.CONNECT_SUCCESS.val());
        return writer.getPacket();
    }

    public static byte[] changePosition(int position) {
        PacketWriter writer = new PacketWriter(SendHeader.CHANGE_POSITION.val());
        writer.writeInt(position);
        return writer.getPacket();
    }

    public static byte[] changeLayout(int layout) {
        PacketWriter writer = new PacketWriter(SendHeader.CHANGE_LAYOUT.val());
        writer.writeInt(layout);
        return writer.getPacket();
    }
}
