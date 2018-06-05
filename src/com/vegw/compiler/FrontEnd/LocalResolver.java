package com.vegw.compiler.FrontEnd;

import com.vegw.compiler.AST.ASTNode;
import com.vegw.compiler.AST.Expr.CreatorNode;
import com.vegw.compiler.AST.Expr.VariableNode;
import com.vegw.compiler.AST.Stmt.BlockNode;
import com.vegw.compiler.AST.Stmt.Def.ClassDefNode;
import com.vegw.compiler.AST.Stmt.Def.DefinitionNode;
import com.vegw.compiler.AST.Stmt.Def.FunctionDefNode;
import com.vegw.compiler.AST.Stmt.Def.VariableDefNode;
import com.vegw.compiler.AST.Stmt.IfNode;
import com.vegw.compiler.AST.Stmt.WhileNode;
import com.vegw.compiler.Entity.*;
import com.vegw.compiler.Exception.SemanticException;
import com.vegw.compiler.Type.ArrayType;
import com.vegw.compiler.Type.ClassType;
import com.vegw.compiler.Type.FunctionType;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.BuiltinFunction;
import com.vegw.compiler.Utils.ErrorHandler;
import com.vegw.compiler.Utils.Location;

import java.util.Stack;


public class LocalResolver extends Visitor {
    private final Stack<Scope> stack;
    private final ErrorHandler errorHandler;
    private ConstantTable constantTable;
    private Scope currentScope;
    private ClassEntity currentClass;
    private TopLevelScope topScope;
    private boolean isBody;


    public LocalResolver(ErrorHandler h) {
        this.errorHandler = h;
        this.stack = new Stack<>();
        this.constantTable = new ConstantTable();
        this.currentClass = null;
    }

    private boolean resolveType(Type type) {
        if (type instanceof ArrayType) return resolveType(((ArrayType) type).baseType());
        if (type == Type.INT || type ==  Type.BOOL ||
            type ==  Type.NULL ||  type ==  Type.VOID)
            return true;
        else if (type instanceof ClassType && ((ClassType) type).entity() == null) {
            try {
                Entity entity = topScope.get(((ClassType) type).name());
                if (entity instanceof ClassEntity)
                    ((ClassType) type).setEntity((ClassEntity) entity);
                else
                    return false;
            }
            catch (SemanticException ex) {
                return false;
            }
        }
        else if (type instanceof FunctionType && ((FunctionType) type).entity() == null){
            try {
                Entity entity = currentScope.get(((FunctionType) type).name());
                if (entity instanceof FunctionEntity) {
                    ((FunctionType) type).setEntity((FunctionEntity) entity);
                    return true;
                }
                else
                    return false;
            }
            catch (SemanticException ex) {
                return false;
            }
        }
        return true;
    }

    public void resolve(ASTNode ast) throws SemanticException {
        TopLevelScope toplevel = new TopLevelScope();
        topScope = toplevel;
        stack.add(toplevel);
        currentScope = stack.peek();

        addGlobalBuiltinFunction();
        for (DefinitionNode node : ast.defs){
            if (node instanceof VariableDefNode)
                continue;
            String name = node.entity().name();
            if (toplevel.entities.get(name) == null) {
                toplevel.entities.put(name, node.entity());
            }
            else {
                throw new SemanticException("Duplicated Declaration: " + name);
            }
        }
        for (DefinitionNode node : ast.defs){
            visit(node);
        }

        ast.setScope(toplevel);
    }

    private void pushScope() {
        LocalScope scope = new LocalScope(currentScope);
        stack.add(scope);
        currentScope = stack.peek();
    }

    private LocalScope popScope() {
        LocalScope returnScope = (LocalScope) stack.pop();
        currentScope = stack.peek();
        return returnScope;
    }

