package com.simplespider.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResultItems {

    private Map<String,Object> fields=new LinkedHashMap<>();

    private Request request;

    public <T> T get(String key) {
        Object o = fields.get(key);
        if (o == null) {
            return null;
        }
        return (T) fields.get(key);
    }

    public Map<String, Object> getAll() {
        return fields;
    }

    public <T> ResultItems put(String key, T value) {
        fields.put(key,value);
        return this;
    }

}
