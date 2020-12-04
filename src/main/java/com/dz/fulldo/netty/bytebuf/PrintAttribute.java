package com.dz.fulldo.netty.bytebuf;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class PrintAttribute {
    private static final Logger Logger = LoggerFactory.getLogger(PrintAttribute.class);

    public static void print(String action, ByteBuf b) {
        Logger.info("after ===========" + action + "============");
        Logger.info("1.0 isReadable(): " + b.isReadable());
        Logger.info("1.1 readerIndex(): " + b.readerIndex());
        Logger.info("1.2 readableBytes(): " + b.readableBytes());
        Logger.info("2.0 isWritable(): " + b.isWritable());
        Logger.info("2.1 writerIndex(): " + b.writerIndex());
        Logger.info("2.2 writableBytes(): " + b.writableBytes());
        Logger.info("3.0 capacity(): " + b.capacity());
        Logger.info("3.1 maxCapacity(): " + b.maxCapacity());
        Logger.info("3.2 maxWritableBytes(): " + b.maxWritableBytes());
    }
}