package com.suifeng.javaparsertool.operation;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.suifeng.javaparsertool.Main;
import com.suifeng.javaparsertool.support.MethodData;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * 方法相关操作类
 */
public class MethodOp {

    /**
     * 复制方法
     *
     * @param srcMethod
     * @param targetMethod
     */
    public static void cloneMethod(MethodDeclaration srcMethod, MethodDeclaration targetMethod) {
        targetMethod.setParameters(srcMethod.getParameters());
        targetMethod.setBody(srcMethod.getBody().get());
        targetMethod.setType(srcMethod.getType());
        targetMethod.setModifiers(srcMethod.getModifiers());
    }

    /**
     * 遍历方法中的所有调用语句，设置正确的caller
     *
     * @param method
     * @param allMethods
     * @param methodData
     */
    public static void setMethodScope(MethodDeclaration method, ArrayList<String> allMethods, Map<String, MethodData> methodData) {
        new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(MethodCallExpr callExpr, Object arg) {
                super.visit(callExpr, arg);
                String beCalledMethod = callExpr.getName().asString();
                if (allMethods.contains(beCalledMethod)) {
                    String className = methodData.get(beCalledMethod).getBelongToClass();
                    boolean isStatic = methodData.get(beCalledMethod).isStatic();
                    if (isStatic) {
                        callExpr.setScope(new NameExpr(className));
                    } else {
                        callExpr.setScope(new NameExpr("new " + className + "()"));
                    }
                }
            }
        }.visit(method, null);
    }

    /**
     * 修改方法的修饰符
     *
     * @param allMethods
     */
    public static void modifyMethodsModifier(ArrayList<MethodDeclaration> allMethods) {
        Random random = new Random();
        for (MethodDeclaration method : allMethods) {
            int i = random.nextInt(100);
            if (i <= Main.mStaticRatio) {
                NodeList<Modifier> modifierList = new NodeList<>();
                modifierList.add(Modifier.publicModifier());
                modifierList.add(Modifier.staticModifier());
                method.setModifiers(modifierList);
            } else {
                NodeList<Modifier> modifierList = new NodeList<>();
                modifierList.add(Modifier.publicModifier());
                method.setModifiers(modifierList);
            }
        }
    }

    /**
     * 创建MethodData
     *
     * @param allMethods
     * @param allMethodData
     */
    public static void buildMethodData(ArrayList<MethodDeclaration> allMethods, Map<String, MethodData> allMethodData) {
        for (MethodDeclaration method : allMethods) {
            MethodData md = new MethodData(method);
            allMethodData.put(method.getName().asString(), md);
        }
    }
}
