package org.fiteagle.core.aaa;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import org.fiteagle.core.config.FiteaglePreferences;
import org.fiteagle.core.config.FiteaglePreferencesXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyStoreManagement {
private FiteaglePreferences preferences;
private Logger log = LoggerFactory.getLogger(getClass()); 
private final String DEFAULT_KEYSTORE_LOCATION=System.getProperty("user.home")+System.getProperty("file.separator")+"fiteagle"+System.getProperty("file.separator")+"jetty-ssl.keystore";
private final String DEFAULT_KEYSTORE_PASSWORD = "jetty6";
private final String DEFAULT_CA_ALIAS ="root";
private final String DEFAULT_CA_PRK_PASS = "jetty6";
private final String DEFAULT_PRK_PASS= "jetty6";
private final String DEFAULT_TRUSTSTORE_LOCATION= System.getProperty("user.home")+System.getProperty("file.separator")+"fiteagle"+System.getProperty("file.separator")+"jetty-ssl.truststore";
private final String DEFAULT_TRUSTSTORE_PASSWORD =  DEFAULT_KEYSTORE_PASSWORD;
private final String DEFAULT_SERVER_ALIAS ="root";

private static KeyStoreManagement keyStoreManagement;
public static KeyStoreManagement getInstance(){
  if(keyStoreManagement == null)
    keyStoreManagement = new KeyStoreManagement();
  return keyStoreManagement;
  
}
private KeyStoreManagement(){
  this.preferences = new FiteaglePreferencesXML(this.getClass());
  if(preferences.get("CAAlias") == null){
    preferences.put("CAAlias", DEFAULT_CA_ALIAS);
  }
  if(preferences.get("keystore_pass") == null){
    preferences.put("keystore_pass", DEFAULT_KEYSTORE_PASSWORD);
  }
  if(preferences.get("keystore") == null){
    preferences.put("keystore", DEFAULT_KEYSTORE_LOCATION);
  }
  if(preferences.get("prk_pass") == null){
    preferences.put("prk_pass", DEFAULT_PRK_PASS);
  }
  if(preferences.get("truststore") == null){
    preferences.put("truststore", DEFAULT_TRUSTSTORE_LOCATION);
  }
  if(preferences.get("truststore_pass") == null){
    preferences.put("truststore_pass", DEFAULT_TRUSTSTORE_PASSWORD);
  }
  if(preferences.get("server_alias")== null){
    preferences.put("server_alias", DEFAULT_SERVER_ALIAS);
  }
  if(preferences.get("ca_prkey_pass")==null){
    preferences.put("ca_prkey_pass", DEFAULT_CA_PRK_PASS);
  }

  
}
  

protected KeyStore loadKeyStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException{
   
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    FileInputStream fis = new FileInputStream(getKeyStorePath());
    char[] pass = getKeyStorePassword();
    ks.load(fis, pass);
    return ks;
  }
  
protected KeyStore loadTrustStore() throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException{
  KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
  FileInputStream fis = new FileInputStream(getTrustStorePath());
  char[] pass = getTrustStorePassword();
  truststore.load(fis, pass);
  return truststore;
}


  private char[] getTrustStorePassword() {
    return preferences.get("truststore_pass").toCharArray();
}
  private String getTrustStorePath() {
    return preferences.get("truststore");
}
  protected void storeCertificate(String alias, X509Certificate cert) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
    KeyStore ks = loadTrustStore();
    ks.setCertificateEntry(alias, cert);
    FileOutputStream fos = new FileOutputStream(getTrustStorePath());
    char[] pass = getTrustStorePassword();
    ks.store(fos, pass);
    
  }
  
  private String getCAAlias() {
    String alias = preferences.get("CAAlias");
    return alias;
  }
  
  
  private String getKeyStorePath() {
    return preferences.get("keystore");
  }

  //TODO !!!!!!!! public ???? how to make this at least a little bit more secure ???
  public char[] getPrivateKeyPassword(){
    return preferences.get("prk_pass").toCharArray();
  }
  private char[] getKeyStorePassword() {
    return preferences.get("keystore_pass").toCharArray();
  }
  public PrivateKey getCAPrivateKey() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableEntryException {
    PrivateKey privateKey = null;
    KeyStore  ks = loadTrustStore();
    PasswordProtection protection = new PasswordProtection(getCAPrivateKeyPassword());
    Entry keyStoreEntry = ks.getEntry(getCAAlias(), protection);
    if(keyStoreEntry instanceof PrivateKeyEntry){
         PrivateKeyEntry privateKeyEntry = (PrivateKeyEntry)keyStoreEntry;
         privateKey = privateKeyEntry.getPrivateKey();
      
    }else {
      throw new PrivateKeyException();
    }
    return privateKey;
  }
  
  private char[] getCAPrivateKeyPassword() {
    return preferences.get("ca_prkey_pass").toCharArray();
  }
  protected X509Certificate getCACert() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException  {
    KeyStore ks = loadTrustStore();
    X509Certificate caCert = (X509Certificate) ks.getCertificate(getCAAlias());
    return caCert;
  }
  
  protected List<X509Certificate> getTrustedCerts() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
   
    KeyStore ks = loadTrustStore();
    List<X509Certificate> certificateList = new LinkedList<>();
    
    for(Enumeration<String> aliases=  ks.aliases(); aliases.hasMoreElements();){
            certificateList.add((X509Certificate)(ks.getCertificate(aliases.nextElement())));
    }
   
    return certificateList;
  }
 
  public X509Certificate[] getStoredCertificate(String alias) {
    try {
      KeyStore ks = loadKeyStore();
      Certificate[] certChain =  ks.getCertificateChain(alias);
      X509Certificate[] returnChain = new X509Certificate[certChain.length];
      for(int i = 0; i<certChain.length; i++){
        returnChain[i] = (X509Certificate) certChain[i];
      }
      return returnChain;
    } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
      throw new CertificateNotFoundException();
    }
  }
 
  
  public class PrivateKeyException extends RuntimeException {
    private static final long serialVersionUID = 2842186524464171483L;
    
  }
  public class CertificateNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -3514307715237455008L;
    
  }
  public PrivateKey getServerPrivateKey() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableEntryException {
    PrivateKey privateKey = null;
    KeyStore  ks = loadKeyStore();
    PasswordProtection protection = new PasswordProtection(getPrivateKeyPassword());
    Entry keyStoreEntry = ks.getEntry(getServerAlias(), protection);
    if(keyStoreEntry instanceof PrivateKeyEntry){
         PrivateKeyEntry privateKeyEntry = (PrivateKeyEntry)keyStoreEntry;
         privateKey = privateKeyEntry.getPrivateKey();
      
    }else {
      throw new PrivateKeyException();
    }
    return privateKey;
  }
  private String getServerAlias() {
    return preferences.get("server_alias");
  }
  
  
}
