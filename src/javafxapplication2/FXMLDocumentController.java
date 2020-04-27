/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafxapplication2.dal.Connect;
import javafxapplication2.tools.MathTools;
import javafxapplication2.xml2db.TecajDTO;

/**
 *
 * @author jerne
 */
public class FXMLDocumentController implements Initializable {
 
    private guiFunctions gf;
    private Connect con;
    private static final String imeDB = "baza.db";
    private static final String urlNaslov = "https://www.bsi.si/_data/tecajnice/dtecbs-l.xml";
    private static final String imeXML = "dtecbs-l.xml";
    private static boolean selectTradeRange = false;
    
    @FXML
    private TextField txt_URLSpletnegaVira;

    @FXML
    private TextField txt_XMLLocation;

    @FXML
    private TextField txt_XMLFileName;

    @FXML
    private TextField txt_DBFileLocation;
    
    @FXML
    private TextArea txta_debugWindow;

    @FXML
    private Button btn_shraniXML;

    @FXML
    private Button btn_pretvoriXMLvSQL;

    @FXML
    private Button btn_loadFromDB;
    
    @FXML
    private TableView<TecajDTO> tv_tabela;
    @FXML
    private TableColumn<TecajDTO, String> tv_tabela_datum;
    
    @FXML
    private TableColumn<TecajDTO, String> tv_tabela_oznaka;
    
    @FXML
    private TableColumn<TecajDTO, String> tv_tabela_sifra;
    
    @FXML
    private TableColumn<TecajDTO, String> tv_tabela_valuta;
    
    @FXML
    private TableColumn<TecajDTO, String> tv_tabela_oportuniteta;
    
    
    @FXML
    private CheckBox chk_izbira_obdobja;

    @FXML
    private DatePicker dt_prikaz_od;

    @FXML
    private DatePicker dt_prikaz_do;
                
    @FXML
    private ListView lv_prikaz_valuta;
    
    @FXML
    private AreaChart<Number,Number> gf_valute;
    
    @FXML
    private NumberAxis gf_valute_os_y;
            
    @FXML
    private NumberAxis gf_valute_os_x;
    
    @FXML
    private TextField txt_zacetniVlozek;
    
    @FXML
    private TextField txt_zasluzekVProcentih;
    
    @FXML
    private TextField txt_zasluzekVEvrih;
            
    @FXML
    private TextField txt_izplacilo;
    
    @FXML
    private Button btn_Izracunaj;
    
    @FXML
    private Label gf_trgovanje_od;
    
