package com.skomorokhin.networkChat.Client.controllers;

import com.skomorokhin.networkChat.Client.model.Network;
import com.skomorokhin.networkChat.Client.model.dialogs.Dialogs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthController {

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button authButton;

    private volatile String login;
    private volatile String password;

    public void executeAuth(ActionEvent actionEvent) {
        setLogin(loginField.getText());
        setPassword(passwordField.getText());

        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            Dialogs.AuthError.EMPTY_CREDENTIALS.show();
        } else if (!Network.getInstance().isConnected()) {
            Dialogs.NetworkError.SERVER_CONNECT.show();
        }
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }


    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
