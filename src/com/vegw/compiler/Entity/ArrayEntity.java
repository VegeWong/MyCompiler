package com.vegw.compiler.Entity;

import com.vegw.compiler.AST.Expr.Literal.IntegerLiteralNode;
import com.vegw.compiler.AST.Stmt.BlockNode;
import com.vegw.compiler.AST.Stmt.ReturnNode;
import com.vegw.compiler.AST.Stmt.StmtNode;
import com.vegw.compiler.FrontEnd.LocalScope;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Location;

import java.util.LinkedList;
import java.util.List;

public class ArrayEntity extends Entity {
    private List<FunctionEntity> builtinFunction;
    private LocalScope scope;

    public ArrayEntity() {
        super(new Location(0, 0), "array");
        Location loc = new Location(0, 0);
        builtinFunction = new LinkedList<FunctionEntity>();
        scope = new LocalScope(null);

        // Add "array.size()" function
        List<StmtNode> stmts = new LinkedList<StmtNode>() {{
            add(new ReturnNode(loc, new IntegerLiteralNode(loc, 0)));
        }};
        BlockNode body = new BlockNode(loc, stmts);
        FunctionEntity size = new FunctionEntity(loc, "size", Type.INT, null, body);
        builtinFunction.add(size);
        scope.entities().put("size", size);
    }

    public LocalScope scope() { return scope; }
}
