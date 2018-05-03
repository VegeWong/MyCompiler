package com.vegw.compiler.AST;

import com.vegw.compiler.Utils.Location;

abstract public class Node {
    abstract public Location location();
    abstract public String toString();
}
