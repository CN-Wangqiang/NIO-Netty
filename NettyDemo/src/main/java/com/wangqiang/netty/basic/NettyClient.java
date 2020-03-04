package com.wangqiang.netty.basic;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @version : V1.0
 * @ClassName: NettyClient
 * @Description: 客户端
 * @Auther: wangqiang
 * @Date: 2020/3/3 16:46
 */
public class NettyClient {
    public static void main(String[] args) throws Exception{

        // 1. 创建一个线程组
        NioEventLoopGroup group = new NioEventLoopGroup();

        // 2. 创建客户端的启动助手，完成相关配置
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)  //3. 设置线程组
                .channel(NioSocketChannel.class)   //4. 设置客户端通道的实现类
                .handler(new ChannelInitializer<SocketChannel>() { //5. 创建一个通道初始化对象

                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyClientHandler());   //6. 往Pipiline链中添加自定义的handler
                    }
                });

        System.out.println("-------Client is Ready--------");

        //7.启动客户端去连接服务器端
        ChannelFuture cf = bootstrap.connect("127.0.0.1", 9999).sync();

        //8.关闭连接,异步非阻塞
        cf.channel().closeFuture().sync();
    }
}
