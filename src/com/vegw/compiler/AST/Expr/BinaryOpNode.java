package com.vegw.compiler.AST.Expr;

import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Location;

public class BinaryOpNode extends ExprNode {

    public enum BinaryOp {
        ADD,                 // +
        SUB,                 // -
        MUL,                 // *
        DIV,                 // /
        MOD,                 // %

        ASSIGN,              // =
        EQ,                  // ==
        NE,                  // !=
        GT,                  // >
        LT,                  // <
        GE,                  // >=
        LE,                  // <=

        LS,                  // <<
        RS,                  // >>

        LOG_OR,              // ||
        LOG_AND,             // &&

        BIT_OR,              // |
        BIT_AND,             // &
        XOR                  // ^
    }


    protected BinaryOp operator;
    protected ExprNode left, right;
    protected Type type;

    public BinaryOpNode(ExprNode left, BinaryOp op, ExprNode right) {
        super();
        this.operator = op;
        this.left = left;
        this.right = right;
        super.isAssignable = false;
    }

    public BinaryOpNode(Type t, ExprNode left, BinaryOp op, ExprNode right) {
        super();
        this.operator = op;
        this.left = left;
        this.right = right;
        this.type = t;
        super.isAssignable = false;
    }

    public BinaryOp operator() {
        return this.operator;
    }

    public Type type() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ExprNode left() {
        return left;
    }

    public ExprNode right() {
        return right;
    }

    public void setLeft(ExprNode left) {
        this.left = left;
    }

    public void setRight(ExprNode right) {
        this.right = right;
    }

    @Override
    public Location location() {
        return left.location();
    }

    @Override
    public String toString() {
        String str = "BinaryOp " + "Operator: " + operator;
        return str;
    }

    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
