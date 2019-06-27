package com.suifeng.javaparsertool.support.data;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * ClassData集合，方便管理，增加功能
 */
public class ClassGroup {
    private Map<String, ClassData> mClassDatas;//类名为key保存的map
    private Map<String, InnerClassData> mInnerClassDatas;//内部类或接口的集合
    private ArrayList<ClassOrInterfaceDeclaration> allClasses;

    public ClassGroup(ArrayList<ClassOrInterfaceDeclaration> clzDeclarations) {
        this.allClasses = clzDeclarations;
        mClassDatas = new HashMap<>();
        mInnerClassDatas = new HashMap<>();
        for (ClassOrInterfaceDeclaration clz : clzDeclarations) {
            buildClassDatas(clz);
        }
    }

    private void buildClassDatas(ClassOrInterfaceDeclaration clz) {
        if (clz.isNestedType()) {
            InnerClassData innerClassData = new InnerClassData(clz,this);
            mInnerClassDatas.put(clz.getNameAsString(), innerClassData);
        } else {
            ClassData classData = new ClassData(clz);
            mClassDatas.put(clz.getNameAsString(), classData);
        }
    }

    public int getClassCount() {
        return mClassDatas.size();
    }

    public ArrayList<MethodDeclaration> getAllMethods() {
        ArrayList<MethodDeclaration> allMethods = new ArrayList<>();
        Iterator<String> iterator = mClassDatas.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            ClassData classData = mClassDatas.get(next);
            allMethods.addAll(classData.getMethods());
        }
        return allMethods;
    }

    public ClassData getClassData(String className) {
        return mClassDatas.get(className);
    }

}
