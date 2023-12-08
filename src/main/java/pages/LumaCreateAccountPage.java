package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LumaCreateAccountPage {

        WebDriver driver;
        /* - Steps to test
        * Navigate to create account page
        * Set all the information
        * Click create account
        * */

        //Locators with page factory design pattern, objects
        @FindBy(xpath = "//div[@class='panel header']//a[normalize-space()='Create an Account']")
        WebElement navigateToCreateButton;

        @FindBy(id="firstname")
        WebElement firstNameBox;

        @FindBy(id="lastname")
        WebElement lastNameBox;

        @FindBy(id="is_subscribed")
        WebElement newsLetterCheck;

        //Sign-in information
        @FindBy(id="email_address")
         WebElement emailBox;
        @FindBy(id="password")
        WebElement passwordField;
        @FindBy(id="password-confirmation")
        WebElement confirmPassword;
        @FindBy(xpath = "//button[@title='Create an Account']//span[contains(text(),'Create an Account')]")
        WebElement createButton;




        //Constructor to pass in the driver

        public LumaCreateAccountPage(WebDriver driver){
                this.driver = driver;
                //Standard for pagefactory, initiliazing the CreateAccountPage locators
                PageFactory.initElements(driver, this);
        }

        //Filling in all the information on the page

        public void goToCreatingPage() {

                        navigateToCreateButton.click();

        }

        public void setFirstName(String text) {

                try {

                        firstNameBox.sendKeys(text);
                } catch (Exception e) {
                        System.out.println("Exception caught" + e.getMessage());

                }
        }
        public void setLastName(String text) {
                try {

                        lastName.sendKeys(text);
                } catch (Exception e) {
                        System.out.println("Exception caught" + e.getMessage());

                }
        }

        public void checkNewsletter() {
                try {
                        newsLetterCheck.click();
                } catch (Exception e) {
                        System.out.println("Exception caught" + e.getMessage());

                }
        }
        public void setEmail(String text) {
                try {

                        emailBox.sendKeys(text);
                } catch (Exception e) {
                        System.out.println("Exception caught" + e.getMessage());

                }
        }

        public void setPassword(String text) {
                try {

                        password_box.sendKeys(text);
                } catch (Exception e) {
                        System.out.println("Exception caught" + e.getMessage());

                }
        }

        public void setPasswordConfirm(String text) {
                try {
                confirmPassword.sendKeys(text);
                } catch (Exception e) {
                        System.out.println("Exception caught" + e.getMessage());

                }

        }
        public void clickCreateButton() {
                try {
                        create_button.click();

                } catch (Exception e) {
                        System.out.println("Exception caught" + e.getMessage());

                }
        }
}
