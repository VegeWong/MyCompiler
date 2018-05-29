package com.vegw.compiler.Utils;

import com.vegw.compiler.AST.Expr.Literal.BoolLiteralNode;
import com.vegw.compiler.AST.Expr.Literal.IntegerLiteralNode;
import com.vegw.compiler.AST.Expr.Literal.StringLiteralNode;
import com.vegw.compiler.AST.Stmt.BlockNode;
import com.vegw.compiler.AST.Stmt.ReturnNode;
import com.vegw.compiler.AST.Stmt.StmtNode;
import com.vegw.compiler.Entity.FunctionEntity;
import com.vegw.compiler.Entity.ParameterEntity;
import com.vegw.compiler.Type.Type;

import java.util.*;

public class BuiltinFunction {
    protected static final Map<String, FunctionEntity> builtinFunctions = new HashMap<String, FunctionEntity>(){{
        Location loc = new Location(0, 0);

        // ========================= String Builtin Function ==================================
        List<StmtNode> lengthStmts = new LinkedList<StmtNode>() {{
            add(new ReturnNode(loc, new IntegerLiteralNode(loc, 0)));
        }};
        BlockNode lengthBody = new BlockNode(loc, lengthStmts);
        FunctionEntity length = new FunctionEntity(loc, "__STRING__.__FUNC__length", Type.INT, new LinkedList<>(), lengthBody);
        put("__STRING__.__FUNC__length", length);

        // Add "string.substring()" function
        List<StmtNode> substringStmts = new LinkedList<StmtNode>() {{
            add(new ReturnNode(loc, new StringLiteralNode(loc, " ")));
        }};
        BlockNode substringBody = new BlockNode(loc, substringStmts);
        List<ParameterEntity> substringParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "left",Type.INT));
            add(new ParameterEntity(loc, "right",Type.INT));
        }};
        FunctionEntity substring = new FunctionEntity(loc, "__STRING__.__FUNC__substring", Type.STRING, substringParams, substringBody);
        put("__STRING__.__FUNC__substring", substring);

        // Add "string.parseInt()" function
        List<StmtNode> parseIntStmts = new LinkedList<StmtNode>() {{
            add(new ReturnNode(loc, new IntegerLiteralNode(loc, 0)));
        }};
        BlockNode parseIntBody = new BlockNode(loc, parseIntStmts);
        FunctionEntity parseInt = new FunctionEntity(loc, "__STRING__.__FUNC__parseInt", Type.INT, new LinkedList<>(), parseIntBody);
        put("__STRING__.__FUNC__parseInt", parseInt);

        // Add "string.ord()" function
        List<StmtNode> ordStmts = new LinkedList<StmtNode>() {{
            add(new ReturnNode(loc, new IntegerLiteralNode(loc, 0)));
        }};
        BlockNode ordBody = new BlockNode(loc, ordStmts);
        List<ParameterEntity> ordParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "pos",Type.INT));
        }};
        FunctionEntity ord = new FunctionEntity(loc, "__STRING__.__FUNC__ord", Type.INT, ordParams, ordBody);
        put("__STRING__.__FUNC__ord", ord);

        // Add "ADD: s1 + s2"
        List<StmtNode> addStmts = new LinkedList<StmtNode>() {{
            add(new ReturnNode(loc, new StringLiteralNode(loc, "")));
        }};
        BlockNode addBody = new BlockNode(loc, addStmts);
        List<ParameterEntity> addParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity add = new FunctionEntity(loc, "__STRING__.__FUNC__add", Type.STRING, addParams, addBody);
        put("__STRING__.__FUNC__add", add);

        // Add "LT: s1 < s2"
        List<StmtNode> ltStmts = new LinkedList<StmtNode>() {{
            add(new ReturnNode(loc, new BoolLiteralNode(loc, true)));
        }};
        BlockNode ltBody = new BlockNode(loc, ltStmts);
        List<ParameterEntity> ltParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity lt = new FunctionEntity(loc, "__STRING__.__FUNC__lt", Type.BOOL, ltParams, ltBody);
        put("__STRING__.__FUNC__lt", lt);

        // Add "GT: s1 > s2"
        List<StmtNode> gtStmts = new LinkedList<StmtNode>() {{
            add(new ReturnNode(loc, new BoolLiteralNode(loc, true)));
        }};
        BlockNode gtBody = new BlockNode(loc, gtStmts);
        List<ParameterEntity> gtParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity gt = new FunctionEntity(loc, "__STRING__.__FUNC__gt", Type.BOOL, gtParams, gtBody);
        put("__STRING__.__FUNC__gt", gt);

        // Add "le: s1 <= s2"
        List<StmtNode> leStmts = new LinkedList<StmtNode>() {{
            add(new ReturnNode(loc, new BoolLiteralNode(loc, true)));
        }};
        BlockNode leBody = new BlockNode(loc, leStmts);
        List<ParameterEntity> leParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity le = new FunctionEntity(loc, "__STRING__.__FUNC__le", Type.BOOL, leParams, leBody);
        put("__STRING__.__FUNC__le", le);

        // Add "ge: s1 >= s2"
        List<StmtNode> geStmts = new LinkedList<StmtNode>() {{
            add(new ReturnNode(loc, new BoolLiteralNode(loc, true)));
        }};
        BlockNode geBody = new BlockNode(loc, geStmts);
        List<ParameterEntity> geParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity ge = new FunctionEntity(loc, "__STRING__.__FUNC__ge", Type.BOOL, geParams, geBody);
        put("__STRING__.__FUNC__ge", ge);

        // Add "eq: s1 == s2"
        List<StmtNode> eqStmts = new LinkedList<StmtNode>() {{
            add(new ReturnNode(loc, new BoolLiteralNode(loc, true)));
        }};
        BlockNode eqBody = new BlockNode(loc, eqStmts);
        List<ParameterEntity> eqParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity eq = new FunctionEntity(loc, "__STRING__.__FUNC__eq", Type.BOOL, eqParams, eqBody);
        put("__STRING__.__FUNC__eq", eq);

        // Add "ne: s1 != s2"
        List<StmtNode> neStmts = new LinkedList<StmtNode>() {{
            add(new ReturnNode(loc, new BoolLiteralNode(loc, true)));
        }};
        BlockNode neBody = new BlockNode(loc, neStmts);
        List<ParameterEntity> neParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity ne = new FunctionEntity(loc, "__STRING__.__FUNC__ne", Type.BOOL, neParams, neBody);
        put("__STRING__.__FUNC__ne", ne);
        
        

        // ========================= Global Builtin Function ==================================

        // Add "print()" function
        List<StmtNode> printStmts = new LinkedList<StmtNode>();
        BlockNode printBody = new BlockNode(loc, printStmts);
        List<ParameterEntity> printParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "str", Type.STRING));
        }};
        FunctionEntity print = new FunctionEntity(loc, "print", Type.VOID, printParams, printBody);
        put("__FUNC__print", print);

        // Add "println()" function
        List<StmtNode> printlnStmts = new LinkedList<StmtNode>();
        BlockNode printlnBody = new BlockNode(loc, printlnStmts);
        List<ParameterEntity> printlnParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "str", Type.STRING));
        }};
        FunctionEntity println = new FunctionEntity(loc, "println", Type.VOID, printlnParams, printlnBody);
        put("__FUNC__println", println);

        // Add "getString()" function
        List<StmtNode> getStringStmts = new LinkedList<StmtNode>(){{
            add(new ReturnNode(loc, new StringLiteralNode(loc, " ")));
        }};
        BlockNode getStringBody = new BlockNode(loc, getStringStmts);
        List<ParameterEntity> getStringParams = new LinkedList<ParameterEntity>() {{
        }};
        FunctionEntity getString = new FunctionEntity(loc, "getString", Type.STRING, getStringParams, getStringBody);
        put("__FUNC__getString", getString);

        // Add "getInt()" function
        List<StmtNode> getIntStmts = new LinkedList<StmtNode>(){{
            add(new ReturnNode(loc, new IntegerLiteralNode(loc, 0)));
        }};
        BlockNode getIntBody = new BlockNode(loc, getIntStmts);
        List<ParameterEntity> getIntParams = new LinkedList<ParameterEntity>() {{
        }};
        FunctionEntity getInt = new FunctionEntity(loc, "getInt", Type.INT, getIntParams, getIntBody);
        put("__FUNC__getInt", getInt);

        // Add "toString()" function
        List<StmtNode> toStringStmts = new LinkedList<StmtNode>(){{
            add(new ReturnNode(loc, new StringLiteralNode(loc, " ")));
        }};
        BlockNode toStringBody = new BlockNode(loc, toStringStmts);
        List<ParameterEntity> toStringParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "i", Type.INT));
        }};
        FunctionEntity toString = new FunctionEntity(loc, "toString", Type.STRING, toStringParams, toStringBody);
        put("__FUNC__toString", toString);

    }};

    public static FunctionEntity get(String InternalName) {
        return builtinFunctions.get(InternalName);
    }
}
