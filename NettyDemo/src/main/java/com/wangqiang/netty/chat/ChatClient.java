package com.wangqiang.netty.chat;

import com.wangqiang.netty.basic.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @version : V1.0
 * @ClassName: ChatClient
 * @Description: 聊天客户端
 * @Auther: wangqiang
 * @Date: 2020/3/3 17:55
 */
public class ChatClient {
    private final String host;
    private final int port;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run(){
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() { //5. 创建一个通道初始化对象

                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //往pipiline链中添加一个解码器
                            pipeline.addLast("decoder",new StringDecoder());
                            //往pipiline链中添加一个编码器
                            pipeline.addLast("encoder",new StringEncoder());
                            //往pipeine链中添加自定义的handler类
                            pipeline.addLast(new ChatClientHandler());
                        }
                    });
            System.out.println("-------Client is Ready-------");

            //7.启动客户端去连接服务器端
            ChannelFuture cf = bootstrap.connect(host, port).sync();
            Channel channel = cf.channel();
            System.out.println("-------" + channel.localAddress().toString().substring(1) + "-------");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String msg = scanner.nextLine();
                channel.writeAndFlush(msg + "\r\n");
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new ChatClient("127.0.0.1",9999).run();
    }
}
