package org.fiteagle.delivery.rest.fiteagle;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.fiteagle.interactors.api.ConfigurationManagerBoundary;
import org.fiteagle.interactors.configuration.ConfigurationManager;

@Path("v1/config")
public class ConfigurationController {
  
  private final ConfigurationManagerBoundary interactor;
  
  public ConfigurationController() {
    this.interactor = new ConfigurationManager();
  }
  
  @GET
  @Path("version")
  @Produces(MediaType.APPLICATION_JSON)
  public String getVersion() {
    return interactor.getVersion();
  }
  
  @GET
  @Path("domain")
  public String getDomain(){
  	return interactor.getDomain();
  }
}
