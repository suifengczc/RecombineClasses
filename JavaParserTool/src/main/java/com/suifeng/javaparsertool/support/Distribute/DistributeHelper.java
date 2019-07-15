package com.suifeng.javaparsertool.support.Distribute;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.suifeng.javaparsertool.support.config.Config;
import com.suifeng.javaparsertool.support.data.ClassGroup;
import com.suifeng.javaparsertool.support.data.MethodData;
import com.suifeng.javaparsertool.support.data.MethodGroup;
import com.suifeng.javaparsertool.support.utils.RandomUtil;
import com.suifeng.javaparsertool.support.utils.WeightRandom;
import com.suifeng.javaparsertool.support.utils.WeightUnit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * method的分配类，把method分配到指定的class中
 */
public class DistributeHelper {

    private Config mConfig;
    private ClassGroup mClassGroup;
    private MethodGroup mMethodGroup;

    private WeightRandom levelRandom;
    private WeightRandom pathRandom;

    private Map<String, ClassDistribution> mClassDis;
    private ArrayList<MethodDistribution> mMethodDis;

    public DistributeHelper(Config config, ClassGroup classGroup, MethodGroup methodGroup) {
        this.mConfig = config;
        this.mClassGroup = classGroup;
        this.mMethodGroup = methodGroup;

        mClassDis = new HashMap<>();
        mMethodDis = new ArrayList<>();
    }

    public DistributeHelper build() throws Exception {
        generateClassDis();
        generateMethodDis();
        return this;
    }

    /**
     * 生成需要输出的类信息
     *
     * @throws Exception
     */
    private void generateClassDis() throws Exception {
        int classCount = this.mConfig.getClassCount();
        String preClassName = mConfig.getPreClassName();
        String prePackageName = mConfig.getPackageName();
        List<String> keepClass = this.mConfig.getWhiteList().getKeepClass();
        int keepedClzCount = keepClass.size();
        //固定类，不可移动
        for (String clz : keepClass) {
            ClassOrInterfaceDeclaration classDec = this.mClassGroup.getClassData(clz).getClassDec();
            String packageName = classDec.getParentNode().get().findCompilationUnit().get().getPackageDeclaration().get().getNameAsString();
            ClassDistribution classDistribution = new ClassDistribution(clz, true, packageName);
            mClassDis.put(clz, classDistribution);
        }
        if (keepedClzCount > classCount) {
            throw new Exception("keeped class count > config class count");
        }

        List<String> holdClass = mClassGroup.getHoldClass();
        for (String aClass : holdClass) {
            String rndPkgName = getRndPkgName(prePackageName, classCount - keepedClzCount);
            ClassDistribution classDistribution = new ClassDistribution(aClass, false, rndPkgName);
            mClassDis.put(aClass, classDistribution);
        }

        //新增类，随意修改
        int count = classCount - keepedClzCount - holdClass.size();

        for (int i = 0; i < count; i++) {
            String rndPkgName = getRndPkgName(prePackageName, count);
            ClassDistribution classDistribution = new ClassDistribution(preClassName + i, false, rndPkgName);
            mClassDis.put(preClassName + i, classDistribution);
        }
    }

