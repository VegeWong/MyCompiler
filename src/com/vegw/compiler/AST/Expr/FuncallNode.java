package com.vegw.compiler.AST.Expr;

import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Type.FunctionType;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Constants;
import com.vegw.compiler.Utils.Location;

import java.util.List;

public class FuncallNode extends ExprNode {

    protected ExprNode name;
    protected List<ExprNode> params;

    public FuncallNode (ExprNode name, List<ExprNode> params) {
        this.name = name;
        this.params = params;
        super.isAssignable = false;
    }

    public ExprNode name() {
        return name;
    }

    public void setIsAssignable(boolean is) {
        super.isAssignable = is;
    }

    public List<ExprNode> params() {
        return params;
    }

    @Override
    public Location location() {
        return name.location();
    }

    @Override
    public String toString() {
        return "Funcall";
    }

    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    public FunctionType functionType() {
        return (FunctionType)name.type();
    }

    @Override
    public Type type() {
        return functionType().entity().returnType();

    }
    @Override
    public int size() { return Constants.NullSize; }

}
