package com.suifeng.javaparsertool;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.suifeng.javaparsertool.operation.ClassOp;
import com.suifeng.javaparsertool.operation.GenerationOp;
import com.suifeng.javaparsertool.operation.MethodOp;
import com.suifeng.javaparsertool.support.MethodData;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Main {
    //配置信息，可通过args修改
    private static String mSrcPath = "source_in";//源码路径;
    private static String mOutPath = "source_out";//输出路径;
    private static String mPackageName = "com.dmy";//生成的类的包名
    private static String mPreClassName = "ClassName";//输出类名前缀

    private static int mClassCount = 3;//默认生成类的个数
    private static int mMethodLowLimit = 2;//每个类中最少包含方法数
    public static int mStaticRatio = 30;//方法设置为static的比例，30表示30%

    private static ArrayList<MethodDeclaration> mAllMethodInAllClasses; //所有类中的方法集合
    private static ArrayList<String> mAllMethodsName = new ArrayList<>();//所有方法名
    private static Map<String, MethodData> mAllMethodData = new HashMap<>();

    public static void main(String[] args) {
        if (args.length > 0) {
            mSrcPath = args[0];
        }
        File projectDir = new File(mSrcPath);
        if (!projectDir.exists()) {
            System.out.println("projectDir is not exist");
            return;
        }

        generalAllMethodsList(projectDir);
        MethodOp.modifyMethodsModifier(mAllMethodInAllClasses);
        MethodOp.buildMethodData(mAllMethodInAllClasses, mAllMethodData);
        getAllMethodsName();

        if (mAllMethodInAllClasses.isEmpty()) {
            System.out.println("there is no methods");
            return;
        }
        File outFileDir = new File(mOutPath + File.separator);
        File tempFileDir = new File(mOutPath + File.separator);
        initDir(outFileDir);
        initDir(tempFileDir);

        //把所有method乱序
        Collections.shuffle(mAllMethodInAllClasses);
        //生成每个类的CompilationUnit
        ArrayList<CompilationUnit> allClassFiles = GenerationOp.CompilationUnitGenerate(mClassCount, mPackageName, mPreClassName);
        //分配每个类的方法个数
        Map<Integer, Integer> methodCountMap = ClassOp.getMethodCountInClasses(mAllMethodInAllClasses.size(), mMethodLowLimit, mClassCount);
        int methodIndex = 0;
        //记录方法被分配到的类
        for (int i = 0; i < allClassFiles.size(); i++) {
            String classFileName = mPreClassName + i;
            int methodCount = methodCountMap.get(i);
            for (int j = 0; j < methodCount; j++) {
                MethodDeclaration srcMethod = mAllMethodInAllClasses.get(methodIndex++);
                mAllMethodData.get(srcMethod.getName().asString()).setBelongToClass(classFileName);
            }
        }
        methodIndex = 0;
        //把方法添加到类中
        for (int i = 0; i < allClassFiles.size(); i++) {
            String classFileName = mPreClassName + i;
            CompilationUnit classFile = allClassFiles.get(i);
            ClassOrInterfaceDeclaration thisClass = classFile.getClassByName(classFileName).get();
            int methodCount = methodCountMap.get(i);
            //添加方法
            for (int j = 0; j < methodCount; j++) {
                MethodDeclaration srcMethod = mAllMethodInAllClasses.get(methodIndex);
                MethodDeclaration addMethod = thisClass.addMethod(srcMethod.getName().toString(), Modifier.publicModifier().getKeyword());
                methodIndex++;
                MethodOp.cloneMethod(srcMethod, addMethod);
                MethodOp.setMethodScope(addMethod, mAllMethodsName, mAllMethodData);
            }
            if (tempFileDir.exists()) {
                ClassOp.generalClassFile(tempFileDir, classFileName + ".java", classFile);
            }
        }
    }

    /**
     * 从所有类中读取全部methods
     *
     * @param projectDir
     */
    public static void generalAllMethodsList(File projectDir) {
        ArrayList<ClassOrInterfaceDeclaration> allClasses = new ArrayList<>();
        ClassOp classOp = new ClassOp();
        classOp.getAllClasses(projectDir, allClasses);
        mAllMethodInAllClasses = new ArrayList<>();
        //遍历所有类获取其中的所有方法
        if (!allClasses.isEmpty()) {
            for (ClassOrInterfaceDeclaration theClass : allClasses) {
                mAllMethodInAllClasses.addAll(theClass.getMethods());
            }
        }
    }

    /**
     * 获取所有方法名
     */
    private static void getAllMethodsName() {
        for (MethodDeclaration method : mAllMethodInAllClasses) {
            mAllMethodsName.add(method.getName().asString());
        }
    }

    /**
     * 删除文件及目录
     *
     * @param curFile   当前文件
     * @param deleteCur 是否删除当前目录
     */
    public static void deleteDirectory(File curFile, boolean deleteCur) {
        if (curFile.isDirectory()) {
            File[] files = curFile.listFiles();
            for (File file : files) {
                deleteDirectory(file, true);
            }
            if (deleteCur) {
                curFile.delete();
            }
        } else {
            curFile.delete();
        }
    }

    /**
     * 初始化输出路径，有文件就删掉
     *
     * @param outFileDir
     */
    public static void initDir(File outFileDir) {
        if (outFileDir.exists()) {
            File[] files = outFileDir.listFiles();
            if (files != null && files.length > 0) {
                deleteDirectory(outFileDir, false);
            }
        } else {
            outFileDir.mkdirs();
        }
    }


}
