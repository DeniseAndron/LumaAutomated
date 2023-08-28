package com.luma.framework;


import com.luma.listeners.DriverListener;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;



import java.util.HashMap;

public class DriverFactory {
    private WebDriver driver;
    private PropertyManager property;

    public DriverFactory(PropertyManager property) {
        this.property = property;
        driver = initDriver(property.getProperty("browser").toLowerCase());
    }

    private WebDriver initDriver(String browser) {
        WebDriver driver;
        String downloadPath = FileProcessor.getDownloadDir();

        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("download.default_directory", downloadPath);
        chromePrefs.put("plugins.always_open_pdf_externally", true);
        chromePrefs.put("excludeSwitches", new String[] { "enable-automation" });

        switch (browser.toLowerCase()) {
            case "jenkins-chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions jenkinsChromeOptions = new ChromeOptions();
                jenkinsChromeOptions.setAcceptInsecureCerts(false);
                jenkinsChromeOptions.setBinary("C:/Program Files/Google/Chrome/Application/chrome.exe");
                driver = new ChromeDriver(jenkinsChromeOptions);
                break;

            case "chrome-headless-linux":
                String chromeBinaryPath = "/usr/bin/google-chrome";
                WebDriverManager.chromedriver().setup();
                System.setProperty("test.disableScreenshotCapture", "true");
                ChromeOptions optionsLinux64 = new ChromeOptions();
                optionsLinux64.setAcceptInsecureCerts(true);
                optionsLinux64.setBinary(chromeBinaryPath);
                optionsLinux64.addArguments("--headless");
                optionsLinux64.addArguments("disable-infobars");
                optionsLinux64.addArguments("--no-sandbox");
                optionsLinux64.addArguments("window-size=1980,1080");
                optionsLinux64.addArguments("--disable-gpu");
                optionsLinux64.addArguments("--disable-dev-shm-usage");
                optionsLinux64.setExperimentalOption("prefs", chromePrefs);
                driver = new ChromeDriver(optionsLinux64);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setCapability("moz:useNonSpecCompliantPointerOrigin", true);
                firefoxOptions.addPreference("browser.privatebrowsing.autostart", true);
                firefoxOptions.addPreference("browser.download.dir", downloadPath);
                firefoxOptions.addPreference("browser.download.panel.shown", false);
                firefoxOptions.addPreference("pdfjs.disabled", true);
                firefoxOptions.addPreference("setEnableNativeEvents", true);
                firefoxOptions.addPreference("browser.download.folderList", 2);
                firefoxOptions.addPreference("browser.helperApps.neverAsk.saveToDisk",
                        "text/plain, application/vnd.openxmlformats-officedocument.wordprocessingml.document, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, "
                                + "application/pdf, image/jpeg, image/gif, application/json, application/vnd.ms-excel,application/octet-stream, application/x-gzip, text/html, application/xml "); // MIME
                // type
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                HashMap<String, Object> edgePrefs = new HashMap<>();
                edgePrefs.put("download.default_directory", downloadPath);
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setCapability("ms:inPrivate", true);
                driver = new EdgeDriver(edgeOptions);
                break;

            case "safari":
                SafariOptions options = new SafariOptions();
                options.setAcceptInsecureCerts(false);
                driver = new SafariDriver(options);
                break;

            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions windowsChromeOptions = new ChromeOptions();
                windowsChromeOptions.setAcceptInsecureCerts(false);
                windowsChromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
                windowsChromeOptions.addArguments("--deny-permission-prompts");
                windowsChromeOptions.addArguments("--disable-infobars");
                windowsChromeOptions.addArguments("incognito");
                windowsChromeOptions.addArguments("log-level=2");
                windowsChromeOptions.addArguments("--remote-allow-origins=*");
                windowsChromeOptions.setExperimentalOption("prefs", chromePrefs);

                driver = new ChromeDriver(windowsChromeOptions);
                break;
        }

        driver.manage().window().maximize();
        return driver;
    }

    public WebDriver getDriver() {
        WebDriverListener listener = new DriverListener(driver, property);
        return new EventFiringDecorator(listener).decorate(driver);
    }
}


