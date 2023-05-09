module com.skomorokhin.timerpcapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.skomorokhin.timerpcapp to javafx.fxml;
    exports com.skomorokhin.timerpcapp;
}