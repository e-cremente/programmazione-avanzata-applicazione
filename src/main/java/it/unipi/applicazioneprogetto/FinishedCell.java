/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazioneprogetto;

import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author edoar
 */

//classe di ausilio che serve per mostrare immagini all'interno delle celle di una tabella
public class FinishedCell extends TableCell<Anime, String>{
    private final ImageView imageView = new ImageView();
    
    @Override
    public void updateItem(String url, boolean empty){
        super.updateItem(url, empty);
        
        if(url == null || empty){
            imageView.setImage(null);
            setGraphic(null);
        } else {
            Image img = new Image(getClass().getResource(url).toExternalForm());
            imageView.setImage(img);
            imageView.setFitWidth(24);
            imageView.setFitHeight(24);
            setGraphic(imageView);
        }
    }
}
