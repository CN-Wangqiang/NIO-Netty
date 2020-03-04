package com.wangqiang.rpc.client;

import com.wangqiang.rpc.clientStub.NettyRPCProxy;

/**
 * @version : V1.0
 * @ClassName: TestNettyRPC
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/3/4 11:15
 */
public class TestNettyRPC {
    public static void main(String[] args) {
        //第一个次远程调用
        HelloNetty helloNetty = (HelloNetty) NettyRPCProxy.create(HelloNetty.class);
        System.out.println(helloNetty.hello());


        //第二次远程调用
        HelloRPC helloRPC = (HelloRPC) NettyRPCProxy.create(HelloRPC.class);
        System.out.println(helloRPC.hello("RPC"));

    }
}
