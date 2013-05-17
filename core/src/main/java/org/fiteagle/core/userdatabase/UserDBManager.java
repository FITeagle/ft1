package org.fiteagle.core.userdatabase;

import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.security.auth.x500.X500Principal;


import org.fiteagle.core.config.FiteaglePreferences;
import org.fiteagle.core.config.FiteaglePreferencesXML;
import org.fiteagle.core.userdatabase.UserDB.DuplicateUIDException;
import org.fiteagle.core.userdatabase.UserDB.RecordNotFoundException;

public class UserDBManager {
  
  private UserDB database;
  private FiteaglePreferences preferences = new FiteaglePreferencesXML(this.getClass());
  
  private static enum databaseType {InMemory, SQLite} 
  private static final String DEFAULT_DATABASE_TYPE = databaseType.InMemory.name();
  
  public UserDBManager() throws SQLException{
    if(preferences.get("databaseType") == null){
      preferences.put("databaseType", DEFAULT_DATABASE_TYPE);
    }
    if(preferences.get("databaseType").equals(databaseType.SQLite.name())){
      database = new SQLiteUserDB();
    }
    else{
      database = new InMemoryUserDB();
      createDummyUser();
    }   
  }
  
  public void add(User u) throws DuplicateUIDException, SQLException{
    database.add(u);
  }
  
  public void delete(String UID) throws SQLException{
    database.delete(UID);
  }
  
  public void delete(User u) throws SQLException{
    database.delete(u);
  }
  
  public void update(User u) throws RecordNotFoundException, SQLException{
    database.update(u);
  }
  
  public void addKey(String UID, String key) throws RecordNotFoundException, SQLException{
    database.addKey(UID, key);
  }
  
  public User get(String UID) throws RecordNotFoundException, SQLException{
    return database.get(UID);
  }
  
  public User get(User u) throws RecordNotFoundException, SQLException{
    return database.get(u);
  }
  
  public User getUserFromCert(X509Certificate userCert) {
   try {
    String username = "";
    Collection<List<?>> alternativeNames =  userCert.getSubjectAlternativeNames();
    if(alternativeNames == null){
      X500Principal prince = userCert.getSubjectX500Principal();
      username = getCN(prince);
    }else{
      Iterator it = alternativeNames.iterator();
      while(it.hasNext()){
        List<?> altName = (List<?>) it.next();
        if(altName.get(0).equals(Integer.valueOf(0))){
          username = new String((byte[])altName.get(1));
        }
      }
    }
      
    User identifiedUser = get(username);
    return identifiedUser;
   } catch (CertificateParsingException e1) {
    
   throw new NonParsableNamingFormat();
  }
   
    catch (SQLException e) {
      throw new DatabaseException();
    }
  }

  private String getCN(X500Principal prince) {
    String uuid = prince.getName();
    LdapName ldapDN = getLdapName(uuid);
   
    for(Rdn rdn: ldapDN.getRdns()) {
        if(rdn.getType().equals("CN")){
          return (String) rdn.getValue();
        }
    }
    throw new NonParsableNamingFormat();
  }

  private LdapName getLdapName(String uuid) {
    try {
      LdapName ldapDN = new LdapName(uuid);
      return ldapDN;
    } catch (InvalidNameException e) {
  
       throw new NonParsableNamingFormat();
    }
   
  }
  
  
  private void createDummyUser() throws DuplicateUIDException, SQLException{
    
    
    String dummyPublicKey = "AAAAB3NzaC1yc2EAAAADAQABAAABAQDFrEGAjMHYsmOeRmBaILZ6IbVW6v5bxYK24o45DTXBW/fxmP8quGiIlGY8Q4g50t5OR+tUTn0G4XMue5ahyyMVwLFhIC5JT2E3g9E1t5QlCOUmFOYzElcOlRUipAFRoRRgY4Te+JdcF+ZTwrHMYGPwPlnTsj8e3i/l1tLeb0nzsADn8oLdnps2XPVFFTF3hTPv7du/w1ewOBfVeWdkm3ugetGs8upq/g4ijxxAcaE+iyuqNxUvq0FzvcMi+Tmr9wGQXRcK50suh2ENLjl+pTLnfJNsBLkV3zgJpAJPm2cP4AkLZhFZqHNdK2Do9wLS9hsNbnogJtNqO6qxziKyP+LH";
    List<String> dummyPubKeys = new ArrayList<>(); 
    dummyPubKeys.add(dummyPublicKey);
    User dummyUser = new User("fiteagle.av.test", "test", "testUser",dummyPubKeys );
    add(dummyUser);
  }
    
  

  
  private class NonParsableNamingFormat extends RuntimeException{
    
    private static final long serialVersionUID = -3819932831236493248L;
    
  }
  
  public class DatabaseException extends RuntimeException {

    private static final long serialVersionUID = -8002909402748409082L;
    
  }
}
