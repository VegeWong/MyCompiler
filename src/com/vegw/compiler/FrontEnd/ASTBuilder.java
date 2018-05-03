package com.vegw.compiler.FrontEnd;

import com.vegw.compiler.AST.ASTNode;
import com.vegw.compiler.AST.Expr.*;
import com.vegw.compiler.AST.Expr.Literal.BoolLiteralNode;
import com.vegw.compiler.AST.Expr.Literal.IntegerLiteralNode;
import com.vegw.compiler.AST.Expr.Literal.NullLiteralNode;
import com.vegw.compiler.AST.Expr.Literal.StringLiteralNode;
import com.vegw.compiler.AST.Stmt.*;
import com.vegw.compiler.AST.Stmt.Def.ClassDefNode;
import com.vegw.compiler.AST.Stmt.Def.DefinitionNode;
import com.vegw.compiler.AST.Stmt.Def.FunctionDefNode;
import com.vegw.compiler.AST.Stmt.Def.VariableDefNode;
import com.vegw.compiler.AST.TypeNode;
import com.vegw.compiler.Entity.ClassEntity;
import com.vegw.compiler.Entity.FunctionEntity;
import com.vegw.compiler.Entity.ParameterEntity;
import com.vegw.compiler.Entity.VariableEntity;
import com.vegw.compiler.Parser.MxstarBaseListener;
import com.vegw.compiler.Parser.MxstarParser;
import com.vegw.compiler.Type.ArrayType;
import com.vegw.compiler.Type.ClassType;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Location;
import com.vegw.compiler.Utils.Mapping;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.LinkedList;
import java.util.List;

public class ASTBuilder extends MxstarBaseListener {
    protected ParseTreeProperty<Object> map = new ParseTreeProperty<>();
    protected ASTNode ast;

    public ASTNode ast() { return ast; }

