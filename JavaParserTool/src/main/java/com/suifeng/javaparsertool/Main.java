package com.suifeng.javaparsertool;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.suifeng.javaparsertool.operation.ClassOp;
import com.suifeng.javaparsertool.operation.GenerationOp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        //配置信息，可通过args修改
        String srcPath = "source_in";//源码路径
        int classCount = 3;//默认生成类的个数
        int methodLowLimit = 2;//每个类中最少包含方法数
        String packageName = "com.dmy";
        String preClassName = "ClassName";

        if (args.length > 0) {
            srcPath = args[0];
        }
        File projectDir = new File(srcPath);
        if (!projectDir.exists()) {
            System.out.println("projectDir is not exist");
            return;
        }
        ArrayList<ClassOrInterfaceDeclaration> allClasses = new ArrayList<>();
        ClassOp classOp = new ClassOp();
        classOp.getAllClasses(projectDir, allClasses);

        ArrayList<MethodDeclaration> allMethodInAllClasses = new ArrayList<>();
        //遍历所有类获取其中的所有方法
        if (!allClasses.isEmpty()) {
            for (ClassOrInterfaceDeclaration theClass : allClasses) {
                allMethodInAllClasses.addAll(theClass.getMethods());
            }
        }

        if (allMethodInAllClasses.isEmpty()) {
            System.out.println("there is no methods");
            return;
        }

        //把所有method乱序
        Collections.shuffle(allMethodInAllClasses);
        //生成每个类的CompilationUnit
        ArrayList<CompilationUnit> allClassFiles = GenerationOp.CompilationUnitGenerate(classCount, packageName, preClassName);
        //分配每个类的方法个数
        Map<Integer, Integer> methodCountMap = ClassOp.getMethodCountInClasses(allMethodInAllClasses.size(), methodLowLimit, classCount);
        int methodIndex = 0;
        //遍历类
        for (int i = 0; i < allClassFiles.size(); i++) {
            CompilationUnit classFile = allClassFiles.get(i);
            ClassOrInterfaceDeclaration thisClass = classFile.getClassByName(preClassName + i).get();
            int methodCount = methodCountMap.get(i);
            //添加方法
            for (int j = 0; j < methodCount; j++) {
                MethodDeclaration srcMethod = allMethodInAllClasses.get(methodIndex);
                methodIndex++;
                MethodDeclaration addMethod = thisClass.addMethod(srcMethod.getName().toString(), Modifier.publicModifier().getKeyword());
                addMethod.setParameters(srcMethod.getParameters());
                addMethod.setBody(srcMethod.getBody().get());
                addMethod.setType(srcMethod.getType());
                addMethod.setModifiers(srcMethod.getModifiers());
            }
            System.out.println(classFile);
        }
    }


}
