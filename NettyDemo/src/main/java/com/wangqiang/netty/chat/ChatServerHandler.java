package com.wangqiang.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @version : V1.0
 * @ClassName: ChatServerHandler
 * @Description: 聊天服务器端处理类
 * @Auther: wangqiang
 * @Date: 2020/3/3 17:56
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    public static List<Channel> channels = new ArrayList<Channel>();

    //通道就绪
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel inChannel = ctx.channel();
        channels.add(inChannel);
        System.out.println("[Server]：" + inChannel.remoteAddress().toString().substring(1) + "上线");

    }

    //通道未就绪
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel inChannel = ctx.channel();
        channels.remove(inChannel);
        System.out.println("[Server：]" + inChannel.remoteAddress().toString().substring(1) + "离线");

    }

    //读取数据
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Channel inChannel = channelHandlerContext.channel();
        for (Channel channel : channels) {
            if (channel != inChannel){
                channel.writeAndFlush("[" + inChannel.remoteAddress().toString().substring(1) +"]" + "说：" + s +"\n");
            }
        }
    }
}
