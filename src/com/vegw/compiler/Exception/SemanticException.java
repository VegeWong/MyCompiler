package com.vegw.compiler.Exception;

public class SemanticException extends Exception {
    String info;

    public SemanticException(String info) {
        this.info = info;
    }
}
