package javafxapplication2.xml2db;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


import org.w3c.dom.Node;
import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafxapplication2.dal.Connect;
import static javafxapplication2.dal.Connect.loadTecajniceFromDB;
import javax.swing.JTextArea;
import javafxapplication2.tools.Timing;

public class Xml2db {

    public static String[] getValuteFromDB(String DBFileLocation, String imeDB,TextArea jta) 
    {
        
        Timing.StartTime(jta,"getValuteFromDB()");
        
        //List<Tecajnica> tecajniceXML = parseTecajnicaXML(potDoXML);
        //Connect.createAndConnect(imeDB,potDoDB);
        Connect.connect(imeDB, DBFileLocation);
        //int stVnosov = Connect.insertMultipleWithTransaction(tecajniceXML);
        String[] strArr = Connect.getValuteFromDataBase(imeDB, DBFileLocation);
        
        Timing.EndTime(jta);
        return strArr;
    }
    public List<Tecajnica> tecajnica;
    public void setTecajnica(List<Tecajnica> tecajnica) {
		this.tecajnica = tecajnica;
	}
    public List<Tecajnica> getTecajnica() {
	return tecajnica;
	}
    public static List<Tecajnica> parseTecajnicaXML(String potDoXML){
		List<Tecajnica> tecajnicaTemp = new ArrayList<Tecajnica>();
		try {
			File fXmlFile = new File(potDoXML);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("tecajnica"); 
			
				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node topNode = nList.item(temp);
					String datumTecajnice = topNode.getAttributes().item(0).getTextContent().toString();
					Tecajnica t = new Tecajnica();
					Date d = t.str2Date(datumTecajnice);
					List<Tecaj> lt = new ArrayList<Tecaj>();
					t.setDatum(d);
					NodeList nodeList = topNode.getChildNodes();
				    for (int i = 0; i < nodeList.getLength(); i++) {
				    	 Node currentNode = nodeList.item(i);
				         if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				        	 String imeNodea = currentNode.getNodeName().toString();
				        	 String oznaka = currentNode.getAttributes().getNamedItem("oznaka").getTextContent().toString();
				        	 String sifra = currentNode.getAttributes().getNamedItem("sifra").getTextContent().toString();
				        	 String vrednost = currentNode.getTextContent().toString();
				        	 Tecaj te = new Tecaj(oznaka, sifra, vrednost);
				        	 lt.add(te);
				         }
				    }
				    t.setListaTecajev(lt);
				    tecajnicaTemp.add(t);
				}
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
			return tecajnicaTemp;
	}
    public static void printTecajnice(List<Tecajnica> listaTecajnic) {
		for (Tecajnica tecajnica : listaTecajnic) {
			Date dt = tecajnica.getDatum();
			System.out.println(dt);
			List<Tecaj> listaTecajev = tecajnica.getListaTecajev();
			for (Tecaj tecaj : listaTecajev) {
				String oznaka = tecaj.getOznaka().toString();
				String sifra  = tecaj.getSifra().toString();
				String vrednost = tecaj.getVrednoststring().toString();
				System.out.println("Oznaka/Sifra/Vrednost = " + oznaka + "/"+ sifra + "/"+ vrednost + "/");
			}
		}
	}
    public static void saveTecajniceToDB(String potDoXML, String potDoDB,String imeDB){
            long startTime = System.nanoTime();
            
            List<Tecajnica> tecajniceXML = parseTecajnicaXML(potDoXML);
            
            Format formatter = new SimpleDateFormat("yyyy-MM-dd"); 
            Connect.createAndConnect(imeDB,potDoDB);
            
          
            
            for (Tecajnica tecajnica : tecajniceXML) {
            
                Date d = tecajnica.getDatum();
                String dateString = formatter.format(d);
                //System.out.println(dateString); // Izpis je ok // Leto-mesec-dan
                tecajnica.getListaTecajev().forEach((tecaj) -> {
                    String oznaka = tecaj.getOznaka();
                    String sifra  = tecaj.getSifra();
                    String vrednost = tecaj.getVrednoststring();
                            //System.out.println("datum/Oznaka/Sifra/Vrednost = " + dateString + "/" + oznaka + "/" + sifra + "/"+ vrednost + "/");
                            //Connect.insert("2007-01-01","USD","840","1.3170");
                    Connect.insert(dateString,oznaka,sifra,vrednost);
                }); 
                
                
            }
            
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Xml2db.class.getName()).log(Level.SEVERE, null, ex);
            }
                    
            
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            double elapsedTimeInSecond = (double) duration / 1000000000;
            
            long duration2 = (endTime - startTime);
            
            
            String str = String.format("%d min, %d sec", 
                    TimeUnit.NANOSECONDS.toMinutes(duration2),
                    TimeUnit.NANOSECONDS.toSeconds(duration2) - TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(duration2)));
            
            
            System.out.println("Uspešno zapisaon v bazo");
            System.out.println("Čas trajanja vnosa: " + str);
            
            
        }
    public static void saveTecajniceToDBTrans(String potDoXML, String potDoDB,String imeDB, TextArea jta) {
        Timing.StartTime(jta,"saveTecajniceToDBTrans()");
        List<Tecajnica> tecajniceXML = parseTecajnicaXML(potDoXML);
        Connect.createAndConnect(imeDB,potDoDB);
        int stVnosov = Connect.insertMultipleWithTransaction(tecajniceXML);
        String strStVnosov = "Število vnosov v bazo: " + stVnosov;
        System.out.println(strStVnosov);
        jta.appendText(strStVnosov + "\n");
        Timing.EndTime(jta);
    }       
    public static List<Tecajnica> getTecajniceFromDB(String potDoDB, String imeDB, TextArea jta) {
        //t = Xml2db.getTecajniceFromDB(potDoDB, imeDB, ta, prikazOd, prikazDo, listaValut);
        Timing.StartTime(jta,"getTecajniceFromDB()");
        List<Tecajnica> lt = javafxapplication2.dal.Connect.loadTecajniceFromDB(imeDB,potDoDB);
        //printTecajnice(lt)
        Timing.EndTime(jta);;
        return lt;
    }
    public static List<Tecajnica> getTecajniceFromDB(String potDoDB, String imeDB, TextArea ta, java.sql.Date prikazOd, java.sql.Date prikazDo, List<String> listaValut) {
        Timing.StartTime(ta,"getTecajniceFromDB()");
        String odDateString = javafxapplication2.tools.DateTools.date2String(prikazOd);
        String doDateString = javafxapplication2.tools.DateTools.date2String(prikazDo);
        List<Tecajnica> lt = javafxapplication2.dal.Connect.loadTecajniceFromDB(imeDB,potDoDB,odDateString,doDateString,listaValut);
        //printTecajnice(lt)
        Timing.EndTime(ta);;
        return lt;
    }
    
}