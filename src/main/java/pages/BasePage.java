package pages;

import framework.Utilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

public class BasePage {

    WebDriver driver;
    public Properties prop;
    public Properties datapProp;

    // we are loading the properties file that it contains the URL and the browser
    //this method will be called first

    public  BasePage() {
        prop = new Properties();
        File propFile = new File(System.getProperty("user.dir")+("\\src\\com.luma.qa.config\\config.properties"));

        datapProp = new Properties();
        File dataPropFile = new File(System.getProperty("user.dir") + ("\\src\\com.luma.qa.testdata\\testdata.properties"));
        try {
            FileInputStream dataFis = new FileInputStream(dataPropFile);
            datapProp.load(dataFis);
        }catch (Throwable e){
            e.printStackTrace();

        }

        try {
            FileInputStream fis = new FileInputStream(propFile);
            prop.load(fis);
        } catch (Throwable e) {
            e.printStackTrace();
        }



    }
    public WebDriver initalizeBrowserAndOpenApp(String browserName) {
        // you can put as many browser as you like and call the one you need for your tests
        if (browserName.equals("firefox")) {
            driver = new FirefoxDriver();
        }

        System.setProperty("webdriver.gecko.driver", "C:/Users/Denisa/Desktop/automation/denisa learns/LumaAutomated/drivers/geckodriver.exe");

        driver.manage().window().maximize();
        //open the website
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Utilities.IMPLICIT_WAIT_TIME));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Utilities.PAGE_WAIT_TIME));
        driver.get(prop.getProperty("url"));

        return driver;




    }

}
