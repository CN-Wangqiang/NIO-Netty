package com.wangqiang.netty.chat;

import com.wangqiang.netty.basic.NettyClientHandler;
import com.wangqiang.netty.basic.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @version : V1.0
 * @ClassName: ChatServer
 * @Description: 聊天服务器端
 * @Auther: wangqiang
 * @Date: 2020/3/3 17:55
 */
public class ChatServer {

    private int port;

    public ChatServer(int port) {
        this.port = port;
    }

    public void run() throws Exception{
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {  //8.创建一个通道初始化对象

                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //往pipiline链中添加一个解码器
                            pipeline.addLast("decoder",new StringDecoder());
                            //往pipiline链中添加一个编码器
                            pipeline.addLast("encoder",new StringEncoder());
                            //往pipeine链中添加自定义的handler类
                            pipeline.addLast(new ChatServerHandler());
                        }
                    });

            System.out.println("------ChatServer is Ready------");
            ChannelFuture cf = serverBootstrap.bind(9999).sync();
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            System.out.println("------ChatServer is close------");


        }

    }

    public static void main(String[] args) throws Exception {
        new ChatServer(9999).run();
    }
}
