package org.fiteagle.delivery.rest.fiteagle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
@Path("/v1/status")
public class StatusPresenter {
  
  
  @GET
  @Path("")
  @Produces("application/json")
  public List<TestbedStatus> getStatus(){
    List<TestbedStatus> dummyList = new ArrayList<>();
    TestbedStatus fuseco = new TestbedStatus();
    fuseco.setId("fuseco");
    fuseco.setStatus("up");
    fuseco.setLastCheck(Calendar.getInstance().getTime());
    
    TestbedStatus fiteagle = new TestbedStatus();
    fiteagle.setId("fiteagle local");
    fiteagle.setStatus("down");
    fiteagle.setLastCheck(Calendar.getInstance().getTime());
    
    dummyList.add(fuseco);
    dummyList.add(fiteagle);
    return dummyList;
    
  }
  
}
