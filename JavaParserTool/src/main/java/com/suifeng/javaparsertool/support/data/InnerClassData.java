package com.suifeng.javaparsertool.support.data;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.suifeng.javaparsertool.support.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内部类或内部接口的信息,目前只考虑接口的情况，不对内部类的情况做处理
 */
public class InnerClassData {
    private ClassOrInterfaceDeclaration mClassDec;//内部类的Declaration
    private String mParentClz;//父类名
    private ClassData mParentClassData;//父类的ClassData

    private ArrayList<String> mImports;//内部类涉及到的导入
    private Map<String, String> mParentImports;//父类的导入

    public InnerClassData(ClassOrInterfaceDeclaration clz, ClassGroup classGroup) {
        mClassDec = clz;
        mParentImports = new HashMap<>();
        mImports = new ArrayList<>();
        mParentClz = ((ClassOrInterfaceDeclaration) clz.getParentNode().get()).getNameAsString();
        mParentClassData = classGroup.getClassData(mParentClz);
        buildInnerImports();
    }

    private void buildImports() {
        ArrayList<String> imports = mParentClassData.getImports();
        for (String anImport : imports) {
            mParentImports.put(anImport.substring(anImport.lastIndexOf(".") + 1), anImport);
        }
    }

    private void buildInnerImports() {
        buildImports();
        List<MethodDeclaration> methods = mClassDec.getMethods();
        for (MethodDeclaration method : methods) {
            String type = method.getType().asString();
            checkAndAddImport(type);
            NodeList<Parameter> parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                String typeAsString = parameter.getTypeAsString();
                checkAndAddImport(typeAsString);
            }
        }
    }

    public ArrayList<String> getImports() {
        return mImports;
    }

    /**
     * 添加import到mImports集合中
     *
     * @param type 类名
     */
    public void checkAndAddImport(String type) {
        String anImport = mParentImports.get(type);
        if (!Utils.isStringEmpty(anImport) && !mImports.contains(anImport)) {
            mImports.add(anImport);
        }
    }


}