    @FXML
    private Label gf_trgovanje_do;
    
    
    // Event handlerss
    EventHandler<ActionEvent> btn_shraniXML_event = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event) {
        String imeDatoteke = txt_XMLFileName.getText();
        String savePath = txt_XMLLocation.getText();
        String url = txt_URLSpletnegaVira.getText();
        gf.shraniXMLDatotekoLokalnoBtnClick(txt_URLSpletnegaVira,txt_XMLLocation,txta_debugWindow,imeDatoteke);
                //jTextField1,jTextField2,jTextArea1,imeDatoteke);
        }
        
    };
    
    EventHandler<ActionEvent> btn_pretvoriXMLvSQL_event = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event) {
            String imeDatoteke      = txt_XMLFileName.getText();
            String XMLSavePath      = txt_XMLLocation.getText();
            String url              = txt_URLSpletnegaVira.getText();
            String DBFileLocation   = txt_DBFileLocation.getText();
            
        
            gf.shraniXmlvSQL(XMLSavePath,imeXML,DBFileLocation,imeDB,txta_debugWindow);
            try {
                naloziValutevListView(lv_prikaz_valuta);    
            } catch (Exception e) {
            }
        }
        
    };
     
    EventHandler<ActionEvent> btn_loadFromDB_event = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event) {
            String imeDatoteke      = txt_XMLFileName.getText();
            String XMLSavePath      = txt_XMLLocation.getText();
            String url              = txt_URLSpletnegaVira.getText();
            String DBFileLocation   = txt_DBFileLocation.getText();
            //btn_loadFromDB.
            TableColumn[] tcols = new TableColumn[5];
            tcols[0] = tv_tabela_datum;
            tcols[1] = tv_tabela_oznaka;
            tcols[2] = tv_tabela_sifra;
            tcols[3] = tv_tabela_valuta;
            tcols[4] = tv_tabela_oportuniteta;
            
            if(!selectTradeRange){
                gf.naloziTecajniceIzSQL(DBFileLocation,imeDB,txta_debugWindow,tv_tabela,tcols);
            }
            else
            {
                gf.naloziTecajniceIzSQLPoObdobju(DBFileLocation,imeDB,txta_debugWindow,tv_tabela,tcols,dt_prikaz_od,dt_prikaz_do,lv_prikaz_valuta);
                int velikost = tv_tabela.getItems().size();
                ObservableList<TecajDTO> olTecajDTO = tv_tabela.getItems();
                int velikost2 = lv_prikaz_valuta.getSelectionModel().getSelectedItems().size();
                ObservableList<String> oznake = lv_prikaz_valuta.getSelectionModel().getSelectedItems();
                String[] strOznake = new String[2];
                for(int i = 0; i <2; i++)
                {
                    strOznake[i] = oznake.get(i);
                }
                if((velikost>4) && (velikost2 == 2))
                {
                    btn_Izracunaj.setDisable(false);
                    ArrayList<TecajDTO> ALTecajDTOFirst = new ArrayList<>();
                    ArrayList<TecajDTO> ALTecajDTOSecond = new ArrayList<>();
                    
                    String secondValutaValue =  "";
                    
                    double seštevek = 0.0D;
                    double kolicnik = 1;
                    for (TecajDTO tecajDTO : olTecajDTO) {
                        
                        String firstValutaValue =  tecajDTO.oportunitetaProperty().getValue();
                        
                        double firstD = MathTools.str2Double(MathTools.getCleanString(firstValutaValue));
                        double secondD = MathTools.str2Double(MathTools.getCleanString(secondValutaValue));
                        
                        
                        if((firstD > 0 || secondD > 0)){
                            if(firstD > secondD)
                            {
                                kolicnik = kolicnik + (kolicnik*firstD);
                            }else if(secondD > firstD){
                                kolicnik = kolicnik + (kolicnik*secondD);
                            }
                            
                        }
                        
                        /*
                        String oznakaV = tecajDTO.oznakaProperty().getValue();
                        if(oznakaV.equals(strOznake[0]))
                        {
                            ALTecajDTOFirst.add(tecajDTO);
                        }
                        if(oznakaV.equals(strOznake[1]))
                        {
                            ALTecajDTOSecond.add(tecajDTO);
                        }
                        */
                        
                        secondValutaValue = firstValutaValue;
                        
                        
                    }

                    int stopmenot = 0;
                    txt_zasluzekVProcentih.setText((kolicnik-1) + "%");
                    izracunOportunitete();
                    
                }
                else
                {
                    btn_Izracunaj.setDisable(true);
                }
            }
            showGraph();
        }
    };

    EventHandler chk_izbira_obdobja_event = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() instanceof CheckBox) {
                CheckBox chk = (CheckBox) event.getSource();
                System.out.println("Action performed on checkbox " + chk.getText());
                System.out.println("Is Selected? " + chk.isSelected());

                if(chk.isSelected()){
                    hideSelectTradeRange(false);
                }
                else{
                    hideSelectTradeRange(true);
                }
            }
        }
    };
    
    EventHandler btn_Izracunaj_event = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            izracunOportunitete();
            //System.out.println("Gumb izračun oportunitenih zaslužkov deluje...");
        }
    };
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setStartTextFieldValues();
        // Seting event handlers for buttons
        
        btn_shraniXML.setOnAction(btn_shraniXML_event);
        btn_loadFromDB.setOnAction(btn_loadFromDB_event);
        btn_pretvoriXMLvSQL.setOnAction(btn_pretvoriXMLvSQL_event);
        btn_Izracunaj.setOnAction(btn_Izracunaj_event);
        chk_izbira_obdobja.setOnAction(chk_izbira_obdobja_event);
        
        
        gf = new guiFunctions();
        con = new Connect();
        
        tv_tabela_datum.setMinWidth(100);
        tv_tabela_datum.setCellValueFactory(new PropertyValueFactory<TecajDTO, String>("datum"));
        tv_tabela_oznaka.setMinWidth(100);
        tv_tabela_oznaka.setCellValueFactory(new PropertyValueFactory<TecajDTO, String>("oznaka"));
        tv_tabela_sifra.setMinWidth(75);
        tv_tabela_sifra.setCellValueFactory(new PropertyValueFactory<TecajDTO, String>("sifra"));
        tv_tabela_valuta.setMinWidth(75);
        tv_tabela_valuta.setCellValueFactory(new PropertyValueFactory<TecajDTO, String>("valuta"));
        tv_tabela_oportuniteta.setMinWidth(150);
        tv_tabela_oportuniteta.setCellValueFactory(new PropertyValueFactory<TecajDTO, String>("oportuniteta"));
        
        
        
        //private TableColumn<TecajDTO, String> tv_tabela_oportuniteta;
        tv_tabela_oportuniteta.setCellFactory( 
                column -> {
                    return new TableCell<TecajDTO, String>(){
                        @Override
                        protected void updateItem(String item, boolean empty) {
                             //super.updateItem(item, empty);
                             
                              if (item == null || empty) {
                                    setText(null);
                                    setStyle("");
                                } 
                              else {
                                    if(item.contains("0.00000 %"))
                                    {
                                        setTextFill(Color.BLACK);
                                        setStyle("");
                                    }else{
                                        if(item.contains("-")){
                                            //setTextFill(Color.RED);
                                            setTextFill(Color.BLACK);
                                            //setStyle("-fx-background-color: red");
                                            setStyle("-fx-background-color: #ff0000");
                                        }else{
                                            setTextFill(Color.BLACK);
                                            //setStyle("");
                                            setStyle("-fx-background-color: #00ff00");
                                        }
                                    }
                                    setText(item);
                                }
                             
                        }
                    };
            }
        );
        naloziValutevListView(lv_prikaz_valuta);
        
        lv_prikaz_valuta.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        LocalDate zdele = LocalDate.now();
        LocalDate nDniNazaj = zdele.minus(Period.ofDays(31));
        dt_prikaz_do.setValue(zdele);
        dt_prikaz_od.setValue(nDniNazaj);
        
        hideSelectTradeRange(true);
        
        txt_zacetniVlozek.setText("1000");
        txt_zasluzekVProcentih.setText("0.000%");
        txt_zasluzekVEvrih.setText("0.000€");
        txt_izplacilo.setText("0€");
        txt_zasluzekVProcentih.setDisable(true);
        txt_zasluzekVEvrih.setDisable(true);
        txt_izplacilo.setDisable(true);
        btn_Izracunaj.setDisable(true);
        izracunOportunitete();
        showGraph();
        
        
    }    
    
    private void setStartTextFieldValues(){
        String currFolder = System.getProperty("user.dir") + "/";
        txt_URLSpletnegaVira.setText(urlNaslov);
        txt_XMLLocation.setText(currFolder);
        txt_XMLFileName.setText(imeXML);
        txt_DBFileLocation.setText(currFolder);
    }
    
    private void hideSelectTradeRange(boolean hide){
        //chk_izbira_obdobja.setDisable(hide);
        dt_prikaz_od.setDisable(hide);
        dt_prikaz_do.setDisable(hide);
        lv_prikaz_valuta.setDisable(hide);
        selectTradeRange = !hide;
    }
    
    private void naloziValutevListView(ListView lv){
        
        String imeDatoteke      = txt_XMLFileName.getText();
        String XMLSavePath      = txt_XMLLocation.getText();
        String url              = txt_URLSpletnegaVira.getText();
        String DBFileLocation   = txt_DBFileLocation.getText();
            
        
        String[] izbraneValue = gf.naloziValuteIzSQL(DBFileLocation,imeDB,txta_debugWindow,lv,txta_debugWindow);
        
        
        for (String valutaStr : izbraneValue) {
              lv.getItems().add(valutaStr);  ////    lv_prikaz_valuta.
            }
          
    }
 
    private void showGraph(){
        
        gf.naloziGraf(
                urlNaslov, 
                imeDB, 
                txta_debugWindow, 
                lv_prikaz_valuta, 
                txta_debugWindow, 
                gf_valute, 
                gf_valute_os_y, 
                gf_valute_os_x,
                dt_prikaz_od, 
                dt_prikaz_do, 
                lv_prikaz_valuta
        );
        
        java.sql.Date prikazOd = null;
        java.sql.Date prikazDo = null;
        
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        String prikazOdStr = "Začetek trgovanja";
        String prikazDoStr = "Konec Trgovanja";
        
        try {
                    prikazOd = java.sql.Date.valueOf(dt_prikaz_od.getValue());
                    prikazDo = java.sql.Date.valueOf(dt_prikaz_do.getValue());
                    prikazOdStr = formatDate.format(prikazOd);
                    prikazDoStr = formatDate.format(prikazDo);
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        
        gf_trgovanje_od.setText(prikazOdStr);
        gf_trgovanje_do.setText(prikazDoStr);
        
        
    }

    private void izracunOportunitete(){
    
    Double fzacetniVlozek = 0.0D;
    Double fzasluzekVProcentih = 0.0D;
    Double fzasluzekVEvrih = 0.0D;
    Double fizplacilo = 0.0D;
    
    String str_zacetniVlozek = txt_zacetniVlozek.getText();
    String str_zasluzekVProcentih = txt_zasluzekVProcentih.getText();
    String str_zasluzekVEvrih = txt_zasluzekVEvrih.getText();
    String str_izplacilo = txt_izplacilo.getText();
    
    String rgxRemoveStrings = " %€";
    str_zacetniVlozek = str_zacetniVlozek.replace(".", "");
    str_zacetniVlozek = str_zacetniVlozek.replace(",00", "D");
    str_zacetniVlozek       = rmvChars(str_zacetniVlozek,rgxRemoveStrings);
    str_zasluzekVProcentih  = rmvChars(str_zasluzekVProcentih,rgxRemoveStrings);
    //str_zasluzekVEvrih      = rmvChars(str_zasluzekVEvrih,rgxRemoveStrings);
    //str_izplacilo           = rmvChars(str_izplacilo,rgxRemoveStrings);
        try{
            fzacetniVlozek = Double.parseDouble(str_zacetniVlozek);
            fzasluzekVProcentih = Double.parseDouble(str_zasluzekVProcentih);
            //fzasluzekVEvrih = Double.parseDouble(str_zasluzekVEvrih);
            //fizplacilo = Double.parseDouble(str_izplacilo);
        }
        catch(Exception e){
                System.out.println("e:"+e.getMessage());
        }
        fzasluzekVEvrih = fzacetniVlozek * fzasluzekVProcentih;
        fizplacilo = fzasluzekVEvrih + fzacetniVlozek;
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        str_zacetniVlozek       = format.format(fzacetniVlozek);
        str_zasluzekVProcentih  = fzasluzekVProcentih.toString();
        str_zasluzekVEvrih      = format.format(fzasluzekVEvrih);
        str_izplacilo           = format.format(fizplacilo);
        txt_zacetniVlozek.setText(str_zacetniVlozek);
        txt_zasluzekVProcentih.setText(str_zasluzekVProcentih);
        txt_zasluzekVEvrih.setText(str_zasluzekVEvrih);
        txt_izplacilo.setText(str_izplacilo);
    }
    public String rmvChars(String input,String regx)
    {
        char[] ca = regx.toCharArray();
        for (char c : ca) {
            input = input.replace("" + c, ""); 
        }
        return input;
    }
}