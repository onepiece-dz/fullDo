package com.dz.fulldo.netty.decoder;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/

import com.dz.fulldo.netty.basic.NettyDiscardServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Byte2IntegerDecoder extends ByteToMessageDecoder {
    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(Byte2IntegerDecoder.class);
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in,
                       List<Object> out) {
        while (in.readableBytes() >= 4) {
            int i = in.readInt();
            Logger.info("解码出一个整数: " + i);
            out.add(i);
        }
    }
}