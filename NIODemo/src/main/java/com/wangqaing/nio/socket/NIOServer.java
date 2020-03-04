package com.wangqaing.nio.socket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import java.nio.channels.ServerSocketChannel;

import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @version : V1.0
 * @ClassName: NIOServer
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/3/2 16:11
 */
public class NIOServer {
    public static void main(String[] args) throws Exception{

        //1. 先得到ServerSocketChannel对象
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //2 得到一个Selector对象
        Selector selector = Selector.open();

        //3. 得到一个select对象
        serverSocketChannel.bind(new InetSocketAddress(9999));

        //4. 设置非阻塞方式
        serverSocketChannel.configureBlocking(false);

        //5. ServerScoketChannel对象注册给Selector对象
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //6. 开始干活
        while (true){
            // 先监控客户端
            if(selector.select(2000) == 0){
                //NIO的非阻塞的优势
                System.out.println("Server:没有客户端搭理我，我就干点别的事");
                continue;
            }

            // 得到SelectionKey，判断通道里的事件
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();

                //客户端的连接事件
                if (key.isAcceptable()) {
                    System.out.println("OP_ACCEPT");
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }

                //读取客户端数据事件
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("客户端发来数据"+new String(buffer.array()));
                }


                // 手动从集合中移除当前key,防止重复处理
                keyIterator.remove();
            }


        }

    }
}
