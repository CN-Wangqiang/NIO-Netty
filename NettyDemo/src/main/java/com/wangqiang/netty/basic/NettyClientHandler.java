package com.wangqiang.netty.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @version : V1.0
 * @ClassName: NettyHandlerClient
 * @Description: 客户端业务处理类
 * @Auther: wangqiang
 * @Date: 2020/3/3 16:47
 */

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    //通道就绪
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户器端的打印"+ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("老板，还钱！", CharsetUtil.UTF_8));
    }

   //读取数据事件
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器发来的消息：" + buf.toString(CharsetUtil.UTF_8));
    }
}
