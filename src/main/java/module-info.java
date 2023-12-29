module it.unipi.applicazioneprogetto {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires com.google.gson;
    requires xstream;
    requires java.base;
    
    opens it.unipi.applicazioneprogetto to javafx.fxml;
    exports it.unipi.applicazioneprogetto;
}
