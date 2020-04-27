package javafxapplication2.dal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConnData {
	private String dbName;
	private String dbFolder;
	private String jdbcString;
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDbFolder() {
		return dbFolder;
	}
	public void setDbFolder(String dbFolder) {
		this.dbFolder = dbFolder;
	}
	public String getJdbcString() {
		return jdbcString;
	}
	public void setJdbcString(String jdbcString) {
		this.jdbcString = jdbcString;
	}
	public ConnData(String dbName, String dbFolder, String jdbcString) {
		super();
		this.dbName = dbName;
		this.dbFolder = dbFolder;
		this.jdbcString = jdbcString;
	}
	
	public ConnData(String dbName) {
		super();
		this.dbName = dbName;
		setCurrFolder();					// Naredi mapo kje bo baza
		this.dbFolder = getDbFolder();
		this.jdbcString = getJDBCStr(dbFolder, dbName);
	}
        
        public ConnData(String dbName,String dbLocation) {
		super();
		this.dbName = dbName;
		this.dbFolder = dbLocation;
		this.jdbcString = getJDBCStr(dbFolder, dbName);
	}
        
	public void setCurrFolder() 
	{
	           String dbpot = System.getProperty("user.dir");// + "/db";
	           Path path = Paths.get(dbpot);
	           try {
	        	   Files.createDirectories(path);
	        	   setDbFolder( path.toString());
	           } 
	           catch (IOException e) {
	        	   e.printStackTrace();
	           }
	}
	public String getJDBCStr(String path, String dbName) {
		String url = "jdbc:sqlite:"+ path + "/" + dbName;
        url = url.replace("\\", "/");
        return url;
	}
	
}
