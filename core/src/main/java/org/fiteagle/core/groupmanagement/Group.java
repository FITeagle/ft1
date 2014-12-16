package org.fiteagle.core.groupmanagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.fiteagle.adapter.common.ResourceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name="GROUPS")
public class Group implements Serializable {

	private static final long serialVersionUID = 6166277346387936454L;

	@Id
	@Column(updatable = false)
	private String groupId;

	private String groupOwnerId;
	
	@ElementCollection
	private List<String> resources;
	
	@ElementCollection
	private List<String> authorizedUsers;

	@JsonIgnore
//	private Logger log = LoggerFactory.getLogger(getClass());

	public Group(String groupId, String groupOwnerId) {
		this.groupId = groupId;
		this.groupOwnerId = groupOwnerId;
		this.resources = new LinkedList<String>();
		this.authorizedUsers = new LinkedList<String>();
	}

	protected Group() {
	}

	//
	// public Group(String urn, String groupOwnerId, List<ResourceAdapter>
	// resourcesList) {
	// this.groupId=urn;
	// this.groupOwnerId=groupOwnerId;
	// this.resources=resourcesList;
	// }

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupOwnerId() {
		return groupOwnerId;
	}

	public void setGroupOwnerId(String groupOwnerId) {
		this.groupOwnerId = groupOwnerId;
	}

	public List<String> getResources() {
		return resources;
	}

	public void setResources(List<String> resources) {
		this.resources = resources;
	}

	public List<String> getAuthorizedUsers() {
		return authorizedUsers;
	}

	public void setAuthorizedUsers(List<String> authorizedUsers) {
		this.authorizedUsers = authorizedUsers;
	}

	public void addAuthorizedUser(String user) {
		if (authorizedUsers == null)
			authorizedUsers = new LinkedList<>();

		authorizedUsers.add(user);
	}

	public void addResource(ResourceAdapter resource) {
		if (this.resources == null)
			this.resources = new LinkedList<>();
		resource.setGroupId(this.getGroupId());
		this.resources.add(resource.getId());
	}

	public boolean contains(String resourceAdapterId) {
		return resources.contains(resourceAdapterId);
	}

	public void deleteResource(String adapterId) {
		resources.remove(adapterId);

	}

}
