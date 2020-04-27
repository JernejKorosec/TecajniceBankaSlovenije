/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2.xml2db;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author jerne
 */
public class TecajDTO {
    private final SimpleStringProperty datum;
    private final SimpleStringProperty oznaka;
    private final SimpleStringProperty sifra;
    private final SimpleStringProperty valuta;
    private final SimpleStringProperty oportuniteta;
    
    public TecajDTO(String datum1,String oznaka1, String sifra1, String valuta1, String oportuniteta1){
        this.datum = new SimpleStringProperty(datum1);
        this.oznaka = new SimpleStringProperty(oznaka1);
        this.sifra = new SimpleStringProperty(sifra1);
        this.valuta = new SimpleStringProperty(valuta1);
        this.oportuniteta = new SimpleStringProperty(oportuniteta1);
    }
    
    public TecajDTO(){
      this(null, null, null, null,null);
    };
                
    public void setDatum(String datum1) {
        this.datum.set(datum1);
    }
    
    public void getDatum() {
        this.datum.get();
    }
    
    public StringProperty datumProperty() {
        return datum;
    }
    
    public void setOznaka(String oznaka1) {
        this.oznaka.set(oznaka1);
    }
    
    public void getOznaka() {
        this.oznaka.get();
    }
    
    public StringProperty oznakaProperty() {
        return oznaka;
    }
    
    public void setSifra(String sifra1) {
        this.sifra.set(sifra1);
    }
    
    public void getSifra() {
        this.sifra.get();
    }
    
    public StringProperty sifraProperty() {
        return sifra;
    }
    
    public void setValuta(String valuta1) {
        this.valuta.set(valuta1);
    }
    
    public void getValuta() {
        this.valuta.get();
    }
    
    public StringProperty valutaProperty() {
        return valuta;
    }
    
     public void setOportuniteta(String oportuniteta1) {
        this.oportuniteta.set(oportuniteta1);
    }
    
    public void getOportuniteta() {
        this.oportuniteta.get();
    }
    
    public StringProperty oportunitetaProperty() {
        return oportuniteta;
    }
                
                
}
