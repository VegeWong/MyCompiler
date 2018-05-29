package com.vegw.compiler.Entity;

import com.vegw.compiler.Utils.Location;

public class StringConstEntity extends Entity {
    public String value;

    public StringConstEntity(String name) {
        super(new Location(0,0), name);
    }
}
