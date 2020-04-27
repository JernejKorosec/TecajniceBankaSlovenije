/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2.tools;

import java.text.DecimalFormat;

/**
 *
 * @author jerne
 */
public class MathTools {
    public static double str2Double(String number){
        double value = 0.0D;
        try 
        {
            value = Double.parseDouble( number.replace(",","."));    
        } 
        catch (Exception e) 
        {
            
        }
        return value;
    }
    public static float str2Float(String number){
        float value = 0.0f;
        try 
        {
            value = Float.parseFloat( number.replace(",","."));    
        } 
        catch (Exception e) 
        {
            
        }
        return value;
    }   
    
    public static String doubletoString(double number){
        String value = "0.0000";
        try 
        {
            value = String.valueOf(number);    
        } 
        catch (Exception e) 
        {
            
        }
        return value;
    }   
    
    public static double getOportunity(double dStartValue, double dEndValue){
        double dDeltaValue = 0.0D;
        DecimalFormat df = new DecimalFormat("#.00000"); 
        
        try{
            if(dEndValue != 0.0D)
            dDeltaValue = (dStartValue/dEndValue)-1;    
        }
        catch(Exception e){
            
        }
        return dDeltaValue;
    }
    
    public static String getOportunityFormatedString(double dValue){
        
        String result = "0.00000";
        
        try{
            result = String.format( "%.5f", dValue );
        }
        catch(Exception e){
            
        }
        return result;
    }
    
    public static String getCleanString(String string){
        String result = "";
        
        try{
            result = string.replace(" %", "");
        }
        catch(Exception e){
            
        }
        return result;
    }
    
    
    
    
    
}