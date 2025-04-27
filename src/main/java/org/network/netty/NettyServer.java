package org.network.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.network.netty.common.NettyDecoder;
import org.network.netty.common.NettyEncoder;

import java.util.logging.Logger;

public class NettyServer extends Thread {
    private final static Logger LOG = Logger.getGlobal();
    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void run() {
        LOG.info(port + "포트로 서버를 개방합니다.");
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch)
                                throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast("decoder", new NettyDecoder());
                            p.addLast("encoder", new NettyEncoder());
                            p.addLast("handler", new NettyHandler());
                        }

                        ;
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_SNDBUF, 1024 * 4)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(port).sync();

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();

        }
    }
}