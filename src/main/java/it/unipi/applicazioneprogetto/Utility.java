/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazioneprogetto;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author edoar
 */
public class Utility {
    
    public static String postJsonRequestStringAnswer(String urlString, String urlParametersString) throws Exception{
        //creo una connessione verso l'url, con i passaggi necessari per specificare che è POST
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        //I dati da passare sono json, quindi includo questa linea
        con.setRequestProperty("Content-Type", "application/json");

        //Attacco i parametri da passare all'url
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(urlParametersString);
        out.flush();
        out.close();

        //Mando la richiesta/Ricevo la risposta
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        
        //Ritorno al chiamante la risposta ricevuta
        return content.toString();
    }
    
    public static JsonElement postStringRequestJsonAnswer(String urlString, String urlParametersString) throws Exception{
        //faccio una richiesta POST 
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        //Attacco i parametri da passare all'url
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(urlParametersString);
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

        Gson gson = new Gson();

        JsonElement json = gson.fromJson(content.toString(), JsonElement.class);
        
        return json;
    }
    
    public static JsonElement postJsonRequestJsonAnswer(String urlString, String urlParametersString) throws Exception{
        //faccio una richiesta GET verso un metodo che mi restituisce tutto il contenuto della tabella anime
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        //Se i dati da passare fossero in json includerei questa linea
        con.setRequestProperty("Content-Type", "application/json");

        //Attacco i parametri da passare all'url
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(urlParametersString);
        out.flush();
        out.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuffer content = new StringBuffer();
        while((inputLine = in.readLine()) != null){
            content.append(inputLine);
        }
        in.close();

        Gson gson = new Gson();

        JsonElement json = gson.fromJson(content.toString(), JsonElement.class);

        return json;
    }
    
    public static JsonArray getRequestJsonAnswer(String urlString) throws Exception{
        //faccio una richiesta GET verso un metodo che mi restituisce tutto il contenuto della tabella anime
        URL url = new URL(urlString);
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
        
        return anime;
    }
    
}
