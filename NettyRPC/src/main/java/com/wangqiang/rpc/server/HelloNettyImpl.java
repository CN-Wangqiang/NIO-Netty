package com.wangqiang.rpc.server;

/**
 * @version : V1.0
 * @ClassName: HelloNettyImpl
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/3/4 11:17
 */
public class HelloNettyImpl implements HelloNetty{
    public String hello() {
        return new String("Hello,Netty");
    }
}
