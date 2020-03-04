package com.wangqaing.nio.file;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @version : V1.0
 * @ClassName: TestNIO
 * @Description: TODO
 * @Auther: wangqiang
 * @Date: 2020/3/2 15:21
 */
public class TestNIO {

    //写入本地文件数据
    @Test
    public void test01() throws Exception{
        //1.创建输出流
        FileOutputStream fos = new FileOutputStream("basic.txt");
        //2.从流中得到一个通道
        FileChannel fc = fos.getChannel();
        //3.提供一个缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //4.往缓冲区中存入数据
        String str = "hello,nio";
        buffer.put(str.getBytes());
        //5.翻转缓冲区
        buffer.flip();
        //6.把缓冲区写到通道中
        fc.write(buffer);
        //7.关闭输出流
        fos.close();

    }

    //从本地文件中读取数据
    @Test
    public void test02() throws Exception{
        File file = new File("basic.txt");
        //1.创建输入流
        FileInputStream fis = new FileInputStream(file);
        //2.得到一个通道
        FileChannel fc = fis.getChannel();
        //3.准备一个缓冲区
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
        //4.从通道里读取数据并存到缓冲去
        fc.read(buffer);
        System.out.println(new String(buffer.array()));
        //5.关闭输入流
        fis.close();

    }


    //使用NIO实现文件复制
    @Test
    public void test03() throws Exception{
        //1.创建两个流
        FileInputStream fis = new FileInputStream("basic.txt");
        FileOutputStream fos = new FileOutputStream("text.txt");

        //2.得到两个通道
        FileChannel sourcFC = fis.getChannel();
        FileChannel destFC = fos.getChannel();

        //3.复制
        destFC.transferFrom(sourcFC,0,sourcFC.size());
//        //两个方法二选一
//        sourcFC.transferTo(0,destFC.size(),destFC);

        //4.关闭流
        fis.close();
        fos.close();

    }

}
