package com.wangqaing.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * @version : V1.0
 * @ClassName: ChatServer
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/3/2 21:05
 */
public class ChatServer {
    private ServerSocketChannel listenerChannel;
    private Selector selector;
    private static final int PORT = 9999;

    public ChatServer(){
        try {
            //1. 先得到ServerSocketChannel对象
            listenerChannel = ServerSocketChannel.open();
            //2 得到一个Selector对象
            selector = Selector.open();
            //3. 得到一个select对象
            listenerChannel.bind(new InetSocketAddress(PORT));
            //4. 设置非阻塞方式
            listenerChannel.configureBlocking(false);
            //5. ServerScoketChannel对象注册给Selector对象
            listenerChannel.register(selector, SelectionKey.OP_ACCEPT);

            printInfo("Chat Server is Ready ...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 开始干活
    public void start() throws Exception{
        try {
            while (true){
                    if (selector.select(2000) == 0){
                        System.out.println("Server：没有客户端找我，我就干别的事");
                        continue;
                    }
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()){
                            //连接请求事件
                            SocketChannel sc = listenerChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector,SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress().toString().substring(1) +"上线了...");

                        }
                        if (key.isReadable()){
                            //读取数据事件
                            readMsg(key);
                        }
                        iterator.remove();
                    }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取客户端发来的消息并广播出去
    private void readMsg(SelectionKey key) throws Exception{
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int count = channel.read(buffer);
        if (count > 0){
            String msg = new String(buffer.array());
            printInfo(msg);

            //发广播
            broadCast(channel,msg);
        }
    }


    //给所有的客户端发广播
    private void broadCast(SocketChannel channel, String msg) throws Exception {
        System.out.println("服务器发送了广播...");
        for (SelectionKey key : selector.keys()) {
            Channel targetChannel = key.channel();
            if (targetChannel instanceof SocketChannel && targetChannel != channel){
                SocketChannel destChannel = (SocketChannel)key.channel();
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                destChannel.write(buffer);
            }
        }
    }

    private void printInfo(String string){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("[" + sdf.format(new Date()) + "] -> " + string );
    }

    public static void main(String[] args) throws Exception {
        new ChatServer().start();
    }
}
