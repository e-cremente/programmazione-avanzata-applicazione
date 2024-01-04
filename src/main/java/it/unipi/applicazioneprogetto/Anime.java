/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazioneprogetto;

import javafx.scene.image.Image;

/**
 *
 * @author edoar
 */
public class Anime {
    private final long id;
    private final String name;
    //private final Boolean finished;
    private final String episodes;
    private final double score;
    private final String image;
    private final String finished;

    public Anime(long id, String name, String finished, String episodes, double score, String image) {
        this.id = id;
        this.name = name;
        this.finished = finished;
        this.episodes = episodes;
        this.score = score;
        this.image = image;
    }
    
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFinished() {
        return finished;
    }

    public String getEpisodes() {
        return episodes;
    }

    public double getScore() {
        return score;
    }

    public String getImage() {
        return image;
    }
    
    
}