    @Override public void exitCompilationUnit(MxstarParser.CompilationUnitContext ctx) {
        List<DefinitionNode> defs = new LinkedList<>();

        for (MxstarParser.DefinitionContext item : ctx.definition()) {
            DefinitionNode definitionNode = (DefinitionNode) map.get(item);
            defs.add(definitionNode);
        }
        ast = new ASTNode(new Location(ctx), defs);
    }
    @Override public void exitVariableDeclaration(MxstarParser.VariableDeclarationContext ctx) {
        VariableEntity entity = new VariableEntity(new Location(ctx), ctx.name.getText(), ((TypeNode) map.get(ctx.typeSpecifier())).type());
        VariableDefNode variableDefNode = new VariableDefNode(entity);
        if (ctx.expression() != null) {
            variableDefNode.entity().setValue((ExprNode) map.get(ctx.expression()));
        }
        map.put(ctx, variableDefNode);
    }
    @Override public void exitBaseTypeSpecifier(MxstarParser.BaseTypeSpecifierContext ctx) {
        if (ctx.primitiveType() != null)
            map.put(ctx, map.get(ctx.primitiveType()));
        else
            map.put(ctx, new TypeNode(new Location(ctx), new ClassType(ctx.Identifier().getText())));
    }
    @Override public void exitPrimitiveType(MxstarParser.PrimitiveTypeContext ctx) {
        Type type = Mapping.PrimitiveType.get(ctx.getText());
        if (type == null)
            throw new Error("ASTBuilder: Unexpected primitive type");
        else
            map.put(ctx, new TypeNode(new Location(ctx),type));
    }
    @Override public void exitArrayTypeSpecifier(MxstarParser.ArrayTypeSpecifierContext ctx) {
        int demension = (ctx.getChildCount() - 1) / 2;
        Type type = new ArrayType(((TypeNode) map.get(ctx.baseTypeSpecifier())).type(), demension);
        map.put(ctx, new TypeNode(new Location(ctx), type));
    }
    @Override public void exitTypeSpecifier(MxstarParser.TypeSpecifierContext ctx) {
        TypeNode type = (TypeNode)((ctx.baseTypeSpecifier() == null)? map.get(ctx.arrayTypeSpecifier()):
                                                        map.get(ctx.baseTypeSpecifier()));
        map.put(ctx, type);
    }
    @Override public void exitDefinition(MxstarParser.DefinitionContext ctx) {
        if (ctx.classDefinition() != null) map.put(ctx, map.get(ctx.classDefinition()));
        else if (ctx.variableDeclaration() != null) map.put(ctx, map.get(ctx.variableDeclaration()));
        else map.put(ctx, map.get(ctx.functionDefinition()));
    }
    @Override public void exitFunctionDefinition(MxstarParser.FunctionDefinitionContext ctx) {
        Type returnType = ((TypeNode) map.get(ctx.returnType)).type();
        List<ParameterEntity> params;
        if (ctx.parameter() == null)  params = new LinkedList<>();
        else params = (List<ParameterEntity>) map.get(ctx.parameter());
        BlockNode body = (BlockNode) map.get(ctx.block());
        FunctionEntity entity = new FunctionEntity(new Location(ctx), ctx.functionName.getText(),
                returnType, params, body);
        map.put(ctx, new FunctionDefNode(entity));
    }
    @Override public void exitParameter(MxstarParser.ParameterContext ctx) {
        List<ParameterEntity> params = new LinkedList<>();
        for (int i = 0; i < ctx.Identifier().size(); ++i) {
            ParameterEntity param = new ParameterEntity(new Location(ctx),
                    ctx.Identifier(i).getText(),
                    ((TypeNode)map.get(ctx.typeSpecifier(i))).type());
            params.add(param);
        }
        map.put(ctx, params);
    }
    @Override public void exitBlock(MxstarParser.BlockContext ctx) {
        List<StmtNode> stmts = new LinkedList<>();
        for (MxstarParser.StatementContext item : ctx.statement()) {
            stmts.add((StmtNode) map.get(item));
        }
        map.put(ctx, new BlockNode(new Location(ctx), stmts));
    }
    @Override public void exitStatement(MxstarParser.StatementContext ctx) {
        StmtNode stmt = null;
        if (ctx.block() != null) stmt = (StmtNode) map.get(ctx.block());
        else if (ctx.variableDeclaration() != null) stmt = (StmtNode) map.get(ctx.variableDeclaration());
        else if (ctx.expression() != null) stmt = new ExprStmtNode((ExprNode) map.get(ctx.expression()));
        else if (ctx.selectionStatement() != null) stmt = (StmtNode) map.get(ctx.selectionStatement());
        else if (ctx.iterationStatement() != null) stmt = (StmtNode) map.get(ctx.iterationStatement());
        else if (ctx.jumpStatement() != null) stmt = (StmtNode) map.get(ctx.jumpStatement());
        map.put(ctx, stmt);
    }
    @Override public void exitSelectionStatement(MxstarParser.SelectionStatementContext ctx) {
        ExprNode cond = (ExprNode) map.get(ctx.cond);
        StmtNode thenBody = (StmtNode) map.get(ctx.thenBody);
        StmtNode elseBody = (StmtNode) map.get(ctx.elseBody);
        Location location = new Location(ctx);
        map.put(ctx, new IfNode(location, cond, thenBody, elseBody));
    }
    @Override public void exitIterationStatement(MxstarParser.IterationStatementContext ctx) {
        Location location = new Location(ctx);
        if (ctx.While() != null) {
            ExprNode cond = (ExprNode) map.get(ctx.cond);
            StmtNode stmt = (StmtNode) map.get(ctx.statement());
            map.put(ctx, new WhileNode(location, cond, stmt));
        }
        else {
            ExprNode init =  (ctx.initialization != null)? (ExprNode) map.get(ctx.initialization):
                    null;
            ExprNode cond =  (ctx.cond != null)? (ExprNode) map.get(ctx.cond):
                    null;
            ExprNode step =  (ctx.step != null)? (ExprNode) map.get(ctx.step):
                    null;
            StmtNode stmt = (StmtNode) map.get(ctx.statement());
            map.put(ctx, new ForNode(location, init, cond, step, stmt));
        }
    }
    @Override public void exitJumpStatement(MxstarParser.JumpStatementContext ctx) {
        Location location = new Location(ctx);
        if (ctx.Continue() != null)  map.put(ctx, new ContinueNode(location));
        else if (ctx.Break() != null)  map.put(ctx, new BreakNode(location));
        else {
            ExprNode expr = (ctx.expression() != null)? (ExprNode) map.get(ctx.expression()) : null;
            map.put(ctx, new ReturnNode(location, expr));
        }
    }
    @Override public void exitClassDefinition(MxstarParser.ClassDefinitionContext ctx) {
        List<VariableDefNode> vars = new LinkedList<>();
        FunctionDefNode constructor = null;
        List<FunctionDefNode> funcs = new LinkedList<>();

        String className = ctx.name.getText();
        for (MxstarParser.VariableDeclarationContext item : ctx.variableDeclaration()) {
            VariableDefNode var = (VariableDefNode) map.get(item);
            vars.add(var);
        }
        for (MxstarParser.ConstructorContext item : ctx.constructor()) {
            FunctionDefNode potential_constructor = (FunctionDefNode) map.get(item);
            if (potential_constructor.entity().name().equals(className)) {
                constructor = potential_constructor;
                constructor.entity().setConstructor();
            }
            else funcs.add(potential_constructor);
        }
        for (MxstarParser.FunctionDefinitionContext item : ctx.functionDefinition()) {
            FunctionDefNode func = (FunctionDefNode) map.get(item);
            funcs.add(func);
        }
        ClassEntity entity = new ClassEntity(new Location(ctx), className, funcs, vars);
        entity.setConstructor(constructor);
        map.put(ctx, new ClassDefNode(entity));
    }
    @Override public void exitConstructor(MxstarParser.ConstructorContext ctx) {
        FunctionEntity entity = new FunctionEntity(new Location(ctx), ctx.name.getText(), null, null,
                (BlockNode) map.get(ctx.block()));
        map.put(ctx, new FunctionDefNode(entity));
    }
    @Override public void exitExpressionList(MxstarParser.ExpressionListContext ctx) {
        List<ExprNode> exprs = new LinkedList<>();
        for (MxstarParser.ExpressionContext item : ctx.expression())
            exprs.add((ExprNode)map.get(item));
        map.put(ctx, exprs);
    }
    @Override public void exitPostfixExpr(MxstarParser.PostfixExprContext ctx) {
        UnaryOpNode.UnaryOp operator = (ctx.op.getText().equals("++"))? UnaryOpNode.UnaryOp.POSP:UnaryOpNode.UnaryOp.POSM;
        map.put(ctx, new UnaryOpNode(operator, (ExprNode) map.get(ctx.expression())));
    }
    @Override public void exitNew(MxstarParser.NewContext ctx) { map.put(ctx, map.get(ctx.creator())); }
    @Override public void exitFuncallExpr(MxstarParser.FuncallExprContext ctx) {
        List<ExprNode> args;
        if (ctx.expressionList() != null) args = (List<ExprNode>) map.get(ctx.expressionList());
        else args = new LinkedList<>();
        map.put(ctx, new FuncallNode((ExprNode) map.get(ctx.expression()), args));
    }
    @Override public void exitVar(MxstarParser.VarContext ctx) {
        map.put(ctx, new VariableNode(new Location(ctx), ctx.Identifier().getText()));
    }
    @Override public void exitMemberExpr(MxstarParser.MemberExprContext ctx) {
        ExprNode field = (ExprNode) map.get(ctx.expression());
        String member = ctx.Identifier().getText();
        map.put(ctx, new MemberNode(field, member));
    }
    @Override public void exitBinaryExpr(MxstarParser.BinaryExprContext ctx) {
        BinaryOpNode.BinaryOp operator = Mapping.BinaryOp.get(ctx.op.getText());
        if (operator == null)
            throw new Error("ASTBuilder: Unexpected binary operation");
        ExprNode left = (ExprNode) map.get(ctx.expression(0));
        ExprNode right = (ExprNode) map.get(ctx.expression(1));
        map.put(ctx, new BinaryOpNode(left, operator, right));
    }
    @Override public void exitLiteralExpr(MxstarParser.LiteralExprContext ctx)  {
        map.put(ctx, map.get(ctx.literal()));
    }
    @Override public void exitUnaryExpr(MxstarParser.UnaryExprContext ctx) {
        UnaryOpNode.UnaryOp operator = Mapping.UnaryOp.get(ctx.op.getText());
        if (operator == null)
            throw new Error("ASTBuilder: Unexpected unary operation");
        else
            map.put(ctx, new UnaryOpNode(operator, (ExprNode) map.get(ctx.expression())));
    }
    @Override public void exitSubExpression(MxstarParser.SubExpressionContext ctx)  {
        map.put(ctx, ctx.expression());
    }
    @Override public void exitArefExpr(MxstarParser.ArefExprContext ctx) {
        ArefNode arefNode = new ArefNode((ExprNode) map.get(ctx.expression(0)), (ExprNode) map.get(ctx.expression(1)));
        map.put(ctx, arefNode);
    }
    @Override public void exitSelfPointer(MxstarParser.SelfPointerContext ctx) {
        map.put(ctx, new VariableNode(new Location(ctx), "this"));
    }
    @Override public void exitErrorCreator(MxstarParser.ErrorCreatorContext ctx) {
        throw new Error("ASTBuilder: Illegal declaration of array demensions");
    }
    @Override public void exitArrayCreator(MxstarParser.ArrayCreatorContext ctx) {
        // Determine the baseType
        Type baseType = Mapping.PrimitiveType.get(ctx.type.getText());
        if (baseType == null)
            baseType = new ClassType(ctx.type.getText());

        List<MxstarParser.ExpressionContext> exprs = ctx.expression();
        List<ExprNode> exprNodes = new LinkedList<>();
        for (MxstarParser.ExpressionContext item : exprs)
            exprNodes.add((ExprNode)map.get(item));

        map.put(ctx, new CreatorNode(new Location(ctx),
                new ArrayType(baseType,(ctx.getChildCount() - 1 - exprs.size()) / 2),
                exprNodes));
    }
    @Override public void exitNonArrayCreator(MxstarParser.NonArrayCreatorContext ctx) {
        Type type = Mapping.PrimitiveType.get(ctx.type.getText());
        if (type == null)
            type = new ClassType(ctx.type.getText());
        map.put(ctx, new CreatorNode(new Location(ctx), type, null));
    }
    @Override public void exitInteger(MxstarParser.IntegerContext ctx) {
        map.put(ctx, new IntegerLiteralNode(new Location(ctx), Integer.parseInt(ctx.IntegerConstant().getText())));
    }
    @Override public void exitString(MxstarParser.StringContext ctx) {
        map.put(ctx, new StringLiteralNode(new Location(ctx), ctx.StringLiteral().getText()));
    }
    @Override public void exitBool(MxstarParser.BoolContext ctx) {
        map.put(ctx, new BoolLiteralNode(new Location(ctx), ctx.BoolConstant().getText().equals("true")));
    }
    @Override public void exitNull(MxstarParser.NullContext ctx)  {
        map.put(ctx, new NullLiteralNode(new Location(ctx)));
    }


















}
