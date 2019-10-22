package kz.zhabassov.project.entity;

import java.util.HashMap;
import java.util.Map;

public class Entity {
    protected Map<Property, Object> properties = new HashMap<>();

    public Map<Property, Object> getProperties() {
        return properties;
    }
}
