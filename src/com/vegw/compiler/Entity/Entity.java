package com.vegw.compiler.Entity;

import com.vegw.compiler.Utils.Location;

public class Entity {
    protected String name;
    protected Location location;

    public Entity(Location location, String name) {
        this.location = location;
        this.name = name;
    }


    public Location location() {
        return location;
    }

    public String name(){
        return name;
    }


}
