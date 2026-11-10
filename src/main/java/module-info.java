module com.mycompany.evotingkampus {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.evotingkampus to javafx.fxml;
    exports com.mycompany.evotingkampus;
}
