package com.skomorokhin.networkChat.Client;

import com.skomorokhin.networkChat.Client.controllers.AuthController;
import com.skomorokhin.networkChat.Client.controllers.ClientController;
import com.skomorokhin.networkChat.Client.model.Network;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class ClientChat extends Application {

    public static ClientChat INSTANCE;
    private String username;

    private Stage primaryStage;
    private Stage authStage;
    private FXMLLoader clientWindowLoader;
    private FXMLLoader authLoader;

    @Override
    public void init() throws Exception {
        INSTANCE = this;
    }


    public static void main(String[] args) {
        launch();
    }


    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;

        initViews();

    }

    private void initViews() throws IOException {
        initChatWindow();
        initAuthDialog();
        Network.getInstance();
    }

    private void initChatWindow() throws IOException {
        clientWindowLoader = new FXMLLoader();
        clientWindowLoader.setLocation(ClientChat.class.getResource("clientChat.fxml"));
        Parent root = clientWindowLoader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.getScene().getStylesheets().add("com/skomorokhin/networkChat/Client/networkChatClient.css");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                getClientController().close();
                System.exit(0);
            }
        });
        primaryStage.show();
    }

    private void initAuthDialog() throws IOException {
        authLoader = new FXMLLoader();
        authLoader.setLocation(ClientChat.class.getResource("authDialog.fxml"));
        Parent authDialogPanel = authLoader.load();

        authStage = new Stage();
        authStage.initOwner(primaryStage);
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.setScene(new Scene(authDialogPanel));
        authStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        this.authStage.show();
    }

    public ClientController getClientController() {
        return clientWindowLoader.getController();
    }


    public AuthController getAuthController() {
        return authLoader.getController();
    }


    public Stage getChatStage() {
        return this.primaryStage;
    }

    public Stage getAuthStage() {
        return  this.authStage;
    }

    public void switchToMainChatWindow(String username) {
        getChatStage().setTitle(username);
        this.username = username;
        getAuthStage().close();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}