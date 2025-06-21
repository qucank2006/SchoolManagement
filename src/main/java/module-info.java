module com.example.dacs1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires jakarta.mail;
    requires jbcrypt;

    opens com.example.dacs1 to javafx.fxml, javafx.graphics;
    exports com.example.dacs1;

    opens com.example.dacs1.Model to javafx.fxml;
    exports com.example.dacs1.Model;

    opens com.example.dacs1.Controller to javafx.fxml;
    exports com.example.dacs1.Controller;

    opens com.example.dacs1.Controller.Admin to javafx.fxml;
    exports com.example.dacs1.Controller.Admin;

    opens com.example.dacs1.Controller.Login to javafx.fxml;
    exports com.example.dacs1.Controller.Login;

    opens com.example.dacs1.Utils to javafx.fxml;
    exports com.example.dacs1.Utils;

    opens com.example.dacs1.Chat to javafx.fxml;
    exports com.example.dacs1.Chat;
}