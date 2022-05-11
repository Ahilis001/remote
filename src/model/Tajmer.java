/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import view.Pocetna;

/**
 *
 * @author iduras
 */
public class Tajmer {
    
    private String key = "mzZTuxP03gmKKROV0zbmn";
    
    private int minuta;
    private int sekunda;
    private int ukupno;
    private int ukupnoTrajanje;
    private String pocetak;
    private String kraj;
    private String naredba;

    public Tajmer(int minuta, int sekunda, String naredba) {
        this.minuta = minuta;
        this.sekunda = sekunda;
        this.ukupno = minuta * 60 + sekunda;
        this.ukupnoTrajanje = this.ukupno;
        this.pocetak = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy.").format(Calendar.getInstance().getTime());
        this.naredba = naredba;
    }
    
    public int smanjiTajmer(){
        
        if(this.ukupno > 0){
            this.ukupno--;
        }
        
        return this.ukupno;
    }

    /**
     * izvršava naredbu
     */
    public void izvrsi(){
        
        String[] command = {"curl ", "-H " ,"POST ", "https://maker.ifttt.com/trigger/" + this.naredba + "/with/key/" + key};
//        String[] command = {"curl -H POST https://maker.ifttt.com/trigger/" + this.naredba + "/with/key/mzZTuxP03gmKKROV0zbmn"};
        ProcessBuilder process = new ProcessBuilder(command); 
        Process p;
        
        try
        {
            p = process.start();
             BufferedReader reader =  new BufferedReader(new InputStreamReader(p.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ( (line = reader.readLine()) != null) {
                        builder.append(line);
                        builder.append(System.getProperty("line.separator"));
                }
                String result = builder.toString();
                System.out.print(result);
                
                provjeraRezultata(result, 2);

        }
        catch (IOException e)
        {   
            provjeraRezultata("Nešto ne štima!", 0);
            e.printStackTrace();
        }
    }
    
    /**
     * popunjuje ili ažurira tabelu tajmera
     * @param tablicaPrikaza
     * @param listaListaTajmera
     */
    public static void popuniAzurirajTabelu(JTable tablicaPrikaza, List<List<Tajmer>> listaListaTajmera){
        //resetiranje modela tablice prikaza
        tablicaPrikaza.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Događaj", "Minuta", "Sekunda"
            }
        ));
        DefaultTableModel model = (DefaultTableModel) tablicaPrikaza.getModel();
        
        Object podaciReda[] = new Object[3];
        for (List<Tajmer> listaTajmera : listaListaTajmera) {
            for (Tajmer tajmer : listaTajmera) {
                podaciReda[0] = tajmer.getNaredba();
                podaciReda[1] = tajmer.getUkupno() / 60;
                podaciReda[2] = tajmer.getUkupno() % 60;
                model.addRow(podaciReda);
            }
        }
    }
    
    /**
     * dodavanje novog zapisa u log
     * @param tablicaLoga
     */
    public void dodajULog(JTable tablicaLoga){
        DefaultTableModel model = (DefaultTableModel) tablicaLoga.getModel();
        
        Object podaciReda[] = new Object[4];
        
        podaciReda[0] = this.getNaredba();
        podaciReda[1] = this.getUkupnoTrajanje();
        podaciReda[2] = this.getPocetak();
        podaciReda[3] = this.getKraj();
        model.addRow(podaciReda);
    }
    
    /**
     * obzirom na proslijeđenu poruku, mijenja poruku u 
     * tekstualnom okviru za poruke
     * @param poruka poruka 
     * @param dobroLose predstavlja boju teksta poruke - 0 - crvena, 1 - zelena, 2 - crna
     */
    public void provjeraRezultata(String poruka, int dobroLose){
        
        JTextPane jTextPoruka = Pocetna.jTextPoruka;
        
        if(poruka.contains("Congratulations")){
            jTextPoruka.setForeground(Color.GREEN);
            jTextPoruka.setText("Uspješno izvršeno!");
        }
        
        else if(dobroLose == 2){
           jTextPoruka.setForeground(Color.BLACK);
           jTextPoruka.setText(poruka); 
        }
        
        else if(dobroLose == 1){
           jTextPoruka.setForeground(Color.GREEN);
           jTextPoruka.setText(poruka); 
        }
        
        else if(dobroLose == 0){
            jTextPoruka.setForeground(Color.RED);
            jTextPoruka.setText(poruka); 
        }
        
        else{
            jTextPoruka.setForeground(Color.RED);
            jTextPoruka.setText("Nešto ne štima!");
        }
    }
    
    public String getNaredba() {
        return naredba;
    }

    public int getUkupno() {
        return ukupno;
    }

    public void setNaredba(String naredba) {
        this.naredba = naredba;
    }

    public int getUkupnoTrajanje() {
        return ukupnoTrajanje;
    }

    public String getPocetak() {
        return pocetak;
    }

    public String getKraj() {
        return kraj;
    }

    public void setKraj(String kraj) {
        this.kraj = kraj;
    }
    
    
}
