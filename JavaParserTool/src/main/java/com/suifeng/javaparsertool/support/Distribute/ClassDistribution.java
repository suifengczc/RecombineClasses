package com.suifeng.javaparsertool.support.Distribute;

import java.util.ArrayList;

public class ClassDistribution {
     public boolean keepedClz;
     public String packageName;
     public String className;
     public ArrayList<String> methodList;

    public ClassDistribution(String className,boolean keepedClz,String packageName) {
        this.className = className;
        this.keepedClz = keepedClz;
        this.packageName = packageName;
        methodList = new ArrayList<>();
    }

    public void addMethod(String methodName) {
        methodList.add(methodName);
    }

    public ArrayList<String> getMethods() {
        return methodList;
    }

}
