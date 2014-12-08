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
  
  private EntityManager manager;
  
  private static final String PERSISTENCE_UNIT_NAME_HIBERNATE = "Users_Hibernate";
  private static final String PERSISTENCE_UNIT_NAME_INMEMORY = "Users_InMemory";
  
  private static JPAUserDB derbyInstance;
  private static JPAUserDB inMemoryInstance;
  
  public JPAUserDB(){
	  derbyInstance = this;
  }
  
  private JPAUserDB(String persistenceUnitName) {
  }
  
  public static JPAUserDB getInMemoryInstance(){
    if(inMemoryInstance == null){
      inMemoryInstance = new JPAUserDB(PERSISTENCE_UNIT_NAME_INMEMORY);
    }
    return inMemoryInstance;
  }
  
  public static JPAUserDB getHibernateInstance(){
    if(derbyInstance == null){
      derbyInstance = new JPAUserDB(PERSISTENCE_UNIT_NAME_HIBERNATE);
    }
    return derbyInstance;
  }
  
  private synchronized EntityManager getEntityManager() {
	  if(manager == null){
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
    try{
      em.persist(user);
    } catch(Exception e){
      if(e.getCause() != null && e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException && e.getMessage().contains("'EMAIL' defined on 'USERS'")){
        throw new DuplicateEmailException();
      }
      if(e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException && e.getMessage().contains("defined on 'USERS'")){
        throw new DuplicateUsernameException();
      }
      throw e;
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
      em.remove(em.merge(user));
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
      user.updateAttributes(firstName, lastName, email, affiliation, password, publicKeys);
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
      user.setRole(role);
  }

  
  public void addKey(String username, UserPublicKey publicKey){
    EntityManager em = getEntityManager();
    try{
      User user = em.find(User.class, username);
      if(user == null){
        throw new UserNotFoundException();
      }
      user.addPublicKey(publicKey);
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
      user.deletePublicKey(description);
  }
  
  public void renameKey(String username, String description, String newDescription){
    EntityManager em = getEntityManager();
    try{
      User user = em.find(User.class, username);
      if(user == null){
        throw new UserNotFoundException();
      }
      user.renamePublicKey(description, newDescription);
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
