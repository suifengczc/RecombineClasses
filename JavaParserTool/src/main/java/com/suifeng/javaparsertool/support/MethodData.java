package com.suifeng.javaparsertool.support;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;

/**
 * 方法数据类,方便获取MethodDeclaration的参数
 */
public class MethodData {
    private NodeList<Modifier> modifiers;
    private boolean isStatic;
    private String belongToClass;//随机后方法属于的类

    public MethodData(MethodDeclaration methodDeclaration) {
        buildMethodData(methodDeclaration);
    }

    private void buildMethodData(MethodDeclaration methodDeclaration) {
        modifiers = methodDeclaration.getModifiers();
        isStatic = false;
        for (Modifier modifier : modifiers) {
            if (Modifier.staticModifier().equals(modifier)) {
                isStatic = true;
            }
        }
    }

    public String getBelongToClass() {
        return belongToClass;
    }

    public void setBelongToClass(String belongToClass) {
        this.belongToClass = belongToClass;
    }

    public boolean isStatic() {
        return isStatic;
    }

}
