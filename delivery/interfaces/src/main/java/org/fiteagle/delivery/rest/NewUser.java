package org.fiteagle.delivery.rest;

import java.util.List;


public class NewUser {
  
  private String UID;
  private String firstName;
  private String lastName;
  private String password;
  private List<String> publicKeys;
  
  public NewUser(){};
  
  public NewUser(String UID, String firstName, String lastName, String password, List<String> publicKeys) {
    this.UID = UID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.password = password;
    this.publicKeys = publicKeys;
  }  
  
  public String getUID() {
    return UID;
  }
  public void setUID(String UID) {
    this.UID = UID;
  }
  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
  
  public List<String> getPublicKeys() {
	return publicKeys;
  }

  public void setPublicKeys(List<String> publicKeys) {
	this.publicKeys = publicKeys;
  }
 
}