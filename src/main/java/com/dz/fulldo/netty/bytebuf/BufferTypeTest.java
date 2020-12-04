package com.dz.fulldo.netty.bytebuf;

import com.dz.fulldo.netty.basic.NettyDiscardServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class BufferTypeTest {
    private static final Logger Logger = LoggerFactory.getLogger(BufferTypeTest.class);
    final static Charset UTF_8 = Charset.forName("UTF-8");

    //堆缓冲区
    public  void testHeapBuffer() {
        //取得堆内存
        ByteBuf heapBuf =  ByteBufAllocator.DEFAULT.buffer();
        heapBuf.writeBytes("疯狂创客圈:高性能学习社群".getBytes(UTF_8));
        if (heapBuf.hasArray()) {
            //取得内部数组
            byte[] array = heapBuf.array();
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            int length = heapBuf.readableBytes();
            Logger.info(new String(array,offset,length, UTF_8));
        }
        heapBuf.release();

    }

    //直接缓冲区
    public  void testDirectBuffer() {
        ByteBuf directBuf =  ByteBufAllocator.DEFAULT.directBuffer();
        directBuf.writeBytes("疯狂创客圈:高性能学习社群".getBytes(UTF_8));
        if (!directBuf.hasArray()) {
            int length = directBuf.readableBytes();
            byte[] array = new byte[length];
            //读取数据到堆内存
            directBuf.getBytes(directBuf.readerIndex(), array);
            Logger.info(new String(array, UTF_8));
        }
        directBuf.release();
    }
}
