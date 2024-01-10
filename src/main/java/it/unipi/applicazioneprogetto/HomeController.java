/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.applicazioneprogetto;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import java.io.IOException;
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
    
    @FXML private Text textBenvenuto;
    @FXML private Text textCopertina;
    @FXML private Text textTopCento;
    @FXML private TableView<Anime> tableviewAnime = new TableView<>();
    @FXML private ImageView imageviewAnime;
    @FXML private Image imageAnime;
    @FXML private Text textLingua;
    @FXML private ChoiceBox choiceboxLingua;
    @FXML private TableColumn idCol;
    @FXML private TableColumn nomeCol;
    @FXML private TableColumn finishedCol;
    @FXML private TableColumn episodesCol;
    @FXML private TableColumn scoreCol;
    @FXML private Text textMessage;
    @FXML private Button buttonLogout;
    @FXML private Button buttonMyList;
    @FXML private Button buttonAggiungi;
    @FXML private Text textList;
    @FXML private ImageView imageviewRandom;
    
    private static final Logger logger = LogManager.getLogger(HomeController.class);   
    private ObservableList<Anime> ol;
    private Linguaggio lang;

    public void initialize() {
        //Creo una tabella che conterrà la top 100 degli anime inseriti nel database
        
        //il nome di propertyvaluefactory deve essere quello corrispondente al nome della variabile
        //della classe (in questo caso Anime) che deve essere inserito in questa colonna
        idCol = new TableColumn("Rank");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setStyle("-fx-alignment: CENTER");
        
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
        episodesCol.setStyle("-fx-alignment: CENTER");
        
        scoreCol = new TableColumn("Score");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreCol.setStyle("-fx-alignment: CENTER");
        
        //Aggiungo le colonne
        tableviewAnime.getColumns().addAll(idCol, nomeCol, finishedCol, episodesCol, scoreCol);
        
        //Cambio la misura delle colonne in modo tale da fargli riempire tutta la lunghezza della tabella
        idCol.prefWidthProperty().bind(tableviewAnime.widthProperty().multiply(0.1));
        nomeCol.prefWidthProperty().bind(tableviewAnime.widthProperty().multiply(0.5));
        finishedCol.prefWidthProperty().bind(tableviewAnime.widthProperty().multiply(0.1));
        episodesCol.prefWidthProperty().bind(tableviewAnime.widthProperty().multiply(0.15));
        scoreCol.prefWidthProperty().bind(tableviewAnime.widthProperty().multiply(0.15));
        
        //observarvable list che mi andrà di fatto a comporre le righe
        ol = FXCollections.observableArrayList();
        
        //Associo ol alla tabella cosicché aggiungendo un elemento a ol, questo compaia direttamente nella tabella
        tableviewAnime.setItems(ol);
        
        Task task = new Task<Void>(){
        @Override public Void call(){
            try{
                
                String url = "http://localhost:8080/all";
                
                JsonArray anime = Utility.getRequestJsonAnswer(url);
                
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
                logger.error(e.getMessage());
            }
            return null;
        }    
        };
        
        new Thread(task).start();
        
        logger.debug("Tabella inizializzata e pagina caricata.");
        
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
        buttonMyList.setText(lang.buttonMyList);
        buttonAggiungi.setText(lang.buttonAggiungi);
        textList.setText(lang.textList);
        removeTemporary();
    }    
    
    //metodo che cambia l'immagine mostrata in base all'anime che viene cliccato sulla tabella
    //invocato a ogni click sulla tabella
    @FXML
    private void updateImg(){
        removeTemporary();
        Anime a = tableviewAnime.getSelectionModel().getSelectedItem();
        imageAnime = new Image(a.getImage()); 
        imageviewAnime.setImage(imageAnime);
        checkIfInList();
    }
    
    //metodo che mostra un'immagine casuale a seconda se l'anime selezionato nella tabella appartiene 
    //alla propria lista oppure no.
    //invocato da updateImg ogni volta che si clicca sulla tabella
    private void checkIfInList(){
        Task task = new Task<Void>(){
        @Override public Void call(){
            try{               
                String url = "http://localhost:8080/findjoin";
                String urlParameters = "{\"idanime\":\"" + tableviewAnime.getSelectionModel().getSelectedItem().getId() + "\", \"username\":\"" + LoginController.utente + "\"}";
                
                String content = Utility.postJsonRequestStringAnswer(url, urlParameters);

                int imagenumber = (int)(Math.random() * 6) + 1;
                String urlImage;
                
                if(content.equals("OK")){
                    urlImage = "img/yes/yes";                
                } else {
                    urlImage = "img/no/no";      
                }
                
                imageviewRandom.setImage(new Image(getClass().getResource(urlImage + imagenumber +".png").toExternalForm()));  

            }catch(Exception e){
                logger.error(e.getMessage());
            }
            return null;
        }    
        };
        
        new Thread(task).start();
    
    }
    
    //funzione che rimuove eventuali testi temporanei
    //viene chiamata quando si clicca in un punto qualsiasi dell'interfaccia
    @FXML
    private void removeTemporary(){
        textMessage.setText("");
    }
    
    //metodo che torna alla schermata della top 100
    //invocato alla pressione del tasto torna alla top 100
    @FXML
    private void goToMyList() throws IOException{
        App.setRoot("mylist");
    }
    
    //metodo che aggiunge l'anime selezionato alla mia lista
    //invocato alla pressione del tasto aggiungi alla mia lista
    @FXML
    private void addAnime(){
        removeTemporary();
        Anime a = tableviewAnime.getSelectionModel().getSelectedItem();
        if(a == null){
            textMessage.setText(lang.erroreAnimeNotSelected);    
            return;
        }
        Task task = new Task<Void>(){
        @Override public Void call(){
            try{
                
                String url = "http://localhost:8080/addtouserlist";
                String urlParameters = "{\"idanime\":\"" + String.valueOf(a.getId()) + "\", \"username\":\"" + LoginController.utente + "\"}";  
                
                String content = Utility.postJsonRequestStringAnswer(url, urlParameters);

                if(content.equals("OK")){
                    textMessage.setText(lang.correttaAggiuntaAnime);                  
                } else {
                    textMessage.setText(lang.erroreAnimeAggiunto);  
                }

            }catch(Exception e){
                logger.error(e.getMessage());
            }
            return null;
        }    
        };
        
        new Thread(task).start();
    }
    
    //metodo per il logout
    //invocato alla pressione del tasto logout
    @FXML
    private void logout() throws IOException{
        LoginController.utente = "";
        App.setRoot("login", 700, 540);
    }
}
