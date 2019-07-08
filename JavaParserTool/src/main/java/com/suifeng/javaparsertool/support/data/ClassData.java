package com.suifeng.javaparsertool.support.data;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;

/**
 * 保存类信息，暂时只取imports和methods信息
 */
public class ClassData {
    private ArrayList<String> imports;//class中导入的import类
    private ArrayList<MethodDeclaration> methods;//class中包含的methods
    private ClassOrInterfaceDeclaration mClassDec;//类的原数据
    private ArrayList<FieldData> mFieldDatas;//成员变量

    public ClassOrInterfaceDeclaration getClassDec() {
        return mClassDec;
    }

    public ClassData(ClassOrInterfaceDeclaration clz) {
        imports = new ArrayList<>();
        methods = new ArrayList<>();
        mFieldDatas = new ArrayList<>();
        mClassDec = clz;
        NodeList<ImportDeclaration> importsList = ((CompilationUnit) clz.getParentNode().get()).getImports();
        for (ImportDeclaration ipt : importsList) {
            this.imports.add(ipt.getName().asString());
        }
        methods.addAll(clz.getMethods());
        new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(FieldDeclaration n, Object arg) {
                mFieldDatas.add(new FieldData(n, clz.getNameAsString()));
                super.visit(n, arg);
            }
        }.visit(clz, null);
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

    public ArrayList<String> getAllMethodsName() {
        ArrayList<String> names = new ArrayList<>();
        for (MethodDeclaration method : methods) {
            String methodName = method.getNameAsString();
            names.add(methodName);
        }
        return names;
    }

    public ArrayList<FieldData> getFieldDatas(){
        return mFieldDatas;
    }

}
