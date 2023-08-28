package com.luma.pages;


import com.luma.framework.PropertyManager;
import com.luma.framework.ThreadPackage;
import com.luma.listeners.Retry;
import com.luma.listeners.TestListener;
import com.luma.framework.FileProcessor;
import com.luma.framework.Log;
import com.luma.utils.UiQueryProcessor;
import io.qameta.allure.Step;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

@Listeners(TestListener.class)
public class BaseTest {
    protected UiQueryProcessor uiQueryProcessor;
    String environment = System.getProperty("luma.env");

    public WebDriver getDriver() {
        return ThreadPackage.getInstance().getThreadDriver();
    }

    public PropertyManager getPropertyManager() {
        return ThreadPackage.getInstance().getThreadPropertyManager();
    }

    public FileProcessor getFileProcessor() {
        return ThreadPackage.getInstance().getThreadFileProcessor();
    }

    public SoftAssert assertion() {
        return ThreadPackage.getInstance().getThreadSoftAssert();
    }

    @BeforeSuite(alwaysRun = true)
    public void setUp(ITestContext context) {
        for (ITestNGMethod method : context.getAllTestMethods()) {
            method.setRetryAnalyzer(new Retry());
        }
        try {
            FileProcessor.cleanDirectory();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PropertyManager propertyManager = getPropertyManager();
        propertyManager.setProperties("browser",System.getProperty("browser"));
        propertyManager.setProperties("environment",environment);
        Counter.setVar(0);
        propertyManager.setProperties("database_check",this.validateDatabase());

        if (this.validateDatabase()) {
            this.uiQueryProcessor = new UiQueryProcessor(propertyManager);
        }
    }

    public boolean validateDatabase() {
        return (StringUtils.isNotEmpty(System.getenv("database_check")))
                ? BooleanUtils.toBoolean(System.getenv("database_check"))
                : StringUtils.isNotEmpty(getPropertyManager().getProperty("database_check"))
                && BooleanUtils.toBoolean(getPropertyManager().getProperty("database_check"));
    }

    @Step
    public void threadSleep(int miliseconds) {
        Log.info("Wait for " + miliseconds);
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step("Sign off")
    public void signOff() {
        Log.info("Sign off");

    }



    @AfterMethod(alwaysRun = true)
    public void shutDown() {
        if (getDriver() != null) {
            getDriver().quit();
            ThreadPackage.getInstance().removeWebDriver();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        try {
            FileProcessor.cleanDirectory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void createDriver() throws AWTException {
        this.getDriver();
        if (getPropertyManager().getProperty("browser").equals("MicrosoftEdge")) {
            // Create object of Robot class
            Robot robot = new Robot();
// Press Shift key + CTRL + N
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_N);
// Release Shift+CTRL+N keys
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_N);
        }
    }

    @Step("Sign on to the system with a non-SSO user without closing tabs")
    public void signOnWithoutClosingTabs(String username,String password) {
        Log.info("Sign on to the system with a non-SSO user without closing tabs");

        LumaLogin loginpage = new LumaLogin(getDriver());
        getDriver().manage().deleteAllCookies();

    }

}

