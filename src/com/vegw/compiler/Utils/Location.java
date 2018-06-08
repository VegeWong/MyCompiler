package com.vegw.compiler.Utils;

import org.antlr.v4.runtime.ParserRuleContext;

public class Location {
    private int line;
    private int column;

    public Location(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public Location(ParserRuleContext ctx) {
        this.line = ctx.start.getLine();
        this.column = ctx.start.getCharPositionInLine();
    }

    public int line() {
        return this.line;
    }

    public int column() {
        return this.column;
    }

    public String toString() {
        return "(line " + line + " column:" + column + ")";
    }
}