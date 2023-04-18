package com.skomorokhin.cloudClient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DirWinClientController {

    protected Stage stage;
    protected MainClientController mainClientController;

    @FXML
    private TextField directory;

    protected String getDirectory() {
        return directory.getText();
    }

    protected void setMainClientController(MainClientController mainClientController) {
        this.mainClientController = mainClientController;
    }

    protected void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void pushOk() {
        mainClientController.initClientTree(mainClientController.getClientTreeView());
        stage.close();
    }
}
