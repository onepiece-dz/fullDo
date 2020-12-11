package com.dz.fulldo.classLoad;

public class Callee extends CalleeFuther implements CalleeIntf {
    public static String staticField = "staticField";
    public static final String staticFieldFinal = "staticFieldFinal";
    static {
        System.out.println("Callees 被初始化了");
    }

    public static String getStaticField() {
        return "yes static";
    }

    public String getField() {
        return "yes";
    }

    @Override
    public String getClassName() {
        return null;
    }
}
