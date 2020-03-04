package com.wangqiang.rpc.clientStub;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @version : V1.0
 * @ClassName: ResultHandler
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/3/4 11:16
 */

//客户端业务处理类
public class ResultHandler extends ChannelInboundHandlerAdapter {

    private Object response;
    public Object getResponse(){
        return response;
    }

    //读取服务器端返回的数据（远程调用的结果）
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        response = msg;
        ctx.close();
    }
}