    @Override
    public Void visit(IfNode node) {
        visit(node.cond());
        if (node.thenBody() != null) {
            pushScope();
            visit(node.thenBody());
            if (node.thenBody() instanceof BlockNode) ((BlockNode) node.thenBody()).setScope(popScope());
            else popScope();
        }
        if (node.elseBody() != null) {
            pushScope();
            visit(node.elseBody());
            if (node.elseBody() instanceof BlockNode) ((BlockNode) node.elseBody()).setScope(popScope());
            else popScope();
        }

        return null;
    }

    @Override
    public Void visit(WhileNode node) {
        visit(node.cond());
        if (node.body() != null) {
            pushScope();
            visit(node.body());
            if (node.body() instanceof BlockNode) ((BlockNode) node.body()).setScope(popScope());
            else popScope();
        }
        return null;
    }

    @Override
    public Void visit(VariableNode node) {
        try {
            Entity ent = currentScope.get(node.name());
            node.setEntity(ent);
                if (!resolveType(node.type()))
                    errorHandler.error(node, "Cannot resolve type" + node.type().toString());
        }
        catch (SemanticException ex) {
            errorHandler.error(node, ex.getMessage());
        }
        return null;
    }

    @Override
    public Void visit(CreatorNode node) {
        if (!resolveType(node.type()))
            errorHandler.error(node, "Cannot resolve type" + node.type().toString());
        super.visit(node);
        return null;
    }

    @Override
    public Void visit(BlockNode node) {
        if (isBody) {
            isBody = false;
            super.visit(node);
            node.setScope(currentScope);
        }
        else {
            pushScope();
            super.visit(node);
            node.setScope(popScope());
        }
        return null;
    }

    @Override
    public Void visit(ClassDefNode node) {
        ClassEntity entity = node.entity();
        currentClass = entity;
        pushScope();
        ClassType thisType = new ClassType(entity.name());
        thisType.setEntity(entity);
//        currentScope.entities().put("this", new VariableEntity(node.location(),"this", thisType));
        for (VariableDefNode var : entity.vars()) {
            visit(var);
        }
        for (FunctionDefNode func : entity.funcs()) {
            currentScope.entities().put(func.entity().name(), func.entity());
        }
        if (entity.constructor() != null) {
            currentScope.entities().put(entity.constructor().entity().name(),
                    entity.constructor().entity());
            visit(entity.constructor());
        }
        for (FunctionDefNode func : entity.funcs()) {
            visit(func);
        }
        entity.setScope(popScope());
        currentClass = null;
        return null;
    }

    @Override
    public Void visit(FunctionDefNode node) {
        pushScope();
        if (!resolveType(node.entity().returnType()))
            errorHandler.error(node, "Cannot resolve type" + node.entity().returnType().toString());

        if (currentClass != null)
            node.entity().params().add(0, new ParameterEntity(node.location(), "this", currentClass.classType()));
        for (ParameterEntity entity : node.entity().params()) {
            String name = entity.name();
            Type type = entity.type();
            if (!resolveType(type))
                errorHandler.error(node, "Cannot resolve type" + type.toString());
            Location location = entity.location();
            VariableEntity nentity = new VariableEntity(location, name, type);
            currentScope.entities().put(name, nentity);
        }
        isBody = true;
        visit(node.entity().body());
        node.entity().setScope(popScope());
        return null;
    }

    @Override
    public Void visit(VariableDefNode node) {
        if (!resolveType(node.entity().type())) {
            errorHandler.error(node, "Cannot resolve type" + node.entity().type().toString());
            return null;
        }
        super.visit(node);
        String name = node.entity().name();
        if (currentScope.entities().get(name) == null)
            currentScope.entities().put(name, node.entity());
        else {
            errorHandler.error(node, "Dumplicated declaration of name" + name);
            return null;
        }
        return null;
    }


    private void addFunction(String funcName) {
        topScope.entities.put(funcName, BuiltinFunction.get(funcName));
    }
    private void addGlobalBuiltinFunction() {
        addFunction("print");
        addFunction("println");
        addFunction("getString");
        addFunction("getInt");
        addFunction("toString");
    }
    private void addBuiltinFunction() {
        Type.STRING.entity().addStringTypeBuiltinFunction();
        addGlobalBuiltinFunction();
    }
}
