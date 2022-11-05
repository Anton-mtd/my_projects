package com.skomorokhin.networkChat.Client.model.dialogs;

import com.skomorokhin.networkChat.Client.ClientChat;
import javafx.scene.control.Alert;

public class Dialogs {

    public enum AuthError {
        EMPTY_CREDENTIALS("Логин и пароль должны быть указаны!"),
        INVALID_CREDENTIALS("Логин и пароль заданы некорректно!"),
        AUTH_TIME_IS_OVER("Время для авторизации вышло");

        private static final String TITLE = "Ошибка аутентификации";
        private static final String TYPE = TITLE;
        private final String message;

        AuthError(String message) {
            this.message = message;
        }

        public void show() {
            showDialog(Alert.AlertType.ERROR, TITLE, TYPE, message);
        }

        public void show(String message) {
            showDialog(Alert.AlertType.ERROR, TITLE, TYPE, message);
        }
    }


    public enum NetworkError {
        SEND_MESSAGE("Не удалось отправить сообщение!"),
        SERVER_CONNECT("Не удалось установить соединение с сервером!");

        private static final String TITLE = "Сетевая ошибка";
        private static final String TYPE = "Ошибка передачи данных по сети";
        private final String message;

        NetworkError(String message) {
            this.message = message;
        }

        public void show() {
            showDialog(Alert.AlertType.ERROR, TITLE, TYPE, message);
        }

    }

    public enum AboutDialog {
        INFO(String.format("Создатель чата: %s %n Ипользуемые технологии: %s", "Скоморохин Антон", "Java 14, JavaFX, Maven"));

        private final String message;
        private static final String TITLE = "Информация о программе";
        private static final String TYPE = "Онлайн чат для локального обмена сообщениями";

        AboutDialog(String message) {
            this.message = message;
        }

        public void show() {
            showDialog(Alert.AlertType.INFORMATION, TITLE, TYPE, message);
        }
    }


    private static void showDialog(Alert.AlertType dialogType, String title, String type, String message) {
        Alert alert = new Alert(dialogType);
        alert.initOwner(ClientChat.INSTANCE.getChatStage());
        alert.setTitle(title);
        alert.setHeaderText(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
