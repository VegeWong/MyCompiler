package com.vegw.compiler.Entity;

import com.vegw.compiler.AST.Expr.Literal.IntegerLiteralNode;
import com.vegw.compiler.AST.Expr.Literal.StringLiteralNode;
import com.vegw.compiler.AST.Stmt.BlockNode;
import com.vegw.compiler.AST.Stmt.ReturnNode;
import com.vegw.compiler.AST.Stmt.StmtNode;
import com.vegw.compiler.FrontEnd.LocalScope;
import com.vegw.compiler.FrontEnd.Scope;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Location;

import java.util.LinkedList;
import java.util.List;

public class StringEntity extends Entity {
    private List<FunctionEntity> builtinFunction;
    private LocalScope scope;

    public StringEntity() {
        super(new Location(0, 0), "string");
        Location loc = new Location(0, 0);
        builtinFunction = new LinkedList<FunctionEntity>();
        scope = new LocalScope(null);


        // Add builtin function
        List<StmtNode> lengthStmts = new LinkedList<StmtNode>() {{
            add(new ReturnNode(loc, new IntegerLiteralNode(loc, 0)));
        }};
        BlockNode lengthBody = new BlockNode(loc, lengthStmts);
        FunctionEntity length = new FunctionEntity(loc, "length", Type.INT, new LinkedList<>(), lengthBody);
        builtinFunction.add(length);

        // Add "string.substring()" function
        List<StmtNode> substringStmts = new LinkedList<StmtNode>() {{
            add(new ReturnNode(loc, new StringLiteralNode(loc, " ")));
        }};
        BlockNode substringBody = new BlockNode(loc, substringStmts);
        List<ParameterEntity> substringParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "left",Type.INT));
            add(new ParameterEntity(loc, "right",Type.INT));
        }};
        FunctionEntity substring = new FunctionEntity(loc, "substring", Type.INT, substringParams, substringBody);
        builtinFunction.add(substring);

        // Add "string.parseInt()" function
        List<StmtNode> parseIntStmts = new LinkedList<StmtNode>() {{
            add(new ReturnNode(loc, new IntegerLiteralNode(loc, 0)));
        }};
        BlockNode parseIntBody = new BlockNode(loc, parseIntStmts);
        FunctionEntity parseInt = new FunctionEntity(loc, "parseInt", Type.INT, new LinkedList<>(), parseIntBody);
        builtinFunction.add(parseInt);

        // Add "string.ord()" function
        List<StmtNode> ordStmts = new LinkedList<StmtNode>() {{
            add(new ReturnNode(loc, new IntegerLiteralNode(loc, 0)));
        }};
        BlockNode ordBody = new BlockNode(loc, ordStmts);
        List<ParameterEntity> ordParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "pos",Type.INT));
        }};
        FunctionEntity ord = new FunctionEntity(loc, "ord", Type.INT, ordParams, ordBody);
        builtinFunction.add(ord);

        for (FunctionEntity func : builtinFunction) {
            scope.entities().put(func.name(), func);
        }
    }

    public LocalScope scope() { return scope; }

}
