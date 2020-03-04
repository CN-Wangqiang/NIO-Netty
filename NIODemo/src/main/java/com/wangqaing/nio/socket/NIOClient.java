package com.wangqaing.nio.socket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @version : V1.0
 * @ClassName: NIOClient
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/3/2 16:01
 */
public class NIOClient {
    public static void main(String[] args) throws Exception{

        //1.得到一个网络通道
        SocketChannel channel = SocketChannel.open();

        //2.设置非阻塞方式
        channel.configureBlocking(false);

        //3.提供服务端的ip地址和端口号
        InetSocketAddress address = new InetSocketAddress("127.0.0.1",9999);

        //4.连接服务器端
        if (!channel.connect(address)) {
            while (!channel.finishConnect()) {
                //NIO作为非阻塞的优势
                System.out.println("Client：连接服务器端的同时，我还可以干别的事情");
            }
//            do {
//                System.out.println("Client：连接服务器端的同时，我还可以干别的事情");
//            }while (!channel.finishConnect());
        }


        //5.得到缓冲区并存入数据
        String msg = "hello,Server";
        ByteBuffer writeBuf = ByteBuffer.wrap(msg.getBytes());

        //6 发送数据
        channel.write(writeBuf);

        //阻止控制台程序结束
        System.in.read();



    }
}
