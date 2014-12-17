package org.fiteagle.core.groupmanagement;


public interface GroupPersistable {
  
  public void addGroup(Group group);
  
  public Group getGroup(String groupId);
  
  public void deleteGroup(String groupId);

  public void updateGroup(Group g3);

public void deleteResourceFromGroup(String resourceId,String groupId);
  
}
