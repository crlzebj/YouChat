package com.zjx.youchat.websocket;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.Properties;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketServer implements ApplicationRunner {
    @Value("${you-chat.websocket-port}")
    private Integer port;

    @Value("${you-chat.application-name}")
    private String name;

    private final WebSocketChatHandler webSocketChatHandler;

    private final NacosDiscoveryProperties nacosDiscoveryProperties;

    private void registerNamingService(String name, Integer port) {
        try {
            Properties properties = new Properties();
            properties.setProperty(PropertyKeyConst.SERVER_ADDR, nacosDiscoveryProperties.getServerAddr());
            properties.setProperty(PropertyKeyConst.NAMESPACE, nacosDiscoveryProperties.getNamespace());
            NamingService namingService = NamingFactory.createNamingService(properties);
            InetAddress address = InetAddress.getLocalHost();
            namingService.registerInstance(name, address.getHostAddress(), port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws",
                                    null, true, 65536,
                                    true, true));
                            ch.pipeline().addLast(webSocketChatHandler);
                        }
                    })
                    .bind(port).sync();

            // 注册到Nacos
            registerNamingService(name, port);

            // 关闭服务端
            // 阻塞主线程直到Server关闭
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
