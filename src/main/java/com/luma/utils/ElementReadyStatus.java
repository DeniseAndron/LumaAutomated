package com.luma.utils;


import com.luma.framework.PropertyManager;
import com.luma.framework.ThreadPackage;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class ElementReadyStatus {
    static final String LUMACREATE = "return window.store.getState().changeControlFullTable.loading";
    static final String LUMAHOMEPAGE = "return window.store.getState().issueFullTable.loading";

    private WebDriver driver;
    private int timeOutInSeconds;
    private WebDriverWait wait;
    private PropertyManager propertyManager;
    private int recursion;

    public ElementReadyStatus() {
        this.driver = ThreadPackage.getInstance().getThreadDriver();
        propertyManager = ThreadPackage.getInstance().getThreadPropertyManager();
        this.timeOutInSeconds = (propertyManager.hasProperty("timeout"))
                ? Integer.parseInt(propertyManager.getProperty("timeout"))
                : 30;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
        this.recursion = 0;
    }

    public ElementReadyStatus(WebDriver driver) {
        this.driver = driver;
        this.timeOutInSeconds = 30;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
        this.recursion = 0;
    }

    public ElementReadyStatus(WebDriver driver, PropertyManager propertyManager) {
        this.driver = driver;
        this.timeOutInSeconds = (propertyManager.hasProperty("timeout"))
                ? Integer.parseInt(propertyManager.getProperty("timeout"))
                : 30;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
        this.recursion = 0;
    }

    public WebDriver getDriver() {
        return ThreadPackage.getInstance().getThreadDriver();
    }

    /**
     * Object Exists by locator using By method to find if it exists and returns
     * true/false for Element Exists.
     *
     * @param locator By Locator object value timeOutInSeconds timeout value for
     *                when locator not found.
     * @return boolean
     */
    public boolean isElementExists(By locator) {
        try {
            return this.wait.until(ExpectedConditions.presenceOfElementLocated(locator)) != null;
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementExists(WebElement element) {
        try {
            return this.wait.until(ExpectedConditions.visibilityOf(element)) != null;
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementExists(String element) {// useful for pseudo-elements
        if (this.recursion < 10) {
            try {
                JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
                int length = Integer
                        .parseInt(jsExecutor.executeScript("return $('" + element + "').length;").toString());
                if (length == 0) {
                    long waitTime = Instant.now().toEpochMilli() + (this.timeOutInSeconds * 1000);// millisecond
                    while (Instant.now().toEpochMilli() < waitTime) {
                        length = Integer
                                .parseInt(jsExecutor.executeScript("return $('" + element + "').length;").toString());
                        if (length == 1) {
                            this.recursion = 0;
                            return true;
                        }

                        this.pause(250);// 1/4 of second
                    }
                }
                this.recursion = 0;
                return true;
            } catch (NoSuchElementException e) {
                this.recursion++;
                this.pause(1000);
                isElementExists(element);
            }
        }
        this.recursion = 0;
        return false;
    }

    public boolean isDownloadShadowRootExists(String element) {// shadowRoot pseudo-element
        if (this.recursion < 10) {
            try {
                JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
                int length = Integer.parseInt(jsExecutor.executeScript(
                                "return document.querySelector('downloads-manager').shadowRoot.querySelectorAll('" + element
                                        + "').length;")
                        .toString());
                if (length == 0) {
                    long waitTime = Instant.now().toEpochMilli() + (this.timeOutInSeconds * 1000);// millisecond
                    while (Instant.now().toEpochMilli() < waitTime) {
                        length = Integer.parseInt(jsExecutor.executeScript(
                                        "return document.querySelector('downloads-manager').shadowRoot.querySelectorAll('"
                                                + element + "').length;")
                                .toString());
                        if (length >= 1) {
                            this.recursion = 0;
                            return true;
                        }

                        this.pause(250);// 1/4 of second
                    }
                }
                this.recursion = 0;
                return true;
            } catch (NoSuchElementException e) {
                this.recursion++;
                this.pause(1000);
                isElementExists(element);
            }
        }
        this.recursion = 0;
        return false;
    }

    /**
     * Object Visible by WebElement using By method to find by visibility and
     * returns true/false for Element Visible.
     *
     * @param element By Locator object value timeOutInSeconds timeout value for
     *                when locator not found.
     * @return boolean
     */
    public boolean isElementVisible(WebElement element) {
        try {
            return this.wait.until(ExpectedConditions.visibilityOf(element)) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementVisible(WebElement element, int timeout, int polling) {
        try {
            return this.wait.withTimeout(Duration.ofSeconds(timeout)).pollingEvery(Duration.ofMillis(polling))
                    .until(ExpectedConditions.visibilityOf(element)) != null;
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Object Visible by locator using By method to find by visibility and returns
     * true/false for Element Visible.
     *
     * @param locator By Locator object value timeOutInSeconds timeout value for
     *                when locator not found.
     * @return boolean
     */
    public boolean isElementVisible(By locator) {
        try {
            return (this.wait.until(ExpectedConditions.visibilityOfElementLocated(locator)) != null) ? true : false;
        } catch (TimeoutException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementVisible(List<WebElement> locator) {
        try {
            return (this.wait.until(ExpectedConditions.visibilityOfAllElements(locator)) != null) ? true : false;
        } catch (TimeoutException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementInvisible(By locator) {
        try {
            return this.wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementClickable(WebElement element) {
        try {
            if (this.isElementVisible(element)) {
                return (this.wait.until(ExpectedConditions.elementToBeClickable(element)) != null) ? true : false;
            }
            return false;
        } catch (TimeoutException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementClickable(By locator) {
        try {
            if (this.isElementVisible(locator)) {
                return (this.wait.until(ExpectedConditions.elementToBeClickable(locator)) != null) ? true : false;
            }
            return false;
        } catch (TimeoutException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementEnabled(WebElement element) {
        try {
            return this.isElementVisible(element) && element.isEnabled();
        } catch (TimeoutException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementEnabled(By locator) {
        try {
            return this.isElementVisible(locator) && this.driver.findElement(locator).isEnabled();
        } catch (TimeoutException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTextPresent(WebElement element, String text) {
        try {
            return this.wait.until(ExpectedConditions.textToBePresentInElement(element, text));
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    public String getText(WebElement element) {
        String text = "";
        try {
            if (this.isElementVisible(element)) {
                text = element.getText();
            }
        } catch (TimeoutException | NoSuchElementException e) {
            text = "";
        }
        return text;
    }

    public String getText(By element) {
        String text = "";
        try {
            if (this.isElementVisible(element)) {
                text = this.driver.findElement(element).getText();
            }
        } catch (TimeoutException | NoSuchElementException e) {
            text = "";
        }
        return text;
    }

    public boolean isLoadingComplete(By locator) { // Loading ... Element
        try {
            if (this.isElementVisible(locator)) {
                return this.wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            }
            return true;
        } catch (TimeoutException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoadingComplete(By locator, boolean checkVisible) {
        try {

            if (checkVisible && this.isElementVisible(locator)) {
                return this.wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            } else {
                return this.wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            }
        } catch (TimeoutException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoadingComplete(List<By> locators) { // List<Loading> ... Element
        try {
            int cnt = 0;
            boolean loaded = true;
            boolean isFirst = true;
            for (By locator : locators) {
                if (this.isLoadingComplete(locator, isFirst)) {
                    cnt++;
                    isFirst = false;
                } else {
                    loaded = false;
                    break;
                }
            }
            return (loaded && (cnt == locators.size()));

        } catch (TimeoutException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTableElementsLoaded(WebElement table) {
        try {
            List<WebElement> tableRows = this.wait
                    .until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(table, By.tagName("tr")));
            return tableRows.size() > 0;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTableElementsLoaded(WebElement table, int trCount) {
        try {
            List<WebElement> tableRows = this.wait
                    .until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(table, By.tagName("tr")));
            return tableRows.size() == trCount;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTableLoaded(WebElement table, int whichTable) {
        if (isElementVisible(table)) {
            return (isObjectReadyInDom(whichTable) && isTableElementsLoaded(table) && isElementVisible(
                    table.findElement(By.tagName("tbody")).findElement(By.tagName("tr")).findElement(By.tagName("td"))))
                    ? true
                    : false;
        }
        return false;

    }

    public boolean isTableLoaded(int tableCol, String searchTxt, WebElement table, int whichTable) {// tableCol start
        // with 0
        if (isElementVisible(table)) {
            return (isTableSearchDone(tableCol, searchTxt, table) && isObjectReadyInDom(whichTable)
                    && isTableElementsLoaded(table)) ? true : false;
        }
        return false;

    }

    public boolean isTableLoaded(String searchTxt, WebElement table, int whichTable) {
        if (isElementVisible(table)) {
            return isTableSearchDone(0, searchTxt, table) && isObjectReadyInDom(whichTable)
                    && isTableElementsLoaded(table);
        }
        return false;

    }

    public boolean isTableSearchDone(int tableCol, String searchTxt, WebElement table) { // Only call after Table search
        if (isElementVisible(table) && this.recursion < 10) {// Max Retry
            String colText = table.findElement(By.tagName("tbody")).findElement(By.tagName("tr"))
                    .findElements(By.tagName("td")).get(tableCol).getText().trim().toLowerCase();
            if (!colText.startsWith(searchTxt.toLowerCase())) {
                long waitTime = Instant.now().toEpochMilli() + (this.timeOutInSeconds * 1000);// millisecond
                while (Instant.now().toEpochMilli() < waitTime) {
                    try {
                        colText = table.findElement(By.tagName("tbody")).findElement(By.tagName("tr"))
                                .findElements(By.tagName("td")).get(tableCol).getText().trim().toLowerCase();
                        if (colText.startsWith(searchTxt.toLowerCase())) {
                            this.recursion = 0;
                            return true;
                        }
                    } catch (Exception e) {// IF WebElement[Table] is not accessible during the loop
                        this.recursion++;
                        this.pause(1000);
                        isTableSearchDone(tableCol, searchTxt, table);
                    }
                    this.pause(250);// 1/4 of second
                }
            } else {
                this.recursion = 0;
                return true;
            }
        }
        this.recursion = 0;
        return false;
    }

    public boolean isTableLoaded(WebElement loading, WebElement table, int whichTable) {
        if (isElementVisible(loading) && isElementVisible(table)) {
            return (isObjectReadyInDom(whichTable) && isTableElementsLoaded(table)) ? true : false;
        }
        return false;

    }

    public boolean isObjectReadyInDom(int whichTable) {
        String opt = null;
        switch (whichTable) {
            case 1:
                opt = LUMACREATE;
                break;

            case 3:
                opt = LUMAHOMEPAGE;
                break;

        }
        long waitTime = Instant.now().toEpochMilli() + (this.timeOutInSeconds * 1000);// millisecond
        JavascriptExecutor js = (JavascriptExecutor) this.driver;
        while (Instant.now().toEpochMilli() < waitTime) {
            if (js.executeScript(opt).toString().equals("false")) {
                return true;
            }
        }
        return false;
    }

    public static By webElementToBy(WebElement we) {
        String element = we.toString()
                .split("(?=\\sid:\\s|\\sname:\\s|\\sselector:\\s|\\slink text:|\\sxpath:\\s|"
                        + "By.id:\\s|By.name:\\s|By.tagName:\\s|By.className:\\s|By.cssSelector:\\s|"
                        + "By.linkText:\\s|By.partialLinkText:\\s|By.xpath:\\s)")[1];

        String[] locator = StringUtils.removeEnd(element, "]").split(":\\s");
        String method = locator[0].trim();
        if (method.equals("xpath"))
            return By.xpath(locator[1]);

        String selector = StringUtils.removeEnd(locator[1], "'");
        switch (method) {
            case "id":
            case "By.id":
                return By.id(selector);
            case "name":
            case "By.name":
                return By.name(selector);
            case "By.tagName":
                return By.tagName(selector);
            case "By.className":
                return By.className(selector);
            case "selector":
            case "By.cssSelector":
                return By.cssSelector(selector);
            case "By.linkText":
                return By.linkText(selector);
            case "link text":
            case "By.partialLinkText":
                return By.partialLinkText(selector);
            case "By.xpath":
                return By.name(selector);
            default:
                System.out.println("Error! [" + method + "]");
                return null;
        }
    }

    public void pause(int miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        this.driver.navigate().refresh();
    }

    public boolean isFrameLoaded(String element) {
        if (this.recursion < 10) {
            try {
                JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
                int length = Integer
                        .parseInt(jsExecutor.executeScript("return $('" + element + "').length;").toString());
                if (length == 0) {
                    long waitTime = Instant.now().toEpochMilli() + (this.timeOutInSeconds * 1000);// millisecond
                    while (Instant.now().toEpochMilli() < waitTime) {
                        try {
                            length = Integer.parseInt(
                                    jsExecutor.executeScript("return $('" + element + "').length;").toString());
                            if (length == 1) {
                                this.recursion = 0;
                                return true;
                            }
                        } catch (Exception e) {
                            this.recursion++;
                            this.pause(1000);
                            isFrameLoaded(element);
                        }
                        this.pause(250);// 1/4 of second
                    }
                }
                this.recursion = 0;
                return true;
            } catch (NoSuchElementException e) {
                this.recursion++;
                this.pause(1000);
                isFrameLoaded(element);
            }
        }
        this.recursion = 0;
        return false;
    }

    public boolean isListOfElementsVisible(List<WebElement> list) {
        try {
            wait.pollingEvery(Duration.ofMillis(250)).until(ExpectedConditions.visibilityOfAllElements(list));
            return true;
        } catch (StaleElementReferenceException | NoSuchElementException e) {
            return false;
        }
    }

    public boolean isFrameLoaded(By frame) {
        try {
            wait.pollingEvery(Duration.ofMillis(250)).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame));
            return true;
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    public boolean isFrameLoaded(WebElement frame) {
        try {
            wait.pollingEvery(Duration.ofMillis(250)).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame));
            return true;
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementInFrame(WebElement frame, By elementLocator) {
        try {
            if (isFrameLoaded(frame)) {
                this.wait.pollingEvery(Duration.ofMillis(250)).until(driver -> {
                    driver.switchTo().defaultContent();
                    driver.switchTo().frame(frame);
                    return driver.findElement(elementLocator);
                });
                return true;
            }
            return false;
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    public boolean isLoadingImageStarted(String element) {
        if (this.recursion < 10) {
            try {
                JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
                int length = Integer
                        .parseInt(jsExecutor.executeScript("return $('" + element + "').length;").toString());
                if (length == 0) {
                    long waitTime = Instant.now().toEpochMilli() + (this.timeOutInSeconds * 1000);// millisecond
                    while (Instant.now().toEpochMilli() < waitTime) {
                        length = Integer
                                .parseInt(jsExecutor.executeScript("return $('" + element + "').length;").toString());
                        if (length == 1) {
                            this.recursion = 0;
                            return true;
                        }

                        this.pause(250);// 1/4 of second
                    }
                }
                this.recursion = 0;
                return true;
            } catch (NoSuchElementException e) {
                this.recursion++;
                this.pause(1000);
                isLoadingImageStarted(element);
            }
        }
        this.recursion = 0;
        return false;
    }

    public boolean isLoadingImageFinished(String element) {
        if (this.recursion < 10) {
            try {
                JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
                int length = Integer
                        .parseInt(jsExecutor.executeScript("return $('" + element + "').length;").toString());
                if (length == 1) {
                    long waitTime = Instant.now().toEpochMilli() + (this.timeOutInSeconds * 1000);// millisecond
                    while (Instant.now().toEpochMilli() < waitTime) {
                        length = Integer
                                .parseInt(jsExecutor.executeScript("return $('" + element + "').length;").toString());
                        if (length == 0) {
                            this.recursion = 0;
                            return true;
                        }

                        this.pause(250);// 1/4 of second
                    }
                }
                this.recursion = 0;
                return true;
            } catch (NoSuchElementException e) {
                this.recursion++;
                this.pause(1000);
                isLoadingImageFinished(element);
            }
        }
        this.recursion = 0;
        return false;
    }

    public boolean isNumberOfWindowsEqual(int numberOfWindows) {
        try {
            this.wait.until(ExpectedConditions.numberOfWindowsToBe(numberOfWindows));
            return true;
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

}

