package com.wangqiang.netty.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @version : V1.0
 * @ClassName: ChatClientHandler
 * @Description: 聊天客户端处理类
 * @Auther: wangqiang
 * @Date: 2020/3/3 17:55
 */
public class ChatClientHandler extends SimpleChannelInboundHandler<String> {
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println(s.trim());
    }
}
