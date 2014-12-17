package org.fiteagle.core;


import java.lang.reflect.InvocationTargetException;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.fiteagle.adapter.common.ResourceAdapter;
import org.fiteagle.adapter.common.ResourceAdapterStatus;
import org.fiteagle.core.groupmanagement.GroupDBManager;
import org.reflections.Reflections;

import javax.enterprise.context.ApplicationScoped;

//TODO exception handling

@ApplicationScoped
public class ResourceAdapterManager {

    private static final String packageName = "org.fiteagle.adapter";



    private ResourceAdapterDatabase adapterInstancesDatabase;
    private ResourceAdapterDatabase adapterDatabase;
    private ScheduledExecutorService executor;
    private HashMap<String, ScheduledFuture<?>> expirationMap;
    private ClassLoader sysloader;

    public ResourceAdapterManager() {

        sysloader = this.getClass().getClassLoader();
        adapterInstancesDatabase = new InMemoryResourceAdapterDatabase();
        adapterDatabase = new InMemoryResourceAdapterDatabase();
        executor = Executors.newScheduledThreadPool(2);
        expirationMap = new HashMap<>();
        Set<Class<? extends ResourceAdapter>> allClassesInPackage = null;

        try {

            allClassesInPackage = findClasses();

        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        if (allClassesInPackage != null) {
            for (Class c : allClassesInPackage) {

                try {

                    java.lang.reflect.Method method = c.getDeclaredMethod("getJavaInstances", null);
                    List<ResourceAdapter> resourceAdapters = (List<ResourceAdapter>) method.invoke(null, null);
                    adapterInstancesDatabase.addResourceAdapters(resourceAdapters);
                    adapterDatabase.addResourceAdapters(resourceAdapters);

                } catch (IllegalAccessException e) {
                    throw new RuntimeException();//TODO: give more information in exception
                } catch (NoSuchMethodException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    private Set<Class<? extends ResourceAdapter>> findClasses() {

        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends ResourceAdapter>> adapters = reflections.getSubTypesOf(ResourceAdapter.class);

        return adapters;
    }


    public List<ResourceAdapter> getResourceAdapters() {

        return adapterDatabase.getResourceAdapters();

    }

    public void addResourceAdapter(ResourceAdapter resourceAdapter) {
        adapterDatabase.addResourceAdapter(resourceAdapter);
    }

    public void addResourceAdapterInstance(ResourceAdapter resourceAdapter) {
        adapterInstancesDatabase.addResourceAdapter(resourceAdapter);
    }

    public List<ResourceAdapter> getResourceAdapterInstances() {
        return adapterInstancesDatabase.getResourceAdapters();

    }


    public ResourceAdapter getResourceAdapterInstance(String instanceId) {
        return adapterInstancesDatabase.getResourceAdapter(instanceId);
    }




  public void deleteResource(String resourceAdapterId,String groupId) {
	  removeAdapterFromGroup(resourceAdapterId, groupId);
  }
  
  public void removeAdapterFromGroup(String resourceId, String groupId) {
		GroupDBManager.getInstance().deleteResourceFromGroup(resourceId,groupId);
	}
  
  
//  private static Class[] getAllClassesInPackage(String packageName)
//			throws ClassNotFoundException, IOException {
//		ClassLoader classLoader = Thread.currentThread()
//				.getContextClassLoader();
//		if(classLoader == null) throw new RuntimeException();//TODO: give more information in exception
//		String path = packageName.replace('.', '/');
//		Enumeration<URL> resources = classLoader.getResources(path);
//		List<File> dirs = new ArrayList<File>();
//		while (resources.hasMoreElements()) {
//			URL resource = resources.nextElement();
//			dirs.add(new File(resource.getFile()));
//		}
//		ArrayList<Class> classes = new ArrayList<Class>();
//		for (File directory : dirs) {
//			classes.addAll(findClassesInDirectory(directory, packageName));
//		}
//		
//		//this has duplicate entries in classes!
//		return classes.toArray(new Class[classes.size()]);
//	}

    public void setExpires(String resourceId, Date allocationExpirationTime) {
        ScheduledFuture<?> scheduler = executor.schedule(new ExpirationCallback(resourceId), allocationExpirationTime.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        adapterInstancesDatabase.getResourceAdapter(resourceId).setExpirationTime(allocationExpirationTime);
        expirationMap.put(resourceId, scheduler);
    }

    private class ExpirationCallback implements Runnable {

        private String resourceId;
        private String groupId;

        public ExpirationCallback(String resourceId) {
            this.resourceId = resourceId;

        }

        @Override
        public void run() {
            ResourceAdapter expiredAdapter = adapterInstancesDatabase.getResourceAdapter(resourceId);
//			removeAdapterFromGroup(resourceId);


		
	}
        
	
		
    }

    public List<ResourceAdapter> getResourceAdapterInstancesAvailable() {
        List<ResourceAdapter> availableAdapters = new LinkedList<>();
        for (ResourceAdapter ra : adapterInstancesDatabase.getResourceAdapters()) {
            if (ra.isAvailable())
                availableAdapters.add(ra);
        }
        return availableAdapters;
    }

    public List<ResourceAdapter> getResourceAdaptersAvailable() {
        List<ResourceAdapter> availableAdapters = new LinkedList<>();
        for (ResourceAdapter ra : adapterInstancesDatabase.getResourceAdapters()) {
            if (ra.isAvailable())
                availableAdapters.add(ra);
        }
        return availableAdapters;
    }

}
