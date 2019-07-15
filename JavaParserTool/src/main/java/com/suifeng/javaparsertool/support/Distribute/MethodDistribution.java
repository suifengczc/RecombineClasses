package com.suifeng.javaparsertool.support.Distribute;

public class MethodDistribution {
    boolean keepedMethod;
    String className;
    String methodName;

    public MethodDistribution(boolean keepedMethod, String className, String methodName) {
        this.keepedMethod = keepedMethod;
        this.className = className;
        this.methodName = methodName;
    }
}
