package com.luma.listeners;

import com.luma.framework.ThreadPackage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.events.WebDriverListener;


import com.luma.framework.PropertyManager;
import com.luma.utils.ElementReadyStatus;
public class DriverListener implements WebDriverListener {
    private ElementReadyStatus status;

    public DriverListener() {
        this.status = new ElementReadyStatus(ThreadPackage.getInstance().getThreadDriver(),
                ThreadPackage.getInstance().getThreadPropertyManager());// May not have additional property
    }

    public DriverListener(WebDriver driver, PropertyManager pm) {
        this.status = new ElementReadyStatus(driver, pm);// May not have additional property
    }

    public void beforeClick(WebElement arg) {
        if (!this.status.isElementClickable(arg)) {
        }
    }

    public void beforeSubmit(WebElement arg) {
        // TODO Auto-generated method stub
    }

}
