package com.luma.utils;


import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class WebDriverWaitLogged {

    private final WebDriverWait wait;

    public WebDriverWaitLogged(WebDriver driver) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(70));
    }

    public void waitForClickable(WebElement element) {
        wait.withTimeout(Duration.ofSeconds(90)).pollingEvery(Duration.ofMillis(250))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class).
                ignoring(ElementClickInterceptedException.class, ElementNotInteractableException.class).
                until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebDriverWait getWaitDriver() {
        return wait;
    }

    public void waitForElementToExist(By locator) {
        wait.withTimeout(Duration.ofSeconds(10)).pollingEvery(Duration.ofMillis(250))
                .ignoring(NoSuchElementException.class).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForVisibilityOfElement(WebElement element) {
        wait.withTimeout(Duration.ofSeconds(60)).pollingEvery(Duration.ofMillis(250))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(element));
    }

    // Added waits to check if elements can be clicked - used for upload com.luma.pages


    // Added waits to check if elements can be clicked - used for upload com.luma.pages
    public void waitForClickableUploadP(By locator) {
        wait.withTimeout(Duration.ofSeconds(90)).pollingEvery(Duration.ofMillis(250))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Added waits to check if element contains particular text
    public void waitForTexts(WebElement element, String text) {
        wait.withTimeout(Duration.ofSeconds(30)).pollingEvery(Duration.ofMillis(250))
                .ignoring(NoSuchElementException.class, ElementNotInteractableException.class)
                .until(ExpectedConditions.textToBePresentInElement(element, text));
    }


    // Added waits to check if element contains particular text
    public void waitForTextLong(WebElement element, String text) {
        wait.withTimeout(Duration.ofSeconds(300)).pollingEvery(Duration.ofMillis(250))
                .ignoring(NoSuchElementException.class)
                .until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    // Added waits to check if element contains particular text
    public void waitForText2(WebElement element, String text) {
        wait.withTimeout(Duration.ofSeconds(30)).pollingEvery(Duration.ofMillis(250))
                .ignoring(NoSuchElementException.class).until(ExpectedConditions.attributeToBe(element, "value", text));
    }

    public void waitForDownload(WebElement element, String text) {
        wait.withTimeout(Duration.ofMinutes(30)).pollingEvery(Duration.ofMillis(1000))
                .ignoring(NoSuchElementException.class)
                .until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public void waitNotVisible(WebElement element) {
        wait.withTimeout(Duration.ofSeconds(60)).pollingEvery(Duration.ofMillis(250))
                .ignoring(NoSuchElementException.class).until(ExpectedConditions.invisibilityOf(element));
    }

    public void waitForElementAttribute(WebElement elm, String attributName, String expectedString) {
        wait.withTimeout(Duration.ofSeconds(30)).pollingEvery(Duration.ofMillis(100))
                .ignoring(NoSuchElementException.class)
                .until(ExpectedConditions.attributeContains(elm, attributName, expectedString));

    }

    public void waitForElementToBeInvisible(By locator) {
        wait.withTimeout(Duration.ofSeconds(60)).pollingEvery(Duration.ofMillis(250))
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitForFrameToBeAvailableAndSwitchToIt(By locator) {
        wait.withTimeout(Duration.ofSeconds(60)).pollingEvery(Duration.ofMillis(250))
                .ignoring(NoSuchElementException.class).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }

    public void waitForPresenceOfListOfElements(By locator) {
        wait.withTimeout(Duration.ofSeconds(60)).pollingEvery(Duration.ofMillis(250))
                .ignoring(StaleElementReferenceException.class, NoSuchElementException.class)
                .until((ExpectedConditions.presenceOfAllElementsLocatedBy(locator)));
    }

    public void waitUntilElementContainsText(WebElement element, String text) {
        wait.withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class)
                .until(driver -> element.getText().contains(text));
    }

    public void waitUntilFileIsDownloaded(String path, String partialName, String extension) {
        File dir = new File(path);
        wait.pollingEvery(Duration.ofMillis(100));
        wait.withTimeout(Duration.ofMinutes(60));
        wait.until(x -> {
            File[] filesInDir = dir.listFiles();
            for (File fileInDir : filesInDir) {
                if (fileInDir.getName().startsWith(partialName) && fileInDir.getName().endsWith(extension)) {
                    return true;
                }
            }
            return false;
        });
    }

    public void waitForElementInFrame(WebElement frame, By elementLocator) {
        wait.pollingEvery(Duration.ofMillis(250))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);

        wait.until(driver -> {
            driver.switchTo().defaultContent();
            driver.switchTo().frame(frame);
            return driver.findElement(elementLocator);
        });
    }
    public void waitForElementInFrame(WebElement frame, WebElement elementLocator) {
        wait.pollingEvery(Duration.ofMillis(250))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);

        wait.until(driver -> {
            driver.switchTo().defaultContent();
            driver.switchTo().frame(frame);
            return elementLocator.isDisplayed();
        });
    }
}
