package org.fiteagle.core.groupmanagement;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.fiteagle.core.groupmanagement.GroupDBManager.GroupNotFound;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class JPAGroupDatabaseTests {
	static InMemoryGroupDatabase memDB;

	// static JPAGroupDatabase gBase;

	@BeforeClass
	public static void setUp() {
		memDB = new InMemoryGroupDatabase();
		memDB.getInstance();
	}

	@Test
	public void testDeleteGroups() {
		Group g1 = new Group("1", "11");
		Group g2 = new Group("2", "22");
		memDB.addGroup(g1);
		memDB.addGroup(g2);
		Assert.assertTrue(memDB.getGroup("1").getGroupOwnerId() == g1
				.getGroupOwnerId());
		Assert.assertTrue(memDB.getGroup("2").getGroupOwnerId() == g2
				.getGroupOwnerId());
		memDB.deleteGroup("1");
		memDB.deleteGroup("2");

		try {
			memDB.getGroup("1");
		} catch (GroupNotFound e) {
			Assert.assertNotNull(e);
		}

		try {
			memDB.getGroup("2");
		} catch (GroupNotFound e) {
			Assert.assertNotNull(e);
		}
		setUp();

	}

	@Test
	public void testAddGroup() {
		Group g1 = new Group("1", "11");
		memDB.addGroup(g1);
		Group testG1 = null;
		testG1 = memDB.getGroup(g1.getGroupId());
		Assert.assertTrue(testG1.getGroupId() == "1"
				&& testG1.getGroupOwnerId() == "11");
		setUp();
	}

	@Test
	public void testGetAllgroups() {
		Group g1 = new Group("1", "11");
		Group g2 = new Group("2", "22");
		memDB.addGroup(g1);
		memDB.addGroup(g2);
		List<Group> groupList;
		groupList = memDB.getGroups();
		Assert.assertTrue(groupList.get(0).getGroupId() == "1"
				&& groupList.get(1).getGroupId() == "2");
		setUp();
	}

	@Test
	public void testAddRessource() {
		Group g1 = new Group("1", "11");
		List<String> resources = new LinkedList();
		resources.add("resource1");
		resources.add("resource2");
		g1.setResources(resources);
		memDB.addGroup(g1);
		Group g2 = memDB.getGroup(g1.getGroupId());
		List<String> resources2 = g2.getResources();
		Assert.assertTrue(resources2.get(0) == resources.get(0) );
		Assert.assertTrue(resources2.get(1) == resources.get(1) );

		setUp();
	}
	
	@Test
	public void testDeleteRessource() {
		Group g1 = new Group("1", "11");
		List<String> resources = new LinkedList();
		resources.add("resource1");
		resources.add("resource2");
		g1.setResources(resources);
		memDB.addGroup(g1);
		g1 = null;
		resources = null;
		memDB.deleteResourceFromGroup("resource1", "1");
		resources = memDB.getGroup("1").getResources();
		Assert.assertTrue(resources.get(0) == "resource2");
		
		setUp();
	}
	
	@Test
	public void testUpdateGroup() {
		Group g1 = new Group("1", "11");
		List<String> resources = new LinkedList();
		resources.add("resource1");
		resources.add("resource2");
		g1.setResources(resources);
		memDB.addGroup(g1);
		g1 = null;
		g1 = new Group("1","123");
		g1.setResources(resources);
		memDB.updateGroup(g1);
		Assert.assertTrue(memDB.getGroup("1").getGroupOwnerId() == "123");
		
		setUp();
	}
	

}
