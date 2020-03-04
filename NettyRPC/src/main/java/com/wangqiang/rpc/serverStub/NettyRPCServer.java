package com.wangqiang.rpc.serverStub;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;


/**
 * @version : V1.0
 * @ClassName: NettyRPCServer
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/3/4 11:17
 */
public class NettyRPCServer {
    private int port;

    public NettyRPCServer(int port) {
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
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //往pipiline链中添加一个编码器
                            pipeline.addLast("decoder",new ObjectEncoder());
                            //往pipiline链中添加一个解码器
                            pipeline.addLast("encoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            //往pipeine链中添加自定义的handler类
                            pipeline.addLast(new InvokeHandler());
                        }
                    });

            System.out.println("------NettyRPCServer is Ready------");
            ChannelFuture cf = serverBootstrap.bind(9999).sync();
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            System.out.println("------NettyRPCServer is close------");


        }

    }

    public static void main(String[] args) throws Exception {
        new NettyRPCServer(9999).run();
    }
}
