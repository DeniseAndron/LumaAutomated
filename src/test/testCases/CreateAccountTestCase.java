import org.openqa.selenium.WebDriver;

import org.testng.annotations.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;


//Listeners are components in TestNG that keep track of test execution and helps to perform actions at multiple stages of test execution

public class CreateAccountTestCase extends BasePage {

    // We call the constructor from BasePage
    public CreateAccountTestCase(){
        super();
    }
    private static WebDriver driver = null;

    @BeforeTest
    public void setUpTest() {
        // we call the properties file from the basepage containing the url

        driver = initalizeBrowserAndOpenApp(prop.getProperty("browser"));



    }
    @Parameters({})
    @Test (priority = 2)
    public  void CreateAccountTest() {
        LumaCreateAccountPage createAccountObj =  new LumaCreateAccountPage(driver);

        // Navigate to Create Account Page
        createAccountObj.goToCreatingPage();
        //Fill in the first name
        createAccountObj.setFirstName("Denisa");
        //Fill in last name
        createAccountObj.setLastName("Andron");
        //Check the NewsLetter button
        createAccountObj.checkNewsletter();
        //Set a random email address
        createAccountObj.setEmail("denisa6" + Utilities.generateTimeStamp() +"@gmail.com");
        //Set password
        createAccountObj.setPassword("Testing123!");
        //Confirm password
        createAccountObj.setPasswordConfirm("Testing123!");
        //Click Button to create account
        createAccountObj.clickCreateButton();
        //Get the title of the page
        String title = driver.getTitle();
        assertEquals (title, "My Account");

    }
    @AfterTest
    public void tearDownTest() {
        //close browser
        driver.close();

        System.out.println("Test completed Successfully");
    }
}
