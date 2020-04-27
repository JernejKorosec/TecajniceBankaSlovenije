/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafxapplication2.tools.*;
import javafxapplication2.xml2db.HttpToXmlLocal;
import javafxapplication2.xml2db.Tecaj;
import javafxapplication2.xml2db.TecajDTO;
import javafxapplication2.xml2db.Tecajnica;
import javafxapplication2.xml2db.Xml2db;

/**
 *
 * @author Jernej Korošec
 * 
 * @see www.programerskopodjetje.com
 * 
 * Funkcije ki povezujejo UI sloj s poslovno logiko
 * 
 */
public class guiFunctions {
    
    public void shraniXMLDatotekoLokalnoBtnClick(TextField jParseUrl, TextField jSavePath, TextArea jTextArea, String fileName){
        
        String ParseUrl = jParseUrl.getText();
        String savePath = jSavePath.getText();
        //String newLine =  "\n";//"\r\n";
        //String fullFileName = savePath + "/" + fileName;
        HttpToXmlLocal x2l = new HttpToXmlLocal();
        try {
            //x2l.getXML(savePath, fileName, XmlURL);
            x2l.getXML(savePath, fileName, ParseUrl,jTextArea);
        } catch (IOException ex) {
            Logger.getLogger(JavaFXApplication2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void shraniXmlvSQL(String XMLFilePath,String XMLFileName, String dbFilePath, String dbFileName, TextArea ta)
    {
        String xmlDatoteka = XMLFilePath + "/" + XMLFileName;
        List<Tecajnica> t = Xml2db.parseTecajnicaXML(xmlDatoteka);
        Xml2db.saveTecajniceToDBTrans(xmlDatoteka,dbFilePath,dbFileName,ta );
    }
    
    public void naloziTecajniceIzSQL(String potDoDB, String imeDB, TextArea ta,TableView table, TableColumn[] columns)
    {
        List<Tecajnica> t = Xml2db.getTecajniceFromDB(potDoDB, imeDB, ta);
        HashMap<String, List<TecajDTO>> HMAPTecajiByOznaka = TecajnicaTools.createListsTecajevByOznaka(t);
        ObservableList<TecajDTO> debug2 = TecajnicaTools.izracunajOportunitete(HMAPTecajiByOznaka);
        for (TecajDTO tecajDTO : debug2) {
            String s = tecajDTO.oportunitetaProperty().getValue();
            if(s.contains("-"))
            {
                
            }
            tecajDTO.oportunitetaProperty().setValue(s + " %");
        }
        //table.getItems().clear();
        table.setItems(debug2);
    }
    void naloziTecajniceIzSQLPoObdobju(String potDoDB, String imeDB, TextArea ta,TableView table, TableColumn[] columns, DatePicker dt_prikaz_od, DatePicker dt_prikaz_do, ListView lv_prikaz_valuta) {
        try {
                java.sql.Date prikazOd = java.sql.Date.valueOf(dt_prikaz_od.getValue());
                java.sql.Date prikazDo = java.sql.Date.valueOf(dt_prikaz_do.getValue());
                if (prikazOd.after(prikazDo)) {
                    System.out.println("prikazOd is after prikazDo");
                    Date tempDate = new Date();
                    tempDate = prikazDo;
                    prikazOd = prikazDo;
                    prikazDo = prikazOd;
                }
                if (prikazOd.before(prikazDo)) {System.out.println("prikazOd is before prikazDo");}
                if (prikazOd.equals(prikazDo)) {System.out.println("prikazOd is equal prikazDo");}
                List<String> listaValut = (List<String>) lv_prikaz_valuta.getSelectionModel().getSelectedItems();
                List<Tecajnica> t = null;
                t = Xml2db.getTecajniceFromDB(potDoDB, imeDB, ta, prikazOd, prikazDo, listaValut);
             
                HashMap<String, List<TecajDTO>> HMAPTecajiByOznaka = TecajnicaTools.createListsTecajevByOznaka(t);
                ObservableList<TecajDTO> debug2 = TecajnicaTools.izracunajOportunitete(HMAPTecajiByOznaka);
                for (TecajDTO tecajDTO : debug2) {
                    String s = tecajDTO.oportunitetaProperty().getValue();
                    if(s.contains("-"))
                    {

                    }
                    tecajDTO.oportunitetaProperty().setValue(s + " %");
                }
                table.setItems(debug2);
        } 
        catch (Exception e) 
        {
        }
    }
    
    public String[] naloziValuteIzSQL(String DBFileLocation, String imeDB, TextArea txta_debugWindow, ListView lv,TextArea ta) {
        String[] valute = Xml2db.getValuteFromDB(DBFileLocation, imeDB,ta);
        return valute;
    }
    public void naloziGraf(
            String DBFileLocation, 
            String imeDB, 
            TextArea txta_debugWindow, 
            ListView lv,
            TextArea ta,
            AreaChart<Number,Number> gf_valute,
            NumberAxis gf_valute_os_y,
            NumberAxis gf_valute_os_x,
            DatePicker dt_prikaz_od, 
            DatePicker dt_prikaz_do, 
            ListView lv_prikaz_valuta
            )
    {
        
        List<Tecajnica> listaTecajnicTop = null;
        List<String> listaValut = (List<String>) lv_prikaz_valuta.getSelectionModel().getSelectedItems();
        int debugExceptionError = 0;
        java.sql.Date prikazOd = null;
        java.sql.Date prikazDo = null;
        try {
                    prikazOd = java.sql.Date.valueOf(dt_prikaz_od.getValue());
                    prikazDo = java.sql.Date.valueOf(dt_prikaz_do.getValue());

                if (prikazOd.after(prikazDo)) {
                    System.out.println("prikazOd is after prikazDo");
                    Date tempDate = new Date();
                    tempDate = prikazDo;
                    prikazOd = prikazDo;
                    prikazDo = prikazOd;

                }
                if (prikazOd.before(prikazDo)) {
                    System.out.println("prikazOd is before prikazDo");
                }

                if (prikazOd.equals(prikazDo)) {
                    System.out.println("prikazOd is equal prikazDo");
                }

                listaTecajnicTop = Xml2db.getTecajniceFromDB(DBFileLocation, imeDB, ta, prikazOd, prikazDo, listaValut);
        
                long stdni = 31;
                if(prikazOd!=null && prikazDo!=null)
                stdni = javafxapplication2.tools.DateTools.daysBetween(prikazOd, prikazDo);
                gf_valute_os_x = new NumberAxis(1, stdni, 1);
                gf_valute.setTitle("Graf valut/oportunitetnih zaslužkov");
                
                gf_valute.getData().clear();
                for (String strOznakaValute : listaValut) {
                   
                        List<TecajDTO> serijaZaValuto = getTecajiForValuta(strOznakaValute, listaTecajnicTop);
                        if((serijaZaValuto != null) && (serijaZaValuto.size()>1)){
                                XYChart.Series serija= new XYChart.Series();
                                int i = 1;
                                for (TecajDTO tecajDTO : serijaZaValuto) {
                                    serija.setName(strOznakaValute);
                                    String valutaStr = tecajDTO.valutaProperty().getValue().toString();
                                    float fValue = 0.0f;
                                    try
                                    {
                                    fValue = Float.parseFloat(valutaStr+"f");
                                    }
                                    catch(Exception ex){
                                        String s = ex.getMessage().toString();
                                    }
                                    serija.getData().add(new XYChart.Data(i, fValue));
                                    i++;
                                }
                                gf_valute.getData().addAll(serija);
                        }
                }
        } 
        catch (Exception e) 
        {
            debugExceptionError++;
        }
    }

    public List<TecajDTO> getTecajiForValuta(String pomembnaValuta, List<Tecajnica> vseValuteList){
        if(vseValuteList != null){
        List<Tecajnica> LTForvaluta = new ArrayList<Tecajnica>();
        List<TecajDTO> LTForvalutaDTO = new ArrayList<TecajDTO>();
        for (Tecajnica tecajnica : vseValuteList) {
            List<Tecaj> listaTecajev = tecajnica.getListaTecajev();
            String datum = tecajnica.getDatum().toString();
            for (Tecaj tecaj : listaTecajev) {
                String oznaka = tecaj.getOznaka();
                String sifra = tecaj.getSifra();
                String valuta = tecaj.getVrednoststring();
                TecajDTO e = new TecajDTO(datum, oznaka, sifra, valuta,null);
                if(pomembnaValuta.equalsIgnoreCase(oznaka)){
                LTForvalutaDTO.add(e);
                }
            }
        }
        return LTForvalutaDTO;
        }
        else return null;
    }    
}