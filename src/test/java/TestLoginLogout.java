import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.appium.java_client.MobileElement;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.TestCaseId;
import ru.yandex.qatools.allure.annotations.Title;

import java.net.MalformedURLException;
import java.util.List;

public class TestLoginLogout extends AndroidSetup {

    @Stories("As a user I want to be able to log in")
    @TestCaseId("TC_KRM_001")
    @Title("Verify user is able to log in")
    @Test(priority = 0)
    public void testLoginSuccess() {
        login("bod@example.com", "10203040");
        
        // Verify if login was successful
        MobileElement menuButton = ad.findElement(By.id("drawerMenu")); 
        menuButton.click();

        MobileElement logoutMenuOption = ad.findElement(By.xpath("//android.widget.TextView[@text='Log Out']"));
        Assert.assertTrue(logoutMenuOption.isDisplayed());
    }

    @Stories("As a user I want to be able to log out")
    @TestCaseId("TC_KRM_002")
    @Title("Verify user is able to log out")
    @Test(priority = 1, dependsOnMethods = "testLoginSuccess")
    public void testLogout() {
        MobileElement menuButton = ad.findElement(By.id("drawerMenu"));
        menuButton.click();

        MobileElement logoutMenuOption = ad.findElement(By.xpath("//android.widget.TextView[@text='Log Out']"));
        logoutMenuOption.click();

        // Verify user is able to log out
        WebDriverWait wait = new WebDriverWait(ad, 10);
            
        MobileElement confirmLogout = (MobileElement) wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text='LOGOUT']")));
        confirmLogout.click();

    
        // Verify logout success by checking if the login screen is displayed
        MobileElement loginScreen = ad.findElement(By.id("loginTV"));
        Assert.assertTrue(loginScreen.isDisplayed());
    }


    @Stories("As a user I'm not able to log in with Empty Fields")
    @TestCaseId("TC_KRM_003")
    @Title("Verify user is not able to log in")
    @Test(priority = 2)
    public void testLoginEmptyFields() {
        login("", "");

        // Verify error message appears
        MobileElement errorMessage = ad.findElement(By.id("com.saucelabs.mydemoapp.android:id/nameErrorTV"));
        Assert.assertTrue(errorMessage.isDisplayed(), "Username is required");
    }

    @Stories("As a user I'm not able to log in with Only Username")
    @TestCaseId("TC_KRM_004")
    @Title("Verify user is not able to log in")
    @Test(priority = 3)
    public void testLoginOnlyUsername() {
        login("bod@example.com", "");

        // Verify error message appears
        MobileElement errorMessage = ad.findElement(By.id("com.saucelabs.mydemoapp.android:id/passwordErrorTV"));
        Assert.assertTrue(errorMessage.isDisplayed(), "Password is required");
    }

    @Stories("As a user I'm not able to log in with Only Password")
    @TestCaseId("TC_KRM_005")
    @Title("Verify user is not able to log in")
    @Test(priority = 4)
    public void testLoginOnlyPassword() {
        login("", "10203040");

        // Verify error message appears
        MobileElement errorMessage = ad.findElement(By.id("com.saucelabs.mydemoapp.android:id/nameErrorTV"));
        Assert.assertTrue(errorMessage.isDisplayed(), "Username is required");
    }

    @Stories("As a user I'm not able to log in with Locked out account")
    @TestCaseId("TC_KRM_006")
    @Title("Verify user is not able to log in")
    @Test(priority = 5)
    public void testLoginLockedAccount() {
        login("alice@example.com", "10203040");

        // Verify error message appears
        MobileElement errorMessage = ad.findElement(By.xpath("//android.widget.TextView[@text='Sorry this user has been locked out.']"));
        Assert.assertTrue(errorMessage.isDisplayed(), "Sorry this user has been locked out.");
    }

    @Stories("As a user I'm able to see product list")
    @TestCaseId("TC_KRM_007")
    @Title("Verify user can see Product List After Login")
    @Test(priority = 6)
    public void testProductList() {
        login("bod@example.com", "10203040");

        MobileElement productTitle = ad.findElement(By.id("com.saucelabs.mydemoapp.android:id/productTV"));
        Assert.assertTrue(productTitle.isDisplayed());

        MobileElement productRecyclerView = ad.findElement(By.id("com.saucelabs.mydemoapp.android:id/productRV"));
        Assert.assertTrue(productRecyclerView.isDisplayed());
    
        MobileElement firstProduct = productRecyclerView.findElement(By.xpath("//android.widget.TextView[@text='Sauce Labs Backpack']"));
        Assert.assertTrue(firstProduct.isDisplayed());
    
    }

    @Stories("As a user I'm able to add a Product to the Cart")
    @TestCaseId("TC_KRM_008")
    @Title("Verify user success to add a Product to the Cart")
    @Test(priority = 7)
    public void testAddProductToCart() {
        MobileElement productTitle = ad.findElement(By.id("com.saucelabs.mydemoapp.android:id/productTV"));
        Assert.assertTrue(productTitle.isDisplayed());

        MobileElement productRecyclerView = ad.findElement(By.id("com.saucelabs.mydemoapp.android:id/productRV"));
        Assert.assertTrue(productRecyclerView.isDisplayed());

        MobileElement firstProduct = productRecyclerView.findElement(By.xpath("//android.widget.TextView[@text='Sauce Labs Backpack']"));
        Assert.assertTrue(firstProduct.isDisplayed());
        firstProduct.click();

        MobileElement addToCartButton = ad.findElement(By.id("com.saucelabs.mydemoapp.android:id/cartBt"));
        Assert.assertTrue(addToCartButton.isDisplayed());
        addToCartButton.click();

        // Open the cart
        MobileElement cartIcon = ad.findElement(By.id("com.saucelabs.mydemoapp.android:id/cartIV"));
        cartIcon.click();

        WebDriverWait wait = new WebDriverWait(ad, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.saucelabs.mydemoapp.android:id/productRV")));


        // Verify product is in the cart
        MobileElement cartPage = ad.findElement(By.id("com.saucelabs.mydemoapp.android:id/productTV"));
        Assert.assertTrue(cartPage.isDisplayed());
        MobileElement cartItem = ad.findElement(By.id("com.saucelabs.mydemoapp.android:id/titleTV"));
        Assert.assertTrue(cartItem.isDisplayed());
        MobileElement removeButton = ad.findElement(By.id("com.saucelabs.mydemoapp.android:id/removeBt"));
        Assert.assertTrue(removeButton.isDisplayed());
    }

    @Stories("As a user I'm able to open checkout page")
    @TestCaseId("TC_KRM_009")
    @Title("Verify user success continue to check out page")
    @Test(priority = 8)
    public void testProceedToCheckout() {
        // Open the cart
        MobileElement cartIcon = ad.findElement(By.id("com.saucelabs.mydemoapp.android:id/cartIV"));
        cartIcon.click();

        WebDriverWait wait = new WebDriverWait(ad, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.saucelabs.mydemoapp.android:id/productRV")));


        // Verify product is in the cart
        MobileElement cartPage = ad.findElement(By.id("com.saucelabs.mydemoapp.android:id/productTV"));
        Assert.assertTrue(cartPage.isDisplayed());

        MobileElement checkoutButton = ad.findElement(By.id("com.saucelabs.mydemoapp.android:id/cartBt"));
        checkoutButton.click();

         // Verify checkout page is displayed
         MobileElement checkoutTitle = ad.findElement(By.id("com.saucelabs.mydemoapp.android:id/checkoutTitleTV"));
        Assert.assertTrue(checkoutTitle.isDisplayed());

    }

    @Stories("As a user I'm able to remove a Product from the Cart")
    @TestCaseId("TC_KRM_010")
    @Title("Verify user success to add a Product to the Cart")
    @Test(priority = 9)
    public void testRemoveProductFromCart() {
        // Click the "Remove" button
        MobileElement removeButton = ad.findElement(By.id("com.saucelabs.mydemoapp.android:id/removeBt"));
        Assert.assertTrue(removeButton.isDisplayed());
        removeButton.click();

        MobileElement cartInfo = ad.findElement(By.id("com.saucelabs.mydemoapp.android:id/cartInfoLL"));
        Assert.assertTrue(cartInfo.isDisplayed());
    }


    private void login(String usernameText, String passwordText) {
        MobileElement menuButton = ad.findElement(By.id("drawerMenu")); 
        menuButton.click();

        MobileElement loginMenuOption = ad.findElement(By.xpath("//android.widget.TextView[@text='Log In']"));
        loginMenuOption.click();

        WebDriverWait wait = new WebDriverWait(ad, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginTV")));

        MobileElement username = ad.findElement(By.id("nameET"));
        MobileElement password = ad.findElement(By.id("passwordET"));

        MobileElement loginButton = ad.findElement(By.id("loginBtn"));
        loginButton.click();

        username.clear();
        password.clear();

        username.sendKeys(usernameText);
        password.sendKeys(passwordText);
        loginButton.click();
    }
}
