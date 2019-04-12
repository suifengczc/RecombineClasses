package com.suifeng.javaparsertool.operation;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;
import com.suifeng.javaparsertool.support.DirExplorer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 类相关操作类
 */
public class ClassOp {

    /**
     * 获取源码目录下所有的类
     *
     * @param projectDir 源码目录
     * @param allClasses ArrayList存储所有类信息
     */
    public void getAllClasses(File projectDir, ArrayList<ClassOrInterfaceDeclaration> allClasses) {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            System.out.println(Strings.repeat("=", path.length()));
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                        super.visit(n, arg);
                        System.out.println(" * " + n.getName());
                        allClasses.add(n);
                    }
                }.visit(new JavaParser().parse(file).getResult().get(), null);
                System.out.println(); // empty line
            } catch (IOException e) {
//                new RuntimeException(e);
                e.printStackTrace();
            }
        }).explore(projectDir);
    }

    /**
     * 随机分配每个类中的方法个数
     *
     * @param methodCount 总方法数
     * @param lowLimit    类中最少方法个数
     * @param classCount  生成类个数
     * @return
     */
    public static Map<Integer, Integer> getMethodCountInClasses(int methodCount, int lowLimit, int classCount) {
        Map<Integer, Integer> methodMap = new HashMap<>();
        int leave = methodCount - lowLimit * classCount;
        Random random = new Random();
        classCount = classCount--;
        while (classCount >= 1) {
            int rdmInt = random.nextInt(leave);
            methodMap.put(classCount, rdmInt + lowLimit);
            leave = leave - rdmInt;
            classCount--;
        }
        methodMap.put(0, leave + lowLimit);
        return methodMap;
    }
}
