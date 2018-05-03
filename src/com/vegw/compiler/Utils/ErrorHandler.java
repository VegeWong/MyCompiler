package com.vegw.compiler.Utils;

import com.vegw.compiler.AST.Node;
import javafx.util.Pair;

import java.io.PrintStream;
import java.util.LinkedList;

public class ErrorHandler {
    private final static LinkedList<ErrorHandler> totalList = new LinkedList<>();
    private LinkedList<Pair<Node, String>> list;
    private String locate;
    private PrintStream s;

    public ErrorHandler(String locate) {
        this.locate = locate;
        list = new LinkedList<>();
        s = System.err;
    }

    public void error(Node errorNode, String message) {
        list.add(new Pair<Node, String>(errorNode, message));
    }

    public void setPrintStream(PrintStream s) { this.s = s; }

    static public void printLog() {
        for (ErrorHandler e : totalList) {
            for (Pair<Node, String> err : e.list) {
                e.s.println(err.getKey().location() + err.getValue());
            }
        }
    }

}