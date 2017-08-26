package com.idata.mq.mdm.test;

public class ApplicationStatus {

    private boolean quit;

    public ApplicationStatus() {
        this.quit = false;
    }

    public boolean isQuit() {
        return quit;
    }

    public void setQuit(boolean quit) {
        this.quit = quit;
    }

}
