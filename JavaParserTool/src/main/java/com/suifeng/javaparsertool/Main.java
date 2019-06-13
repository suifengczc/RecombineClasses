package com.suifeng.javaparsertool;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.suifeng.javaparsertool.operation.ClassOp;
import com.suifeng.javaparsertool.operation.GenerationOp;
import com.suifeng.javaparsertool.operation.MethodOp;
import com.suifeng.javaparsertool.operation.XmlOp;
import com.suifeng.javaparsertool.support.MethodData;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Main {
    //============配置信息，可通过args修改============
    private static String mSrcPath = "source_in";//源码路径;
    private static String mOutPath = "source_out";//输出路径;
    private static String mPackageName = "com.dmy";//生成的类的包名
    private static String mPreClassName = "ClassName";//输出类名前缀
    private static String mEntryMethodName = "loadInnerSdk";//入口方法名
    private static String mSdkToolConfigName = "SdkToolConfig.xml";

    private static int mClassCount = 5;//默认生成类的个数
    private static int mMethodLowLimit = 2;//每个类中最少包含方法数
    public static float mStaticRatio = 0.8F;//方法设置为static的比例，80表示80%
    //================================================

    private static ArrayList<MethodDeclaration> mAllMethodInAllClassesList; //所有类中的方法集合
    private static ArrayList<String> mAllMethodsNameList = new ArrayList<>();//所有方法名
    private static Map<String, MethodData> mAllMethodDataMap = new HashMap<>();//所有方法的methodData
    private static Map<String, String> mAllStringMap = new HashMap<>();//所有非空字符串的map，用于生成sdkToolConfig.xml

    public static JavaParser mJavaParser;//解析类对象


    public static void main(String[] args) {
        if (args.length > 0) {
            mSrcPath = args[0];
        }

        File projectDir = new File(mSrcPath);
        if (!projectDir.exists()) {
            System.out.println("projectDir is not exist");
            return;
        }

        initJavaParser(projectDir);

//        ParseResult<CompilationUnit> parse = mJavaParser.parse(projectDir + File.separator + "Common.java");
//        CompilationUnit compilationUnit = parse.getResult().get();
//        compilationUnit.findAll(MethodCallExpr.class).forEach(mce -> System.out.println(mce.resol));

        buildAllMethodsList(projectDir);
        MethodOp.modifyMethodsModifier(mAllMethodInAllClassesList);

//        ResolvedType type = JavaParserFacade.get(new JavaParserTypeSolver(projectDir)).getType(mAllMethodInAllClassesList.get(0).getChildNodes().get(0).getParentNode().get());
//        System.out.println(type);
//        MethodOp.modifyMethodsParamsName(projectDir,mAllMethodInAllClassesList);
        MethodOp.buildMethodData(mAllMethodInAllClassesList, mAllMethodDataMap);
        buildAllMethodsNameList();
        buildAllStringsMap();

        if (mAllMethodInAllClassesList.isEmpty()) {
            System.out.println("there is no methods");
            return;
        }
        File outFileDir = new File(mOutPath + File.separator);
        initDir(outFileDir);
        //把所有method乱序
        Collections.shuffle(mAllMethodInAllClassesList);
        //生成每个类的CompilationUnit
        ArrayList<CompilationUnit> allClassFiles = GenerationOp.CompilationUnitGenerate(mClassCount, mPackageName, mPreClassName);
        //分配每个类的方法个数
        Map<Integer, Integer> methodCountMap = ClassOp.getMethodCountInClasses(mAllMethodInAllClassesList.size(), mMethodLowLimit, mClassCount);
        int methodIndex = 0;
        //记录方法被分配到的类
        for (int i = 0; i < allClassFiles.size(); i++) {
            String classFileName = mPreClassName + i;
            int methodCount = methodCountMap.get(i);
            for (int j = 0; j < methodCount; j++) {
                MethodDeclaration srcMethod = mAllMethodInAllClassesList.get(methodIndex++);
                mAllMethodDataMap.get(srcMethod.getName().asString()).setBelongToClass(classFileName);
            }
        }
        XmlOp.buildXml(mOutPath + File.separator + mSdkToolConfigName, mClassCount, mPreClassName, mAllMethodsNameList, mAllStringMap, mAllMethodDataMap, mPackageName, mEntryMethodName);
        methodIndex = 0;
        //把方法添加到类中
        for (int i = 0; i < allClassFiles.size(); i++) {
            String classFileName = mPreClassName + i;
            CompilationUnit classFile = allClassFiles.get(i);
            ClassOrInterfaceDeclaration thisClass = classFile.getClassByName(classFileName).get();
            int methodCount = methodCountMap.get(i);
            //添加方法
            for (int j = 0; j < methodCount; j++) {
                MethodDeclaration srcMethod = mAllMethodInAllClassesList.get(methodIndex);
                MethodDeclaration addMethod = thisClass.addMethod(srcMethod.getName().toString(), Modifier.publicModifier().getKeyword());
                methodIndex++;
                MethodOp.cloneMethod(srcMethod, addMethod);
                MethodOp.setMethodScope(addMethod, mAllMethodsNameList, mAllMethodDataMap);
            }
            if (outFileDir.exists()) {
                ClassOp.generalClassFile(outFileDir, classFileName + ".java", classFile);
            }
        }
    }

    /**
     * 初始化JavaParser
     */
    private static void initJavaParser(File projectDir) {
        ReflectionTypeSolver reflectionTypeSolver = new ReflectionTypeSolver();
        JavaParserTypeSolver javaParserTypeSolver = new JavaParserTypeSolver(projectDir);
        TypeSolver typeSolver = new CombinedTypeSolver();
        ((CombinedTypeSolver) typeSolver).add(reflectionTypeSolver);
        ((CombinedTypeSolver) typeSolver).add(javaParserTypeSolver);
        JavaSymbolSolver solver = new JavaSymbolSolver(typeSolver);
        mJavaParser = new JavaParser();
        mJavaParser.getParserConfiguration().setSymbolResolver(solver);
    }



    /**
     * 构建所有string的map
     */
    public static void buildAllStringsMap() {
        class ForCount {
            int count = 1;
        }
        ForCount forCount = new ForCount();
        //遍历所有字符串
        for (MethodDeclaration method : mAllMethodInAllClassesList) {
            new VoidVisitorAdapter<Object>() {
                @Override
                public void visit(StringLiteralExpr n, Object arg) {
                    String matchStr = n.asString();
                    if (matchStr != null && matchStr.length() > 0 && !matchStr.equals("\"\"") && !":".equals(matchStr) && !"%x".equals(matchStr)) {
                        if ("PluginConfig".equals(matchStr) || "AssetName".equals(matchStr)) {
                            mAllStringMap.put(matchStr, matchStr);
                        } else {
                            mAllStringMap.put(matchStr, "Label" + forCount.count++);
                        }
                    }
                    super.visit(n, arg);
                }
            }.visit(method, null);
        }
        mAllStringMap.put("noIdea", "Label" + forCount.count++);
        mAllStringMap.put("loadAttachContext", "Label" + forCount.count++);
    }

    /**
     * 从所有类中读取全部methods
     *
     * @param projectDir
     */
    public static void buildAllMethodsList(File projectDir) {
        ArrayList<ClassOrInterfaceDeclaration> allClasses = new ArrayList<>();
        ClassOp classOp = new ClassOp();
        classOp.getAllClasses(projectDir, allClasses);
        mAllMethodInAllClassesList = new ArrayList<>();
        //遍历所有类获取其中的所有方法
        if (!allClasses.isEmpty()) {
            for (ClassOrInterfaceDeclaration theClass : allClasses) {
                mAllMethodInAllClassesList.addAll(theClass.getMethods());
            }
        }
    }

    /**
     * 获取所有方法名
     */
    private static void buildAllMethodsNameList() {
        for (MethodDeclaration method : mAllMethodInAllClassesList) {
            mAllMethodsNameList.add(method.getName().asString());
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
