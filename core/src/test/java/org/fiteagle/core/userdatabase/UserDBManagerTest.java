package org.fiteagle.core.userdatabase;

import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import junit.framework.Assert;

import org.fiteagle.core.aaa.KeyManagement;
import org.fiteagle.core.userdatabase.JPAUserDB.DuplicateEmailException;
import org.fiteagle.core.userdatabase.JPAUserDB.DuplicateUsernameException;
import org.fiteagle.core.userdatabase.JPAUserDB.UserNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserDBManagerTest {
  
  UserDBManager userDBManager = UserDBManager.getInstance();
  
  User testUser;  
  
  @Before
  public void setUp() throws Exception {
    testUser = new User("test1", "test", "testName", "test@test.org", "testAffiliation", "password", new ArrayList<UserPublicKey>());
    try{
      userDBManager.add(testUser);
    } catch(DuplicateUsernameException | DuplicateEmailException e){  
      userDBManager.delete(testUser);
      userDBManager.add(testUser);
    }
  }
    
  @Test
  public void testVerifyPassword() throws DuplicateUsernameException, NoSuchAlgorithmException, IOException{    
    Assert.assertTrue(userDBManager.verifyPassword("password",testUser.getPasswordHash(),testUser.getPasswordSalt()));    
  }
  
  @Test
  public void testVerifyCredentials() throws UserNotFoundException, IOException, NoSuchAlgorithmException{    
    Assert.assertTrue(userDBManager.verifyCredentials(testUser.getUsername(), "password"));    
  } 
  
  @Test
  public void testCreateUserCertAndPrivateKey() throws Exception{
    String currentPath = System.getProperty("user.dir");
    KeyPair keyPair = KeyManagement.getInstance().loadKeyPairFromFile(currentPath+"/src/test/resources/testPublicKey.key", currentPath+"/src/test/resources/testPrivateKey.key");
    Assert.assertTrue(userDBManager.createUserCertificate(testUser.getUsername(), "my passphrase", keyPair).contains("ENCRYPTED"));
  }  
  
  @Test
  public void testCreateUserCertAndPrivateKeyWithoutPasshphrase() throws Exception{
    String currentPath = System.getProperty("user.dir");
    KeyPair keyPair = KeyManagement.getInstance().loadKeyPairFromFile(currentPath+"/src/test/resources/testPublicKey.key", currentPath+"/src/test/resources/testPrivateKey.key");
    Assert.assertFalse(userDBManager.createUserCertificate(testUser.getUsername(), "", keyPair).contains("ENCRYPTED"));
  }  
  
  @Test
  public void testCreateCertForPublicKey() throws Exception{
    UserPublicKey key = new UserPublicKey("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAAAgQCarsTCyAf8gYXwei8rhJnLTqYI6P88weRaY5dW9j3DT4mvfQPna79Bjq+uH4drmjbTD2n3s3ytqupFfNko1F0+McstA2EEkO8pAo5NEPcreygUcB2d49So032GKGPETB8chRkDsaBCm/KKL2vXdQoicofli8JJRPK2kXfUW34qww==", "my first key");
    userDBManager.addKey(testUser.getUsername(), key);
    Assert.assertNotNull(userDBManager.createUserCertificateForPublicKey(testUser.getUsername(), key.getDescription()));
  }
  
  @After
  public void deleteTestUser(){
    userDBManager.delete(testUser);
  }
  
}
