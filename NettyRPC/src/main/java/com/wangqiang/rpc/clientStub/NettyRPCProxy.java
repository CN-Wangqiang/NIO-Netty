package com.wangqiang.rpc.clientStub;

import com.wangqiang.rpc.serverStub.ClassInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Scanner;

/**
 * @version : V1.0
 * @ClassName: NettyRPCProxy
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/3/4 11:16
 */

//客户端代理类
public class NettyRPCProxy {
    //根据接口创建代理对象
    public static Object create(final Class target){
        return Proxy.newProxyInstance(target.getClassLoader(), new Class[]{target}, new InvocationHandler() {

            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                ClassInfo classInfo = new ClassInfo();
                classInfo.setClassName(target.getName());
                classInfo.setMethodName(method.getName());
                classInfo.setObjects(args);
                classInfo.setTypes(method.getParameterTypes());

                //开始Netty发送数据
                NioEventLoopGroup group = new NioEventLoopGroup();
                ResultHandler resultHandler = new ResultHandler();
                try {
                    Bootstrap bootstrap = new Bootstrap()
                            .group(group)
                            .channel(NioSocketChannel.class)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    ChannelPipeline pipeline = socketChannel.pipeline();
                                    //往pipiline链中添加一个编码器
                                    pipeline.addLast("decoder",new ObjectEncoder());
                                    //往pipiline链中添加一个解码器
                                    pipeline.addLast("encoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                                    //往pipeine链中添加自定义的handler类
                                    pipeline.addLast("handler",resultHandler);
                                }
                            });

                    //7.启动客户端去连接服务器端
                    ChannelFuture cf = bootstrap.connect("127.0.0.1", 9999).sync();
                    cf.channel().writeAndFlush(classInfo).sync();
                    cf.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    group.shutdownGracefully();
                }

                return resultHandler.getResponse();
            }
        });
    }
}
