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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
public class MylistController {

    @FXML private TableView<Anime> tableviewMyAnime = new TableView<>();
    @FXML private ImageView imageviewAnime;
    @FXML private Image imageAnime;
    @FXML private TableColumn idCol;
    @FXML private TableColumn nomeCol;
    @FXML private TableColumn finishedCol;
    @FXML private TableColumn episodesCol;
    @FXML private TableColumn scoreCol;
    @FXML private Label labelMyList;
    @FXML private Label textCopertinaList;
    @FXML private Button buttonTopCento;
    @FXML private Text textLingua;
    @FXML private ChoiceBox choiceboxLingua;
    @FXML private Text textMessage;
    @FXML private Button buttonLogout;
    @FXML private Label labelMyScore;
    @FXML private TextField textfieldMyScore;
    @FXML private Label labelNotes;
    @FXML private TextArea textareaNotes;
    @FXML private Button buttonSalvaModifiche;
    @FXML private MenuItem menuitemRemove;
    
    private static final Logger logger = LogManager.getLogger(HomeController.class);   
    private ObservableList<Anime> ol;
    private Linguaggio lang;
    
    public void initialize() {
        //Creo una tabella che conterrà la mia personale lista anime
        
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
        tableviewMyAnime.getColumns().addAll(idCol, nomeCol, finishedCol, episodesCol, scoreCol);
        
        //Cambio la misura delle colonne in modo tale da fargli riempire tutta la lunghezza della tabella
        idCol.prefWidthProperty().bind(tableviewMyAnime.widthProperty().multiply(0.1));
        nomeCol.prefWidthProperty().bind(tableviewMyAnime.widthProperty().multiply(0.5));
        finishedCol.prefWidthProperty().bind(tableviewMyAnime.widthProperty().multiply(0.1));
        episodesCol.prefWidthProperty().bind(tableviewMyAnime.widthProperty().multiply(0.15));
        scoreCol.prefWidthProperty().bind(tableviewMyAnime.widthProperty().multiply(0.15));
        
        //observarvable list che mi andrà di fatto a comporre le righe
        ol = FXCollections.observableArrayList();
        
        //Associo ol alla tabella cosicché aggiungendo un elemento a ol, questo compaia direttamente nella tabella
        tableviewMyAnime.setItems(ol);
        
        Task task = new Task<Void>(){
        @Override public Void call(){
            try{
                
                String url = "http://localhost:8080/join";
                String urlParameters = "username=" + LoginController.utente;
                
                JsonArray anime = Utility.postStringRequestJsonAnswer(url, urlParameters).getAsJsonArray();
   
                //in un ciclo, creo istanze di oggetti Anime e li aggiungo a ol cosicché vengano aggiunti alla tabella
                for(int i = 0; i < anime.size(); i++){
                    JsonObject d = anime.get(i).getAsJsonObject();
                    //sembra che il booleano sia al contrario, invece è giusto. MySql salva i booleani al contrario
                    String urlImage = d.get("finished").getAsBoolean() ? "img/complete.png" : "img/incomplete.png";
                    Anime a = new Anime(d.get("id").getAsLong(), d.get("name").getAsString(), urlImage, 
                                        d.get("episodes").getAsString(), d.get("score").getAsDouble(), d.get("image").getAsString());
                    
                    ol.add(a);
                }
                
                //Mostro l'immagine del primo anime nella lista
                if(!ol.isEmpty()){
                    Anime firstAnime = ol.get(0);
                    if(firstAnime != null){
                        imageAnime = new Image(firstAnime.getImage()); 
                        imageviewAnime.setImage(imageAnime);
                    }      
                }
                
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }    
        };
        
        new Thread(task).start();
        
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
        buttonTopCento.setText(lang.buttonTopCento);
        textLingua.setText(lang.textLingua);
        buttonLogout.setText(lang.buttonLogout);
        labelMyScore.setText(lang.labelMyScore);
        labelMyScore.setStyle("-fx-alignment: CENTER");
        labelNotes.setText(lang.labelNotes);
        buttonSalvaModifiche.setText(lang.buttonSalvaModifiche);
        labelMyList.setText(lang.labelMyList);
        textCopertinaList.setText(lang.textCopertina);
        menuitemRemove.setText(lang.menuitemRemove);
        removeTemporary();
    }
    
    //funzione che rimuove eventuali testi temporanei
    //viene chiamata quando si clicca in un punto qualsiasi dell'interfaccia
    @FXML
    private void removeTemporary(){
        textMessage.setText("");
    }
    
    //metodo che torna nella pagina della top 100 anime
    //invocato alla pressione del tasto torna alla top 100
    @FXML
    private void goToTopCento() throws IOException{
        App.setRoot("home");
    }
    
    //metodo che cambia l'immagine mostrata in base all'anime che viene cliccato sulla tabella
    //mostra anche il punteggio personale e le note di un determinato anime, se sono state settate
    //invocato a ogni click sulla tabella
    @FXML
    private void updateImg(){
        removeTemporary();
        Anime a = tableviewMyAnime.getSelectionModel().getSelectedItem();
        
        if(a == null){
            textMessage.setText(lang.erroreAnimeNonPresenti);
            return;
        }
        
        imageAnime = new Image(a.getImage()); 
        imageviewAnime.setImage(imageAnime);
        
        Task task = new Task<Void>(){
        @Override public Void call(){
            try{
                
                String url = "http://localhost:8080/getscorenotes";
                String urlParameters = "{\"idanime\":\"" + tableviewMyAnime.getSelectionModel().getSelectedItem().getId() + "\", \"username\":\"" + LoginController.utente + "\"}";
                
                JsonObject anime = Utility.postJsonRequestJsonAnswer(url, urlParameters).getAsJsonObject();
                
                String scoreTrovato = anime.get("ownscore").getAsString();
                String score = scoreTrovato.equals("0.0") ? "" : scoreTrovato.equals("10.0") ? "10" : scoreTrovato;
                
                textfieldMyScore.setText(score);
                textareaNotes.setText(anime.get("notes").getAsString());
                
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }    
        };
        
        new Thread(task).start();
    }
    
    //metodo che salva i nuovi valori di punteggio personale e note
    //invocato alla premuta del tasto salva modifiche
    @FXML
    private void salvaModifiche(){
        Anime a = tableviewMyAnime.getSelectionModel().getSelectedItem();
        if(a == null){
            textMessage.setText(lang.erroreAnimeNotSelected);
            return;
        }
        if(textareaNotes.getText().length() > 1000){
            textMessage.setText(lang.erroreNoteLunghe);
            return;
        }
        if(!textfieldMyScore.getText().matches("\\d\\d?|\\d\\d?\\.\\d")){
            textMessage.setText(lang.erroreScoreFormat);
            return;
        }
        if(Double.valueOf(textfieldMyScore.getText()) > 10.0){
            textMessage.setText(lang.erroreScoreValue);
            return;
        }
        buttonSalvaModifiche.setDisable(true);
        Task task = new Task<Void>(){
        @Override public Void call(){
            try{
                
                String url = "http://localhost:8080/addscorenotes";
                String urlParameters = "{\"idanime\":\"" + tableviewMyAnime.getSelectionModel().getSelectedItem().getId() + "\", \"username\":\"" + LoginController.utente + "\","
                                     + " \"ownscore\":\"" + textfieldMyScore.getText() + "\", \"notes\":\"" + textareaNotes.getText() +"\"}";  
                
                String content = Utility.postJsonRequestStringAnswer(url, urlParameters);
                
                if(content.equals("OK")){
                    textMessage.setText(lang.correttoUpdateAnime);
                } else {
                    textMessage.setText(lang.erroreUpdateAnime);
                }
                
                buttonSalvaModifiche.setDisable(false);

            }catch(Exception e){
                logger.error(e.getMessage());
                textMessage.setText(lang.erroreCaratteri);
                buttonSalvaModifiche.setDisable(false);
            }
            return null;
        }    
        };
        
        new Thread(task).start();
    }
    
    //Metodo che rimuove un anime dal database e conseguentemente dalla mia lista.
    //Invocato quando si preme remove dal context menu
    @FXML
    private void removeAnime(){
        Task task = new Task<Void>(){
        @Override public Void call(){
            try{
                Anime a = tableviewMyAnime.getSelectionModel().getSelectedItem();
                
                String url = "http://localhost:8080/remove";
                String urlParameters = "{\"idanime\":\"" + a.getId() + "\", \"username\":\"" + LoginController.utente + "\"}";  
                
                String content = Utility.postJsonRequestStringAnswer(url, urlParameters);
                
                if(content.equals("OK")){
                    ol.remove(a);
                } else {
                    textMessage.setText(lang.erroreUpdateAnime);
                }
                
            }catch(Exception e){
                logger.error(e.getMessage());
            }
            return null;
        }    
        };
        
        new Thread(task).start();
    }
    
    @FXML
    private void logout() throws IOException{
        LoginController.utente = "";
        App.setRoot("login", 640, 480);
    }
    
}
