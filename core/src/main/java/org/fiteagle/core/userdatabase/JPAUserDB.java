package org.fiteagle.core.userdatabase;


import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.fiteagle.core.userdatabase.User.Role;

public class JPAUserDB{
  
  protected EntityManager manager;
  
  
  private static JPAUserDB instance;
  
//  public JPAUserDB(){
//	  instance = this;
//  }
  
  protected void beginTransaction(EntityManager em) {
  }
  
  protected void commitTransaction(EntityManager em) {
  }
  
  protected void flushTransaction(EntityManager em) {
    em.flush();
  }
  
  protected JPAUserDB() {
  }
  
  public static JPAUserDB getInstance(){
    if(instance == null){
      instance = new JPAUserDB();
    }
    return instance;
  }
  
  protected synchronized EntityManager getEntityManager() {
    if (manager == null) {
      try {
        Context context = new InitialContext();
        EntityManagerFactory f = (EntityManagerFactory) context.lookup("java:/fiteagle1/users/entitymanagerFactory");
        manager = f.createEntityManager();
      } catch (NamingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return manager;
  }
  
  public void add(User user){
    EntityManager em = getEntityManager();
    List<User> users = getAllUsers();
    for (User u : users) {
      if (u.getUsername().equals(user.getUsername())) {
        throw new DuplicateUsernameException();
      }
      if (u.getEmail().equals(user.getEmail())) {
        throw new DuplicateEmailException();
      }
    }
    
    try{
      beginTransaction(em);
      em.persist(user);
    } catch(Exception e){
      if(e.getCause() != null && e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException && e.getMessage().contains("'EMAIL' defined on 'USERS'")){
        throw new DuplicateEmailException();
      }
      if(e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException && e.getMessage().contains("defined on 'USERS'")){
        throw new DuplicateUsernameException();
      }
      throw e;
    } finally {
      commitTransaction(em);
    }
  }
  
  public User get(User user) throws UserNotFoundException{
    return get(user.getUsername());
  }
  
  public User get(String username) throws UserNotFoundException{
    EntityManager em = getEntityManager();
      User user = em.find(User.class, username);
      if(user == null){
        throw new UserNotFoundException();
      }
      return user;
  }
  
  public void delete(User user){
    EntityManager em = getEntityManager();
    beginTransaction(em);
    em.remove(em.merge(user));
    commitTransaction(em);
  }
  
  public void delete(String username){
    delete(get(username));
  }
 
  public void update(String username, String firstName, String lastName, String email, String affiliation, String password, List<UserPublicKey> publicKeys) {
    EntityManager em = getEntityManager();
    try{
      User user = em.find(User.class, username);
      if(user == null){
        throw new UserNotFoundException();
      }
      List<User> users = getAllUsers();
      for (User u : users) {
        if (u.getEmail().equals(email) && !u.getUsername().equals(username)) {
          throw new DuplicateEmailException();
        }
      }
      beginTransaction(em);
      user.updateAttributes(firstName, lastName, email, affiliation, password, publicKeys);
      commitTransaction(em);
    }catch(Exception e){
      if(e.getCause() != null && e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException && e.getMessage().contains("'EMAIL' defined on 'USERS'")){
        throw new DuplicateEmailException();
      }
      throw e;
    }
  }

  public void setRole(String username, Role role) {
    EntityManager em = getEntityManager();
      User user = em.find(User.class, username);
      if(user == null){
        throw new UserNotFoundException();
      }
      beginTransaction(em);
      user.setRole(role);
      commitTransaction(em);
  }
  
  public void addKey(String username, UserPublicKey publicKey){
    EntityManager em = getEntityManager();
    try{
      User user = em.find(User.class, username);
      if(user == null){
        throw new UserNotFoundException();
      }
      if (user.getPublicKeys().contains(publicKey)) {
        throw new DuplicatePublicKeyException();
      }
      beginTransaction(em);
      user.addPublicKey(publicKey);
      commitTransaction(em);
    }catch(Exception e){
      if(e.getCause() != null && e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException && e.getMessage().contains("defined on 'PUBLICKEYS'")){
        throw new DuplicatePublicKeyException();
      }
      throw e;
    }
  }
  
  public void deleteKey(String username, String description){
    EntityManager em = getEntityManager();
      User user = em.find(User.class, username);
      if(user == null){
        throw new UserNotFoundException();
      }
      beginTransaction(em);
      user.deletePublicKey(description);
      commitTransaction(em);
  }
  
  public void renameKey(String username, String description, String newDescription){
    EntityManager em = getEntityManager();
    try{
      User user = em.find(User.class, username);
      if(user == null){
        throw new UserNotFoundException();
      }
      for(UserPublicKey key : user.getPublicKeys()){
        if(key.getDescription().equals(newDescription)){
          throw new DuplicatePublicKeyException();
        }
      }
      beginTransaction(em);
      user.renamePublicKey(description, newDescription);
      commitTransaction(em);
    }catch(Exception e){
      if(e.getCause() != null && e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException && e.getMessage().contains("defined on 'PUBLICKEYS'")){
        throw new DuplicatePublicKeyException();
      }
      throw e;
    }
  }
  
  public List<User> getAllUsers(){
    EntityManager em = getEntityManager();
      Query query = em.createQuery("SELECT u FROM User u");
      @SuppressWarnings("unchecked")
      List<User> resultList = (List<User>) query.getResultList();
      return resultList;
  }
  
  public static class UserNotFoundException extends RuntimeException {    
    private static final long serialVersionUID = 2315125279537534064L;
    
    public UserNotFoundException(){
      super("no user with this username could be found in the database");
    }
  }
  
  public static class DuplicateUsernameException extends RuntimeException {
    private static final long serialVersionUID = -7242105025265481986L;   
    
    public DuplicateUsernameException(){
      super("another user with the same username already exists in the database");
    }
  }
  
  public static class DuplicateEmailException extends RuntimeException {
    private static final long serialVersionUID = 5986984055945876422L;
    
    public DuplicateEmailException(){
      super("another user with the same email already exists in the database");
    }
  }
  
  public static class DuplicatePublicKeyException extends RuntimeException {
    private static final long serialVersionUID = -8863826365649086008L; 
    
    public DuplicatePublicKeyException(){
      super("either this public key already exists or another public key with the same description already exists for this user");
    }
  }
  
}
