package com.vegw.compiler.Utils;

import com.vegw.compiler.AST.Expr.BinaryOpNode;
import com.vegw.compiler.AST.Expr.UnaryOpNode;
import com.vegw.compiler.Type.Type;

import java.util.HashMap;
import java.util.Map;

public class Mapping {
    final static public Map<String, BinaryOpNode.BinaryOp> BinaryOp = new HashMap<String, BinaryOpNode.BinaryOp>() {{
        put("+", BinaryOpNode.BinaryOp.ADD);
        put("-", BinaryOpNode.BinaryOp.SUB);
        put("*", BinaryOpNode.BinaryOp.MUL);
        put("/", BinaryOpNode.BinaryOp.DIV);
        put("%", BinaryOpNode.BinaryOp.MOD);

        put("=", BinaryOpNode.BinaryOp.ASSIGN);
        put("==", BinaryOpNode.BinaryOp.EQ);
        put("!=", BinaryOpNode.BinaryOp.NE);
        put(">", BinaryOpNode.BinaryOp.GT);
        put("<", BinaryOpNode.BinaryOp.LT);
        put(">=", BinaryOpNode.BinaryOp.GE);
        put("<=", BinaryOpNode.BinaryOp.LE);

        put("<<", BinaryOpNode.BinaryOp.LS);
        put(">>", BinaryOpNode.BinaryOp.RS);

        put("||", BinaryOpNode.BinaryOp.LOG_OR);
        put("&&", BinaryOpNode.BinaryOp.LOG_AND);

        put("|", BinaryOpNode.BinaryOp.BIT_OR);
        put("&", BinaryOpNode.BinaryOp.BIT_AND);
        put("^", BinaryOpNode.BinaryOp.XOR);
    }};
    final static public Map<String, UnaryOpNode.UnaryOp> UnaryOp = new HashMap<String, UnaryOpNode.UnaryOp>() {{
        put("++", UnaryOpNode.UnaryOp.PREP);
        put("--", UnaryOpNode.UnaryOp.PREM);
        put("+", UnaryOpNode.UnaryOp.POS);
        put("-", UnaryOpNode.UnaryOp.NEG);
        put("!", UnaryOpNode.UnaryOp.LOGN);
        put("~", UnaryOpNode.UnaryOp.BITN);
    }};
    final static public Map<String, Type> PrimitiveType = new HashMap<String, Type>(){{
        put("int", Type.INT);
        put("bool", Type.BOOL);
        put("string", Type.STRING);
        put("void", Type.VOID);
    }};
}
