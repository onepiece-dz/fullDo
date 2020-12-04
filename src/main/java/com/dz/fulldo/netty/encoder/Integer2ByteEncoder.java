package com.dz.fulldo.netty.encoder;

import com.dz.fulldo.netty.basic.NettyDiscardServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class Integer2ByteEncoder extends MessageToByteEncoder<Integer> {
    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(Integer2ByteEncoder.class);

    @Override
    public void encode(ChannelHandlerContext ctx, Integer msg, ByteBuf out)
            throws Exception {
        out.writeInt(msg);
        Logger.info("encoder Integer = " + msg);
    }
}
