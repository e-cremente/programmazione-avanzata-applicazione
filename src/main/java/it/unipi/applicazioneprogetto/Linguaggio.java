/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazioneprogetto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;

/**
 *
 * @author edoar
 */

@XStreamAlias("linguaggio")
public class Linguaggio implements Serializable{
    
    public static String lang = "English";
    
    //oggetti interfaccia grafica
    public String textTitle;
    public String textUsername;
    public String textPassword;
    public String buttonLogin;
    public String textRegister;
    public String buttonRegister;
    public String textRegistrati;
    public String labelUserReg;
    public String labelPwdReg;
    public String buttonRegistrazione;
    public String buttonGoToLogin;
    public String textLingua;
    public String idCol;
    public String nomeCol;
    public String finishedCol;
    public String episodesCol;
    public String scoreCol;
    public String buttonLogout;
    public String textBenvenuto;
    public String textCopertina;
    public String textTopCento;
    public String buttonMyList;
    public String buttonTopCento;
    public String buttonAggiungi;
    public String textList;
    public String labelMyScore;
    public String labelNotes;
    public String buttonSalvaModifiche;
    public String labelMyList;
    
    //messaggi di successo
    public String correttaRegistrazione;
    public String correttaAggiuntaAnime;
    public String correttoUpdateAnime;
    
    //messaggi di errore
    public String erroreUserPwdBlank;
    public String erroreUserFound;
    public String erroreUserNotFound;
    public String erroreAnimeNotSelected;
    public String erroreAnimeAggiunto;
    public String erroreUpdateAnime;
    public String erroreNoteLunghe;
    public String erroreScoreFormat;
    public String erroreScoreValue;
    public String erroreCaratteri;
}
