package com.skomorokhin.timerpcapp;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;


import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class TimerController implements Initializable {

    private List<String> actions = new ArrayList<>(Arrays.asList("PowerOff", "Reset", "Sleep"));
    private TimeValue timeValue = new TimeValue();
    private final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private String selectedAction;
    Timer timer;


    @FXML
    private TextField hh;
    @FXML
    private TextField mm;
    @FXML
    private TextField ss;

    @FXML
    Label timerLabel;

    @FXML
    protected ComboBox<String> comboBoxAction;
    @FXML
    Button startButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBoxAction.getItems().addAll(actions);
        timerLabel.setText(timeValue.getInTimeFormat());

    }

    private void setTimeValue() {
        Integer hhValue = !hh.getText().equals("") ? Integer.parseInt(hh.getText()) : 0;
        Integer mmValue = !mm.getText().equals("") ? Integer.parseInt(mm.getText()) : 0;
        Integer ssValue = !ss.getText().equals("") ? Integer.parseInt(ss.getText()) : 0;
        timeValue.setTimeValue(hhValue, mmValue, ssValue);
    }


    @FXML
    private void checkInputTextField(KeyEvent event) {
        TextField textField = (TextField) event.getSource();

        String nameTextField = textField.promptTextProperty().get();
        if (event.getCharacter().matches("\\D")) {
            event.consume();
            textField.setText("");
            enableDisableStartButton();
        }
        if (!textField.getText().isEmpty()) {
            int currentValue = Integer.parseInt(textField.getText());
            if (!nameTextField.equals("HH")) {
                textField.setText(String.valueOf(checkAvailableValue(currentValue, 59)));
            } else {
                textField.setText(String.valueOf(checkAvailableValue(currentValue, 23)));
            }
            textField.positionCaret(textField.getText().length());
            enableDisableStartButton();
        }
    }

    private int checkAvailableValue(int currentValue, int availableValue) {
        return Math.min(currentValue, availableValue);
    }

    @FXML
    private void startTimer() {
        resetTimer();
        setTimeValue();
        selectedAction = comboBoxAction.getSelectionModel().getSelectedItem();
        timer = new Timer();
        TimerTask timerTick = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (timeValue.getTimeInSecond() != 0) {
                            timerLabel.setText(timeValue.getInTimeFormat());
                            timeValue.decreaseTimeValue(1);
                            timerLabel.setText(timeValue.getInTimeFormat());
                        }
                    }
                });
            }
        };
        TimerTask timerIsOver = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ProcessBuilder builder = new ProcessBuilder();
                        try {
                            switch (selectedAction) {
                                case ("PowerOff"): powerOff(builder);
                                case ("Reset"): reset(builder);
                                case ("Sleep"): sleep(builder);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };

        timer.schedule(timerIsOver, (long) timeValue.getTimeInSecond() * 1000);
        timer.schedule(timerTick, 0, 1000);
    }

    @FXML
    private void resetTimer() {
        if (timer != null) {
            timeValue.setTimeValue(0);
            timerLabel.setText(timeValue.getInTimeFormat());
            timer.cancel();
            timer.purge();
        }
    }

    @FXML
    private void enableDisableStartButton() {
        if (!hh.getText().equals("") || !mm.getText().equals("") || !ss.getText().equals("")) {
            if (comboBoxAction.getSelectionModel().getSelectedItem() != null)
            startButton.disableProperty().set(false);
        } else
            startButton.disableProperty().set(true);
    }

    private void powerOff(ProcessBuilder builder) throws IOException {
        builder.command("shutdown ", "/l");
        builder.start();
    }

    private void reset(ProcessBuilder builder) throws IOException {
        builder.command("shutdown ", "/r");
        builder.start();
    }

    private void sleep(ProcessBuilder builder) throws IOException {
        builder.command("rundll32.exe ", "powrprof.dll,SetSuspendState", "Sleep");
        builder.start();
    }
}