    /**
     * 生成方法的分配策略
     * 1.先按照类最少方法数分配可移动方法到非固定类中
     * 2.分配剩余的可移动方法到所有类中
     * 3.分配不可移动类到原类中
     *
     * @throws Exception
     */
    private void generateMethodDis() throws Exception {
        Map<String, MethodData> allMethodMap = mMethodGroup.getAllMethodMap();
        List<String> canMoveList = allMethodMap.values().stream().filter(it -> it.canMove).map(MethodData::getName).collect(Collectors.toList());
        int size = canMoveList.size();
        int methodLowLimit = mConfig.getMethodLowLimit();
        int classCount = mConfig.getClassCount();
        int keepClzCount = mConfig.getWhiteList().getKeepClass().size();
        //可移动的方法不够分配到非固定类中
        List<String> classNameList = mClassDis.values().stream().filter(it -> !it.keepedClz).map(it -> it.className).collect(Collectors.toList());
        if (methodLowLimit * (classNameList.size()) > size) {
            throw new Exception("method low limit is too big");
        }
        Collections.shuffle(canMoveList);
        int index = 0;
        //先按照最小方法数分配方法到可移动类中
        for (String className : classNameList) {
            ClassDistribution classDistribution = mClassDis.get(className);
            for (int i = 0; i < methodLowLimit; i++) {
                String methodName = canMoveList.get(index++);
                MethodDistribution methodDistribution = new MethodDistribution(false, className, methodName);
                mMethodDis.add(methodDistribution);
                classDistribution.addMethod(methodName);
                mMethodGroup.getMethodData(methodName).setSendToClass(className);
            }
        }
        //剩下的method随机分配到所有类中
        for (int i = index; i < size; i++) {
            String methodName = canMoveList.get(i);
            String className = getRandomClz();
            MethodDistribution methodDistribution = new MethodDistribution(false, className, methodName);
            mMethodDis.add(methodDistribution);
            mClassDis.get(className).addMethod(methodName);
            mMethodGroup.getMethodData(methodName).setSendToClass(className);
        }
        //不可移动method分配到原类中
        List<String> cantMoveList = allMethodMap.values().stream().filter(it -> !it.canMove).map(MethodData::getName).collect(Collectors.toList());
        for (String methodName : cantMoveList) {
            String initialClass = mMethodGroup.getMethodData(methodName).getInitialClass();
            MethodDistribution methodDistribution = new MethodDistribution(true, initialClass, methodName);
            mMethodDis.add(methodDistribution);
            mClassDis.get(initialClass).addMethod(methodName);
            mMethodGroup.getMethodData(methodName).setSendToClass(initialClass);
        }
    }

    /**
     * 随机取一个class名字
     *
     * @return
     */
    private String getRandomClz() {
        int rndInt = RandomUtil.randInt(0, mClassDis.size() - 1);
        String[] strings = (String[]) mClassDis.keySet().toArray(new String[]{});
        return strings[rndInt];
    }

    /**
     * 在prePackageName的基础上随机更多层级
     *
     * @return 生成随机包名
     */
    private String getRndPkgName(String prePkgName, int count) {
        if (this.levelRandom == null) {
            this.levelRandom = new WeightRandom(getLevelUnits(count));
        }
        if (this.pathRandom == null) {
            this.pathRandom = new WeightRandom(getPathUnits(count));
        }
        StringBuffer sb = new StringBuffer(prePkgName);
        WeightUnit levelUnit = levelRandom.getRandomUnit();
        int levelCount = (int) levelUnit.unit;
        for (int i = 0; i < levelCount; i++) {
            String name = (String) pathRandom.getRandomUnit().unit;
            sb.append(".");
            sb.append(name);
        }

        return sb.toString();
    }

    private ArrayList<WeightUnit> getLevelUnits(int count) {
        ArrayList<WeightUnit> rst = new ArrayList<>();
        int levelWeight_0 = RandomUtil.randInt(2, 4);
        int levelWeight_1 = RandomUtil.randInt(2, 4);
        int levelWeight_2 = RandomUtil.randInt(2, 4);
        int levelWeight_3 = RandomUtil.randInt(1, 2);

        rst.add(new WeightUnit(0, levelWeight_0));
        rst.add(new WeightUnit(1, levelWeight_1));
        rst.add(new WeightUnit(2, levelWeight_2));
        rst.add(new WeightUnit(3, levelWeight_3));
        return rst;
    }

    private ArrayList<WeightUnit> getPathUnits(int count) {
        ArrayList<WeightUnit> rst = new ArrayList<>();
        rst.add(new WeightUnit("a", 4));
        rst.add(new WeightUnit("b", 3));
        rst.add(new WeightUnit("c", 2));
        rst.add(new WeightUnit("d", 1));
        return rst;
    }

    public Map<String, ClassDistribution> getClzDistribution() {
        return mClassDis;
    }

    public ArrayList<MethodDistribution> getMethodDistribution() {
        return mMethodDis;
    }

}
