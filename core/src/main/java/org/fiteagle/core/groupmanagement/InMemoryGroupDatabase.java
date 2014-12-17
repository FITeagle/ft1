package org.fiteagle.core.groupmanagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class InMemoryGroupDatabase extends JPAGroupDatabase implements GroupPersistable {
  
	private static final String PERSISTENCE_UNIT_NAME_INMEMORY = "Users_Inmemory";
	
	public InMemoryGroupDatabase(){
	    
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
