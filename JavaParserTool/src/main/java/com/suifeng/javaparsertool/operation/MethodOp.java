package com.suifeng.javaparsertool.operation;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.suifeng.javaparsertool.support.data.MethodData;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * 方法相关操作类
 */
public class MethodOp {

    /**
     * 复制方法
     *
     * @param srcMethodData method数据源
     * @param targetMethod 目标method
     */
    public static void cloneMethod(MethodData srcMethodData, MethodDeclaration targetMethod) {
        MethodDeclaration srcMethod = srcMethodData.getOriginData();
        targetMethod.setParameters(srcMethod.getParameters());
        targetMethod.setBody(srcMethod.getBody().get());
        targetMethod.setType(srcMethod.getType());
        targetMethod.setAnnotations(srcMethod.getAnnotations());
        targetMethod.setThrownExceptions(srcMethod.getThrownExceptions());
        targetMethod.setAnnotations(srcMethod.getAnnotations());
//        targetMethod.setJavadocComment(srcMethod.getJavadoc().get());
        //修改方法修饰符
        if (srcMethodData.isStatic()) {
            NodeList<Modifier> modifierList = new NodeList<>();
            modifierList.add(Modifier.publicModifier());
            modifierList.add(Modifier.staticModifier());
            targetMethod.setModifiers(modifierList);
        } else {
            NodeList<Modifier> modifierList = new NodeList<>();
            modifierList.add(Modifier.publicModifier());
            targetMethod.setModifiers(modifierList);
        }
    }

    /**
     * 遍历method中的所有调用语句，设置正确的caller
     *
     * @param method 被修改方法
     * @param allMethods 方法名集合
     * @param methodData methodData集合
     */
    public static void setMethodScope(MethodDeclaration method, ArrayList<String> allMethods, Map<String, MethodData> methodData) {
        new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(MethodCallExpr callExpr, Object arg) {
                super.visit(callExpr, arg);
                String beCalledMethod = callExpr.getName().asString();
                if (allMethods.contains(beCalledMethod)) {
                    String className = methodData.get(beCalledMethod).getSendToClass();
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

    public static void modifyMethodsParamsName(File projectDir, ArrayList<MethodDeclaration> allMethods) {
        for (MethodDeclaration method : allMethods) {
            String methodName = method.getName().asString();
            new VoidVisitorAdapter<Object>() {
                @Override
                public void visit(Parameter parameter, Object arg) {
                    super.visit(parameter, arg);
                    String parameterName = parameter.getName().asString();
                    parameter.setName(methodName + "_" + parameterName);
                }

                @Override
                public void visit(ExpressionStmt n, Object arg) {
                    super.visit(n, arg);
//                    System.out.println(n);
                    Expression expression = n.getExpression();
                    if (expression instanceof VariableDeclarationExpr) {
//                        System.out.println(expression);
                        NodeList<VariableDeclarator> variables = expression.asVariableDeclarationExpr().getVariables();
                        for (VariableDeclarator variable : variables) {
                            SimpleName simpleName = variable.getName();
                            String name = simpleName.getIdentifier();
                            simpleName.setIdentifier(methodName + "_" + name);
                        }
                    } else if (expression instanceof MethodCallExpr) {

                        JavaSymbolSolver javaSymbolSolver = new JavaSymbolSolver(new TypeSolver() {
                            @Override
                            public TypeSolver getParent() {
                                return null;
                            }

                            @Override
                            public void setParent(TypeSolver parent) {

                            }

                            @Override
                            public SymbolReference<ResolvedReferenceTypeDeclaration> tryToSolveType(String name) {
                                return null;
                            }
                        });


                    } else if (expression instanceof AssignExpr) {
                        Expression target = ((AssignExpr) expression).getTarget();
                        if (target instanceof ArrayAccessExpr) {
                            NameExpr nameExpr = ((ArrayAccessExpr) target).getName().asNameExpr();
                            String name = nameExpr.getNameAsString();
                            nameExpr.setName(methodName + "_" + name);
                        } else if (target instanceof NameExpr) {
                            SimpleName simpleName = ((NameExpr) target).getName();
                            String name = simpleName.asString();
                            simpleName.setIdentifier(methodName + "_" + name);
                        } else {
                            System.out.println("nameExpr else ---> " + "expression instanceof AssignExpr");
                        }
                    } else if (expression instanceof UnaryExpr) {

                    } else {
//                        System.out.println(expression.getClass().toString());
                    }
                }

                @Override
                public void visit(VariableDeclarationExpr n, Object arg) {
                    super.visit(n, arg);
//                    System.out.println(n);
//                    NodeList<VariableDeclarator> variables = n.getVariables();
//                    if (!variables.isEmpty()) {
//                        for (VariableDeclarator variable : variables) {
//                            String varNama = variable.getName().asString();
//                            variable.setName(methodName + "_" + varNama);
//                        }
//                    }
                }
            }.visit(method, null);
        }

    }

}
