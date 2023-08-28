package com.luma.framework;



import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;
import com.luma.framework.FileProcessor;
public class ThreadPackage {
    ThreadLocal<PropertyManager> threadPropertyManager = new ThreadLocal<>();
    ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();
    ThreadLocal<SoftAssert> threadSoftAssert = new ThreadLocal<>();
    ThreadLocal<FileProcessor> threadFileProcessor = new ThreadLocal<>();
    private static ThreadPackage threadPackage;

    private String environment;
    private final String apiVersion;
    private final String browser;
    private final String url;

    private ThreadPackage() {
        environment = System.getProperty("luma.env");
        apiVersion = System.getProperty("env.apiVersion");
        browser = System.getProperty("browser");
        url = System.getProperty("env.url");
    }

    public static ThreadPackage getInstance() {
        if (threadPackage == null) {
            threadPackage = new ThreadPackage();
        }
        return threadPackage;
    }

    public void setThreadPropertyManager(PropertyManager pm) {
        threadPropertyManager.set(pm);
    }

    public PropertyManager getThreadPropertyManager() {
        if (threadPropertyManager.get() == null) {
            PropertyManager propertyManager = new PropertyManager(this.environment, this.apiVersion);
            propertyManager.setProperties("browser", browser);
            propertyManager.setProperties("environment", environment);
            propertyManager.setProperties("version", apiVersion);
            if (!propertyManager.hasProperty("url")) {
                propertyManager.setProperties("url", url);
            }
            this.setThreadPropertyManager(propertyManager);
        }
        return threadPropertyManager.get();
    }


    private void setThreadDriver(WebDriver driver) {
        threadDriver.set(driver);
    }

    public WebDriver getThreadDriver() {
        if (threadDriver.get() == null) {
            this.setThreadDriver(new DriverFactory(getThreadPropertyManager()).getDriver());
        }
        return threadDriver.get();
    }

    public void removeWebDriver() {
        threadDriver.remove();
    }

    public void setSoftAssert(SoftAssert softAssert) {
        threadSoftAssert.set(softAssert);
    }

    public SoftAssert getThreadSoftAssert() {
        if (threadSoftAssert.get() == null) {
            this.setSoftAssert(new SoftAssert());
        }
        return threadSoftAssert.get();
    }

    public void threadSoftAssertAll() {
        threadSoftAssert.get().assertAll();
    }

    //
    public void setThreadFileProcessor(FileProcessor fileUtils) {
        threadFileProcessor.set(fileUtils);
    }

    public FileProcessor getThreadFileProcessor() {
        if (threadFileProcessor.get() == null) {
            this.setThreadFileProcessor(new FileProcessor(threadPropertyManager.get()));
        }
        return threadFileProcessor.get();
    }
}
