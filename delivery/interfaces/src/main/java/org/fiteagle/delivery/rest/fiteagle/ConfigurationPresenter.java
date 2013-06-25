package org.fiteagle.delivery.rest.fiteagle;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.fiteagle.interactors.api.ConfigurationManagerBoundary;

import com.google.inject.Inject;

@Path("v1/config")
public class ConfigurationPresenter {
  
  private final ConfigurationManagerBoundary interactor;
  
  @Inject
  public ConfigurationPresenter(final ConfigurationManagerBoundary interactor) {
    this.interactor = interactor;
  }
  
  @GET
  @Path("version")
  @Produces(MediaType.APPLICATION_JSON)
  public String getVersion() {
    return interactor.getVersion();
  }
}