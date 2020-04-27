/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2.tools;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import javafx.scene.control.TextArea;
import javax.swing.JTextArea;

/**
 *
 * @author jerne
 */
public class Timing {
    
    private static long startTime;
    private static long endTime;
    private static final String linestr = "------------------------------";
    private static final String zacetekstr = "Začetek izvajanja: ";
    private static final String konecstr = "Konec Izvajanja: ";
    private static final String casstr = "Čas Izvajanja  : ";
    
    
    public static void StartTime(){
        startTime = System.nanoTime();
        System.out.println(linestr);
        System.out.println(zacetekstr + currTime());
    }
    
     public static void StartTime(TextArea jta){
        startTime = System.nanoTime();

        // Print to console
        System.out.println(linestr);
        System.out.println(zacetekstr + currTime());
        
        //print to JTextArea
        if(jta != null)
        jta.appendText(linestr + "\n" + zacetekstr + currTime() + "\n");
        
        
    }
     
     public static void StartTime(String functionName){
        startTime = System.nanoTime();
        System.out.println(linestr);
        System.out.println(functionName);
        System.out.println(zacetekstr + currTime());
    }
    
     public static void StartTime(TextArea jta, String functionName){
        startTime = System.nanoTime();

        // Print to console
        System.out.println(linestr);
        System.out.println(functionName);
        System.out.println(linestr);
        System.out.println(zacetekstr + currTime());
        
        //print to JTextArea
        if(jta != null)
        jta.appendText(linestr + "\n" + functionName + "\n" + linestr + "\n" + zacetekstr + currTime() + "\n");
        
        
    }
    
    public static void EndTime(){
        endTime = System.nanoTime();
        long duration = (endTime - startTime);
        String str = String.format("%d minut, %d sekund", 
                TimeUnit.NANOSECONDS.toMinutes(duration),
                TimeUnit.NANOSECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(duration)));
        System.out.println(casstr + str);
        System.out.println(konecstr + currTime());
        System.out.println(linestr);
        
    }
    
    public static void EndTime(TextArea jta){
        endTime = System.nanoTime();
        long duration = (endTime - startTime);
        String str = String.format("%d minut, %d sekund", 
                TimeUnit.NANOSECONDS.toMinutes(duration),
                TimeUnit.NANOSECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(duration)));
        
        System.out.println(casstr + str);
        System.out.println(konecstr + currTime());
        System.out.println(linestr);
        if(jta != null)
        jta.appendText(casstr + str + "\n" + konecstr + currTime() + "\n" + linestr + "\n");
        
    }
    
    public static String  currTime(){
        LocalTime time = LocalTime.now(); 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return time.format(formatter);
    }
    
    
}
