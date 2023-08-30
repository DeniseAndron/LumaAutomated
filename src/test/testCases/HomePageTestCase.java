import org.openqa.selenium.WebDriver;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.Assert;






public class HomePageTestCase extends BasePage{

    //Using the constructor of BasePage

    public HomePageTestCase(){
        super();
    }

    private static WebDriver driver = null;

    @BeforeTest
    public void setUpTest() {


        driver = initalizeBrowserAndOpenApp(prop.getProperty("browser"));

    }

    //1. We are login in with valid email and Password in the app
    @Test(priority = 1)
    public void test_01_ValidLogin() {
    LumaLogin loginObj = new LumaLogin(driver);

    //We are getting validEmail and valid Password from the confing properties file
    loginObj.goToLoginPage();
    loginObj.setEmailAddress(prop.getProperty("validEmail"));
    loginObj.setPassword_field(prop.getProperty("validPassword"));
    loginObj.signInButton();
    }
    //2. Login with invalid password and email, check for error message
    //We are getting validEmail and valid Password from the confing testdataproperties file

    @Test(priority = 2)
    public void test_02_invalidData(){
        LumaLogin loginObj = new LumaLogin(driver);
        loginObj.goToLoginPage();
        loginObj.setEmailAddress(datapProp.getProperty("invalidEmail"));
        Assert.assertTrue(loginObj.invalidEmailErrorMessage(), "Please enter a valid email address (Ex: johndoe@domain.com).");
        loginObj.setPassword_field(datapProp.getProperty("invalidPassword"));
        loginObj.signInButton();
    }


    @AfterTest
    public void tearDownTest() {
        //close browser
        driver.close();

        System.out.println("Test completed Successfully");
    }
}
