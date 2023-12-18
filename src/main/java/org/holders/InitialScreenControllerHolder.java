package org.holders;

import org.controller.InitialScreenController;

public class InitialScreenControllerHolder {

    private InitialScreenController initialScreenController;
    private final static InitialScreenControllerHolder INSTANCE = new InitialScreenControllerHolder();

    private InitialScreenControllerHolder() {}

    public static InitialScreenControllerHolder getInstance() {
        return INSTANCE;
    }

    public void setInitialScreenController(InitialScreenController controller) {
        this.initialScreenController = controller;
    }

    public InitialScreenController getInitialScreenController() {
        return this.initialScreenController;
    }
}
