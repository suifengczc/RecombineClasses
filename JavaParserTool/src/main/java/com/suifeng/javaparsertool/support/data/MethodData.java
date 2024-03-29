package com.suifeng.javaparsertool.support.data;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.suifeng.javaparsertool.Main;
import com.suifeng.javaparsertool.support.utils.RandomUtil;
import com.suifeng.javaparsertool.support.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 方法数据类,方便获取MethodDeclaration的参数
 */
public class MethodData {
    private NodeList<Modifier> mModifiers;//方法的修饰符
    private boolean isStatic;//随机后是否static方法
    private String sendToClass;//随机后方法属于的类
    private String initialClass;//初始所在类
    private NodeList<Parameter> parameters;//方法的传参list
    private MethodDeclaration mMethodDeclaration;
    private ArrayList<String> mImports;//当前方法需要import的类
    private String mMethodName;//方法名称
    private ClassData mClassData;//初始所在类的ClassData
    private HashMap<String, String> mClassImports;//初始类的imports
    public boolean canMove = true; // method是否可以移动到其他类中,不使用任何类私有变量或方法


    public MethodData(MethodDeclaration methodDeclaration, ClassGroup classGroup) throws Exception {
        this.mMethodDeclaration = methodDeclaration;
        buildMethodData(methodDeclaration, classGroup);
    }

    /**
     * @param methodDeclaration 方法定义
     * @param classGroup        所有类数据
     */
    private void buildMethodData(MethodDeclaration methodDeclaration, ClassGroup classGroup) throws Exception {
        mModifiers = methodDeclaration.getModifiers();
        mMethodName = methodDeclaration.getNameAsString();
        //指定随机后方法是否static
        if (RandomUtil.randFloat() <= Main.config.getStaticRatio() || Main.config.getStaticMethod().contains(mMethodName)) {
            isStatic = true;
        } else {
            isStatic = false;
        }
        initialClass = ((TypeDeclaration) methodDeclaration.getParentNode().get()).getName().asString();
        mClassData = classGroup.getClassData(initialClass);
        mClassImports = new HashMap<>();
        mImports = new ArrayList<>();
        buildImports();
        parameters = methodDeclaration.getParameters();
        ArrayList<FieldData> fieldDatas = mClassData.getFieldDatas();
        String methodBody = methodDeclaration.getBody().get().toString();
        if (Main.config.getWhiteList().getKeepMethod().contains(mMethodName) || hasModifier(Modifier.privateModifier()) || hasModifier(Modifier.protectedModifier())) {
            //配置列表中设置了不可移动或者method本身是private的或者method本身是protected
            canMove = false;
        } else {
            for (FieldData fieldData : fieldDatas) {
                if (fieldData.hasModifier(Modifier.privateModifier()) || fieldData.hasModifier(Modifier.protectedModifier())) {
                    String fieldName = fieldData.getFieldName();
                    if (methodBody.contains(fieldName)) {
                        canMove = false;//使用了类私有成员变量的方法不可以移动
                        break;
                    }
                }
            }
            ArrayList<MethodDeclaration> methods = mClassData.getMethods();
            for (MethodDeclaration method : methods) {
                //判断是否调用了当前类的其他private或protected方法
                NodeList<Modifier> modifiers = method.getModifiers();
                boolean isPrivate = false;
                for (Modifier modifier : modifiers) {
                    if (modifier.equals(Modifier.privateModifier()) || modifier.equals(Modifier.protectedModifier())) {
                        isPrivate = true;
                    }
                }
                if (isPrivate && methodBody.contains(method.getNameAsString())) {
                    canMove = false;//使用了私有方法的method不可以移动
                    break;
                }
            }
        }
        if (!canMove) {
            classGroup.addHoldClass(initialClass);
        }

    }

    /**
     * 创建method使用的import集合
     */
    private void buildImports() {
        buildClassImports();
        //遍历方法体获取所有使用到类的地方
        new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(FieldAccessExpr n, Object arg) {
                String fieldExpr = n.toString();
                String scope = fieldExpr.substring(0, fieldExpr.indexOf("."));
                checkAndAddImport(scope);
                super.visit(n, arg);
            }

            @Override
            public void visit(MethodCallExpr n, Object arg) {
                //多层调用下getScope不准确
                if (n.getScope().isPresent()) {
                    String expression = n.toString();
                    String scope = expression.substring(0, expression.indexOf("."));
                    checkAndAddImport(scope);
                }
//                System.out.println("method call scope = " + scope);
                super.visit(n, arg);
            }

            @Override
            public void visit(ClassOrInterfaceType n, Object arg) {
//                System.out.println("ClassOrInterfaceType = " + n);
                String type = n.getNameAsString();
                checkAndAddImport(type);
                super.visit(n, arg);
            }

        }.visit(mMethodDeclaration, null);

        String returnType = mMethodDeclaration.getType().asString();
        checkAndAddImport(returnType);
        NodeList<ReferenceType> thrownExceptions = mMethodDeclaration.getThrownExceptions();
        for (ReferenceType exception : thrownExceptions) {
            String exceptionType = ((ClassOrInterfaceType) exception).getNameAsString();
            checkAndAddImport(exceptionType);
        }

        NodeList<Parameter> parameters = mMethodDeclaration.getParameters();
        for (Parameter parameter : parameters) {
            String parameterType = parameter.getTypeAsString();
            checkAndAddImport(parameterType);
        }

    }

    /**
     * 把import以类名为key存储
     */
    public void buildClassImports() {
        ArrayList<String> imports = mClassData.getImports();
        for (String anImport : imports) {
            try {
                mClassImports.put(anImport.substring(anImport.lastIndexOf(".") + 1), anImport);//取类名为key保存
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加import到mImports集合中
     *
     * @param type 类名
     */
    public void checkAndAddImport(String type) {
        String anImport = mClassImports.get(type);
        if (!Utils.isStringEmpty(anImport) && !mImports.contains(anImport)) {
            mImports.add(anImport);
        }
    }

    public boolean hasModifier(Modifier modifier) {
        return mModifiers.contains(modifier);
    }

    public List<Modifier> getModifiers() {
        return mModifiers;
    }

    public String getInitialClass() {
        return initialClass;
    }

    public String getName() {
        return mMethodName;
    }

    public ArrayList<String> getImports() {
        return mImports;
    }

    public String getSendToClass() {
        return sendToClass;
    }

    public void setSendToClass(String sendToClass) {
        this.sendToClass = sendToClass;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean b) {
        this.isStatic = b;
    }

    public NodeList<Parameter> getParameters() {
        return parameters;
    }

    public MethodDeclaration getOriginData() {
        return mMethodDeclaration;
    }

}
