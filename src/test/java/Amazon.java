import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

public class Amazon {
    public static void main(String[] args) throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://ec2-54-208-152-154.compute-1.amazonaws.com/");
        List<WebElement> coins = driver.findElements(By.xpath("//*[@*='coins']//button"));
        WebElement weighButton = driver.findElement(By.id("weigh"));
        WebElement resetButton = driver.findElement(By.xpath("//*[.='Reset']"));
        List<String> group = new ArrayList<>();
        for (WebElement coin : coins) {
            group.add(coin.getText());
        }
        int numberOfWeighs=1;
        while (group.size() >= 3){
            List<String> group1 = new ArrayList<>();
            List<String> group2 = new ArrayList<>();
            List<String> group3 = new ArrayList<>();
            for (int i = 0; i < group.size() / 3; i++) {
                group1.add(group.get(i));
                group2.add(group.get((group.size()/3+i)));
                group3.add(group.get((group.size()/3*2+i)));
            }
            for (int i = 0; i < group.size() / 3; i++) {
                driver.findElement(By.id("left_" + i + "")).sendKeys("" + group1.get(i));
                driver.findElement(By.id("right_" + i + "")).sendKeys("" + group2.get(i));
            }
            weighButton.click();
            WebElement results = driver.findElement(By.id("reset"));
            if (results.getText().equals("=")) {
                group.removeAll(group1);
                group.removeAll(group2);
                resetButton.click();
            } else if (results.getText().equals("<")) {
                group.removeAll(group2);
                group.removeAll(group3);
                resetButton.click();
            } else {
                group.removeAll(group1);
                group.removeAll(group3);
                resetButton.click();
            }
            if (group.size() == 1) {
                coins.get(Integer.parseInt(group.get(0))).click();
                System.out.println("numberOfWeighs = " + numberOfWeighs);
                Thread.sleep(1000);
                Alert alert = driver.switchTo().alert();
                System.out.println(alert.getText());
                alert.accept();
                driver.quit();
            }
            numberOfWeighs++;
        }
    }
}
