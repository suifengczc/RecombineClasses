package com.suifeng.javaparsertool.support;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.ReceiverParameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.SwitchExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleExportsDirective;
import com.github.javaparser.ast.modules.ModuleOpensDirective;
import com.github.javaparser.ast.modules.ModuleProvidesDirective;
import com.github.javaparser.ast.modules.ModuleRequiresDirective;
import com.github.javaparser.ast.modules.ModuleUsesDirective;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.UnparsableStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.IntersectionType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MyVoidVisitorAdapter<A> extends VoidVisitorAdapter<A> {
    @Override
    public void visit(AnnotationDeclaration n, A arg) {
        super.visit(n, arg);
    }


    @Override
    public void visit(AnnotationMemberDeclaration n, A arg) {
        System.out.println("AnnotationMemberDeclaration n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ArrayAccessExpr n, A arg) {
        System.out.println("ArrayAccessExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ArrayCreationExpr n, A arg) {
        System.out.println("ArrayCreationExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ArrayInitializerExpr n, A arg) {
        System.out.println("ArrayInitializerExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(AssertStmt n, A arg) {
        System.out.println("AssertStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(AssignExpr n, A arg) {
        System.out.println("AssignExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(BinaryExpr n, A arg) {
        System.out.println("BinaryExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(BlockComment n, A arg) {
        System.out.println("BlockComment n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(BlockStmt n, A arg) {
        System.out.println("BlockStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(BooleanLiteralExpr n, A arg) {
        System.out.println("BooleanLiteralExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(BreakStmt n, A arg) {
        System.out.println("BreakStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(CastExpr n, A arg) {
        System.out.println("CastExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(CatchClause n, A arg) {
        System.out.println("CatchClause n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(CharLiteralExpr n, A arg) {
        System.out.println("CharLiteralExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ClassExpr n, A arg) {
        System.out.println("ClassExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, A arg) {
        System.out.println("ClassOrInterfaceDeclaration n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ClassOrInterfaceType n, A arg) {
        System.out.println("ClassOrInterfaceType n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(CompilationUnit n, A arg) {
        System.out.println("CompilationUnit n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ConditionalExpr n, A arg) {
        System.out.println("ConditionalExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ConstructorDeclaration n, A arg) {
        System.out.println("ConstructorDeclaration n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ContinueStmt n, A arg) {
        System.out.println("ContinueStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(DoStmt n, A arg) {
        System.out.println("DoStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(DoubleLiteralExpr n, A arg) {
        System.out.println("DoubleLiteralExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(EmptyStmt n, A arg) {
        System.out.println("EmptyStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(EnclosedExpr n, A arg) {
        System.out.println("EnclosedExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(EnumConstantDeclaration n, A arg) {
        System.out.println("EnumConstantDeclaration n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(EnumDeclaration n, A arg) {
        System.out.println("EnumDeclaration n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ExplicitConstructorInvocationStmt n, A arg) {
        System.out.println("ExplicitConstructorInvocationStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ExpressionStmt n, A arg) {
        System.out.println("ExpressionStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(FieldAccessExpr n, A arg) {
        System.out.println("FieldAccessExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(FieldDeclaration n, A arg) {
        System.out.println("FieldDeclaration n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ForEachStmt n, A arg) {
        System.out.println("ForEachStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ForStmt n, A arg) {
        System.out.println("ForStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(IfStmt n, A arg) {
        System.out.println("IfStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(InitializerDeclaration n, A arg) {
        System.out.println("InitializerDeclaration n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(InstanceOfExpr n, A arg) {
        System.out.println("InstanceOfExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(IntegerLiteralExpr n, A arg) {
        System.out.println("IntegerLiteralExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(JavadocComment n, A arg) {
        System.out.println("JavadocComment n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(LabeledStmt n, A arg) {
        System.out.println("LabeledStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(LineComment n, A arg) {
        System.out.println("LineComment n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(LongLiteralExpr n, A arg) {
        System.out.println("LongLiteralExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(MarkerAnnotationExpr n, A arg) {
        System.out.println("MarkerAnnotationExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(MemberValuePair n, A arg) {
        System.out.println("MemberValuePair n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(MethodCallExpr n, A arg) {
        System.out.println("MethodCallExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(MethodDeclaration n, A arg) {
        System.out.println("MethodDeclaration n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(NameExpr n, A arg) {
        System.out.println("NameExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(NormalAnnotationExpr n, A arg) {
        System.out.println("NormalAnnotationExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(NullLiteralExpr n, A arg) {
        System.out.println("NullLiteralExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ObjectCreationExpr n, A arg) {
        System.out.println("ObjectCreationExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(PackageDeclaration n, A arg) {
        System.out.println("PackageDeclaration n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(Parameter n, A arg) {
        System.out.println("Parameter n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(PrimitiveType n, A arg) {
        System.out.println("PrimitiveType n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(Name n, A arg) {
        System.out.println("Name n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(SimpleName n, A arg) {
        System.out.println("SimpleName n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ArrayType n, A arg) {
        System.out.println("ArrayType n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ArrayCreationLevel n, A arg) {
        System.out.println("ArrayCreationLevel n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(IntersectionType n, A arg) {
        System.out.println("IntersectionType n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(UnionType n, A arg) {
        System.out.println("UnionType n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ReturnStmt n, A arg) {
        System.out.println("ReturnStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(SingleMemberAnnotationExpr n, A arg) {
        System.out.println("SingleMemberAnnotationExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(StringLiteralExpr n, A arg) {
        System.out.println("StringLiteralExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(SuperExpr n, A arg) {
        System.out.println("SuperExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(SwitchEntry n, A arg) {
        System.out.println("SwitchEntry n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(SwitchStmt n, A arg) {
        System.out.println("SwitchStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(SynchronizedStmt n, A arg) {
        System.out.println("SynchronizedStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ThisExpr n, A arg) {
        System.out.println("ThisExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ThrowStmt n, A arg) {
        System.out.println("ThrowStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(TryStmt n, A arg) {
        System.out.println("TryStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(LocalClassDeclarationStmt n, A arg) {
        System.out.println("LocalClassDeclarationStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(TypeParameter n, A arg) {
        System.out.println("TypeParameter n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(UnaryExpr n, A arg) {
        System.out.println("UnaryExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(UnknownType n, A arg) {
        System.out.println("UnknownType n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(VariableDeclarationExpr n, A arg) {
        System.out.println("VariableDeclarationExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(VariableDeclarator n, A arg) {
        System.out.println("VariableDeclarator n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(VoidType n, A arg) {
        System.out.println("VoidType n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(WhileStmt n, A arg) {
        System.out.println("WhileStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(WildcardType n, A arg) {
        System.out.println("WildcardType n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(LambdaExpr n, A arg) {
        System.out.println("LambdaExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(MethodReferenceExpr n, A arg) {
        System.out.println("MethodReferenceExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(TypeExpr n, A arg) {
        System.out.println("TypeExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(NodeList n, A arg) {
        System.out.println("NodeList n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ImportDeclaration n, A arg) {
        System.out.println("ImportDeclaration n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ModuleDeclaration n, A arg) {
        System.out.println("ModuleDeclaration n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ModuleRequiresDirective n, A arg) {
        System.out.println("ModuleRequiresDirective n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ModuleExportsDirective n, A arg) {
        System.out.println("ModuleExportsDirective n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ModuleProvidesDirective n, A arg) {
        System.out.println("ModuleProvidesDirective n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ModuleUsesDirective n, A arg) {
        System.out.println("ModuleUsesDirective n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ModuleOpensDirective n, A arg) {
        System.out.println("ModuleOpensDirective n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(UnparsableStmt n, A arg) {
        System.out.println("UnparsableStmt n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ReceiverParameter n, A arg) {
        System.out.println("ReceiverParameter n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(VarType n, A arg) {
        System.out.println("VarType n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(Modifier n, A arg) {
        System.out.println("Modifier n  " + n);
        super.visit(n, arg);
    }

    @Override
    public void visit(SwitchExpr n, A arg) {
        System.out.println("SwitchExpr n  " + n);
        super.visit(n, arg);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

}
