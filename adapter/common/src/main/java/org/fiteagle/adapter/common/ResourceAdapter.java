package org.fiteagle.adapter.common;

import java.rmi.server.ObjID;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class ResourceAdapter {

	public abstract void start();
	public abstract void stop();
	public abstract void create();
	public abstract void configure(AdapterConfiguration configuration);
	public abstract void release();
	public abstract List<ResourceAdapter> getJavaInstances();
	
	public abstract boolean isLoaded();
	public abstract void setLoaded(boolean loaded);
	
	private HashMap<String, Object> properties = new HashMap<String, Object>();
	private String type;//class of the implementing adapter
	private String id;
	private String groupId;
	private String status;
	private boolean exclusive = false;
	private boolean available = true;
	
	public HashMap<String, Object> getProperties() {
	  if (properties !=null){
	    return properties;
	  }else {
	    properties = new HashMap<String, Object>();
	    return properties;
    }
	}
	
	public void setProperties(HashMap<String, Object> properties) {
		this.properties = properties;
	}
	
	public void addProperty(String key, Object value) {
		this.properties.put(key, value);
	}
	
	public ResourceAdapter() {
	  ObjID objId = new ObjID();
	  this.setId(UUID.randomUUID().toString());
	  this.setStatus("geni_allocated");
  }
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
  public String getGroupId() {
    return groupId;
  }
  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public boolean isExclusive() {
    return exclusive;
  }
  public void setExclusive(boolean exclusive) {
    this.exclusive = exclusive;
  }
	
  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;  
  }
	
	
	
	
}
