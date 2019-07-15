package com.suifeng.javaparsertool.support.data;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * ClassData集合，方便管理，增加功能
 */
public class ClassGroup {
    private Map<String, ClassData> mClassDatas;//类名为key保存的map
    private Map<String, InnerClassData> mInnerClassDatas;//内部类或接口的集合
    private ArrayList<ClassOrInterfaceDeclaration> allClasses;
    private List<String> mHoldClass;

    public ClassGroup(ArrayList<ClassOrInterfaceDeclaration> clzDeclarations) throws Exception {
        this.allClasses = clzDeclarations;
        mClassDatas = new HashMap<>();
        mInnerClassDatas = new HashMap<>();
        mHoldClass = new ArrayList<>();
        for (ClassOrInterfaceDeclaration clz : clzDeclarations) {
            buildClassDatas(clz);
        }
    }

    public ClassGroup(TreeMap<ClassOrInterfaceDeclaration, CompilationUnit> treeMap) throws Exception {
        this.allClasses = new ArrayList<>();
        mClassDatas = new HashMap<>();
        mInnerClassDatas = new HashMap<>();
        mHoldClass = new ArrayList<>();
        Iterator<ClassOrInterfaceDeclaration> iterator = treeMap.keySet().iterator();
        while (iterator.hasNext()) {
            ClassOrInterfaceDeclaration next = iterator.next();
            allClasses.add(next);
            buildClassDatas(next, treeMap.get(next));
        }
    }

    private void buildClassDatas(ClassOrInterfaceDeclaration clz) throws Exception {
        //是否内部类或内部接口
        if (clz.isNestedType()) {
            InnerClassData innerClassData = new InnerClassData(clz, this);
            mInnerClassDatas.put(clz.getNameAsString(), innerClassData);
        } else {
            ClassData classData = new ClassData(clz);
            mClassDatas.put(clz.getNameAsString(), classData);
        }
    }

    private void buildClassDatas(ClassOrInterfaceDeclaration clz, CompilationUnit unit) throws Exception {
        //是否内部类或内部接口
        if (clz.isNestedType()) {
            InnerClassData innerClassData = new InnerClassData(clz, this, unit);
            mInnerClassDatas.put(clz.getNameAsString(), innerClassData);
        } else {
            ClassData classData = new ClassData(clz, unit);
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

    public ClassData getClassData(String className) throws Exception {
        return mClassDatas.get(className);
    }

    public void addHoldClass(String holdClz) {
        if (!mHoldClass.contains(holdClz)) {
            mHoldClass.add(holdClz);
        }
    }

    public List<String> getHoldClass() {
        return mHoldClass;
    }

}
