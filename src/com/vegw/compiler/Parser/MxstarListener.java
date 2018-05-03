package com.vegw.compiler.Parser;// Generated from .\Mxstar.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MxstarParser}.
 */
public interface MxstarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MxstarParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void enterCompilationUnit(MxstarParser.CompilationUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void exitCompilationUnit(MxstarParser.CompilationUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(MxstarParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(MxstarParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#baseTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterBaseTypeSpecifier(MxstarParser.BaseTypeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#baseTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitBaseTypeSpecifier(MxstarParser.BaseTypeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(MxstarParser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(MxstarParser.PrimitiveTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#arrayTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterArrayTypeSpecifier(MxstarParser.ArrayTypeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#arrayTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitArrayTypeSpecifier(MxstarParser.ArrayTypeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#typeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeSpecifier(MxstarParser.TypeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#typeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeSpecifier(MxstarParser.TypeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterDefinition(MxstarParser.DefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitDefinition(MxstarParser.DefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#functionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDefinition(MxstarParser.FunctionDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#functionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDefinition(MxstarParser.FunctionDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(MxstarParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(MxstarParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(MxstarParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(MxstarParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(MxstarParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(MxstarParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#selectionStatement}.
	 * @param ctx the parse tree
	 */
	void enterSelectionStatement(MxstarParser.SelectionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#selectionStatement}.
	 * @param ctx the parse tree
	 */
	void exitSelectionStatement(MxstarParser.SelectionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#iterationStatement}.
	 * @param ctx the parse tree
	 */
	void enterIterationStatement(MxstarParser.IterationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#iterationStatement}.
	 * @param ctx the parse tree
	 */
	void exitIterationStatement(MxstarParser.IterationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#jumpStatement}.
	 * @param ctx the parse tree
	 */
	void enterJumpStatement(MxstarParser.JumpStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#jumpStatement}.
	 * @param ctx the parse tree
	 */
	void exitJumpStatement(MxstarParser.JumpStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#classDefinition}.
	 * @param ctx the parse tree
	 */
	void enterClassDefinition(MxstarParser.ClassDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#classDefinition}.
	 * @param ctx the parse tree
	 */
	void exitClassDefinition(MxstarParser.ClassDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#constructor}.
	 * @param ctx the parse tree
	 */
	void enterConstructor(MxstarParser.ConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#constructor}.
	 * @param ctx the parse tree
	 */
	void exitConstructor(MxstarParser.ConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(MxstarParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(MxstarParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PostfixExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPostfixExpr(MxstarParser.PostfixExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PostfixExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPostfixExpr(MxstarParser.PostfixExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code New}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNew(MxstarParser.NewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code New}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNew(MxstarParser.NewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FuncallExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFuncallExpr(MxstarParser.FuncallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FuncallExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFuncallExpr(MxstarParser.FuncallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MemberExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMemberExpr(MxstarParser.MemberExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MemberExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMemberExpr(MxstarParser.MemberExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Var}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterVar(MxstarParser.VarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Var}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitVar(MxstarParser.VarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpr(MxstarParser.BinaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpr(MxstarParser.BinaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExpr(MxstarParser.LiteralExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExpr(MxstarParser.LiteralExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpr(MxstarParser.UnaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpr(MxstarParser.UnaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SubExpression}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSubExpression(MxstarParser.SubExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SubExpression}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSubExpression(MxstarParser.SubExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArefExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterArefExpr(MxstarParser.ArefExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArefExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitArefExpr(MxstarParser.ArefExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SelfPointer}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSelfPointer(MxstarParser.SelfPointerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SelfPointer}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSelfPointer(MxstarParser.SelfPointerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ErrorCreator}
	 * labeled alternative in {@link MxstarParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterErrorCreator(MxstarParser.ErrorCreatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ErrorCreator}
	 * labeled alternative in {@link MxstarParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitErrorCreator(MxstarParser.ErrorCreatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrayCreator}
	 * labeled alternative in {@link MxstarParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreator(MxstarParser.ArrayCreatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrayCreator}
	 * labeled alternative in {@link MxstarParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreator(MxstarParser.ArrayCreatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nonArrayCreator}
	 * labeled alternative in {@link MxstarParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterNonArrayCreator(MxstarParser.NonArrayCreatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nonArrayCreator}
	 * labeled alternative in {@link MxstarParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitNonArrayCreator(MxstarParser.NonArrayCreatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Integer}
	 * labeled alternative in {@link MxstarParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterInteger(MxstarParser.IntegerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Integer}
	 * labeled alternative in {@link MxstarParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitInteger(MxstarParser.IntegerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code String}
	 * labeled alternative in {@link MxstarParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterString(MxstarParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code String}
	 * labeled alternative in {@link MxstarParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitString(MxstarParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Bool}
	 * labeled alternative in {@link MxstarParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterBool(MxstarParser.BoolContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Bool}
	 * labeled alternative in {@link MxstarParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitBool(MxstarParser.BoolContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Null}
	 * labeled alternative in {@link MxstarParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterNull(MxstarParser.NullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Null}
	 * labeled alternative in {@link MxstarParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitNull(MxstarParser.NullContext ctx);
}