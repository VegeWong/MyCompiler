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
        FunctionEntity length = new FunctionEntity(loc, "__STRING__.__FUNC__length", Type.INT, new LinkedList<>(), null);
        put("__STRING__.__FUNC__length", length);

        // Add "string.substring()" function
        List<ParameterEntity> substringParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "left",Type.INT));
            add(new ParameterEntity(loc, "right",Type.INT));
        }};
        FunctionEntity substring = new FunctionEntity(loc, "__STRING__.__FUNC__substring", Type.STRING, substringParams, null);
        put("__STRING__.__FUNC__substring", substring);

        // Add "string.parseInt()" function
        FunctionEntity parseInt = new FunctionEntity(loc, "__STRING__.__FUNC__parseInt", Type.INT, new LinkedList<>(), null);
        put("__STRING__.__FUNC__parseInt", parseInt);

        // Add "string.ord()" function
        List<ParameterEntity> ordParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "pos",Type.INT));
        }};
        FunctionEntity ord = new FunctionEntity(loc, "__STRING__.__FUNC__ord", Type.INT, ordParams, null);
        put("__STRING__.__FUNC__ord", ord);

        // Add "ADD: s1 + s2"
        List<ParameterEntity> addParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity add = new FunctionEntity(loc, "__STRING__.__FUNC__add", Type.STRING, addParams, null);
        put("__STRING__.__FUNC__add", add);

        // Add "LT: s1 < s2"
        List<ParameterEntity> ltParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity lt = new FunctionEntity(loc, "__STRING__.__FUNC__lt", Type.BOOL, ltParams, null);
        put("__STRING__.__FUNC__lt", lt);

        // Add "GT: s1 > s2"
        List<ParameterEntity> gtParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity gt = new FunctionEntity(loc, "__STRING__.__FUNC__gt", Type.BOOL, gtParams, null);
        put("__STRING__.__FUNC__gt", gt);

        // Add "le: s1 <= s2"
        List<ParameterEntity> leParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity le = new FunctionEntity(loc, "__STRING__.__FUNC__le", Type.BOOL, leParams, null);
        put("__STRING__.__FUNC__le", le);

        // Add "ge: s1 >= s2"
        List<ParameterEntity> geParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity ge = new FunctionEntity(loc, "__STRING__.__FUNC__ge", Type.BOOL, geParams, null);
        put("__STRING__.__FUNC__ge", ge);

        // Add "eq: s1 == s2"
        List<ParameterEntity> eqParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity eq = new FunctionEntity(loc, "__STRING__.__FUNC__eq", Type.BOOL, eqParams, null);
        put("__STRING__.__FUNC__eq", eq);

        // Add "ne: s1 != s2"
        List<ParameterEntity> neParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "s1",Type.STRING));
            add(new ParameterEntity(loc, "s2",Type.STRING));
        }};
        FunctionEntity ne = new FunctionEntity(loc, "__STRING__.__FUNC__ne", Type.BOOL, neParams, null);
        put("__STRING__.__FUNC__ne", ne);
        
        

        // ========================= Global Builtin Function ==================================

        // Add "print()" function
        List<ParameterEntity> printParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "str", Type.STRING));
        }};
        FunctionEntity print = new FunctionEntity(loc, "print", Type.VOID, printParams, null);
        put("__FUNC__print", print);

        // Add "println()" function
        List<ParameterEntity> printlnParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "str", Type.STRING));
        }};
        FunctionEntity println = new FunctionEntity(loc, "println", Type.VOID, printlnParams, null);
        put("__FUNC__println", println);

        // Add "getString()" function
        List<ParameterEntity> getStringParams = new LinkedList<ParameterEntity>();
        FunctionEntity getString = new FunctionEntity(loc, "getString", Type.STRING, getStringParams, null);
        put("__FUNC__getString", getString);

        // Add "getInt()" function
        List<ParameterEntity> getIntParams = new LinkedList<ParameterEntity>();
        FunctionEntity getInt = new FunctionEntity(loc, "getInt", Type.INT, getIntParams, null);
        put("__FUNC__getInt", getInt);

        // Add "toString()" function
        List<ParameterEntity> toStringParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "i", Type.INT));
        }};
        FunctionEntity toString = new FunctionEntity(loc, "toString", Type.STRING, toStringParams, null);
        put("__FUNC__toString", toString);


        // ========================= Malloc Function ==================================
        List<ParameterEntity> mallocParams = new LinkedList<ParameterEntity>() {{
            add(new ParameterEntity(loc, "size", Type.INT));
        }};
        FunctionEntity malloc = new FunctionEntity(loc, "malloc", Type.INT, mallocParams, null);
        put("__FUNC__malloc", malloc);

    }};

    public static FunctionEntity get(String InternalName) {
        return builtinFunctions.get(InternalName);
    }
}
