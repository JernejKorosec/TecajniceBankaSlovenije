/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2.xml2db;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import javafx.scene.control.TextArea;
import javax.swing.JTextArea;

/**
 *
 * @author jerne
 */
public class HttpToXmlLocal {
    // Prebere zadeve s spletnega naslova
     public String XmlURL2 = "http://www.bsi.si/_data/tecajnice/dtecbs-l.xml";
     public String XmlURL = "https://www.bsi.si/_data/tecajnice/dtecbs-l.xml";
     
    
    

    public void getXML(String workingDirectory, String fileName, String parseURL, TextArea jta) throws FileNotFoundException, IOException{
        javafxapplication2.tools.Timing.StartTime(jta, "getXML()");
        String savePath = workingDirectory + "/" + fileName;
        URL website = new URL(parseURL);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(savePath);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        javafxapplication2.tools.Timing.EndTime(jta);
    }
    public String getXML() throws FileNotFoundException, IOException{
        
        
        URL website = new URL(XmlURL);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream("dtecbs-l.xml");
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        return "";
    }
}