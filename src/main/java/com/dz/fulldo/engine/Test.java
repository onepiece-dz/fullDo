package com.dz.fulldo.engine;

public class Test {
    public static void main(String[] args) {
        {
            byte[] placeholder = new byte[64 * 1024 * 1024];
        }
        int a = 0;// 加该行代码和不加的区别：加了之后，placeholder对应的内存会被回收（注：解释执行逻辑）
        System.gc();
    }
}
