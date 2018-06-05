package com.vegw.compiler.Utils;

import com.vegw.compiler.Entity.FunctionEntity;
import com.vegw.compiler.Entity.ParameterEntity;
import com.vegw.compiler.Type.Type;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BuiltinFunction {
    protected static final Map<String, FunctionEntity> builtinFunctions = new HashMap<String, FunctionEntity>(){{
        Location loc = new Location(0, 0);

        // ========================= String Builtin Function ==================================
        FunctionEntity length = new FunctionEntity(loc, "string.length", Type.INT, new LinkedList<>(), null);
        length.rename("string.length");
        put("string.length", length);

        // Add "string.substring()" function
        List<ParameterEntity> substringParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "left",Type.INT));
            add(new ParameterEntity(loc, "right",Type.INT));
        }};
        FunctionEntity substring = new FunctionEntity(loc, "string.subString", Type.STRING, substringParams, null);
        substring.rename("string.subString");
        put("string.subString", substring);

        // Add "string.parseInt()" function
        FunctionEntity parseInt = new FunctionEntity(loc, "string.parseInt", Type.INT, new LinkedList<>(), null);
        parseInt.rename("string.parseInt");
        put("string.parseInt", parseInt);

        // Add "string.ord()" function
        List<ParameterEntity> ordParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "pos",Type.INT));
        }};
        FunctionEntity ord = new FunctionEntity(loc, "string.ord", Type.INT, ordParams, null);
        ord.rename("string.ord");
        put("string.ord", ord);

        // Add "ADD: s1 + s2"
        List<ParameterEntity> addParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity add = new FunctionEntity(loc, "string.add", Type.STRING, addParams, null);
        add.rename("string.add");
        put("string.add", add);

        // Add "LT: s1 < s2"
        List<ParameterEntity> ltParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity lt = new FunctionEntity(loc, "string.lt", Type.BOOL, ltParams, null);
        lt.rename("string.lt");
        put("string.lt", lt);

        // Add "GT: s1 > s2"
        List<ParameterEntity> gtParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity gt = new FunctionEntity(loc, "string.gt", Type.BOOL, gtParams, null);
        gt.rename("string.gt");
        put("string.gt", gt);

        // Add "le: s1 <= s2"
        List<ParameterEntity> leParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity le = new FunctionEntity(loc, "string.le", Type.BOOL, leParams, null);
        le.rename("string.le");
        put("string.le", le);

        // Add "ge: s1 >= s2"
        List<ParameterEntity> geParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity ge = new FunctionEntity(loc, "string.ge", Type.BOOL, geParams, null);
        ge.rename("string.ge");
        put("string.ge", ge);

        // Add "eq: s1 == s2"
        List<ParameterEntity> eqParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity eq = new FunctionEntity(loc, "string.eq", Type.BOOL, eqParams, null);
        eq.rename("string.eq");
        put("string.eq", eq);

        // Add "ne: s1 != s2"
        List<ParameterEntity> neParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity ne = new FunctionEntity(loc, "string.ne", Type.BOOL, neParams, null);
        ne.rename("string.ne");
        put("string.ne", ne);
        
        

        // ========================= Global Builtin Function ==================================

        // Add "print()" function
        List<ParameterEntity> printParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "str", Type.STRING));
        }};
        FunctionEntity print = new FunctionEntity(loc, "print", Type.VOID, printParams, null);
        print.rename("print");
        put("print", print);

        // Add "println()" function
        List<ParameterEntity> printlnParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "str", Type.STRING));
        }};
        FunctionEntity println = new FunctionEntity(loc, "println", Type.VOID, printlnParams, null);
        println.rename("println");
        put("println", println);

        // Add "getString()" function
        List<ParameterEntity> getStringParams = new LinkedList<ParameterEntity>();
        FunctionEntity getString = new FunctionEntity(loc, "getString", Type.STRING, getStringParams, null);
        getString.rename("getString");
        put("getString", getString);

        // Add "getInt()" function
        List<ParameterEntity> getIntParams = new LinkedList<ParameterEntity>();
        FunctionEntity getInt = new FunctionEntity(loc, "getInt", Type.INT, getIntParams, null);
        getInt.rename("getInt");
        put("getInt", getInt);

        // Add "toString()" function
        List<ParameterEntity> toStringParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "i", Type.INT));
        }};
        FunctionEntity toString = new FunctionEntity(loc, "toString", Type.STRING, toStringParams, null);
        toString.rename("toString");
        put("toString", toString);


        // ========================= Malloc Function ==================================
        List<ParameterEntity> mallocParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "size", Type.INT));
        }};
        FunctionEntity malloc = new FunctionEntity(loc, "malloc", Type.INT, mallocParams, null);
        malloc.rename("malloc");
        put("__FUNC__malloc", malloc);

    }};

    public static FunctionEntity get(String InternalName) {
        return builtinFunctions.get(InternalName);
    }
}
