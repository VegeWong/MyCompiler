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
import com.vegw.compiler.Entity.VariableEntity;

import java.util.List;

public class Visitor implements ASTVisitor<Void, Void> {

    public void visit(ExprNode node) {
        node.accept(this);
    }

    public void visit(StmtNode node) {
        node.accept(this);
    }

    @Override
    public Void visit(UnaryOpNode node) {
            visit(node.expr());
        return null;
    }

    @Override
    public Void visit(BinaryOpNode node) {
            visit(node.left());
            visit(node.right());
        return null;
    }

    @Override
    public Void visit(FuncallNode node) {
        visit(node.name());
        for (ExprNode param : node.params()) {
            visit(param);
        }
        return null;
    }

    @Override
    public Void visit(BoolLiteralNode node) {
        return null;
    }

    @Override
    public Void visit(NullLiteralNode node) {
        return null;
    }

    @Override
    public Void visit(IntegerLiteralNode node) {
        return null;
    }

    @Override
    public Void visit(StringLiteralNode node) {
        return null;
    }

    @Override
    public Void visit(ArefNode node) {
        visit(node.base());
        visit(node.index());
        return null;
    }

    @Override
    public Void visit(MemberNode node) {
        visit(node.field());
        return null;
    }

    @Override
    public Void visit(VariableNode node) {
        return null;
    }

    @Override
    public Void visit(CreatorNode node) {
        for (ExprNode expr : node.dimensionExpr()) {
            visit(expr);
        }
        return null;
    }

    @Override
    public Void visit(BlockNode node) {
        for (StmtNode stmt : node.stmts())
            visit(stmt);
        return null;
    }

    @Override
    public Void visit(BreakNode node) {
        return null;
    }

    @Override
    public Void visit(ContinueNode node) {
        return null;
    }

    @Override
    public Void visit(ExprStmtNode node) {
        visit(node.expr());
        return null;
    }

    @Override
    public Void visit(ForNode node) {
        if (node.init() != null)
            visit(node.init());
        if (node.cond() != null)
            visit(node.cond());
        if (node.step() != null)
            visit(node.step());
        if (node.body() != null)
            visit(node.body());
        return null;
    }

    @Override
    public Void visit(IfNode node) {
        if (node.cond() != null)
            visit(node.cond());
        if (node.thenBody() != null)
            visit(node.thenBody());
        if (node.elseBody() != null)
            visit(node.elseBody());
        return null;
    }

    @Override
    public Void visit(ReturnNode node) {
        if (node.expr() != null) {
            visit(node.expr());
        }
        return null;
    }

    @Override
    public Void visit(WhileNode node) {
        visit(node.cond());
        if (node.body() != null)
            visit(node.body());
        return null;
    }

    @Override
    public Void visit(ClassDefNode node) {
        for (VariableDefNode var : node.entity().vars()) {
            visit(var);
        }
        for (FunctionDefNode func : node.entity().funcs()) {
            visit(func);
        }
        if (node.entity().constructor() != null)
            visit(node.entity().constructor());
        return null;
    }

    @Override
    public Void visit(FunctionDefNode node) {
        visit(node.entity().body());
        return null;
    }

    @Override
    public Void visit(VariableDefNode node) {
        if (node.entity().value() != null)
            visit(node.entity().value());
        return null;
    }
}
