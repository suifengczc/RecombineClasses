package com.suifeng.javaparsertool.support.data;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.google.common.io.MoreFiles;

import java.util.ArrayList;

/**
 * 类的成员变量的数据类
 */
public class FieldData {
    private String mParentName;//变量所在类
    private ArrayList<String> mModifiers;//变量的修饰符
    private FieldDeclaration mFieldDeclaration;//变量数据源
    private String mFieldName;//变量名
    private Object mFieldValue;//变量值

    public static final String MODIFIER_PUBLIC = "public";
    public static final String MODIFIER_PRIVATE = "private";
    public static final String MODIFIER_STATIC = "static";
    public static final String MODIFIER_FINAL = "final";

    public FieldData(FieldDeclaration fieldDeclaration,String clzName) {
        this.mParentName = clzName;
        mModifiers = new ArrayList<>();
        buildModifiers(fieldDeclaration);
        VariableDeclarator variableDeclarator = fieldDeclaration.getVariables().get(0);
        mFieldName = variableDeclarator.getNameAsString();
//        mFieldValue = variableDeclarator.getInitializer().isPresent()?get();
    }

    private void buildModifiers(FieldDeclaration f) {
        NodeList<Modifier> modifiers = f.getModifiers();
        for (Modifier modifier : modifiers) {
            mModifiers.add(modifier.getKeyword().name());
        }
    }

    public boolean isPublic(){
        return mModifiers.contains(Modifier.publicModifier().getKeyword().asString());
    }

    public boolean hasModifier(String m){
        return mModifiers.contains(m);
    }


    public String getFieldName(){
        return mFieldName;
    }


}
