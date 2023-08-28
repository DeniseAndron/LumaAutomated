package com.luma.pages;


import com.luma.framework.FileProcessor;
import com.luma.framework.Log;
import com.luma.framework.PropertyManager;
import com.luma.framework.ThreadPackage;
import com.luma.listeners.TestListener;
import com.luma.utils.*;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BasePage {
    protected int timeout = 30;// second
    protected final WebDriverWaitLogged driverWait;
    private final Actions actions;
    protected PropertyManager propertyManager;
    protected ElementReadyStatus elementReadyStatus;
    protected FileProcessor fileProcessor;
    // Section Block

    public WebDriver getDriver() {
        return ThreadPackage.getInstance().getThreadDriver();
    }

    public PropertyManager getPropertyManager() {
        return ThreadPackage.getInstance().getThreadPropertyManager();
    }

    public SoftAssert assertion() {
        return ThreadPackage.getInstance().getThreadSoftAssert();
    }

    public BasePage() {
        actions = new Actions(getDriver());
        driverWait = new WebDriverWaitLogged(getDriver());
        this.propertyManager = ThreadPackage.getInstance().getThreadPropertyManager();
        this.timeout = (this.propertyManager.hasProperty("timeout"))
                ? Integer.parseInt(this.propertyManager.getProperty("timeout"))
                : timeout;
        elementReadyStatus = new ElementReadyStatus(getDriver());
        fileProcessor = ThreadPackage.getInstance().getThreadFileProcessor();
        PageFactory.initElements(getDriver(),this);
    }

    @Step("Open URL")
    protected void openURL(String url) {
        Log.info("Open URL");
        getDriver().navigate().to(url);
    }

    // *********************Screenshots*****************************//


    @Step("Take a screenshot of element")
    protected Screenshot getElementScreenshot(WebElement element) {
        Log.info("Take a screenshot of element");
        return new AShot().coordsProvider(new WebDriverCoordsProvider()).takeScreenshot(getDriver(),element);
    }


    ///// ********BASIC FUNCTIONS
    ///// SCROLL/HOVER/GETTEXT/UPLOADS*************************//


    @Step("Scroll to element")
    public void scrollToElement(WebElement element) {
        Log.info("Scroll to element");
        threadSleep(500);
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);",element);
        threadSleep(200);
    }

    @Step("Scroll and put the element in the center of the page")
    public void scrollToElementCentered(WebElement element) {
        Log.info("Scroll and put the element in the center of the page");
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].scrollIntoView(({ behavior: 'auto', block: 'center' }));",element);
        threadSleep(200);
    }

    @Step("Type into field")
    protected void typeTextWithoutClear(WebElement webElement,String text) {
        Log.info("Type into field");
        webElement.sendKeys(text);
    }

    @Step("Retrieve text from element")
    protected String getTextOrValue(WebElement element) {
        Log.info("Retrieve text from element");
        String hold = element.getText();
        if (hold.length() <= 0) {
            hold = element.getAttribute("value");
        }
        return hold;
    }

    @Step("Retrieve text from element")
    protected String getText(WebElement element) {
        Log.info("Retrieve text from element");
        return getTextOrValue(element);
    }


    @Step("click with action")
    public void clickWithAction(WebElement element) {
        Log.info("click with action");
        Actions action = new Actions(getDriver());
        action.click(element).click().build().perform();
    }

    @Step("Check if fields display or exist")
    public boolean chkFieldDisplayExist(WebElement elm) {
        Log.info("Check if fields display or exist");
        try {
            elm.isDisplayed();
            return true;

        } catch (NoSuchElementException exception) {
            return false;
        }
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

    @Step("Clear field")
    public void clearField(WebElement elm) {
        Log.info("Clear field");
        threadSleep(500);
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].select();",elm);
        elm.sendKeys(Keys.BACK_SPACE);
        threadSleep(500);
    }

    @Step("Clear field and set value")
    public void clearAndSetValueToField(WebElement element,String value) {
        Log.info("Clear field and set value");
        clearField(element);
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].select();",element);
        element.sendKeys(value);
        threadSleep(500);
    }

    public boolean isElementPresent(String cssLocator) {
        try {
            getDriver().findElement(By.cssSelector(cssLocator));
            return true;// Success!
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }



    ////////// Functions that search ///////////////////////////////////////////

    /// This function in only used on the account role permission page, the name of
    // the user is in a seperate row from the delete button - no easy way to change
    public int containsOptionInRow(List<WebElement> liselm,String txtOpt) {
        String answer = "false";
        int total = 0;

        for (WebElement elm : liselm) {

            String hold = elm.getText().toLowerCase();
            total = total + 1;
            if (hold.contains(txtOpt.toLowerCase())) {
                answer = "true";
                total = total - 1;
                break;
            }
        }
        if (answer.equals("false")) {
            total = 488;
        }
        return total;

    }

    public boolean containsOnlyTwo(List<WebElement> liselm,String one,String two) {
        boolean answer = true;
        for (WebElement elm : liselm) {

            String hold = elm.getText().toLowerCase();
            if (!hold.equalsIgnoreCase(one) && (!hold.equalsIgnoreCase(two))) {

                answer = false;
            }

        }
        return answer;
    }

    public boolean listContains(List<WebElement> listElm,String txtOpt) {
        boolean answer = false;
        ArrayList<String> listTxt = new ArrayList<>();
        for (WebElement elm : listElm) {
            listTxt.add(elm.getText().toLowerCase());
        }
        if (listTxt.contains(txtOpt.toLowerCase())) {
            answer = true;
        }
        return answer;
    }

    public boolean listContainsExact(List<WebElement> liselm,String txtOpt) {
        boolean answer = false;

        for (WebElement elm : liselm) {

            String hold = elm.getText().toLowerCase();
            if (hold.equalsIgnoreCase(txtOpt.toLowerCase())) {
                {
                    answer = true;
                    break;
                }
            }

        }
        return answer;
    }

    // Attribute Exist
    public boolean listContainsAttribute(List<WebElement> liselm,String at,String txtOpt) {
        boolean answer = false;

        for (WebElement elm : liselm) {

            String hold = elm.getAttribute(at).toLowerCase();
            if (hold.contains(txtOpt.toLowerCase())) {
                {
                    answer = true;
                    break;
                }
            }

        }
        return answer;
    }

    // This is used on Issues/Change control page when you click roles/permission to
    // see which roles are included
    public boolean checkEachLine(List<WebElement> liselm,List<WebElement> liselm2,String txtOpt) {
        boolean answer = false;

        for (WebElement elm : liselm) {
            answer = false;
            for (WebElement elm2 : liselm2) {

                String hold = elm2.getText().toLowerCase();
                if (hold.contains(txtOpt.toLowerCase())) {
                    {
                        answer = true;
                        break;
                    }
                }
            }

        }
        return answer;
    }

    // This is used to get a list of roles currently on the system - when
    // users click the signature add role the list that comes back is compared to
    // this list - once db reads are set-up this can be replaced
    public List<String> returnListValues(List<WebElement> liselm) {

        List<String> listOfRoles = new ArrayList<>();
        for (WebElement elm : liselm) {

            String hold = elm.getText();
            listOfRoles.add(hold);
        }
        return listOfRoles;
    }

    public boolean containsOnlyOne(List<WebElement> liselm,String txtOpt) {
        boolean answer = true;

        for (WebElement elm : liselm) {

            String hold = elm.getText().toLowerCase().trim();
            if (!hold.equals(txtOpt.toLowerCase().trim())) {
                {
                    answer = false;
                    break;
                }
            }

        }
        return answer;
    }

    public boolean containsOnlyOneClass(List<WebElement> liselm,String txtOpt) {
        boolean answer = true;

        for (WebElement elm : liselm) {

            String hold = elm.getAttribute("class");

            if (!hold.equals(txtOpt.toLowerCase())) {
                {
                    answer = false;
                    break;
                }
            }

        }
        return answer;
    }

    public boolean isClassInList(List<WebElement> liselm,String txtOpt) {
        boolean answer = false;

        for (WebElement elm : liselm) {
            String hold = elm.getAttribute("class");
            if (hold.equals(txtOpt.toLowerCase())) {
                {
                    answer = true;
                    break;
                }
            }

        }
        return answer;
    }

    public boolean containsClassValue(WebElement elm,String txtOpt) {
        boolean answer = false;
        String hold = elm.getAttribute("class");
        Log.debug("HOld trim" + hold);
        Log.debug("text trim" + txtOpt);
        if (hold.equals(txtOpt)) {
            answer = true;
        }

        return answer;
    }

    // Used to select option from drop down
    public void clickMatchingOption(List<WebElement> liselm,String txtOpt) {
        for (WebElement elm : liselm) {
            String hold = elm.getText().toLowerCase();
            if (hold.length() == 0) {
                hold = elm.getAttribute("value").toLowerCase();
            }
            if (hold.equals(txtOpt.toLowerCase())) {
                {
                    driverWait.waitForClickable(elm);
                    elm.click();
                    break;
                }
            }
        }
    }

    // Scroll through drop-down until option is found and then click on the option
    public void selectMatchingOption(List<WebElement> liselm,String txtOpt) {
        for (WebElement elm : liselm) {
            scrollToElement(elm);
            if (elm.getText().equalsIgnoreCase(txtOpt)) {
                elm.click();
                break;
            }
        }
    }

    // Used to select option from drop down
    public void clickMatchingOptionAction(List<WebElement> liselm,String txtOpt) {

        for (WebElement elm : liselm) {

            String hold = elm.getText().toLowerCase();
            if (hold.length() == 0) {
                hold = elm.getAttribute("value").toLowerCase();
            }
            if (hold.equals(txtOpt.toLowerCase())) {
                {
                    driverWait.waitForClickable(elm);
                    Actions action = new Actions(getDriver());
                    action.click(elm).build().perform();
                    break;
                }
            }

        }
    }

    // Same as above but uses clickSimple - need to see if I can get rid of one
    public void clickMatchingOptionJs(List<WebElement> liselm,String txtOpt) {

        for (WebElement elm : liselm) {

            String hold = elm.getText().toLowerCase();
            if (hold.equals(txtOpt.toLowerCase())) {
                {
                    clickSimpleWithJS(elm);
                }
            }

        }

    }

    // Need to check if this can be replaced with all matching
    public void clickContainsOption(List<WebElement> liselm,String txtOpt) {
        for (WebElement elm : liselm) {
            String hold = elm.getText().toLowerCase();
            if (hold.length() == 0) {
                hold = elm.getAttribute("value").toLowerCase();
            }

            if (hold.contains(txtOpt.toLowerCase())) {
                driverWait.waitForClickable(elm);
                elm.click();
                break;
            }
        }
    }

    // Need to check if this can be replaced with all matching
    public void clickContainsOptionDtext(List<WebElement> liselm,String txtOpt) {
        for (WebElement elm : liselm) {
            String hold = elm.getText().toLowerCase();
            if (hold.length() == 0) {
                hold = elm.getAttribute("innerHTML").toLowerCase();
            }
            Log.debug(hold);
            if (hold.contains(txtOpt.toLowerCase())) {
                driverWait.waitForClickable(elm);
                elm.click();
                break;
            }
        }
    }

    // Need to check if this can be replaced with all matching
    public boolean confirmOptionIsInList(List<WebElement> liselm,String txtOpt) {
        boolean answer = false;
        for (WebElement elm : liselm) {
            String hold = elm.getText().toLowerCase();
            if (hold.contains(txtOpt.toLowerCase())) {
                answer = true;
                break;
            }
        }
        return answer;
    }

    // Need to check if this can be replaced with all matching


    public void clickContainsOptionJS(List<WebElement> liselm,String txtOpt) {
        for (WebElement elm : liselm) {
            String hold = elm.getText().toLowerCase();
            if (hold.length() == 0) {
                hold = elm.getAttribute("value").toLowerCase();
            }
            if (hold.contains(txtOpt.toLowerCase())) {

                JavascriptExecutor js = (JavascriptExecutor) getDriver();
                js.executeScript("arguments[0].click();",elm);
                break;
            }
        }
    }

    public void clickFirstOption(List<WebElement> liselm) {

        for (WebElement elm : liselm) {
            driverWait.waitForClickable(elm);
            elm.click();
            break;

        }

    }

    public void clickAll(List<WebElement> liselm) {

        for (int i = 0;i <= liselm.size();i++) {
            for (WebElement elm : liselm) {

                threadSleep(400);
// driverWait.waitForClickable(elm);
                JavascriptExecutor js = (JavascriptExecutor) getDriver();
                js.executeScript("arguments[0].click();",elm);
                threadSleep(200);
                break;
            }
        }

    }

//////***************TIME AND DAY PICKER****************************///

    @Step("Reformat Current Date")
    public String reformatCurrentDateUTC2(String pattern,int difference) {
        Log.info("Reformat Current Date");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate date = LocalDate.from(LocalDateTime.now(ZoneOffset.UTC)).plusDays(difference);
        return date.format(formatter).toUpperCase();

    }

    public boolean existsElement(By locator) {

        try {

            getDriver().findElement(locator);

        } catch (NoSuchElementException e) {

            return false;
        }

        return true;

    }

    @Step("Find object id so you can attach to new object")
    public String findObjectId(String txtOpt) {
        Log.info("Find object id so you can attach to new object");
        getDriver().switchTo().parentFrame();
        String id = null;
        driverWait.waitForElementToExist(By.cssSelector(String.format("[id*='%s']",txtOpt)));
        List<WebElement> myObjects = getDriver().findElements(By.cssSelector(String.format("[id*='%s']",txtOpt)));
        for (WebElement elm : myObjects) {
            if (elm.getAttribute("id").contains(txtOpt)) {
                return elm.getAttribute("id");
            }
        }
        return id;
    }
    @Step("Find object id so you can attach to new object")
    public WebElement findObjectElement(String txtOpt) {
        Log.info("Find object id so you can attach to new object");
        getDriver().switchTo().parentFrame();
        String id = null;
        driverWait.waitForElementToExist(By.cssSelector(String.format("[id*='%s']",txtOpt)));
        List<WebElement> myObjects = getDriver().findElements(By.cssSelector(String.format("[id*='%s']",txtOpt)));
        for (WebElement elm : myObjects) {
            if (elm.getAttribute("id").contains(txtOpt)) {
                return elm;
            }
        }
        return null;
    }

    @Step("Find object id so you can attach to new object")
    public String findObjectId(String txtOpt,String className) {
        Log.info("Find object id so you can attach to new object");
        getDriver().switchTo().parentFrame();
        String id = null;
        List<WebElement> myObjects = getDriver().findElements(By.className(className));
        for (WebElement elm : myObjects) {
            if (elm.getAttribute("id").contains(txtOpt)) {
                id = elm.getAttribute("id");
            }
        }
        return id;
    }

    public String getOnlyNumbers(String txt) {
        String answer = null;
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(txt);
        while (m.find()) {
            answer = m.group();
        }
        return answer;
    }

    public void waitForJQuery() {
        (new WebDriverWait(getDriver(),Duration.ofSeconds(30))).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                JavascriptExecutor js = (JavascriptExecutor) d;
                return (Boolean) js.executeScript("return jQuery.active == 0");
            }
        });
    }

    ///// ***************************************Elments associated with
    ///// wait******************************//

    public void waitForLoadingScreen() {
        threadSleep(2000);
    }

    @Step("Basic Click")
    public void basicClick(WebElement elm) {
        Log.info("Basic Click");
        driverWait.waitForClickable(elm);
        scrollToElement(elm);
        elm.click();
    }

    @Step("Retrieve value")
    public String getValue(WebElement elm) {
        Log.info("Retrieve value");
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        return js.executeScript("elm.getContent()").toString();

    }

    @Step("Wait for loading screen to disapper")
    public void waitForLoadingScreenOriginal() {
        Log.info("Wait for loading screen to disapper");
        threadSleep(5000);
    }

    protected void clickWithJS(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) getDriver();
        executor.executeScript("arguments[0].click();",element);
    }

    protected void clickSimpleWithJS(WebElement element) {
        driverWait.waitForClickable(element);
        JavascriptExecutor executor = (JavascriptExecutor) getDriver();
        executor.executeScript("arguments[0].click();",element);
    }

    protected void clickSimpleWithJS2(WebElement element) {
        waitForLoadingScreen();
        JavascriptExecutor executor = (JavascriptExecutor) getDriver();
        executor.executeScript("arguments[0].click();",element);
        waitForLoadingScreen();
    }



    @Step
    protected void clickSimpleNonLegacy(WebElement webElement) {
        Log.info("clickSimpleNonLegacy");
        driverWait.waitForClickable(webElement);
        webElement.click();
    }



    @Step("Get color of button")
    public void confirmButtonIsBlue(WebElement elm) {
        Log.info("Get color of button");
        if (System.getProperty("browser").equals("firefox")) {
            Assert.assertEquals(elm.getCssValue("background-color"),("rgb(0, 123, 255)"));
        } else {
            Assert.assertEquals(elm.getCssValue("background-color"),("rgba(0, 123, 255, 1)"));// #007BFF blue
        }
    }

    @Step("Get color of button")
    public void confirmButtonIsWhite(WebElement elm) {
        Log.info("Get color of button");
        if (System.getProperty("browser").equals("firefox")) {
            Assert.assertEquals(elm.getCssValue("background-color"),("rgb(255, 255, 255, 1)"));
        } else {
            Assert.assertEquals(elm.getCssValue("background-color"),("rgba(255, 255, 255, 1)"));// #007BFF blue
        }
    }

    @Step("Get color of button")
    public void confirmButtonIsRed(WebElement elm) {
        Log.info("Get color of button");
        if (System.getProperty("browser").equals("firefox")) {
            Assert.assertEquals(elm.getCssValue("background-color"),("rgb(207, 71, 87)"));
        } else {
            Assert.assertEquals(elm.getCssValue("background-color"),("rgba(207, 71, 87, 1)"));// #cf4757 red
        }
    }

    @Step("Get color of button")
    public void confirmButtonIsSecondaryBlue(WebElement elm) {
        Log.info("Get color of button");
        if (System.getProperty("browser").equals("firefox")) {
            Assert.assertEquals(elm.getCssValue("background-color"),("rgb(255, 255, 255)"));
            Assert.assertEquals(elm.getCssValue("border-color"),("rgb(0, 123, 255)"));
        } else {
            Assert.assertEquals(elm.getCssValue("background-color"),("rgba(255, 255, 255, 1)"));
            Assert.assertEquals(elm.getCssValue("border-color"),("rgb(0, 123, 255)"));// #007BFF blue
        }
    }

    @Step("Get color of text")
    public void confirmTextIsBlue(WebElement elm) {
        Log.info("Get color of text");
        if (System.getProperty("browser").equals("firefox")) {
            Assert.assertEquals(elm.getCssValue("color"),("rgb(0, 123, 255)"));
        } else {
            Assert.assertEquals(elm.getCssValue("color"),("rgba(0, 123, 255, 1)"));// #007BFF blue
        }
    }

    @Step("Get text of button")
    public void confirmButtonIsGrey(WebElement elm) {
        Log.info("Get text of button");
        if (System.getProperty("browser").equals("firefox")) {
            Assert.assertEquals(elm.getCssValue("background-color"),("rgb(148, 148, 148)"));
        } else {
            Assert.assertEquals(elm.getCssValue("background-color"),("rgba(148, 148, 148, 1)"));// #007BFF blue
        }
    }

    @Step("Confirm button is Green 4D802D")
    public void confirmButtonIsGreen(WebElement elm) {
        Log.info("Confirm button is Green 4D802D");
        if (System.getProperty("browser").equals("firefox")) {
            Assert.assertEquals(elm.getCssValue("color"),("rgb(77, 128, 45)"));
        } else {
            Assert.assertEquals(elm.getCssValue("color"),("rgba(77, 128, 45, 1)"));// #4D802D
        }
    }

    @Step("Confirm button is Green 4D802D")
    public void confirmButtonZenPrimaryGreen(WebElement elm) {
        Log.info("Confirm button is Green 4D802D");
        if (System.getProperty("browser").equals("firefox")) {
            Assert.assertEquals(elm.getCssValue("background-color"),("rgb(77, 128, 45)"));
        } else {
            Assert.assertEquals(elm.getCssValue("background-color"),("rgba(77, 128, 45, 1)"));// #4D802D
        }
    }
}

