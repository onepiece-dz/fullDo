package com.dz.fulldo.classLoad;

public interface CalleeIntf {
    String getClassName();
    default String getIntfClassName() {
        return CalleeIntf.class.getName();
    }
}
