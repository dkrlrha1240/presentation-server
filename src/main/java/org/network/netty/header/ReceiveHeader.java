package org.network.netty.header;

public enum ReceiveHeader {
    PING(0),

    CLIENT_VERSION(10),
    CLIENT_AUTH_CODE(20),
    CLIENT_INFORMATION(30),

    CHANGE_POSITION(101),
    SCREEN_IMAGE(102),

    ;
    private final int value;

    ReceiveHeader(int value) { this.value = value; }

    public int val() { return value; }
}
