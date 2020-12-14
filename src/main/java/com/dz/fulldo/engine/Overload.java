package com.dz.fulldo.engine;

import java.io.Serializable;

public class Overload {
    public static void sayHello(Object arg) {
        System.out.println("hello Object");
    }

    public static void sayHello(int arg) {
        System.out.println("hello int");
    }

    public static void sayHello(long arg) {
        System.out.println("hello long");
    }

    public static void sayHello(Character arg) {
        System.out.println("hello Character");
    }

    public static void sayHello(char arg) {
        System.out.println("hello char");
    }

    public static void sayHello(char... arg) {
        System.out.println("hello char...");
    }

    public static void sayHello(Serializable arg) {
        System.out.println("hello Serializable");
    }

    /**
     * 字面量没有显式的静态类型，在寻找重载方法时，会进行自动转型和自动装箱的
     * @param args
     */
    public static void main(String[] args) {
        sayHello('a');
    }
}
