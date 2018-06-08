package com.vegw.compiler.FrontEnd;

import com.vegw.compiler.AST.Expr.*;
import com.vegw.compiler.AST.Expr.Literal.BoolLiteralNode;
import com.vegw.compiler.AST.Expr.Literal.IntegerLiteralNode;
import com.vegw.compiler.AST.Expr.Literal.NullLiteralNode;
import com.vegw.compiler.AST.Expr.Literal.StringLiteralNode;
import com.vegw.compiler.AST.Stmt.*;
import com.vegw.compiler.AST.Stmt.Def.ClassDefNode;
import com.vegw.compiler.AST.Stmt.Def.FunctionDefNode;
import com.vegw.compiler.AST.Stmt.Def.VariableDefNode;

public interface ASTVisitor<S, E> {

    E visit(UnaryOpNode node);
    E visit(BinaryOpNode node);

    E visit(FuncallNode node);

    E visit(BoolLiteralNode node);
    E visit(NullLiteralNode node);
    E visit(IntegerLiteralNode node);
    E visit(StringLiteralNode node);

    E visit(ArefNode node);
    E visit(MemberNode node);
    E visit(VariableNode node);
    E visit(CreatorNode node);


    S visit(BlockNode node);
    S visit(BreakNode node);
    S visit(ContinueNode node);
    S visit(ExprStmtNode node);
    S visit(ForNode node);
    S visit(IfNode node);
    S visit(ReturnNode node);
    S visit(WhileNode node);
    S visit(ClassDefNode node);
    S visit(FunctionDefNode node);
    S visit(VariableDefNode node);
}