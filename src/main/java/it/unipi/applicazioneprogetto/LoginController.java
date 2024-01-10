package it.unipi.applicazioneprogetto;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import java.io.IOException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController {
    
    @FXML private Text textTitle;
    @FXML private Label textUsername;
    @FXML private Label textPassword;
    @FXML private Button buttonLogin;
    @FXML private Text textRegister;
    @FXML private Button buttonRegister;
    @FXML private TextField fieldUsername;
    @FXML private PasswordField fieldPassword;
    @FXML private Text textMessage;
    @FXML private Text textLingua;
    @FXML private ChoiceBox choiceboxLingua;
    
    private static final Logger logger = LogManager.getLogger(LoginController.class);
    private Linguaggio lang;
    public static String utente;
    
    //funzione di inizializzazione
    @FXML
    public void initialize(){
        choiceboxLingua.getItems().addAll("Italiano", "English", "中文");
        choiceboxLingua.setValue(Linguaggio.lang);   
        choiceboxLingua.setOnAction(e -> {changeLanguage();});
        changeLanguage();
    }
    
    //metodo che prende il valore all'interno del box della lingua, lo assegna a una variabile, e in base a quello cambia la lingua
    private void changeLanguage(){
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.alias("linguaggio", Linguaggio.class);
        Linguaggio.lang = choiceboxLingua.getValue().toString();
        lang = (Linguaggio) xstream.fromXML(getClass().getResource("languages/language_"+ Linguaggio.lang +".xml"));      
        changeLanguageText();
    }
    
    //funzione da chiamare ogni volta che la lingua viene cambiata (o all'avvio)
    private void changeLanguageText(){
        textTitle.setText(lang.textTitle);
        textUsername.setText(lang.textUsername);
        textPassword.setText(lang.textPassword);
        buttonLogin.setText(lang.buttonLogin);
        textRegister.setText(lang.textRegister);
        buttonRegister.setText(lang.buttonRegister);
        textLingua.setText(lang.textLingua);
        removeTemporary();
    }

    //funzione che rimuove eventuali testi temporanei
    //viene chiamata quando si clicca in un punto qualsiasi dell'interfaccia
    @FXML
    private void removeTemporary(){
        textMessage.setText("");
    }
    
    //metodo che porta alla visualizzazione della schermata di registrazione
    //invocato quando si preme il tasto di registrazione
    @FXML
    private void goToRegistrationPage() throws IOException{
        App.setRoot("registrazione");
    }
    
    //funzione per registrare nel database un nuovo utente.
    //invocato alla premuta del tasto di login
    @FXML
    private void login(){
        if(fieldUsername.getText().isBlank() || fieldPassword.getText().isBlank()){
            textMessage.setText(lang.erroreUserPwdBlank);    
            return;
        }
        Task task = new Task<Void>(){
        @Override public Void call(){
            try{
                //preparo url e parametri per fare una richiesta POST
                String url = "http://localhost:8080/login";
                String urlParameters = "{\"username\":\"" + fieldUsername.getText() + "\", \"password\":\"" + fieldPassword.getText() + "\"}";
                
                String content = Utility.postJsonRequestStringAnswer(url, urlParameters);

                if(content.equals("OK")){
                    utente = fieldUsername.getText();
                    App.setRoot("home", 1280, 800);
                } else {
                    textMessage.setText(lang.erroreUserNotFound);  
                }

            }catch(Exception e){
                e.printStackTrace();
                logger.error(e.getMessage());
            }
            return null;
        }    
        };
        
        new Thread(task).start();
    }
    
}
