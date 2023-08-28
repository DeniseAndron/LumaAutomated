package com.luma;

import com.luma.framework.Utilities;
import com.luma.pages.BaseTest;
import jdk.jfr.Description;
import org.openqa.selenium.WebDriver;
import com.luma.pages.LumaCreateAccountPage;

import org.testng.annotations.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;


//Listeners are components in TestNG that keep track of test execution and helps to perform actions at multiple stages of test execution

public class CreateAccountTestCase extends BaseTest {



    @Parameters({})
    @Test (priority = 2)
    @Description("Create account")
    public  void CreateAccountTest() {
        LumaCreateAccountPage createAccountObj =  new LumaCreateAccountPage(getDriver());
        String firstName = "Denisa";
        String lastName = "Andron";
        String email = "denisa";
        String password = "Testing123!";


        // Navigate to Create Account Page
        createAccountObj.goToCreatingPage();
        //Fill in the first name
        createAccountObj.setFirstName(firstName);
        //Fill in last name
        createAccountObj.setLastName(lastName);
        //Check the NewsLetter button
        createAccountObj.checkNewsletter();
        //Set a random email address
        createAccountObj.setEmail(email + Utilities.generateTimeStamp() +"@gmail.com");
        //Set password
        createAccountObj.setPassword(password);
        //Confirm password
        createAccountObj.setPasswordConfirm(password);
        //Click Button to create account
        createAccountObj.clickCreateButton();
        //Get the title of the page
        String title = getDriver().getTitle();
        assertEquals (title, "My Account");

    }
    @AfterTest
    public void tearDownTest() {
        //close browser
        getDriver().close();
        System.out.println("Test completed Successfully");
    }
}
