package com.loginbox.app.acceptance.framework.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SetupPageDriver extends SeleniumDriver {
    public SetupPageDriver(SystemDriver systemDriver) {
        super(systemDriver);
    }

    public void ensureWelcomeMessagePresent() {
        findWelcomeMessage();
    }

    private WebElement findWelcomeMessage() {
        return findElement(By.className("welcome-message"));
    }

    public void enterUsername(String username) {
        findUsernameField()
                .sendKeys(username);
    }

    private WebElement findUsernameField() {
        return findElement(By.name("username"));
    }

    public void enterContactEmail(String emailAddress) {
        findContactEmailField()
                .sendKeys(emailAddress);
    }

    private WebElement findContactEmailField() {
        return findElement(By.name("contactEmail"));
    }

    public void enterPassword(String password) {
        findPasswordField()
                .sendKeys(password);
    }

    private WebElement findPasswordField() {
        return findElement(By.name("password"));
    }

    public void enterPasswordConfirmation(String password) {
        findPasswordConfirmationField()
                .sendKeys(password);
    }

    private WebElement findPasswordConfirmationField() {
        return findElement(By.name("confirmPassword"));
    }

    public void clickCompleteSetup() {
        findCompleteSetupButton()
                .click();
    }

    private WebElement findCompleteSetupButton() {
        return findElement(By.cssSelector(".btn.btn-default"));
    }
}
