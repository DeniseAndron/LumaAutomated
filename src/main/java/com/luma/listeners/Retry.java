package com.luma.listeners;


import com.google.common.base.Throwables;
import com.luma.framework.Log;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {

    private static final int MAX_TRY= Integer.parseInt(System.getProperty("retry"));
    private int count = 0;

    @Override
    public boolean retry(ITestResult iTestResult) {

        if (count <MAX_TRY) {                            //Check if maxtry count is reached
            Log.info("==================RETRYING " + iTestResult.getMethod().getMethodName() + " AFTER==================");
            Log.info("===============================================");
            Log.info(Throwables.getStackTraceAsString(iTestResult.getThrowable()));
            Log.info("===============================================");
            count++;//Increase the maxTry count by 1

            return true;//Tells TestNG to re-run the test
        }

        return false;
    }
}

