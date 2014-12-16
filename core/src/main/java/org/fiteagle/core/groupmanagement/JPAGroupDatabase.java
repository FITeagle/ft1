package org.fiteagle.core.groupmanagement;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public class JPAGroupDatabase implements
		GroupPersistable {
	 protected EntityManager manager;
	 private static JPAGroupDatabase instance;
	 
	 
	 protected void beginTransaction(EntityManager em) {
	  }
	  
	  protected void commitTransaction(EntityManager em) {
	  }
	  
	  protected void flushTransaction(EntityManager em) {
	    em.flush();
	  }
	  
	  protected JPAGroupDatabase() {
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
	  
	  public static JPAGroupDatabase getInstance(){
		    if(instance == null){
		      instance = new JPAGroupDatabase();
		    }
		    return instance;
		  }
	  
	  
	  @Override
	public void addGroup(Group group){
		  getEntityManager();
//	  List<Group> groups = getGroups();
//	  for (Group g : groups) {
//	      if (g.getGroupId().equals(group.getGroupId())) {
//	        throw new DuplicateGroupIdException();
//	      }
//	    }

	      beginTransaction(manager);
	      manager.persist(group);
	      commitTransaction(manager);
	  }
	  
//	  public List<Group> getGroups(){
//		      Query query = manager.createQuery("SELECT g FROM Group g");
//		      @SuppressWarnings("unchecked")
//			List<Group> resultList = (List<Group>) query.getResultList();
//		      return resultList;
//		  }
	  
		private void addResources(Group group) {
			for (String resourceId : group.getResources()) {
					addResource(group.getGroupId(), resourceId);
				}
		}
		
		private void addResource(String groupId, String resourceId){
			getEntityManager();
				Group group = manager.find(Group.class, groupId);
				if(group == null){
			        throw new GroupNotFoundException();
			      }
				beginTransaction(manager);
				List<String> resources = group.getResources();
				resources.add(resourceId);
				group.setResources(resources);
				commitTransaction(manager);
		}
		
		@Override
		public Group getGroup(String groupId) {
			getEntityManager();
			Group group = manager.find(Group.class, groupId);
			if(group == null){
		        throw new GroupNotFoundException();
		      }
				return group;
		}
		
		@Override
		public void deleteGroup(String groupId)  {
			getEntityManager();
			if((manager.find(Group.class, groupId)) == null){
		        throw new GroupNotFoundException();
		      }
			else manager.remove(manager.find(Group.class, groupId));

			
			deleteAllResourcesFromSingleGroup(groupId);
		}
		
		
		private void deleteAllResourcesFromSingleGroup(String groupId) {
			getEntityManager();
		Group group = manager.find(Group.class, groupId);
		beginTransaction(manager);
		group.setResources(null);
		commitTransaction(manager);
		}
		
		@Override
		public void deleteResourceFromGroup(String resourceId,String groupId) {
			getEntityManager();
			Group group = manager.find(Group.class, groupId);
			beginTransaction(manager);
			List<String> resources = (List<String>) group.getResources();
			resources.remove(resourceId);
			group.setResources(resources);
			commitTransaction(manager);
		}
		
		@Override
		public void updateGroup(Group g) {
			getEntityManager();
			Group group = manager.find(Group.class, g.getGroupId());
			beginTransaction(manager);
			manager.remove(group);
			manager.persist(g);
			commitTransaction(manager);			
		}
		
	  
	  public static class DuplicateGroupIdException extends RuntimeException {
		    private static final long serialVersionUID = -7242105025265481986L;   
		    
		    public DuplicateGroupIdException(){
		      super("another group with the same groupId already exists in the database");
		    }
		  }
	  public static class GroupNotFoundException extends RuntimeException {
		    private static final long serialVersionUID = -7242105025265481123L;   
		    
		    public GroupNotFoundException(){
		      super("The Asked Group could not be found. It don't exist");
		    }
		  }
	  

//	public JPAGroupDatabase() throws SQLException {
//		Connection connection = getConnection();
//		try {
//			createTable(connection,
//					"CREATE TABLE IF NOT EXISTS Groups (groupId, ownerId, PRIMARY KEY(groupId))");
//			createTable(
//					connection,
//					"CREATE TABLE IF NOT EXISTS Resources (id INTEGER PRIMARY KEY AUTOINCREMENT,resourceId,groupId)");
//		} finally {
//			connection.close();
//		}
//	}
	
	

//	@Override
//	public void addGroup(Group group) throws SQLException {
//		PreparedStatement ps = null;
//		Connection connection = getConnection();
//		try {
//
//			ps = connection.prepareStatement("INSERT INTO Groups VALUES(?,?)");
//
//			ps.setString(1, group.getGroupId());
//			ps.setString(2, group.getGroupOwnerId());
//
//			ps.execute();
//			connection.commit();
//			connection.close();
//		} catch (SQLException e) {
//			log.error(e.getMessage(), e);
//			throw new CouldNotCreateGroup();
//		} finally {
//			connection.close();
//			try {
//				ps.close();
//			} catch (SQLException e) {
//				throw new RuntimeException(e);
//			}
//		}
//
//		addResources(group);
//
//	}

//	private void addResources(Group group) {
//		for (String resourceId : group.getResources()) {
//			try {
//				addResource(group.getGroupId(), resourceId);
//			} catch (SQLException e) {
//				log.error(e.getMessage(), e);
//				throw new RuntimeException();
//			}
//		}
//	}

//	private void addResource(String groupId, String resourceId)
//			throws SQLException {
//		PreparedStatement ps = null;
//		Connection connection = getConnection();
//		try {
//
//			ps = connection
//					.prepareStatement("INSERT INTO Resources(id,resourceId,groupId) VALUES($next_id,?,?)");
//			ps.setString(2, resourceId);
//			ps.setString(3, groupId);
//			ps.execute();
//			connection.commit();
//			
//		} catch (SQLException e) {
//			log.error(e.getMessage(), e);
//			throw new RuntimeException("could not add resource");
//		} finally {
//			connection.close();
//			ps.close();
//
//		}
//	}

//	@Override
//	public Group getGroup(String groupId) throws SQLException {
//		Group g = null;
//		Connection connection = getConnection();
//		try {
//			
//			PreparedStatement ps = connection
//					.prepareStatement("SELECT Groups.groupId, Groups.ownerId, Resources.resourceId FROM Groups LEFT OUTER JOIN Resources ON Groups.groupId=Resources.groupId WHERE Groups.groupId = ? ");
//			ps.setString(1, groupId);
//			ResultSet result = ps.executeQuery();
//			
//			if (result.next()) {
//				g = evaluateResultSet(result);
//				connection.commit();
//				
//			}else{
//				throw new GroupNotFound(groupId);
//			}
//		}catch(SQLException e){
//			throw new GroupNotFound("Group not found");
//		}finally{
//			connection.close();
//		}
//	
//			return g;
//	}


//	@Override
//	public List<Group> getGroups() {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public void deleteGroup(String groupId) throws SQLException {
//		Connection connection = getConnection();
//		try {
//
//			
//			PreparedStatement ps = connection
//					.prepareStatement("DELETE FROM Groups WHERE Groups.groupId = ?");
//			ps.setString(1, groupId);
//			ps.execute();
//			connection.commit();
//			
//		} catch (SQLException e) {
//			log.error(e.getMessage(), e);
//			throw new CouldNotDeleteGroup();
//		}finally{
//			connection.close();
//		}
//		deleteAllResourcesFromSingleGroup(groupId);
//	}

//	private void deleteAllResourcesFromSingleGroup(String groupId) throws SQLException {
//		Connection connection = getConnection();
//		try {
//
//			
//			PreparedStatement ps = connection
//					.prepareStatement("DELETE FROM Resources WHERE Resources.groupId = ?");
//			ps.setString(1, groupId);
//			ps.execute();
//			connection.commit();
//			
//		} catch (SQLException e) {
//			log.error(e.getMessage(), e);
//			throw new CouldNotDeleteGroup();
//		}finally{
//			connection.close();
//		}
//	}

	
//	@Override
//	public void updateGroup(Group g3) throws SQLException {
//		Group existent = null;
//		try {
//			existent = getGroup(g3.getGroupId());
//		} catch (GroupNotFound e1) {
//			return;
//		}
//		try {
//			deleteGroup(g3.getGroupId());
//		} catch (CouldNotDeleteGroup e) {
//			return;
//		}
//		try {
//			addGroup(g3);
//		} catch (CouldNotCreateGroup e2) {
//			addGroup(existent);
//		}
//	}

//	@Override
//	public void deleteResourceFromGroup(String resourceId) throws SQLException {
//
//		Connection connection = getConnection();
//		try {
//
//			PreparedStatement ps = connection
//					.prepareStatement("DELETE FROM Resources WHERE Resources.resourceId = ?");
//			ps.setString(1, resourceId);
//			ps.execute();
//			connection.commit();
//		
//		} catch (SQLException e) {
//			log.error(e.getMessage(), e);
//			throw new CouldNotDeleteGroup();
//		}finally{
//			connection.close();
//		}
//
//	}
	
	public static class CouldNotCreateGroup extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	public static class CouldNotDeleteGroup extends RuntimeException {
		private static final long serialVersionUID = 2L;
	}
	public static class CouldNotUpdateGroup extends RuntimeException{

		private static final long serialVersionUID = 1L;
		
	}
	public static class CouldNotDeleteResource extends RuntimeException{

		private static final long serialVersionUID = 1L;
		
	}
}
