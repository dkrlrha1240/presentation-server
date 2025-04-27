package org.network.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.client.Client;
import org.client.storage.ClientStorage;
import org.network.netty.common.PacketReader;
import org.network.netty.header.ReceiveHeader;
import org.network.netty.packet.Packet;
import org.screen.LayoutManager;
import org.security.Authentication;

import java.util.logging.Logger;

public class NettyHandler  extends SimpleChannelInboundHandler<byte[]> {
    private final static Logger LOG = Logger.getGlobal();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String address = ctx.channel().remoteAddress().toString().split(":")[0];
        Client client = new Client(ctx.channel(), address);
        LOG.info(address + " 에서 서버에 연결을 시도했습니다.");
        ClientStorage.addClient(client);
        ctx.channel().attr(Client.CLIENT_KEY).set(client);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String address = ctx.channel().remoteAddress().toString().split(":")[0];
        Client client = ctx.channel().attr(Client.CLIENT_KEY).get();
        if (client == null)
            return;

        if (client.getPosition() != -1)
            LayoutManager.getInstance().changePosition(-1, client);
        client.disconnect();
        ClientStorage.removeClient(client.getId());
        ctx.channel().attr(Client.CLIENT_KEY).set(null);
        LOG.info(address + " 에서 서버와 연결이 끊어졌습니다.");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] bytes) throws Exception {
        if (bytes.length == 0)
            return;

        Client client = ctx.channel().attr(Client.CLIENT_KEY).get();
        if (client == null)
            return;

        PacketReader reader = new PacketReader(bytes);
        int headerId = reader.getHeader();
        ReceiveHeader header = null;
        for (ReceiveHeader recv : ReceiveHeader.values()) {
            if (recv.val() == headerId) {
                header = recv;
                break;
            }
        }

        switch (header) {
            case PING -> {
                client.setPingTime(System.currentTimeMillis());
                client.getChannel().writeAndFlush(Packet.pong());
            }

            case CLIENT_VERSION -> {
                String version = reader.readString();
                if (!Authentication.CLIENT_VERSION.equalsIgnoreCase(version)) {
                    // 버전이 일치하지 않음 메시지 전송
                    ctx.writeAndFlush(Packet.discordVersion());
                    return;
                }
                // 버전이 일치하는 경우 성공 메시지 전송
                LOG.info(client.getIp() + "에서 버전인증에 성공했습니다.");
                ctx.writeAndFlush(Packet.correctVersion());
            }

            case CLIENT_AUTH_CODE -> {
                String code = reader.readString();
                if (!Authentication.CODE.equals(code)) {
                    // 인증코드 불일치 메시지 전송
                    ctx.writeAndFlush(Packet.discordAuthCode());
                    return;
                }
                // 인증코드 일치 전송
                LOG.info(client.getIp() + "에서 클라이언트 인증에 성공했습니다.");
                ctx.writeAndFlush(Packet.correctAuthCode());
            }

            case CLIENT_INFORMATION -> {
                String job = reader.readString();
                String name = reader.readString();
                client.connect(job, name);
                LOG.info(client.getIp() + "에서 " + job + " " + name + "으로 서버에 접속됐습니다.");
            }

            case SCREEN_IMAGE -> {
                if (client.getPosition() == -1) // 숨김인 경우 수신 X
                    return;

                int size = reader.readInt();
                byte[] data = new byte[size];
                for (int i = 0; i < size; i++)
                    data[i] = reader.readByte();

                LayoutManager.getInstance().setScreen(client.getPosition(), data);
            }

            case CHANGE_POSITION -> {
                int position = reader.readInt();
                LayoutManager.getInstance().changePosition(position, client);
            }

        }
    }
}