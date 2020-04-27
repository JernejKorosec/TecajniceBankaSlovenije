package javafxapplication2.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxapplication2.xml2db.Tecaj;
import javafxapplication2.xml2db.Tecajnica;
import javafxapplication2.tools.DateTools;
import jdk.nashorn.internal.objects.NativeArray;

public class Connect {
	public static Connection conn;
        
	public static ConnData cd;
        
	public static void createDB(String dbName) {
	       cd = new ConnData(dbName);
	}
        
        
        public static void createDB(String dbName,String dbPath) {
	       cd = new ConnData(dbName,dbPath);
	}
        
	public static void createAndConnect(String dbName) {
		conn = null;
		createDB(dbName);
                try 
                {
                    conn = DriverManager.getConnection(cd.getJdbcString());
                    System.out.println("Connection to SQLite has been established.");
                  //System.out.println("cd.getJdbcString(): " + cd.getJdbcString());
                    createNewTable();
                    System.out.println("Table created!");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } finally {
                    try {
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
	}
        
	public static void createAndConnect(String dbName, String dbPath) {
		conn = null;
		createDB(dbName,dbPath);
                try 
                {
                    conn = DriverManager.getConnection(cd.getJdbcString());
                    System.out.println("Connection to SQLite has been established.");
                  //System.out.println("cd.getJdbcString(): " + cd.getJdbcString());
                    createNewTable();
                    System.out.println("Table created!");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } finally {
                    try {
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
	}
	/*
	<tecajnica datum="2007-01-01">
	<tecaj oznaka="USD" sifra="840">1.3170</tecaj>
	*/
	public static void createNewTable() 
        {
        // SQL statement for creating a new table
        String deleteSQL = "DELETE FROM tecajnica; ";
        String dropSQL   = "DROP TABLE IF EXISTS tecajnica; ";
        String createSQL =   "CREATE TABLE IF NOT EXISTS tecajnica (\n"
                + "    id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "    datum TEXT NOT NULL,\n"
                + "    oznaka TEXT NOT NULL,\n"
                + "    sifra TEXT NOT NULL,\n"
                + "    vrednost TEXT NOT NULL\n"
                + ");";
        
        //String sql = dropSQL + createSQL;
        
            /*
            try (
            Connection conn = DriverManager.getConnection(cd.getJdbcString());
            Statement stmt1 = conn.createStatement()) {stmt1.execute(deleteSQL);} 
            catch (SQLException e) {System.out.println(e.getMessage());}
            */
            try (
            Connection conn = DriverManager.getConnection(cd.getJdbcString());
            Statement stmt2 = conn.createStatement()) {stmt2.execute(dropSQL);} 
            catch (SQLException e) {System.out.println(e.getMessage());}
            
            try (
            Connection conn = DriverManager.getConnection(cd.getJdbcString());
            Statement stmt3 = conn.createStatement()) {stmt3.execute(createSQL);} 
            catch (SQLException e) {System.out.println(e.getMessage());}
            
        }

	public static Connection connect() 
        {
        try {
            /*
                if(cd==null){
                    createDB(dbName,dbPath);
                }
            */
        	conn = DriverManager.getConnection(cd.getJdbcString());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
        }
        
        public static Connection connect(String dbName,String dbPath) 
        {
        try {
                if(cd==null){
                    createDB(dbName,dbPath);
                }
        	conn = DriverManager.getConnection(cd.getJdbcString());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
        }
        
	public static void insert(String datum, String oznaka, String sifra,String vrednost) 
        {
            String sql = "INSERT INTO tecajnica(id,datum,oznaka,sifra,vrednost) VALUES (NULL,?,?,?,?)";
        try (
			Connection conn = connect();
			PreparedStatement pstmt = conn.prepareStatement(sql)
            ) 
            {

                    pstmt.setString(1, datum);
                    pstmt.setString(2, oznaka);
                    pstmt.setString(3, sifra);
                    pstmt.setString(4, vrednost);
                pstmt.executeUpdate();
            } 
            catch (SQLException e) 
            {
                System.out.println(e.getMessage());
            }
        }
        
        public static int insertMultipleWithTransaction(List<Tecajnica> tecajnice)
        {
            
            int stVnosov=0;
            
            String sql = "INSERT INTO tecajnica(id,datum,oznaka,sifra,vrednost) VALUES (NULL,?,?,?,?)";
            try (Connection conn = connect();PreparedStatement pstmt = conn.prepareStatement(sql))
            {
                conn.setAutoCommit(false);
                Format formatter = new SimpleDateFormat("yyyy-MM-dd"); 
                for (Tecajnica tecajnica : tecajnice) 
                {
                    Date dt = tecajnica.getDatum();
                    String datum = formatter.format(dt);
                    for (Tecaj tecaj : tecajnica.getListaTecajev()) 
                    {
                        String oznaka = tecaj.getOznaka();
			String sifra  = tecaj.getSifra();
			String vrednost = tecaj.getVrednoststring();
                        // Loop Po 1000 z števcom.
                        pstmt.setString(1, datum);
                        pstmt.setString(2, oznaka);
                        pstmt.setString(3, sifra);
                        pstmt.setString(4, vrednost);
                        pstmt.addBatch();
                        stVnosov++;
                    }
                    pstmt.executeBatch();
                }
                //pstmt.executeBatch();
                conn.commit();
                conn.close();
                
            } 
            catch (SQLException e) {System.out.println(e.getMessage());}
            
            return stVnosov;
        }
        
        public static List<Tecajnica> loadTecajniceFromDB(String dbName, String dbPath){
            List<Tecajnica> listaTecajnic = new ArrayList<Tecajnica>();
            String sql = "SELECT id,datum,oznaka,sifra,vrednost FROM tecajnica";
            try (
                    Connection conn = connect(dbName,dbPath);
                    Statement stmt  = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)
                ){
                String datumStr=null;
                String datumStrTemp=null;
                Tecajnica tecajnica = new Tecajnica();
                List<Tecaj> listaTecajev = new ArrayList<Tecaj>();
                while (rs.next()) {
                    datumStrTemp = datumStr;
                    String idStr = rs.getString("id");
                    datumStr = rs.getString("datum");
                    String oznakaStr = rs.getString("oznaka");
                    String sifraStr = rs.getString("sifra");
                    String vrednostStr = rs.getString("vrednost");
                    if(datumStrTemp == null){
                        datumStrTemp = datumStr;
                        tecajnica.setDatum(DateTools.str2Date(datumStr));
                    } // v primeru da začnemo z zanko
                    Tecaj tecaj = new Tecaj(oznakaStr,sifraStr,vrednostStr);
                    try {
                        if(DateTools.isNewDay(datumStrTemp, datumStr)){
                            tecajnica.setListaTecajev(listaTecajev);
                            listaTecajnic.add(tecajnica);
                            tecajnica = new Tecajnica();
                            tecajnica.setDatum(DateTools.str2Date(datumStr));
                            listaTecajev = new ArrayList<Tecaj>();
                            listaTecajev.add(tecaj);
                        }
                        else
                        {
                            listaTecajev.add(tecaj);
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return listaTecajnic;
        }

        public static String[] getValuteFromDataBase(String dbName, String dbPath) {
            
            String sql = "SELECT DISTINCT oznaka FROM tecajnica;";
            
            List<String> al = new  ArrayList<String>();
            
            try (
                    Connection conn = connect(dbName,dbPath);
                    Statement stmt  = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)
                ){
                while (rs.next()) {
                    String oznaka = rs.getString("oznaka");
                    al.add(oznaka);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            
            
            return al.toArray(new String[0]);
        }
        
        public static void main(String[] args) {
		createAndConnect("baza.db");
		insert("2007-01-01","USD","840","1.3170");
		insert("2007-01-01","USD","840","1.3170");
		insert("2007-01-01","USD","840","1.3170");
		insert("2007-01-01","USD","840","1.3170");
		insert("2007-01-01","USD","840","1.3170");
	}

    public static List<Tecajnica> loadTecajniceFromDB(String imeDB, String potDoDB, String odDateString, String doDateString, List<String> listaValut) {
            List<Tecajnica> lt;
            lt = new ArrayList<>();
            String valuteString = "";
            for (String string : listaValut) {
                valuteString += "'" + string + "',";
            }
            valuteString = valuteString.substring(0, valuteString.length()-1);
            String sql = "SELECT DISTINCT oznaka FROM tecajnica;";
            sql = "SELECT id,datum,oznaka,sifra,vrednost FROM tecajnica WHERE datum >= '" + odDateString + 
                    "' AND datum <= '" + doDateString + 
                    "' AND oznaka IN ("+ valuteString + ");";
            //"SELECT * FROM tecajnica WHERE datum >= '2010-01-03' AND datum <= '2013-01-09' AND oznaka IN ('USD', 'EEK');"
             List<Tecajnica> listaTecajnic = new ArrayList<Tecajnica>();
            try (
                    Connection conn = connect(imeDB,potDoDB);
                    Statement stmt  = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)
                ){
                String datumStr=null;
                String datumStrTemp=null;
                Tecajnica tecajnica = new Tecajnica();
                List<Tecaj> listaTecajev = new ArrayList<Tecaj>();
                while (rs.next()) {
                    datumStrTemp = datumStr;
                    String idStr = rs.getString("id");
                    datumStr = rs.getString("datum");
                    String oznakaStr = rs.getString("oznaka");
                    String sifraStr = rs.getString("sifra");
                    String vrednostStr = rs.getString("vrednost");
                    if(datumStrTemp == null){
                        datumStrTemp = datumStr;
                        tecajnica.setDatum(DateTools.str2Date(datumStr));
                    } // v primeru da začnemo z zanko
                    Tecaj tecaj = new Tecaj(oznakaStr,sifraStr,vrednostStr);
                    try {
                        if(DateTools.isNewDay(datumStrTemp, datumStr)){
                            tecajnica.setListaTecajev(listaTecajev);
                            listaTecajnic.add(tecajnica);
                            tecajnica = new Tecajnica();
                            tecajnica.setDatum(DateTools.str2Date(datumStr));
                            listaTecajev = new ArrayList<Tecaj>();
                            listaTecajev.add(tecaj);
                        }
                        else
                        {
                            listaTecajev.add(tecaj);
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            
            
            
            return listaTecajnic;
    }
}
