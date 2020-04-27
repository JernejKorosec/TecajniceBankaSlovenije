/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jerne
 */
public class DateTools {
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    public static boolean isNewDday(Date oldDate, Date newDate) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        if (newDate.compareTo(oldDate) > 0) 
        {
            //System.out.println("newDate is after oldDate");
            return true;
        }
        else
        {
            return false;
        }
        
    }
    
    public static boolean isNewDay(String oldDate, String newDate) throws ParseException{
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(newDate);
        Date date2 = sdf.parse(oldDate);
        
        if (date1.compareTo(date2) > 0) {
            //System.out.println("newDate is after oldDate");
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static Date str2Date(String date){
        try {
            Date datedate = sdf.parse(date);
            return datedate;
        } catch (ParseException ex) {
            Logger.getLogger(DateTools.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static String date2String(Date d){
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  // mm je bnlo prej z malo
                String strDate = dateFormat.format(d);  
            return strDate;    
    }

    
    public static long daysBetween(Date one, Date two) 
    { 
        long difference = (one.getTime()-two.getTime())/86400000; 
        return Math.abs(difference); 
    }


    
}