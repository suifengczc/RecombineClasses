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
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.suifeng.javaparsertool.operation.ClassOp;
import com.suifeng.javaparsertool.operation.GenerationOp;
import com.suifeng.javaparsertool.operation.MethodOp;
import com.suifeng.javaparsertool.operation.XmlOp;
import com.suifeng.javaparsertool.support.Distribute.DistributeHelper;
import com.suifeng.javaparsertool.support.config.Config;
import com.suifeng.javaparsertool.support.data.ClassGroup;
import com.suifeng.javaparsertool.support.data.MethodData;
import com.suifeng.javaparsertool.support.data.MethodGroup;
import com.suifeng.javaparsertool.support.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    private static Map<String, String> mAllStringMap = new HashMap<>();//所有非空字符串的map，用于生成sdkToolConfig.xml
    private static ClassGroup mClassGroup;//所有类信息
    private static MethodGroup mMethodGroup;//所有方法信息

    public static JavaParser mJavaParser;//解析类对象

    public static Config config;//随机配置信息

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.out.println("args length error");
                return;
            }
            Gson gson = new Gson();
            try {
                config = gson.fromJson(args[0], Config.class);
            } catch (JsonSyntaxException e) {
                System.out.println("json error");
                return;
            }

            if (config.getType() == 0) {
                randomForeign();
            } else if (config.getType() == 1) {
                randomQlj();
            } else {
                System.out.println("type error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void randomQlj() {

    }

    private static void randomForeign() throws Exception {
        File projectDir = new File(config.getSrcPath());
        if (!projectDir.exists()) {
            System.out.println("projectDir is not exist");
            return;
        }

        initJavaParser(projectDir);
        buildClassesGroup(projectDir);
        buildMethodGroup();

        buildAllStringsMap();

        if (mClassGroup.getClassCount() <= 0) {
            System.out.println("there is no class");
            return;
        }
        if (mMethodGroup.getMethodCount() <= 0) {
            System.out.println("there is no method");
            return;
        }
        File outFileDir = new File(config.getOutPath() + File.separator);
        initDir(outFileDir);


        GenerationOp.generateOutClz(config, mClassGroup, mMethodGroup);
        /*

        ArrayList<MethodData> allMethods = mMethodGroup.getAllMethodAsList();
        Collections.shuffle(allMethods);//乱序
        //生成每个类的CompilationUnit
        try {
            new DistributeHelper(config, mClassGroup, mMethodGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<CompilationUnit> allClassUnits = GenerationOp.compilationUnitGenerate(config.getClassCount(), config.getPackageName(), config.getPreClassName());
        //分配每个类的方法个数
        Map<Integer, Integer> methodCountMap = ClassOp.getMethodCountInClasses(allMethods.size(), config.getMethodLowLimit(), config.getClassCount());
        int methodIndex = 0;
        //记录方法被分配到的类
        for (int i = 0; i < allClassUnits.size(); i++) {
            String classFileName = config.getPreClassName() + i;
            int methodCount = methodCountMap.get(i);
            for (int j = 0; j < methodCount; j++) {
                MethodData srcMethod = allMethods.get(methodIndex++);
                srcMethod.setSendToClass(classFileName);
            }
        }
        if (!Utils.isStringEmpty(config.getConfigXmlName())) {
            XmlOp.getInstance(config).buildXml(config.getOutPath() + File.separator + config.getConfigXmlName(), config.getClassCount(), config.getPreClassName(), mMethodGroup.getAllMethodNames(), mAllStringMap, mMethodGroup.getAllMethodMap(), config.getPackageName(), config.getEntryMethod());
        }
        methodIndex = 0;
        //把方法添加到类中
        for (int i = 0; i < allClassUnits.size(); i++) {
            String classFileName = config.getPreClassName() + i;
            CompilationUnit classUnit = allClassUnits.get(i);
            ClassOrInterfaceDeclaration thisClass = classUnit.getClassByName(classFileName).get();
            int methodCount = methodCountMap.get(i);
            //添加方法
            for (int j = 0; j < methodCount; j++) {
                MethodData methodData = allMethods.get(methodIndex);
                MethodDeclaration addMethod = thisClass.addMethod(methodData.getName(), Modifier.publicModifier().getKeyword());
                methodIndex++;
                MethodOp.cloneMethod(methodData, addMethod);
                MethodOp.setMethodScope(addMethod, mMethodGroup.getAllMethodNames(), mMethodGroup.getAllMethodMap());
                ArrayList<String> imports = methodData.getImports();
                for (String anImport : imports) {
                    classUnit.addImport(anImport);
                }
            }
            if (outFileDir.exists()) {
                ClassOp.generalClassFile(outFileDir, classFileName + ".java", classUnit);
            }
        }
        */
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
    private static void buildAllStringsMap() {
        class ForCount {
            int count = 1;
        }
        ForCount forCount = new ForCount();
        ArrayList<MethodData> allMethodList = mMethodGroup.getAllMethodAsList();
        //遍历所有方法
        for (MethodData method : allMethodList) {
            new VoidVisitorAdapter<Object>() {
                @Override
                public void visit(StringLiteralExpr n, Object arg) {
                    String matchStr = n.asString();
                    if (matchStr != null && matchStr.length() > 0 && !matchStr.equals("\"\"") && !":".equals(matchStr) && !"%x".equals(matchStr) && !"true".equals(matchStr) && !"false".equals(matchStr)) {
                        if ("PluginConfig".equals(matchStr) || "AssetName".equals(matchStr)) {
                            mAllStringMap.put(matchStr, matchStr);
                        } else {
                            mAllStringMap.put(matchStr, "Label" + forCount.count++);
                        }
                    }
                    super.visit(n, arg);
                }
            }.visit(method.getOriginData(), null);
        }
        mAllStringMap.put("noIdea", "Label" + forCount.count++);
        mAllStringMap.put("loadAttachContext", "Label" + forCount.count++);
    }

    /**
     * 创建所有类的数据集合
     *
     * @param projectDir 资源目录
     */
    private static void buildClassesGroup(File projectDir) throws Exception {
//        ArrayList<ClassOrInterfaceDeclaration> allClasses = ClassOp.getAllClasses(projectDir);
        ArrayList<CompilationUnit> allUnit = ClassOp.getAllUnits(projectDir);
        //把class排序，把内部类或内部接口放到后面，保证在处理内部类时父类已经处理好了
        TreeMap<ClassOrInterfaceDeclaration, CompilationUnit> map = new TreeMap<>((t0, t1) -> {
//            if ((t0.isNestedType() && t1.isNestedType()) || (!t0.isNestedType() && !t1.isNestedType())) {
//                return 0;
//            } else
            if (t0.isNestedType() && !t1.isNestedType()) {
                return 1;
            } else {
                return -1;
            }
        });
        for (CompilationUnit unit : allUnit) {
            new VoidVisitorAdapter<Object>() {
                @Override
                public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                    map.put(n, unit);
                    super.visit(n, arg);
                }
            }.visit(unit, null);
        }

        mClassGroup = new ClassGroup(map);
    }

    /**
     * 创建所有方法的数据集合
     */
    private static void buildMethodGroup() throws Exception {
        mMethodGroup = new MethodGroup(mClassGroup);
    }


    /**
     * 删除文件及目录
     *
     * @param curFile   当前文件
     * @param deleteCur 是否删除当前目录
     */
    private static void deleteDirectory(File curFile, boolean deleteCur) {
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
    private static void initDir(File outFileDir) {
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
