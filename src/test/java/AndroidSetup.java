import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileBrowserType;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AndroidSetup {

    protected AndroidDriver<MobileElement> ad;
    protected WebDriverWait wait;

    protected void androidSetUp(String udid) throws MalformedURLException {
        File appDir = new File("/Users/labora/Documents/Work/apk");
        File app = new File(appDir, "mda-2.2.0-25.apk");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appium-version", "1.0");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "6.0");
//        capabilities.setCapability("deviceName", "Nexus 6P");
        capabilities.setCapability("deviceName", udid);
        capabilities.setCapability("udid", udid);
        capabilities.setCapability("app", app.getAbsolutePath());
        capabilities.setCapability("appPackage", "com.saucelabs.mydemoapp.android");
        capabilities.setCapability("appActivity", "com.saucelabs.mydemoapp.android.MainActivity");


        capabilities.setCapability("noReset", false);
        capabilities.setCapability("fullReset", true);

        ad = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        wait = new WebDriverWait(ad, 10);

        ad.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    }

    @Parameters({"udid"})
    @BeforeClass
    public void setUp(String udid) throws MalformedURLException {
        androidSetUp(udid);
    }

    @AfterClass
    public void tearDown() {
        ad.quit();
    }

}
