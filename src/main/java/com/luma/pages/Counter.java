package com.luma.pages;


public class Counter {
    private static int var= 0;

    public static int getVar() {
        return Counter.var;
    }

    //If you do not want to change the var ever then do not include this
    public static void setVar(int var) {
        Counter.var= var;
    }
}

