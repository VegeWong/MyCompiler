package com.vegw.compiler.FrontEnd;


import com.vegw.compiler.Entity.StringConstEntity;

import java.util.HashMap;
import java.util.Map;

public class ConstantTable {
    protected Map<String, StringConstEntity> strs;

    public ConstantTable() {
        strs = new HashMap<String, StringConstEntity>();
    }

    public void put(String key, StringConstEntity value) {
        strs.put(key, value);
    }

    public StringConstEntity get(String key) {
        return strs.get(key);
    }

}
