package com.wangqiang.rpc.server;

/**
 * @version : V1.0
 * @ClassName: HelloRPCImpl
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/3/4 11:17
 */
public class HelloRPCImpl  implements HelloRPC{
    public String hello(String rpc) {
        return new String("Hello," + rpc);
    }
}
