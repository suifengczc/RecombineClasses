package com.suifeng.javaparsertool.support.data;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 类的成员变量的数据类
 */
public class FieldData {
    private String mParentName;//变量所在类
    //    private ArrayList<String> mModifiers;//变量的修饰符
    private ArrayList<Modifier> mModifiers;//变量的修饰符
    private FieldDeclaration mFieldDeclaration;//变量数据源
    private String mFieldName;//变量名
    private String mType;//变量类型
    private Object mFieldValue;//变量值

    public static final String MODIFIER_PUBLIC = "public";
    public static final String MODIFIER_PRIVATE = "private";
    public static final String MODIFIER_PROTECTED = "protected";
    public static final String MODIFIER_STATIC = "static";
    public static final String MODIFIER_FINAL = "final";

    public FieldData(FieldDeclaration fieldDeclaration, String clzName) {
        this.mParentName = clzName;
        mModifiers = new ArrayList<>();
        buildModifiers(fieldDeclaration);
        VariableDeclarator variableDeclarator = fieldDeclaration.getVariables().get(0);
        mFieldName = variableDeclarator.getNameAsString();
        mType = variableDeclarator.getTypeAsString();
//        mFieldValue = variableDeclarator.getInitializer().isPresent()?get();
    }

    private void buildModifiers(FieldDeclaration f) {
        NodeList<Modifier> modifiers = f.getModifiers();
        for (Modifier modifier : modifiers) {
            mModifiers.add(modifier);
        }
    }

    public boolean hasModifier(Modifier m) {
        return mModifiers.contains(m);
    }

    public ArrayList<Modifier> getModifiers() {
        return mModifiers;
    }

    public String getFieldName() {
        return mFieldName;
    }

    public String getFieldType() {
        return mType;
    }


}
