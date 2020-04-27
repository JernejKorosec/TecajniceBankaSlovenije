/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2.tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafxapplication2.xml2db.Tecaj;
import javafxapplication2.xml2db.TecajDTO;
import javafxapplication2.xml2db.Tecajnica;
import javafxapplication2.xml2db.Xml2db;
import javafxapplication2.tools.*;

/**
 *
 * @author jerne
 */
public class TecajnicaTools {
    
    public static List<String> getListOfAllValutaOznake(ObservableList<TecajDTO> lTecajev){
        ArrayList<String> als = new ArrayList<String>();
        boolean zeObstaja = false;
        String oznakaValute = "";
        //for (Tecaj tecaj : lTecajev) {
        for (TecajDTO tecaj : lTecajev) {
             //oznakaValute = tecaj.getOznaka().toUpperCase();
             oznakaValute = tecaj.oznakaProperty().getValue().toUpperCase();
             zeObstaja = als.contains(oznakaValute);
            if(!zeObstaja){
                als.add(oznakaValute);
            }
        }
        return als;
    }
    public static List<String> getListOfAllValutaOznake(List<Tecaj> lTecajev){
        ArrayList<String> als = new ArrayList<String>();
        boolean zeObstaja = false;
        String oznakaValute = "";
        //for (Tecaj tecaj : lTecajev) {
        for (Tecaj tecaj : lTecajev) {
             //oznakaValute = tecaj.getOznaka().toUpperCase();
             oznakaValute = tecaj.getOznaka().toUpperCase();
             zeObstaja = als.contains(oznakaValute);
            if(!zeObstaja){
                als.add(oznakaValute);
            }
        }
        return als;
    }
    public static List<String> getListOfAllValutaOznake(List<Tecajnica> lTecajnic, boolean TypeErasureGenericProblem){
        ArrayList<String> als = new ArrayList<String>();
        boolean zeObstaja = false;
        String oznakaValute = "";
        for (Tecajnica tecajnica : lTecajnic) {
            List<Tecaj> lt = tecajnica.getListaTecajev();
            for (Tecaj tecaj : lt) {
                oznakaValute = tecaj.getOznaka().toUpperCase();
                zeObstaja = als.contains(oznakaValute);
                    if(!zeObstaja){
                        als.add(oznakaValute);
                    }
            }
        }
        return als;
    }
    public static HashMap<String, List<TecajDTO>> createListsTecajevByOznaka(List<Tecajnica> lTecajev){
        //HashMap<String, String> capitalCities = new HashMap<String, String>();
        //capitalCities.put("England", "London");
        HashMap<String, List<TecajDTO>> hMapStrTDTO = new HashMap<>();
        
        List<String> valOznake = getListOfAllValutaOznake(lTecajev,false);
        
        for (String oznValute : valOznake) 
        {
            List<TecajDTO> ltDTO = new ArrayList<TecajDTO>();
            hMapStrTDTO.put(oznValute, ltDTO);
        }
        
        for (Tecajnica tecajnica : lTecajev) {
            List<Tecaj> lTecaj = tecajnica.getListaTecajev();
            String strDatum = DateTools.date2String(tecajnica.getDatum());
            for (Tecaj tecaj : lTecaj) {
                String strOznakaValue = tecaj.getOznaka();
                List<TecajDTO> o = hMapStrTDTO.get(strOznakaValue);
                String strOznaka = tecaj.getOznaka();
                String strSifra = tecaj.getSifra();
                String strValuta =tecaj.getVrednoststring();
                TecajDTO tDTOtemp = new TecajDTO(strDatum, strOznaka, strSifra, strValuta, null);
                o.add(tDTOtemp);
                int testmenot=0;
            }
        }
        return hMapStrTDTO;
    }
    public static ObservableList<TecajDTO> izracunajOportunitete(ObservableList<TecajDTO> tecajDTO){
        ObservableList<TecajDTO> newTecajData = FXCollections.observableArrayList(); //vrne novo arraylisto
        String strStartValue = "";
        String strEndValue = "";
        String strDelta = "";
        double dStartValue = 0.0D;
        double dEndValue = 0.0D;
        double dDeltaValue = 0.0D;
        int i = 0;
        for (TecajDTO tecajDTO1 : tecajDTO) {
            
            strStartValue = tecajDTO1.valutaProperty().getValue();
            
            //if(i==0) strEndValue = strStartValue;
            
            dStartValue = MathTools.str2Double(strStartValue);
            dEndValue = MathTools.str2Double(strEndValue);
            dDeltaValue = MathTools.getOportunity(dStartValue, dEndValue);
            /*
            if(dDeltaValue > 2){
                int neki = 0;
                neki += 1;
            }
            */
            /*
            if(i==3270)
            {
                int neki = 0;
                neki += 1;
            }
            */
            strDelta = MathTools.getOportunityFormatedString(dDeltaValue);
            String datum = tecajDTO1.datumProperty().getValue();
            String oznaka = tecajDTO1.oznakaProperty().getValue();
            String sifra = tecajDTO1.sifraProperty().getValue();
            TecajDTO newTecajDTO = new TecajDTO(datum, oznaka,sifra,strStartValue,strDelta);
            newTecajData.add(newTecajDTO);
            strEndValue = strStartValue;
            i++;
        }
        return newTecajData;
    }
    public static List<TecajDTO> izracunajOportunitete(List<TecajDTO> tecajDTOList){
        List<TecajDTO> lTDTO = new ArrayList<>();
        String strStartValue = "";
        String strEndValue = "";
        String strDelta = "";
        double dStartValue = 0.0D;
        double dEndValue = 0.0D;
        double dDeltaValue = 0.0D;
        for (TecajDTO tecajDTO : tecajDTOList) {
            strStartValue = tecajDTO.valutaProperty().getValue();
            dStartValue = MathTools.str2Double(strStartValue);
            dEndValue = MathTools.str2Double(strEndValue);
            dDeltaValue = MathTools.getOportunity(dStartValue, dEndValue);
            strDelta = MathTools.getOportunityFormatedString(dDeltaValue);
            String datum = tecajDTO.datumProperty().getValue();
            String oznaka = tecajDTO.oznakaProperty().getValue();
            String sifra = tecajDTO.sifraProperty().getValue();
            TecajDTO newTecajDTO = new TecajDTO(datum, oznaka,sifra,strStartValue,strDelta);
            lTDTO.add(newTecajDTO);
            strEndValue = strStartValue;
        }
        return lTDTO;
    }    
    public static ObservableList<TecajDTO> izracunajOportunitete(HashMap<String, List<TecajDTO>> hashMapTecajDTO){
        ObservableList<TecajDTO> lTecajnic = FXCollections.observableArrayList();
        Iterator<String> itr = hashMapTecajDTO.keySet().iterator();
        while (itr.hasNext()) 
        {
                String key = itr.next();
                List<TecajDTO> listaTecajDTOs = hashMapTecajDTO.get(key);
                listaTecajDTOs = izracunajOportunitete(listaTecajDTOs);
                for (TecajDTO tecajDTO : listaTecajDTOs) 
                {
                    String strDatum = tecajDTO.datumProperty().getValue();
                    String strOportuniteta = tecajDTO.oportunitetaProperty().getValue();
                    String strOznaka = tecajDTO.oznakaProperty().getValue();
                    String strSifra = tecajDTO.sifraProperty().getValue();
                    String strValuta = tecajDTO.valutaProperty().getValue();
                    TecajDTO t = new TecajDTO(strDatum, strOznaka, strSifra, strValuta, strOportuniteta);
                    lTecajnic.add(t);
                }
        }
        return lTecajnic;
    }
}