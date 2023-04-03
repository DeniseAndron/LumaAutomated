import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class LumaLogin {

    //Locators

    @FindBy(xpath = "//div[@class='panel header']//a[contains(text(),'Sign In')]")
    WebElement sign_in_homepage_button;
    @FindBy(id = "email")
    WebElement email_field;
    @FindBy(id = "pass")
    WebElement password_field;
    @FindBy(xpath = "//fieldset[@class='fieldset login']//span[contains(text(),'Sign In')]")
    WebElement sign_in_button;
    @FindBy(xpath = "//a[@class='action remind']//span[contains(text(),'Forgot Your Password?')]")
    WebElement forgot_password_button;
    @FindBy(xpath = "//a[@class='action create primary']//span[contains(text(),'Create an Account')]")
    WebElement create_account_button;

    @FindBy (linkText = "Please enter a valid email address (Ex: johndoe@domain.com).")
    WebElement invalidEmailAddress;

    WebDriver driver;
    //Constructor to call the driver
    public LumaLogin(WebDriver driver){
        this.driver = driver;
    }

    public void goToLoginPage() {
        try {
            sign_in_homepage_button.click();
        } catch (Exception e) {
            System.out.println("Exception caught" + e.getMessage());
        }
    }

    public void setEmailAddress(String text) {

        try {

           email_field.sendKeys(text);
        } catch (Exception e) {
            System.out.println("Exception caught" + e.getMessage());

        }
    }

    public void setPassword_field(String text) {

        try {
            password_field.sendKeys(text);
        } catch (Exception e) {
            System.out.println("Exception caught" + e.getMessage());

        }
    }

    public void signInButton() {

        try {

            sign_in_button.click();
        } catch (Exception e) {
            System.out.println("Exception caught" + e.getMessage());

        }
    }
    public void forgotYourPassword() {

        try {

            forgot_password_button.click();
        } catch (Exception e) {
            System.out.println("Exception caught" + e.getMessage());

        }
    }

    public void createAccountButton() {

        try {

            create_account_button.click();
        } catch (Exception e) {
            System.out.println("Exception caught" + e.getMessage());

        }
    }

    public boolean invalidEmailErrorMessage() {

        boolean displayErrorInvalidEmail = invalidEmailAddress.isDisplayed();
        return displayErrorInvalidEmail;
    }

}
