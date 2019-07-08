package com.suifeng.javaparsertool.operation;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;
import com.suifeng.javaparsertool.Main;
import com.suifeng.javaparsertool.support.DirExplorer;

import java.io.File;
import java.io.FileOutputStream;
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
     * 生成类文件
     *
     * @param outFileDir out path
     * @param className
     * @param classFile
     */
    public static void generalClassFile(File outFileDir, String className, CompilationUnit classFile) {
        try {
            File outClassFile = new File(outFileDir, className);
            outClassFile.createNewFile();
            FileOutputStream fops = new FileOutputStream(outClassFile);
            fops.write(classFile.toString().getBytes());
            fops.flush();
            fops.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回源码目录下所有的类
     *
     * @param projectDir 源码目录
     */
    public static ArrayList<ClassOrInterfaceDeclaration> getAllClasses(File projectDir) {
        ArrayList<ClassOrInterfaceDeclaration> allClasses = new ArrayList<>();
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            System.out.println(Strings.repeat("=", path.length()));
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                        super.visit(n, arg);
                        allClasses.add(n);
                    }
                }.visit(Main.mJavaParser.parse(file).getResult().get(), null);
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).explore(projectDir);
        return allClasses;
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
        int count = classCount - 1;
        while (count >= 1) {
            int rdmInt = random.nextInt(leave);
            methodMap.put(count, rdmInt + lowLimit);
            leave = leave - rdmInt;
            count--;
        }
        methodMap.put(0, leave + lowLimit);
        return methodMap;
    }
}
