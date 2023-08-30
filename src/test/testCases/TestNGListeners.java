
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Listeners;

public class TestNGListeners implements ITestListener {


    public void onTestStart(ITestResult result) {
        System.out.println("********Test started: " + result.getName());
    }

    public void onTestSuccess(ITestResult result) {
        System.out.println("********Test is successful: " + result.getName());
    }


    public void onTestFailure(ITestResult result) {
        System.out.println("********Test is failed: " + result.getName());
    }


    public void onTestSkipped(ITestResult result) {
        System.out.println("********Test is skipped: " + result.getName());
    }


    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("********Test failed but within success percentage: " + result.getName());
    }


    public void onTestFailedWithTimeout(ITestResult result) {
        System.out.println("********Test failed with timeout: " + result.getName());
    }


    public void onStart(ITestContext context) {
        System.out.println("********Test started: " + context.getName());
    }


    public void onFinish(ITestContext context) {
        System.out.println("********Test completed: " + context.getName());
    }
}
