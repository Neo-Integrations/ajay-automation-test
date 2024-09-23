package automation.test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public enum VRNValidation implements Closeable {
    INSTANCE("/Users/mhaque/Downloads/chrome-mac-x64/chromedriver");

    private final String[] headers =
            new String[] {"make", "model", "year", "colour", "transmission", "engineSize", "firstRegistered"};
    private final WebDriver driver;
    private VRNValidation(String driverLocation) {
        System.setProperty("webdriver.chrome.driver", driverLocation);
        driver = new ChromeDriver();
    }
    public Map<String, String> getVRNDetails(String url, String vrn, int mileage) {
        Map<String, String> data = new HashMap<>();
        try {
            driver.get(url);
            // Wait for the element with ID "vehicleReg" to be visible
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement vehicleRegElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("vehicleReg"))
            );
            WebElement mileageElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("Mileage"))
            );
            vehicleRegElement.sendKeys(vrn.replace(" ", ""));
            mileageElement.sendKeys(String.valueOf(mileage));
            driver.findElement(By.id("btn-go")).click();
            Thread.sleep(6000);

            wait.until((ExpectedCondition<Boolean>) wd ->
                    ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete")
            );
            List<WebElement> elements = driver.findElements(By.cssSelector("div.d-table-cell.value"));
            int index = 0;
            for (WebElement element : elements) {
                if(!element.getText().trim().isEmpty()) {
                    data.put(headers[index], element.getText().trim());
                    index++;
                }
            }
        } catch(Exception exp) {
            System.out.println("Something went wrong while loading the website");
            System.out.println(exp.getMessage());
        }
        return data;
    }

    @Override
    public void close() throws IOException {
        driver.quit();
    }
}
