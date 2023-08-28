package com.luma.listeners;


import com.google.common.base.Throwables;
import com.luma.framework.Log;
import com.luma.framework.ThreadPackage;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import ru.yandex.qatools.ashot.Screenshot;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class TestListener extends TestListenerAdapter implements ITestListener {
    // shows more detailed logs in console if set to true
    static final Boolean DEBUG_INFO= true;
    public WebDriver driver;

    @Attachment(value = "Element screenshot",type = "image/png")
    public static byte[]  attachScreenshot(Screenshot screenshot) {
        byte[] screenshotAs = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(screenshot.getImage(),"png",baos);
            screenshotAs = baos.toByteArray();
        } catch (Exception ignored) {
        }
        return screenshotAs;
    }

    //runs any time a test starts
    @Override
    public void onTestStart(ITestResult result) {
        Thread.currentThread().setName(
                Thread.currentThread().getId() + " - "
                        + result.getMethod().getMethodName());

// get test browser name
        String browserName = ThreadPackage.getInstance().getThreadPropertyManager().getProperty("browser");
        Log.info("===============================================");
        Log.info("================Test Started on browser " + browserName + " ================");
        Log.info("===============================================");
    }

    // runs any time a test passes
    @Override
    public void onTestSuccess(ITestResult result) {
        // get the method name for the test
        String methodName = result.getMethod().getMethodName();
        Log.info("================TEST PASSED================");
        Log.info("================================");
     }

    @Override
    public void onTestSkipped(ITestResult result) {
        Log.info("================TEST SKIPPED================");
        Log.info("================================");
        Log.error(Throwables.getStackTraceAsString(result.getThrowable()));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ITestNGMethod m = result.getMethod();
// store the test method name
        String methodName = m.getMethodName();
        Log.info("================TEST FAILED================");
        Log.info("================================");
        Log.error(Throwables.getStackTraceAsString(result.getThrowable()));

        String errorMessage = result.getThrowable().toString();

    }

    @Attachment(value = "Page screenshot",type = "image/png")
    public byte[] attachScreenshot() {
        return ((TakesScreenshot) ThreadPackage.getInstance().getThreadDriver()).getScreenshotAs(OutputType.BYTES);
    }
}
