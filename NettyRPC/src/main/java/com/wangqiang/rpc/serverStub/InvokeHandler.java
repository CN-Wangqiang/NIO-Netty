package com.wangqiang.rpc.serverStub;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @version : V1.0
 * @ClassName: InvokeHandler
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/3/4 11:17
 */
//服务端业务处理类
public class InvokeHandler extends ChannelInboundHandlerAdapter {
    //得到某接口下某个实现类的名字
    private String getImplClassName(ClassInfo classInfo)throws Exception{
        //服务方接口和实现类所在的包路径
        String interfacePath = "com.wangqiang.rpc.server";
        int lastDot = classInfo.getClassName().lastIndexOf(".");
        String interfaceName = classInfo.getClassName().substring(lastDot);
        Class superClass = Class.forName(interfacePath + interfaceName);
        Reflections reflections = new Reflections(interfacePath);
        //得到某接口下的所有实现类
        Set<Class> implClassSet = reflections.getSubTypesOf(superClass);
        if (implClassSet.size() == 0){
            System.out.println("没有找到实现类");
            return null;
        }else if (implClassSet.size()>1){
            System.out.println("找到多个实现类,未明确使用哪一个");
            return null;
        }else {
            Class[] classes = implClassSet.toArray(new Class[0]);
            return classes[0].getName();//得到实现类的名字
        }
    }

    //读取客户端发来的数据并通过反射调用实现类的方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ClassInfo classInfo = (ClassInfo)msg;
        Object clazz = Class.forName(getImplClassName(classInfo)).newInstance();
        Method method = clazz.getClass().getMethod(classInfo.getMethodName(), classInfo.getTypes());
        //通过反射调用实现类的方法
        Object result = method.invoke(clazz, classInfo.getObjects());
        ctx.writeAndFlush(result);
    }
}
