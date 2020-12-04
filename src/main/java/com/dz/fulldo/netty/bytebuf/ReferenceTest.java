package com.dz.fulldo.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class ReferenceTest {
    private static final Logger Logger = LoggerFactory.getLogger(ReferenceTest.class);

    public  void testRef()
    {

        ByteBuf buffer  = ByteBufAllocator.DEFAULT.buffer();
        Logger.info("after create:"+buffer.refCnt());
        buffer.retain();
        Logger.info("after retain:"+buffer.refCnt());
        buffer.release();
        Logger.info("after release:"+buffer.refCnt());
        buffer.release();
        Logger.info("after release:"+buffer.refCnt());
        //错误:refCnt: 0,不能再retain
        buffer.retain();
        Logger.info("after retain:"+buffer.refCnt());
    }
}
