package com.suifeng.javaparsertool.support.data;

import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * MethodData管理类
 */
public class MethodGroup {

    private ArrayList<String> allMethodNames;//所有方法的方法名
    private Map<String, MethodData> mMethodDatas;//以方法名为key保存的MethodData

    public MethodGroup(ArrayList<MethodDeclaration> allMethod,ClassGroup classGroup) {
        allMethodNames = new ArrayList<>();
        mMethodDatas = new HashMap<>();
        buildMethodDatas(allMethod,classGroup);
    }

    private void buildMethodDatas(ArrayList<MethodDeclaration> allMethod,ClassGroup classGroup) {
        for (MethodDeclaration methodDeclaration : allMethod) {
            MethodData methodData = new MethodData(methodDeclaration, classGroup);
            String methodName = methodDeclaration.getNameAsString();
            mMethodDatas.put(methodName, methodData);
            allMethodNames.add(methodName);
        }
    }

    public MethodData getMethodData(String methodName) {
        return mMethodDatas.get(methodName);
    }

    public ArrayList<MethodData> getAllMethodAsList() {
        return new ArrayList<>(mMethodDatas.values());
    }

    public Map<String, MethodData> getAllMethodMap() {
        return mMethodDatas;
    }

    public int getMethodCount() {
        return mMethodDatas.size();
    }

    public ArrayList<String> getAllMethodNames() {
        return allMethodNames;
    }
}
