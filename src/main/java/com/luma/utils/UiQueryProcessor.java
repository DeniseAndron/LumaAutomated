package com.luma.utils;


import com.luma.framework.PropertyManager;
import org.testng.SkipException;


public class UiQueryProcessor {

    PropertyManager propertyManager;

    public UiQueryProcessor(PropertyManager pm) {

        try {
            this.propertyManager = pm;

        } catch (Exception e) {
            throw new SkipException(e.getMessage());
        }
    }


}
