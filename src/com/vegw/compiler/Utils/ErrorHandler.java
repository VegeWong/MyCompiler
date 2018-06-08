package com.vegw.compiler.Utils;

import com.vegw.compiler.AST.Node;

import java.io.PrintStream;
import java.util.LinkedList;

public class ErrorHandler {
    private final static LinkedList<ErrorHandler> totalList = new LinkedList<>();
    private LinkedList<String> list;
    private String locate;
    private PrintStream s;

    public ErrorHandler(String locate) {
        this.locate = locate;
        list = new LinkedList<>();
        s = System.err;
        totalList.add(this);
    }

    public void error(Node errorNode, String message) {
        list.add(errorNode.location().toString() + message);
    }

    public static final void printOuterError(String message) {
        System.err.println(message);
    }

    public void setPrintStream(PrintStream s) { this.s = s; }

    static public void printLog() {
        for (ErrorHandler e : totalList) {
            for (String err : e.list) {
                e.s.println(e.locate + err);
            }
        }
    }

    static public int errorCount() {
        int count = 0;
        for (ErrorHandler e : totalList) {
            count += e.list.size();
        }
        return count;
    }

}