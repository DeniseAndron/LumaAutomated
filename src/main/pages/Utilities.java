import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;
import java.util.Date;

public class Utilities {

    public WebDriver _driver;

    //we can update only here the wait time for implicit wait or page time
    public  static  final int IMPLICIT_WAIT_TIME=10;
    public static final int PAGE_WAIT_TIME=5;


   // We can use this method to generate unique emails, passwords etc, just add this method with + for email name
    public static String generateTimeStamp(){
        Date date = new Date();
        return date.toString().replace(" ", "_").replace(":","_");

    }



}
