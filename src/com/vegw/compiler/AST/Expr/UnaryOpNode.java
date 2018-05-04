package com.vegw.compiler.AST.Expr;

import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Location;

public class UnaryOpNode extends ExprNode {


    public enum UnaryOp {
         PREP, PREM,
         POSP, POSM,
         POS, NEG,
         LOGN,
         BITN
     }

    protected UnaryOp operator;
    protected ExprNode expr;
    protected Type type;

    public UnaryOpNode(UnaryOp op, ExprNode expr) {
        this.operator = op;
        this.expr = expr;
        super.isAssignable = false;
    }

    public UnaryOp operator() {
        return this.operator;
    }

    public Type type() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
        super.isDetermined = expr.isDetermined;
    }

    public ExprNode expr() {
        return expr;
    }

    @Override
    public Location location() {
        return expr.location();
    }

    @Override
    public String toString() {
        String str = "UnaryOp " + "Operator: " + operator.name();
        return str;
    }

    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

}
