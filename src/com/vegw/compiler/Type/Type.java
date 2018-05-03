package com.vegw.compiler.Type;

public abstract class Type {
    final static public IntegerType INT = new  IntegerType();
    final static public StringType STRING = new StringType();
    final static public BoolType BOOL = new BoolType();
    final static public VoidType VOID = new VoidType();
    final static public NullType NULL = new NullType();

    public abstract boolean isSameType(Type a);
    public abstract String toString();

}
