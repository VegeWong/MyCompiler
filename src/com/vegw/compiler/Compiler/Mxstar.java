package com.vegw.compiler.Compiler;

import com.vegw.compiler.AST.ASTNode;
import com.vegw.compiler.FrontEnd.ASTBuilder;
import com.vegw.compiler.FrontEnd.TypeChecker;
import com.vegw.compiler.Parser.MxstarLexer;
import com.vegw.compiler.Parser.MxstarParser;
import com.vegw.compiler.Utils.ErrorHandler;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;

public class Mxstar {
    static final public String ProgramName = "Mxstar";
    private final ErrorHandler errorHandler;

    public Mxstar(String programName) {
        this.errorHandler = new ErrorHandler(programName);
    }

    public static void main(String[] args) {
        new Mxstar(ProgramName).commandMain(args);
    }

    public static void commandMain(String[] args) {
        Options opts = Options.parse(args);
        String src = opts.SourceFile();
        try {
            build(src, opts);
        } catch (IOException ce) {
            System.err.println("Compile failed");
        }
    }

    public static void build(String src, Options opts) throws IOException {
        try {
            compile(src, opts.asmFileName(), opts);
        } catch (IOException ce) {
            ;
        }
    }

    public static void compile(String srcPath, String dstPath, Options opts) throws IOException {
        ASTNode ast = parseFile(srcPath, opts);
    }

    public static ASTNode parseFile(String srcPath, Options opts) throws IOException {
//        CharStream input = CharStreams.fromFileName("E:\\College-4\\Compiler\\mxstarw\\test\\1.mx");

        CharStream input = CharStreams.fromFileName(srcPath);
        MxstarLexer lexer = new MxstarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MxstarParser parser = new MxstarParser(tokens);
        MxstarParser.CompilationUnitContext tree = parser.compilationUnit(); // parse a compilationUnit

        ParseTreeWalker walker = new ParseTreeWalker();
        ASTBuilder listener = new ASTBuilder();

        walker.walk(listener, tree);   // 0th pass, CST -> AST

        ASTNode ast  = listener.ast();
        TypeChecker typeChecker = new TypeChecker(new ErrorHandler("TypeCheck"));
        typeChecker.check(ast);
        ErrorHandler.printLog();
        return ast;
    }

}
