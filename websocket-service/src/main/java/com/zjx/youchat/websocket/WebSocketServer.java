package com.zjx.youchat.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WebSocketServer implements ApplicationRunner {
    @Value("${you-chat.websocket-port}")
    private Integer port;

    @Autowired
    private WebSocketChatHandler webSocketChatHandler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // 若配置JVM参数，则不使用yml文件中的端口号
        String portStr = System.getProperty("websocket.port");
        int port = (portStr == null || portStr.isEmpty()) ?
                this.port : Integer.parseInt(portStr);

        try {
            // 启动服务端
            ChannelFuture channelFuture = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new HttpObjectAggregator(64 * 1024));
                            // ch.pipeline().addLast(new IdleStateHandler(60, 0, 0));
                            // ch.pipeline().addLast(new HeartBeatHandler());
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler("/websocket",
                                    null, true, 65536,
                                    true, true));
                            ch.pipeline().addLast(webSocketChatHandler);
                        }
                    })
                    .bind(port).sync();

            // 关闭服务端
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("WebSocket服务器异常：{}", e.getMessage());
        } finally {
            // 释放资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
