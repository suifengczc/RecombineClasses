package com.suifeng.javaparsertool.operation;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;

import java.util.ArrayList;

/**
 * 生成代码相关类
 */
public class GenerationOp {

    /**
     * 生成每个类文件的CompilationUnit
     *
     * @param classCount   生成类个数
     * @param packageName  类的包名
     * @param preClassName 类名前缀
     * @return
     */
    public static ArrayList<CompilationUnit> CompilationUnitGenerate(int classCount, String packageName, String preClassName) {
        ArrayList<CompilationUnit> allClassFiles = new ArrayList<>();
        for (int i = 0; i < classCount; i++) {
            CompilationUnit compilationUnit = new CompilationUnit();
            compilationUnit.setPackageDeclaration(packageName);
            compilationUnit.addClass(preClassName + i, Modifier.publicModifier().getKeyword());
            allClassFiles.add(compilationUnit);
        }
        return allClassFiles;
    }

}
