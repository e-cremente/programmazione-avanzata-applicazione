package it.unipi.applicazioneprogetto;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

public class LoginController {
    
    @FXML private Text textTitle;
    @FXML private Label textUsername;
    @FXML private Label textPassword;
    @FXML private Button buttonLogin;
    @FXML private Text textRegister;
    @FXML private Button buttonRegister;
    @FXML private TextField fieldUsername;
    @FXML private PasswordField fieldPassword;
    @FXML private VBox vboxLogin;
    @FXML private Text textMessage;
    @FXML private Text textLingua;
    @FXML private ChoiceBox choiceboxLingua;
    
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
        textTitle.setText(lang.textTitle);
        textUsername.setText(lang.textUsername);
        textPassword.setText(lang.textPassword);
        buttonLogin.setText(lang.buttonLogin);
        textRegister.setText(lang.textRegister);
        buttonRegister.setText(lang.buttonRegister);
        textLingua.setText(lang.textLingua);
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
    //invocato alla premuta del tasto di registrazione
    @FXML
    private void login(){
        if(fieldUsername.getText().isBlank() || fieldPassword.getText().isBlank()){
            textMessage.setText(lang.erroreUserPwdBlank);    
            return;
        }
        Task task = new Task<Void>(){
        @Override public Void call(){
            try{
                //creo una connessione verso l'url, con i passaggi necessari per specificare che è POST
                URL url = new URL("http://localhost:8080/login");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                //Se i dati da passare fossero in json includerei questa linea
                con.setRequestProperty("Content-Type", "application/json");

                //Scrivo la stringa di parametri che ho bisogno di passare
                String urlParameters = "{\"username\":\"" + fieldUsername.getText() + "\", \"password\":\"" + fieldPassword.getText() + "\"}";  

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
                    //App.setRoot("home"); to do
                    textMessage.setText("pagina home");
                } else {
                    textMessage.setText(lang.erroreUserNotFound);  
                }

            }catch(Exception e){
                logger.error(e.getMessage());
            }
            return null;
        }    
        };
        
        new Thread(task).start();
    }
    
    /*
    @FXML
    private void mostraConto(){
        Task task = new Task<Void>(){
        @Override public Void call(){
            try{
                
                URL url = new URL("http://localhost:8080/count");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                
                String inputLine;
                StringBuffer content = new StringBuffer();
                while((inputLine = in.readLine()) != null){
                    content.append(inputLine);
                }
                in.close();
                
                System.out.println(content);
                
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }    
        };
        
        new Thread(task).start();
    }

    private void viewAll() throws IOException {
        Task task = new Task<Void>(){
        @Override public Void call(){
            try{
                
                URL url = new URL("http://localhost:8080/all");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                
                String inputLine;
                StringBuffer content = new StringBuffer();
                while((inputLine = in.readLine()) != null){
                    content.append(inputLine);
                }
                in.close();
                
                Gson gson = new Gson();
                
                JsonElement json = gson.fromJson(content.toString(), JsonElement.class);
                
                JsonArray anime = json.getAsJsonArray();
                              
                for(int i = 0; i < anime.size(); i++){
                    JsonObject d = anime.get(i).getAsJsonObject();
                    System.out.println(d.get("name").getAsString());
                }
                
                logger.debug("Dati caricati.");
                
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }    
        };
        
        new Thread(task).start();
    }
    
    @FXML
    private void initializeDB() throws IOException {
        Task task = new Task<Void>(){
            @Override public Void call(){
                try{
                    //creo una connessione verso l'url, con i passaggi necessari per specificare che è POST
                    URL url = new URL("http://localhost:8080/add");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    //Se i dati da passare fossero in json includerei questa linea
                    con.setRequestProperty("Content-Type", "application/json");
                    
                    //Scrivo la stringa di parametri che ho bisogno di passare
                    String urlParameters = "{\"name\":\"PeePeePooPoo\"}";  
                    
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
                    
                    System.out.println(content);
                    
                    }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }    
        };
        
        new Thread(task).start();
    }*/
}
