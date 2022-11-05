package com.skomorokhin.networkChat.Client.controllers;

import com.skomorokhin.networkChat.Client.ClientChat;
import com.skomorokhin.networkChat.Client.model.Network;
import com.skomorokhin.networkChat.Data.Command;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;
import java.util.Optional;

public class ClientController {

    @FXML
    public ListView<String> userList;

    @FXML
    public Button sendButton;

    @FXML
    public TextFlow textFlow;

    @FXML
    private TextField textField;


    public void close() {
        String userName = ClientChat.INSTANCE.getUsername();
        Network.getInstance().getClientHandler().setOutComingCommand(Command.unsubscribeUserCommand(userName));
        Network.getInstance().getClientHandler().sendingCommand();
        Network.getInstance().getClientHandler().setOutComingCommand(Command.updateUserListCommand(null));
        Network.getInstance().getClientHandler().sendingCommand();
        ClientChat.INSTANCE.getChatStage().close();
    }


    @FXML
    public void sendMessage(ActionEvent actionEvent) {
        String message = textField.getText().trim();

        if (message.isEmpty()) {
            textField.clear();
            return;
        }

        String sender = ClientChat.INSTANCE.getUsername();
        String recipient = null;
        if (!userList.getSelectionModel().isEmpty()) {
            recipient = userList.getSelectionModel().getSelectedItem();
        }
        if (recipient != null) {
            Network.getInstance().getClientHandler().setOutComingCommand(Command.privateMessageCommand(sender, recipient, message));
            Network.getInstance().getClientHandler().sendingCommand();
        } else {
            Network.getInstance().getClientHandler().setOutComingCommand(Command.publicMessageCommand(sender, message));
            Network.getInstance().getClientHandler().sendingCommand();
        }
        textField.clear();
    }


    public void about(ActionEvent actionEvent) {
    }

    public void updateUserList(List<String> userList) {
        Platform.runLater(() -> {
            ClientChat.INSTANCE.getClientController().userList.setItems(FXCollections.observableList(userList));
        });
    }


    public void displayMessageInChatWindow(String prefix, String message, boolean isPrivate) {

        Platform.runLater(() -> {
            StringBuilder sb = new StringBuilder();

            sb.append(prefix);
            sb.append(": ");
            sb.append(message);
            sb.append("\n");

            Text text = new Text(sb.toString());

            if (isPrivate) {
                text.setFill(Color.FUCHSIA);
            }

            textFlow.getChildren().add(text);

        });

    }

    public void updateUsername(ActionEvent actionEvent) {
        TextInputDialog editDialog = new TextInputDialog();
        editDialog.setTitle("Изменить имя пользователя");
        editDialog.setHeaderText("Введите новое имя мпользователя");
        editDialog.setContentText("Username:");

        Optional<String> result = editDialog.showAndWait();
        if (result.isPresent()) {
            String previousUsername = ClientChat.INSTANCE.getUsername();
            String newUsername = result.get();

            Network.getInstance().getClientHandler().setOutComingCommand(Command.updateUsername(previousUsername, newUsername));
            Network.getInstance().getClientHandler().sendingCommand();

            ClientChat.INSTANCE.setUsername(newUsername);
            ClientChat.INSTANCE.getChatStage().setTitle(newUsername);

            Network.getInstance().getClientHandler().setOutComingCommand(Command.updateUserListCommand(null));
            Network.getInstance().getClientHandler().sendingCommand();
        }
    }

    public void unselectUser(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() > 1) {
            userList.getSelectionModel().clearSelection();
        }
    }
}