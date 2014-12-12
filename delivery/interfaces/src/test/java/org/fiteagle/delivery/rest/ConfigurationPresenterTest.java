package org.fiteagle.delivery.rest;

import junit.framework.Assert;

import org.fiteagle.delivery.rest.fiteagle.ConfigurationController;
import org.junit.Test;

public class ConfigurationPresenterTest {
  
  @Test
  public void test() {
    ConfigurationController presenter = new ConfigurationController();
    Assert.assertNotNull(presenter.getVersion());
  }  
}
