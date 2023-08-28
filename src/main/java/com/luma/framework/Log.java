package com.luma.framework;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Log {
    private static final Logger LOGGER = LogManager.getLogger(Log.class);

    public static void info(String message) {
        LOGGER.info("\u001B[33m" + message +  "\u001B[0m");
    }

    public static void debug(String message) {
        LOGGER.debug("\u001B[34m" + message + "\u001B[0m");
    }

    public static void error(String log) {
        LOGGER.error("\u001B[31m" + log +  "\u001B[0m");
    }
}
