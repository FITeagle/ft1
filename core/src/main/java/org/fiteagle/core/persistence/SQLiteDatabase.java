package org.fiteagle.core.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.fiteagle.core.config.FiteaglePreferences;
import org.fiteagle.core.config.FiteaglePreferencesXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SQLiteDatabase {
protected Connection connection;  
private  final String DEFAULT_DATABASE_PATH = System.getProperty("user.home")+"/.fiteagle/db/";
protected FiteaglePreferences preferences;
protected static Logger log = LoggerFactory.getLogger(SQLiteDatabase.class); 
static {
  try {
      Class.forName("org.sqlite.JDBC"); 
  } catch (ClassNotFoundException e) {
      
      log.error(e.getMessage(),e);
  }
}


 public SQLiteDatabase(){
   preferences = new FiteaglePreferencesXML(SQLiteDatabase.class);
   connection  = null;
 }
  protected  void getConnection() throws SQLException{
    if(connection!=null){
      connection.close();
    }
    connection = DriverManager.getConnection("jdbc:sqlite:" + getDatabasePath());
    connection.setAutoCommit(false);
    updatePreferences();
    
  }
  
  
  private void updatePreferences() {
   this.preferences = new FiteaglePreferencesXML(SQLiteDatabase.class);
    
  }
  
  private String getDatabasePath() {
    if(preferences.get("database_path") == null)
      preferences.put("database_path", DEFAULT_DATABASE_PATH);
    
    String path = preferences.get("database_path");
    File dir = new File(path);
    dir.mkdirs();
    return path + "database.db";
  }
  
  protected void createTable(String command) throws SQLException{
    Statement st = connection.createStatement();
    st.executeUpdate(command);
    st.close();
  }
}
