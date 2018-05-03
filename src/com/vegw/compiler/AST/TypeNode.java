package com.vegw.compiler.AST;

import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Location;

public class TypeNode extends Node {
    protected Location location;
    protected Type type;

    public TypeNode(Location location, Type type) {
        this.location = location;
        this.type = type;
    }

    public Type type() {
        return type;
    }


    @Override
    public Location location() {
        return location;
    }

    @Override
    public String toString() {
        return "Type";
    }
}
