package org.fiteagle.core.userdatabase;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class InMemoryJPAUserDB extends JPAUserDB {
  
  private static final String PERSISTENCE_UNIT_NAME_INMEMORY = "Users_Inmemory";
  
  public InMemoryJPAUserDB(){
    
  }
  
  @Override
  protected synchronized EntityManager getEntityManager() {
    if (manager == null) {
      try {
        java.lang.Class.forName("org.h2.Driver");
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
      EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME_INMEMORY);
      manager = factory.createEntityManager();
    }
    return manager;
  }
  
  @Override
  protected void beginTransaction(EntityManager em) {
    em.getTransaction().begin();
  }
  
  @Override
  protected void commitTransaction(EntityManager em) {
    em.getTransaction().commit();
  }
  
  @Override
  protected void flushTransaction(EntityManager em) {
  }
  
}
