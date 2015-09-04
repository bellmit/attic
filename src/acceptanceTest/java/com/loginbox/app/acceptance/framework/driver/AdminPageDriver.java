package com.loginbox.app.acceptance.framework.driver;

public class AdminPageDriver extends SeleniumDriver {
    public AdminPageDriver(SystemDriver systemDriver) {
        super(systemDriver);
    }

    public String getAdminHeading() {
        return findPageHeading()
                .getText();
    }

    @Deprecated
    public void open() {
        webDriver().navigate().to(systemDriver.baseUrl() + "admin");
    }
}
