package org.fiteagle.delivery.xmlrpc.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.fiteagle.interactors.sfa.ISFA;
import org.fiteagle.interactors.sfa.SFAInteractor_v3;
import org.fiteagle.interactors.sfa.common.AMResult;
import org.fiteagle.interactors.sfa.getSelfCredential.jaxbClasses.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SFARegistryHandler extends SFAHandler {
  
Logger log =  LoggerFactory.getLogger(this.getClass());

    @Inject
    SFAInteractor_v3 interactor;
	
	@Override
	public Object invoke(String methodName, List arguments) throws Throwable {

    setInteractor(interactor);

		Method calledMethod = getMethod(methodName,arguments);
		Object response = calledMethod.invoke(this.interactor, arguments.toArray());
		return response;
	}


	@Override
	public Object xmlStructToObject(Object object, Object object2) {
		return object2;
		
	}
	
	private Object createResponse(AMResult result) {
    Object response = new HashMap<>();
    try {
      response = introspect(result);
    } catch (IOException ioException) {
      log.error(ioException.getMessage(),ioException);
    }
    return response;
  }


  @Override
  public Object invoke(String methodName, List arguments, X509Certificate certificate) throws Throwable {

    interactor.setCertificate(certificate);
    setInteractor(interactor);

    
    Method calledMethod = getMethod(methodName, arguments);
    Object response = calledMethod.invoke(this.interactor, arguments.toArray());
    if(response instanceof AMResult)
      return createResponse((AMResult)response);
    return response;
  }

}
