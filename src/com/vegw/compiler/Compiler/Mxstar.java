package com.vegw.compiler.Compiler;

import com.vegw.compiler.AST.ASTNode;
import com.vegw.compiler.BackEnd.IRGenerator;
import com.vegw.compiler.BackEnd.Translator;
import com.vegw.compiler.Exception.SemanticException;
import com.vegw.compiler.FrontEnd.ASTBuilder;
import com.vegw.compiler.FrontEnd.LocalResolver;
import com.vegw.compiler.FrontEnd.TypeChecker;
import com.vegw.compiler.IR.LinearIR.RegisterList;
import com.vegw.compiler.Parser.MxstarLexer;
import com.vegw.compiler.Parser.MxstarParser;
import com.vegw.compiler.Utils.ErrorHandler;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static java.lang.System.exit;

public class Mxstar {
    public static void main(String[] args) {
        try {
//             CharStream input = CharStreams.fromFileName("E:\\College-4\\Compiler\\mxstar\\test\\1.mx");
//             FileOutputStream out = new FileOutputStream("E:\\College-4\\Compiler\\mxstar\\test\\1.out");
//
           InputStream in = System.in;
           ANTLRInputStream input = new ANTLRInputStream(in);
           OutputStream out = System.out;
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
                ErrorHandler.printOuterError(se.getMessage());
                ErrorHandler.printLog();
                exit(1);
            }

            TypeChecker typeChecker = new TypeChecker(new ErrorHandler("TypeCheck"));
            try {
                typeChecker.check(ast);
            } catch (SemanticException se) {
                ErrorHandler.printOuterError(se.getMessage());
                ErrorHandler.printLog();
                exit(1);
            }

            if (ErrorHandler.errorCount() != 0){
                ErrorHandler.printLog();
                exit(1);
            }

            RegisterList registerList = new RegisterList();
            IRGenerator irGenerator = new IRGenerator(ast, registerList);
            irGenerator.generate();

            Translator translator = new Translator(irGenerator, registerList);
            translator.translate();


            for (String line : translator.list) {
                byte data[] = line.getBytes();
                out.write(data);
            }

            out.close();
        } catch (IOException io) {
            ErrorHandler.printOuterError("Compile Start Error: IOException");
            ErrorHandler.printOuterError("Detail: " + io.getMessage());
            exit(1);
        }


    }
}
