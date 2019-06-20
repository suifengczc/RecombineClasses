package com.suifeng.javaparsertool.support.data;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.ArrayList;

/**
 * 保存类信息，暂时只取imports和methods信息
 */
public class ClassData {
    private ArrayList<String> imports;//class中导入的import类
    private ArrayList<MethodDeclaration> methods;//class中包含的methods

    public ClassData(ClassOrInterfaceDeclaration clz) {
        imports = new ArrayList<>();
        methods = new ArrayList<>();
        NodeList<ImportDeclaration> importsList = ((CompilationUnit) clz.getParentNode().get()).getImports();
        for (ImportDeclaration ipt : importsList) {
            this.imports.add(ipt.getName().asString());
        }
        methods.addAll(clz.getMethods());
    }

    public void addImport(String ipt) {
        if (!imports.contains(ipt)) {
            imports.add(ipt);
        }
    }

    public ArrayList<String> getImports() {
        return imports;
    }

    public String getImport(int index) {
        return imports.get(index);
    }

    public ArrayList<MethodDeclaration> getMethods() {
        return methods;
    }

}
