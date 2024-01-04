/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.applicazioneprogetto;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author edoar
 */
public class HomeController{
    
    @FXML Text textBenvenuto;
    @FXML Text textCopertina;
    @FXML Text textTopCento;
    @FXML TableView<Anime> tableviewAnime = new TableView<>();
    @FXML ImageView imageviewAnime;
    @FXML Image imageAnime;
    @FXML private Text textLingua;
    @FXML private ChoiceBox choiceboxLingua;
    @FXML private TableColumn idCol;
    @FXML private TableColumn nomeCol;
    @FXML private TableColumn finishedCol;
    @FXML private TableColumn episodesCol;
    @FXML private TableColumn scoreCol;
    @FXML private Text textMessage;
    @FXML private Button buttonLogout;
    
    private static final Logger logger = LogManager.getLogger(HomeController.class);   
    private ObservableList<Anime> ol;
    private Linguaggio lang;

    public void initialize() throws IOException{
        //Creo una tabella che conterrà la top 100 degli anime inseriti nel database
        
        //il nome di propertyvaluefactory deve essere quello corrispondente al nome della variabile
        //della classe (in questo caso Anime) che deve essere inserito in questa colonna
        idCol = new TableColumn("Rank");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        nomeCol = new TableColumn("Name");
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        finishedCol = new TableColumn("Finished");
        finishedCol.setCellValueFactory(new PropertyValueFactory<>("finished"));
        finishedCol.setStyle("-fx-alignment: CENTER");
        //questo di seguito è un metodo che mi permette di usare una classe di ausilio per mostrare delle piccole
        //immagini all'interno delle colonne della tabella
        finishedCol.setCellFactory(p -> new FinishedCell());
        
        episodesCol = new TableColumn("Episodes");
        episodesCol.setCellValueFactory(new PropertyValueFactory<>("episodes"));
        
        scoreCol = new TableColumn("Score");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        
        //Aggiungo le colonne
        tableviewAnime.getColumns().addAll(idCol, nomeCol, finishedCol, episodesCol, scoreCol);
        
        //observarvable list che mi andrà di fatto a comporre le righe
        ol = FXCollections.observableArrayList();
        
        //Associo ol alla tabella cosicché aggiungendo un elemento a ol, questo compaia direttamente nella tabella
        tableviewAnime.setItems(ol);
        
        Task task = new Task<Void>(){
        @Override public Void call(){
            try{
                //faccio una richiesta GET verso un metodo che mi restituisce tutto il contenuto della tabella anime
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
                
                //in un ciclo, creo istanze di oggetti Anime e li aggiungo a ol cosicché vengano aggiunti alla tabella
                for(int i = 0; i < anime.size(); i++){
                    JsonObject d = anime.get(i).getAsJsonObject();
                    //sembra che il booleano sia al contrario, invece è giusto. MySql salva i booleani al contrario
                    String urlImage = d.get("finished").getAsBoolean() ? "img/complete.png" : "img/incomplete.png";
                    Anime a = new Anime(d.get("id").getAsLong(), d.get("name").getAsString(), urlImage, 
                                        d.get("episodes").getAsString(), d.get("score").getAsDouble(), d.get("image").getAsString());
                    
                    ol.add(a);
                }
                
                //Mostro l'immagine del primo anime in classifica
                Anime firstAnime = ol.get(0);
                imageAnime = new Image(firstAnime.getImage()); 
                imageviewAnime.setImage(imageAnime);
                
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }    
        };
        
        new Thread(task).start();
        
        logger.debug("Tabella inizializzata e pagina caricata.");
        
        choiceboxLingua.getItems().addAll("Italiano", "English");
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
        idCol.setText(lang.idCol);
        nomeCol.setText(lang.nomeCol);
        finishedCol.setText(lang.finishedCol);
        episodesCol.setText(lang.episodesCol);
        scoreCol.setText(lang.scoreCol);
        textLingua.setText(lang.textLingua);
        buttonLogout.setText(lang.buttonLogout);
        textBenvenuto.setText(lang.textBenvenuto + LoginController.utente + "!");
        textCopertina.setText(lang.textCopertina);
        textTopCento.setText(lang.textTopCento);
    }    
    
    //metodo che cambia l'immagine mostrata in base all'anime che viene cliccato sulla tabella
    //invocato a ogni click sulla tabella
    @FXML
    private void updateImg(){
        Anime a = tableviewAnime.getSelectionModel().getSelectedItem();
        imageAnime = new Image(a.getImage()); 
        imageviewAnime.setImage(imageAnime);
    }
    
    //funzione che rimuove eventuali testi temporanei
    //viene chiamata quando si clicca in un punto qualsiasi dell'interfaccia
    @FXML
    private void removeTemporary(){
        textMessage.setText("");
    }
    
    @FXML
    private void logout() throws IOException{
        LoginController.utente = "";
        App.setRoot("login", 640, 480);
    }
}
