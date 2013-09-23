package org.fiteagle.interactors.sfa.performoperationalaction;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.mail.MethodNotSupportedException;

import org.fiteagle.adapter.common.ResourceAdapter;
import org.fiteagle.core.ResourceAdapterManager;
import org.fiteagle.core.groupmanagement.Group;
import org.fiteagle.core.groupmanagement.GroupDBManager;
import org.fiteagle.core.util.URN;
import org.fiteagle.interactors.sfa.common.AMCode;
import org.fiteagle.interactors.sfa.common.AMResult;
import org.fiteagle.interactors.sfa.common.Authorization;
import org.fiteagle.interactors.sfa.common.GENISliverAllocationState;
import org.fiteagle.interactors.sfa.common.GENISliverOperationalState;
import org.fiteagle.interactors.sfa.common.GENI_CodeEnum;
import org.fiteagle.interactors.sfa.common.GeniSliversOperationalStatus;
import org.fiteagle.interactors.sfa.common.ListCredentials;
import org.fiteagle.interactors.sfa.common.SFAv3RequestProcessor;
import org.fiteagle.interactors.sfa.performoperationalaction.Action.MethodNotFound;
import org.fiteagle.interactors.sfa.rspec.SFAv3RspecTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformOperationalActionRequestProcessor extends
		SFAv3RequestProcessor {

	Logger log = LoggerFactory.getLogger(getClass()); 
	GENI_CodeEnum code = GENI_CodeEnum.SUCCESS;

	public PerformOperationalActionResult processRequest(
			ArrayList<String> urns, ListCredentials credentials, String action,
			PerformOperationalActionOptions performOpOptions) {
		PerformOperationalActionResult result = getResult(urns, credentials,
				action, performOpOptions);
		return result;
	}

	private PerformOperationalActionResult getResult(ArrayList<String> urns,
			ListCredentials credentials, String action,
			PerformOperationalActionOptions performOpOptions) {

		String output = "";
		AMCode returnCode = null;

		Authorization auth = new Authorization();

		auth.checkCredentialsList(credentials);

		PerformOperationalActionResult result = new PerformOperationalActionResult();

		if (!auth.areCredentialTypeAndVersionValid()) {
			returnCode = auth.getReturnCode();
			output = auth.getAuthorizationFailMessage();
			result.setCode(returnCode);
			result.setOutput(output);
			return result;
		}

		result.setValue(getPerformOperationalActionResultValue(urns, action));
		returnCode = getReturnCode(code);
		result.setCode(returnCode);
		result.setOutput(returnCode.retrieveGeni_code().getDescription());
		return result;
	}

	private ArrayList<GeniSliversOperationalStatus> getPerformOperationalActionResultValue(
			ArrayList<String> urns, String action) {

		if (urns == null || urns.size() == 0)
			throw new RuntimeException();

		SFAv3RspecTranslator translator = new SFAv3RspecTranslator();
		ResourceAdapterManager resourceManager = ResourceAdapterManager
				.getInstance();
		ArrayList<GeniSliversOperationalStatus> slivers = new ArrayList<GeniSliversOperationalStatus>();
		if (urns.get(0).contains("+slice+")) {
			Group group = GroupDBManager.getInstance().getGroup(
					new URN(urns.get(0)).getSubjectAtDomain());
			List<String> resourceAdapterInstanceIds = group.getResources();
			List<ResourceAdapter> resourceAdapterInstances = resourceManager
					.getResourceAdapterInstancesById(resourceAdapterInstanceIds);

			for (Iterator iterator = resourceAdapterInstances.iterator(); iterator
					.hasNext();) {
				ResourceAdapter resourceAdapter = (ResourceAdapter) iterator
						.next();

				performActionOnAdapter(action, resourceAdapter);

				if (isStillSuccessfull()) {
					GeniSliversOperationalStatus tmpSliver = new GeniSliversOperationalStatus();
					String urn = URN.getURNFromResourceAdapter(resourceAdapter).toString();
					tmpSliver.setGeni_sliver_urn(urn);
					tmpSliver
							.setGeni_allocation_status((String) resourceAdapter
									.getProperties().get("allocation_status"));
					slivers.add(tmpSliver);
				} else {
					break;
				}

			}
		} else {

			for (Iterator iterator = urns.iterator(); iterator.hasNext();) {
				String urn = (String) iterator.next();
				URN u = new URN(urn);
				String id = u.getSubject();

				ResourceAdapter resourceAdapter = resourceManager
						.getResourceAdapterInstance(id);
				performActionOnAdapter(action, resourceAdapter);
				if (isStillSuccessfull()) {
					GeniSliversOperationalStatus tmpSliver = new GeniSliversOperationalStatus();
					tmpSliver.setGeni_sliver_urn(urn);
					tmpSliver
							.setGeni_allocation_status((String) resourceAdapter
									.getProperties().get("allocation_status"));
					slivers.add(tmpSliver);
				} else {
					break;
				}
			}

		}

		return slivers;

	}

	private boolean isStillSuccessfull() {
		return code.equals(GENI_CodeEnum.SUCCESS);
	}

	private void performActionOnAdapter(String action,
			ResourceAdapter resourceAdapter) {
		try {
			if (action.equalsIgnoreCase("geni_start")) {
				resourceAdapter.start();
				return;
			}

			if (action.equalsIgnoreCase("geni_stop")) {
				resourceAdapter.stop();
				// TODO: change the state!!!
			} else {
				Action requestedAction = new Action(action, resourceAdapter);
				requestedAction.doAction();
			}
		} catch (IllegalArgumentException argumentError) {
			log.error(argumentError.getMessage(),argumentError);
			code = GENI_CodeEnum.BADARGS;
		} catch (MethodNotFound methodNotFound) {
			log.error(methodNotFound.getMessage(),methodNotFound);
			code = GENI_CodeEnum.UNSUPPORTED;
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(),e);
			code = GENI_CodeEnum.SERVERERROR;
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(),e);
			code = GENI_CodeEnum.FORBIDDEN;
		}
	}

	@Override
	public AMResult processRequest(ListCredentials credentials,
			Object... specificArgs) {
		// TODO Auto-generated method stub
		return null;
	}

}
