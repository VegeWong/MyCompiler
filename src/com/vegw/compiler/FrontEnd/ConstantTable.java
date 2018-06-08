package com.vegw.compiler.FrontEnd;


import java.util.HashMap;
import java.util.Map;

public class ConstantTable {
    public Map<String, String> strs;

    public ConstantTable() {
        strs = new HashMap<String, String>();
    }

    public void put(String key, String name) {
        strs.put(key, name);
    }

    public String get(String key) {
        return strs.get(key);
    }

}
