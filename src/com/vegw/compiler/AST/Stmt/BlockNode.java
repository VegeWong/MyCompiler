package com.vegw.compiler.AST.Stmt;

import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.FrontEnd.Scope;
import com.vegw.compiler.Utils.Location;

import java.util.LinkedList;
import java.util.List;

public class BlockNode extends StmtNode {
    protected List<StmtNode> stmts;
    protected Location location;
    protected Scope scope;

    public BlockNode(Location location, List<StmtNode> stmts) {
        this.location = location;
        this.stmts = stmts;
        scope = null;
    }

    public static BlockNode cast(StmtNode node) {
        if (node == null) return null;

        if (node instanceof BlockNode) return (BlockNode)node;
        // https://blog.csdn.net/zhangketuan/article/details/45535989
        return new BlockNode(node.location(),
                new LinkedList<StmtNode>() {{
                    add(node);
                }});

    }

    public List<StmtNode> stmts() {
        return stmts;
    }

    public void setScope(Scope scope) { this.scope = scope; }

    public Scope scope() { return this.scope; }

    @Override
    public Location location() {
        return location;
    }

    @Override
    public String toString() {
        return "Block:\n";
    }

    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
