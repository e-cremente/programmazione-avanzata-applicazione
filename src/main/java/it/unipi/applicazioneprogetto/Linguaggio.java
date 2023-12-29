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
    //oggetti interfaccia grafica
    public String textTitle;
    public String textUsername;
    public String textPassword;
    public String buttonLogin;
    public String textRegister;
    public String buttonRegister;
    
    //messaggi di successo
    public String correttaRegistrazione;
    
    //messaggi di errore
    public String erroreUserPwdBlank;
    public String erroreUserFound;
}
