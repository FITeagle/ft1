package org.fiteagle.interactors.sfa.common;

import org.fiteagle.core.ResourceAdapterManager;
import org.fiteagle.core.aaa.KeyStoreManagement;
import org.fiteagle.core.groupmanagement.GroupDBManager;
import org.fiteagle.interactors.sfa.allocate.AllocateRequestProcessor;
import org.fiteagle.interactors.sfa.delete.DeleteRequestProcessor;
import org.fiteagle.interactors.sfa.describe.DescribeRequestProcessor;
import org.fiteagle.interactors.sfa.getSelfCredential.GetSelfCredentialRequestProcessor;
import org.fiteagle.interactors.sfa.getversion.GetVersionRequestProcessor;
import org.fiteagle.interactors.sfa.listresources.ListResourceRequestProcessor;
import org.fiteagle.interactors.sfa.performoperationalaction.PerformOperationalActionRequestProcessor;
import org.fiteagle.interactors.sfa.provision.ProvisionRequestProcessor;
import org.fiteagle.interactors.sfa.register.RegisterRequestProcessor;
import org.fiteagle.interactors.sfa.renew.RenewRequestProcessor;
import org.fiteagle.interactors.sfa.renewSlice.RenewSliceRequestProcessor;
import org.fiteagle.interactors.sfa.resolve.ResolveRequestProcessor;
import org.fiteagle.interactors.sfa.status.StatusRequestProcessor;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;



public class SFARequestProcessorFactory {


	@Inject
	ResourceAdapterManager resourceAdapterManager;



	@SuppressWarnings("unchecked")
	public <E extends SFAv3RequestProcessor> E createRequestProcessor(
			SFAv3MethodsEnum method) {

		E result = null;
		switch (method) {
		case ALLOCATE:
			AllocateRequestProcessor allocateRequestProcessor = new AllocateRequestProcessor();
			allocateRequestProcessor.setResourceAdapterManager(resourceAdapterManager);
			result = (E) allocateRequestProcessor;

			break;
		case DELETE:
			DeleteRequestProcessor delProc = new DeleteRequestProcessor();
			delProc.setResourceAdapterManager(resourceAdapterManager);
			delProc.setGroupDBManager(GroupDBManager.getInstance());
			result = (E) delProc;
			break;
		case DESCRIBE:
			DescribeRequestProcessor describeRequestProcessor = new DescribeRequestProcessor();
			describeRequestProcessor.setResourceAdapterManager(resourceAdapterManager);
			result = (E) describeRequestProcessor;
			break;
		case LIST_RESOURCES:
			ListResourceRequestProcessor listResourceRequestProcessor = new ListResourceRequestProcessor();
			listResourceRequestProcessor.setResourceAdapterManager(resourceAdapterManager);
			result = (E) listResourceRequestProcessor;
			break;
		case PERFORM_OPERATIONAL_ACTION:
			PerformOperationalActionRequestProcessor performOperationalActionRequestProcessor = new PerformOperationalActionRequestProcessor();
			performOperationalActionRequestProcessor.setResourceAdapterManager(resourceAdapterManager);
			result = (E) performOperationalActionRequestProcessor;
			break;
		case PROVISION:
			ProvisionRequestProcessor provisionRequestProcessor = new ProvisionRequestProcessor();
			provisionRequestProcessor.setResourceAdapterManager(resourceAdapterManager);
			result = (E) provisionRequestProcessor;
			break;
		case RENEW_SLICE:
			RenewSliceRequestProcessor renewSliceRequestProcessor = new RenewSliceRequestProcessor(KeyStoreManagement.getInstance(), GroupDBManager.getInstance());
			renewSliceRequestProcessor.setResourceAdapterManager(resourceAdapterManager);
			result = (E) renewSliceRequestProcessor;
			break;
		case RENEW:
			RenewRequestProcessor renewRequestProcessor = new RenewRequestProcessor(resourceAdapterManager, GroupDBManager.getInstance());
			result = (E) renewRequestProcessor;
			break;
		case SHUTDOWN:
			break;
		case GET_SELF_CREDENTIAL:
			result = (E) new GetSelfCredentialRequestProcessor();
			break;
		case STATUS:
			StatusRequestProcessor statusRequestProcessor = new StatusRequestProcessor();
			statusRequestProcessor.setResourceAdapterManager(resourceAdapterManager);
			result = (E) statusRequestProcessor;
			break;
		case GET_VERSION:
			GetVersionRequestProcessor getVersionRequestProcessor = new GetVersionRequestProcessor();
			getVersionRequestProcessor.setResourceAdapterManager(resourceAdapterManager);
			result = (E) getVersionRequestProcessor;

			break;
		case REGISTER:
			RegisterRequestProcessor registerRequestProcessor = new RegisterRequestProcessor(
					KeyStoreManagement.getInstance(),
					GroupDBManager.getInstance());
			result = (E) registerRequestProcessor;
			break;
		case RESOLVE:
			result = (E) new ResolveRequestProcessor();
			break;
		default:
			break;

		}

		return result;
	}
}
