package org.fiteagle.core.groupmanagement;

import junit.framework.Assert;

import org.fiteagle.adapter.common.ResourceAdapter;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class GroupManagerTest {
  static GroupPersistable db;
  Group group ;
  ResourceAdapter ra;
  
  @BeforeClass
  public static void setUp() throws Exception {
   db = new InMemoryGroupDatabase();
  }
  
 
   
  @Test
  public void testGetGroup(){
	  Group group1 = new Group("123", "1345");
	  db.addGroup(group1);
	  
	  Group group = db.getGroup("123");
	  System.out.println(group.getGroupId());
	  Assert.assertNotNull(group);
  }
  
  
  @After
  public void deleteGroup(){
    
  }


  
 
}
