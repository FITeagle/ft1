package org.fiteagle.delivery.rest.fiteagle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.fiteagle.interactors.api.ResourceMonitoringBoundary;
import org.fiteagle.interactors.monitoring.MonitoringManager;
import org.fiteagle.core.monitoring.StatusTable;

@Path("/v1/status")
public class StatusPresenter {

	private ResourceMonitoringBoundary monitor;

	public StatusPresenter() {
		this.monitor = new MonitoringManager();
	}

	@GET
	@Path("")
	@Produces("application/json")
	public List<TestbedStatus> getStatus() {
		
		ArrayList<TestbedStatus> status = new ArrayList<TestbedStatus>(); 
		
		Collection<StatusTable> monitoringData = monitor.getMonitoringData();
		
		for (Iterator iterator = monitoringData.iterator(); iterator.hasNext();) {
			StatusTable statusTable = (StatusTable) iterator.next();
			status.add(statusTable2Testbedstatus(statusTable));
		}
		
		return status;

	}

	@GET
	@Path("{id}")
	@Produces("application/json")
	public TestbedStatus getTestBedStatusById(@PathParam("id") String id) {
		return statusTable2Testbedstatus(monitor.getMonitoringDataById(id));
	}
	
	private TestbedStatus statusTable2Testbedstatus(StatusTable statusTable){
		if(statusTable==null) return null;
		TestbedStatus testbedStatus = new TestbedStatus();
		
		testbedStatus.setId(statusTable.getId());
		testbedStatus.setLastCheck(statusTable.getLastCheck());
		testbedStatus.setStatus(statusTable.getStatus());
		
		if(statusTable.getComponents()!=null){
			Collection<StatusTable> components = statusTable.getComponents();
			for (Iterator iterator = components.iterator(); iterator.hasNext();) {
				StatusTable statusTable2 = (StatusTable) iterator.next();
				testbedStatus.addComponent(statusTable2Testbedstatus(statusTable2));
			}
		}
		
		return testbedStatus;
		
	}

}
