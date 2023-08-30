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
        WebElement first_name_box;

        @FindBy(id="lastname")
        WebElement last_name_box;

        @FindBy(id="is_subscribed")
        WebElement newsletter_check;

        //Sign-in information
        @FindBy(id="email_address")
         WebElement email_box;
        @FindBy(id="password")
        WebElement password_box;
        @FindBy(id="password-confirmation")
        WebElement confirm_password;
        @FindBy(xpath = "//button[@title='Create an Account']//span[contains(text(),'Create an Account')]")
        WebElement create_button;




        //Constructor to pass in the driver

        public LumaCreateAccountPage(WebDriver driver){
                this.driver = driver;
                //Standard for pagefactory, initiliazing the CreateAccountPage locators
                PageFactory.initElements(driver, this);
        }

        //Filling in all the information on the page

        public void goToCreatingPage() {
                try {
                        navigateToCreateButton.click();
                } catch (Exception e) {
                        System.out.println("Exception caught" + e.getMessage());
                }
        }

        public void setFirstName(String text) {

                try {

                        first_name_box.sendKeys(text);
                } catch (Exception e) {
                        System.out.println("Exception caught" + e.getMessage());

                }
        }
        public void setLastName(String text) {
                try {

                        last_name_box.sendKeys(text);
                } catch (Exception e) {
                        System.out.println("Exception caught" + e.getMessage());

                }
        }

        public void checkNewsletter() {
                try {
                        newsletter_check.click();
                } catch (Exception e) {
                        System.out.println("Exception caught" + e.getMessage());

                }
        }
        public void setEmail(String text) {
                try {

                        email_box.sendKeys(text);
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
                confirm_password.sendKeys(text);
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
