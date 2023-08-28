package com.luma.framework;



import java.util.ArrayList;
import java.util.HashMap;
import io.restassured.response.Response;



public class PropertyManager {
    private HashMap<String, Object> properties;

    public PropertyManager(String environment, String program) {
        try {
            this.properties = new HashMap<>();

            this.properties.putAll(JsonManager.getInstance("program_configuration.json")
                    .getData(environment, program));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setProperties(String key, String value) {
        this.properties.put(key, value);
    }
    public void setProperties(String key, boolean value) {
        this.properties.put(key, value);
    }

    public void setProperties(String key, ArrayList<String> value) {
        this.properties.put(key, value);
    }

    public void setProperties(String key, Response value) {
        this.properties.put(key, value);
    }

    public void setProperties(String key, HashMap<String, Object> value) {
        this.properties.put(key, value);
    }
    public String getProperty(String key) {
        return this.properties.get(key).toString();
    }
    public Object getProperties(String key) {
        return this.properties.get(key);
    }
    public HashMap<String, Object> getProperties() {
        return this.properties;
    }

    public Boolean hasProperty(String key) {
        return this.properties.containsKey(key);
    }

    public Boolean deleteProperty(String key) {
        if (this.properties.containsKey(key)) {
            this.properties.remove(key);
            return true;
        }
        return false;
    }
}
