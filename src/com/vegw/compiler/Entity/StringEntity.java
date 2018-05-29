package com.vegw.compiler.Entity;

import com.vegw.compiler.FrontEnd.LocalScope;
import com.vegw.compiler.Utils.BuiltinFunction;
import com.vegw.compiler.Utils.Location;

public class StringEntity extends Entity {
    // This is the global string entity, which is only applied in Type Checking
    // Replaced by StringConstEntity in IR procedure
    private LocalScope scope;

    public StringEntity() {
        super(new Location(0, 0), "string");
        scope = new LocalScope(null);
    }

    private void addBuiltinFunction(String funcName) {
        scope.entities().put(funcName, BuiltinFunction.get("__String__.__FUNC__" + funcName));
    }

    public void addStringTypeBuiltinFunction() {
        addBuiltinFunction("length");
        addBuiltinFunction("substring");
        addBuiltinFunction("parseInt");
        addBuiltinFunction("ord");
    }

    public LocalScope scope() { return scope; }

}
