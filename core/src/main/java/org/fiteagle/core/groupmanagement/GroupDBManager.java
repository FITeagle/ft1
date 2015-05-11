package org.fiteagle.core.groupmanagement;


import org.fiteagle.core.config.FiteaglePreferences;
import org.fiteagle.core.config.FiteaglePreferencesXML;
import org.fiteagle.core.groupmanagement.JPAGroupDatabase.CouldNotCreateGroup;
import org.fiteagle.core.groupmanagement.JPAGroupDatabase.CouldNotDeleteGroup;
import org.fiteagle.core.groupmanagement.JPAGroupDatabase.CouldNotDeleteResource;
import org.fiteagle.core.groupmanagement.JPAGroupDatabase.CouldNotUpdateGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupDBManager {

  private static GroupDBManager gm = null;
  private GroupPersistable groupDatabase;
  private FiteaglePreferences preferences = new FiteaglePreferencesXML(this.getClass());
  private Logger log = LoggerFactory.getLogger(getClass());
  
  private static enum databaseType {
	  
	  
    InMemory, Persistent
  }
  private static final String DEFAULT_DATABASE_TYPE = databaseType.Persistent.name();

  private GroupDBManager() {
	  
	  Boolean inTestingMode = Boolean.valueOf(System.getProperty("org.fiteagle.core.userdatabase.UserDBManager.testing"));
	  if(inTestingMode){
		groupDatabase = new InMemoryGroupDatabase();
	    return;
	  }
		if (preferences.get("databaseType") == null) {
			preferences.put("databaseType", DEFAULT_DATABASE_TYPE);
		}
		if (preferences.get("databaseType").equals(databaseType.InMemory.name())) {
			groupDatabase = new InMemoryGroupDatabase();
		} else {
			groupDatabase = new JPAGroupDatabase();
		}
  }
  
  public void addGroup(Group group) throws CouldNotCreateGroup {
		groupDatabase.addGroup(group);
  }

  public Group getGroup(String groupId) throws GroupNotFound{
		return groupDatabase.getGroup(groupId);
  }

  public void deleteGroup(String groupId) throws CouldNotDeleteGroup {
		groupDatabase.deleteGroup(groupId);
  }
  
  public static GroupDBManager getInstance()  {
    if(gm == null ){
      gm = new GroupDBManager();      
    }
    return gm;
  }
public void updateGroup(Group g3) throws CouldNotUpdateGroup{
		groupDatabase.updateGroup(g3);
}
public void deleteResourceFromGroup(String resourceId,String groupId) throws  CouldNotDeleteResource{
		groupDatabase.deleteResourceFromGroup(resourceId,groupId);	
}
public static class GroupNotFound extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GroupNotFound() {

        super("The Asked Group could not be found. It doesn't exist");

	}

}
  
}
