package com.suifeng.javaparsertool.operation;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.suifeng.javaparsertool.support.Distribute.ClassDistribution;
import com.suifeng.javaparsertool.support.Distribute.DistributeHelper;
import com.suifeng.javaparsertool.support.Distribute.MethodDistribution;
import com.suifeng.javaparsertool.support.config.Config;
import com.suifeng.javaparsertool.support.data.ClassData;
import com.suifeng.javaparsertool.support.data.ClassGroup;
import com.suifeng.javaparsertool.support.data.FieldData;
import com.suifeng.javaparsertool.support.data.MethodData;
import com.suifeng.javaparsertool.support.data.MethodGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 生成代码相关类
 */
public class GenerationOp {


    /**
     * 生成每个类文件的CompilationUnit
     *
     * @param classCount   生成类个数
     * @param packageName  类的包名
     * @param preClassName 类名前缀
     * @return
     */
    public static ArrayList<CompilationUnit> compilationUnitGenerate(int classCount, String packageName, String preClassName) {
        ArrayList<CompilationUnit> allClassFiles = new ArrayList<>();
        for (int i = 0; i < classCount; i++) {
            CompilationUnit compilationUnit = new CompilationUnit();
            compilationUnit.setPackageDeclaration(packageName);
            compilationUnit.addClass(preClassName + i, Modifier.publicModifier().getKeyword());
            allClassFiles.add(compilationUnit);
        }
        return allClassFiles;
    }

    public static void generateOutClz(Config config, ClassGroup classGroup, MethodGroup methodGroup) throws Exception {
        DistributeHelper distribute = new DistributeHelper(config, classGroup, methodGroup).build();
        List<ClassDistribution> clzDistribution = distribute.getClzDistribution().values().stream().collect(Collectors.toList());
        ArrayList<MethodDistribution> methodDistribution = distribute.getMethodDistribution();
        Map<String, CompilationUnit> unitMap = new HashMap<>();
        int index = 0;
        //生成类unit
        for (ClassDistribution classDistribution : clzDistribution) {
            String className = classDistribution.className;
            String packageName = classDistribution.packageName;
            ClassData classData = classGroup.getClassData(className);
            CompilationUnit unit = new CompilationUnit(packageName);
            unitMap.put(className, unit);
//            if (classData != null) {
//                ArrayList<String> imports = classData.getImports();
//                //添加import
//                for (String anImport : imports) {
//                    unit.addImport(anImport);
//                }
//            }
            //添加class
            ClassOrInterfaceDeclaration thisClass = unit.addClass(className);
            if (classData != null) {
                ArrayList<FieldData> fieldDatas = classData.getFieldDatas();
                //class添加局部变量
                for (FieldData fieldData : fieldDatas) {
                    Modifier.Keyword[] keywords = fieldData.getModifiers().stream().map(Modifier::getKeyword).collect(Collectors.toList()).toArray(new Modifier.Keyword[]{});
                    thisClass.addField(fieldData.getFieldType(), fieldData.getFieldName(), keywords);
                }
            }
            ArrayList<String> methods = classDistribution.getMethods();
            for (String methodName : methods) {
                MethodData methodData = methodGroup.getMethodData(methodName);
                List<Modifier> modifiers = methodData.getModifiers();
                Keyword[] keywords = convert(modifiers);
                MethodDeclaration targetMethod = thisClass.addMethod(methodName, keywords);//添加方法修饰符
                MethodOp.cloneMethod(methodData, targetMethod);//clone method
                MethodOp.setMethodScope(targetMethod, methodGroup.getAllMethodNames(), methodGroup.getAllMethodMap());//修改method中的自己方法调用关系
                ArrayList<String> imports = methodData.getImports();
                for (String anImport : imports) {
                    unit.addImport(anImport);
                }
            }
            File outPath = new File(config.getOutPath() + File.separator + packageName.replaceAll("\\.", "\\\\") + File.separator);
            ClassOp.generalClassFile(outPath, className + ".java", unit);

        }

//        ArrayList<MethodData> methodList = methodGroup.getAllMethodAsList();
//        for (MethodData methodData : methodList) {
//            String name = methodData.getName();
//            String sendToClass = methodData.getSendToClass();
//        }
    }

    public static Keyword[] convert(List<Modifier> modifiers) {
        int size = modifiers.size();
        Keyword[] keywords = new Keyword[size];
        for (int i = 0; i < size; i++) {
            keywords[i] = modifiers.get(i).getKeyword();
        }
        return keywords;
    }


}
