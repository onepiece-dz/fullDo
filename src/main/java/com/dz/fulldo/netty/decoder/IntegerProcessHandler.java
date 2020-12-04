package com.dz.fulldo.netty.decoder;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntegerProcessHandler extends ChannelInboundHandlerAdapter {
    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(IntegerProcessHandler.class);
     @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Integer integer = (Integer) msg;
        Logger.info("打印出一个整数: " + integer);

    }
}