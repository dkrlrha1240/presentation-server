package org.client;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.client.storage.ClientStorage;
import org.network.netty.packet.Packet;
import org.screen.LayoutManager;
import org.screen.form.ClientDTO;
import org.screen.form.FormController;

public class Client {
    final public static AttributeKey<Client> CLIENT_KEY = AttributeKey.valueOf("session_netty");
    private static int INCREASE_ID = 0;
    final private String ip; // IP
    final private int id;
    private String job; // 직책
    private String name; // 성명
    private int position = -1; // 위치  -1 : 숨김, 0 : 전체
    private boolean isConnected; // 연결상태
    private long connectTime; // 접속시간
    private long disconnectTime; // 연결해제 시간
    private long pingTime;
    private Channel channel;

    public Client(Channel channel, String ip) {
        this.id = generateID();
        this.channel = channel;
        this.ip = ip;
        System.out.println(id + " 클라이언트 생성");
    }

    public void connect(String job, String name) {
        this.setJob(job);
        this.setName(name);
        this.setConnected(true);
        this.writeAndFlush(Packet.connectSuccess());
        this.setConnectTime(System.currentTimeMillis());
        this.writeAndFlush(Packet.changeLayout(LayoutManager.getInstance().getLayout()));
        FormController.getInstance().addRow(toClientDTO());
    }

    public void disconnect() {
        this.setConnected(false);
        this.setDisconnectTime(System.currentTimeMillis());
        FormController.getInstance().removeRowById(id);
    }

    public ClientDTO toClientDTO() {
        return new ClientDTO(id, isConnected() ? "●" : "X", ip, job, name, position == -1 ? "숨김" : position == 0 ? "전체" : position + "번");
    }

    public int getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        this.writeAndFlush(Packet.changePosition(position));
        FormController.getInstance().updateRowByClient(toClientDTO());
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public long getConnectTime() {
        return connectTime;
    }

    public void setConnectTime(long connectTime) {
        this.connectTime = connectTime;
    }

    public long getDisconnectTime() {
        return disconnectTime;
    }

    public void setDisconnectTime(long disconnectTime) {
        this.disconnectTime = disconnectTime;
    }

    public long getPingTime() {
        return pingTime;
    }

    public void setPingTime(long pingTime) {
        this.pingTime = pingTime;
    }


    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) { this.channel = channel; }
    public boolean writeAndFlush(byte[] packet) {
        if (!isConnected() || channel == null)
            return false;

        this.channel.writeAndFlush(packet);
        return true;
    }

    public static int generateID() {
        if (++INCREASE_ID >= Integer.MAX_VALUE)
            INCREASE_ID = 0;
        return INCREASE_ID;
    }
}
