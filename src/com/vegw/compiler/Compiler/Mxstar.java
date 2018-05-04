package com.vegw.compiler.Compiler;

import com.vegw.compiler.AST.ASTNode;
import com.vegw.compiler.Exception.SemanticException;
import com.vegw.compiler.FrontEnd.ASTBuilder;
import com.vegw.compiler.FrontEnd.LocalResolver;
import com.vegw.compiler.FrontEnd.TypeChecker;
import com.vegw.compiler.Parser.MxstarLexer;
import com.vegw.compiler.Parser.MxstarParser;
import com.vegw.compiler.Utils.ErrorHandler;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.io.InputStream;

import static java.lang.System.exit;

public class Mxstar {
    public static void main(String[] args) {

        try {
            InputStream in = System.in;
            ANTLRInputStream input = new ANTLRInputStream(in);
//            CharStream input = CharStreams.fromFileName("E:\\College-4\\Compiler\\mxstarw\\test\\1.mx");
            MxstarLexer lexer = new MxstarLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MxstarParser parser = new MxstarParser(tokens);
            MxstarParser.CompilationUnitContext tree = parser.compilationUnit(); // parse a compilationUnit

            ParseTreeWalker walker = new ParseTreeWalker();
            ASTBuilder listener = new ASTBuilder();

            walker.walk(listener, tree);   // 0th pass, CST -> AST

            ASTNode ast  = listener.ast();
            LocalResolver resolver = new LocalResolver(new ErrorHandler("LocalResolver"));
            try {
                resolver.resolve(ast);
            } catch (SemanticException se) {
                exit(1);
            }
            TypeChecker typeChecker = new TypeChecker(new ErrorHandler("TypeCheck"));
            typeChecker.check(ast);
            ErrorHandler.printLog();
            if (ErrorHandler.errorCount() != 0) exit(1);
        } catch (IOException io) {
            exit(1);
        }
    }
}
