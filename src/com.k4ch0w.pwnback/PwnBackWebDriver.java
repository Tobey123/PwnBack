package com.k4ch0w.pwnback;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;


/**
 * Created by k4ch0w on 3/27/17.
 */
public class PwnBackWebDriver implements Runnable {
    private final PwnBackMediator mediator;
    private WebDriver driver = null;

    public PwnBackWebDriver(PwnBackMediator mediator) {
        this.mediator = mediator;
        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/Applications/phantomjs");
        capability.setCapability("takesScreenshot", false);
        String[] args = {"--ignore-ssl-errors=yes"};
        capability.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, args);
        capability.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
        driver = new PhantomJSDriver(capability);

    }

    @Override
    public void run() {
        while (true) {
            PwnBackURL url = mediator.getURL();
            mediator.addLog("Processing: " + url);
            driver.get(url.getURL());
            String html = driver.getPageSource();
            switch (url.getType()) {
                case WAYBACKAPI:
                    mediator.addLog("Wayback done");
                    mediator.addDocument(new PwnBackDocument(html, PwnBackType.WAYBACKAPI));
                    break;
                case ROBOTS:
                    mediator.addDocument(new PwnBackDocument(html, PwnBackType.ROBOTS));
                    break;
                case SITEMAPXML:
                    mediator.addDocument(new PwnBackDocument(html, PwnBackType.SITEMAPXML));
                    break;
                case HTML:
                    mediator.addDocument(new PwnBackDocument(html, PwnBackType.HTML));
                    break;
            }
        }
    }

}