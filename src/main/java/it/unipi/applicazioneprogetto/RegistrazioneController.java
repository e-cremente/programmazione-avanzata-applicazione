package it.unipi.applicazioneprogetto;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegistrazioneController {
    
    @FXML private Text textRegistrati;
    @FXML private Label labelUserReg;
    @FXML private TextField fieldUserReg;
    @FXML private Label labelPwdReg;
    @FXML private PasswordField fieldPwdReg;
    @FXML private Button buttonRegistrazione;
    @FXML private Text textMessage;
    @FXML private Text textLingua;
    @FXML private ChoiceBox choiceboxLingua;
    @FXML private Button buttonGoToLogin;
    @FXML private VBox vboxRegistrazione;
       
    private static final Logger logger = LogManager.getLogger(LoginController.class);
    private Linguaggio lang;
    
    //funzione di inizializzazione
    @FXML
    public void initialize(){
        choiceboxLingua.getItems().addAll("Italiano", "English");
        choiceboxLingua.setValue(Linguaggio.lang);   
        choiceboxLingua.setOnAction(e -> {changeLanguage();});
        changeLanguage();            
    }
    
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
        textRegistrati.setText(lang.textRegistrati);
        labelUserReg.setText(lang.labelUserReg);        
        labelPwdReg.setText(lang.labelPwdReg);
        buttonRegistrazione.setText(lang.buttonRegistrazione);
        buttonGoToLogin.setText(lang.buttonGoToLogin);
    }
    
    //funzione per registrare nel database un nuovo utente.
    //invocato alla premuta del tasto di registrazione
    @FXML
    private void registraUtente(){
        if(fieldUserReg.getText().isBlank() || fieldPwdReg.getText().isBlank()){
            textMessage.setText(lang.erroreUserPwdBlank);    
            return;
        }
        Task task = new Task<Void>(){
        @Override public Void call(){
            try{
                //creo una connessione verso l'url, con i passaggi necessari per specificare che è POST
                URL url = new URL("http://localhost:8080/utente");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                //Se i dati da passare fossero in json includerei questa linea
                con.setRequestProperty("Content-Type", "application/json");

                //Scrivo la stringa di parametri che ho bisogno di passare
                String urlParameters = "{\"username\":\"" + fieldUserReg.getText() + "\", \"password\":\"" + fieldPwdReg.getText() + "\"}";  

                //La attacco all'url
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.writeBytes(urlParameters);
                out.flush();
                out.close();

                //Ricevo la risposta
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                if(content.toString().equals("OK")){
                    fieldUserReg.setDisable(true);
                    fieldPwdReg.setDisable(true);
                    buttonRegistrazione.setDisable(true);
                    textMessage.setText(lang.correttaRegistrazione);                  
                } else {
                    textMessage.setText(lang.erroreUserFound);  
                }

            }catch(Exception e){
                logger.error(e.getMessage());
            }
            return null;
        }    
        };
        
        new Thread(task).start();
    }
    
    //metodo che torna alla schermata di login
    //invocato alla premuta del tasto go back to login page
    @FXML
    private void goToLogin(){
        try{
            App.setRoot("login");
        }catch(IOException e){
            logger.error(e.getMessage());
        }       
    }
    
    //funzione che rimuove eventuali testi temporanei
    //viene chiamata quando si clicca in un punto qualsiasi dell'interfaccia
    @FXML
    private void removeTemporary(){
        textMessage.setText("");
    }
    
}