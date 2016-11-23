package org.fiteagle.adapter.openstackvmadapter.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.fiteagle.adapter.openstackvmadapter.client.model.Images;
import org.fiteagle.adapter.openstackvmadapter.client.model.Server;

import com.woorea.openstack.base.client.Entity;
import com.woorea.openstack.base.client.HttpMethod;
import com.woorea.openstack.base.client.OpenStackRequest;
import com.woorea.openstack.connector.JerseyConnector;
import com.woorea.openstack.keystone.Keystone;
import com.woorea.openstack.keystone.api.TokensResource;
import com.woorea.openstack.keystone.model.Access;
import com.woorea.openstack.keystone.model.Tenant;
import com.woorea.openstack.keystone.model.Tenants;
import com.woorea.openstack.keystone.model.authentication.TokenAuthentication;
import com.woorea.openstack.keystone.model.authentication.UsernamePassword;
import com.woorea.openstack.nova.Nova;
import com.woorea.openstack.nova.api.ServersResource.AssociateFloatingIp;
import com.woorea.openstack.nova.model.Flavors;
import com.woorea.openstack.nova.model.FloatingIp;
import com.woorea.openstack.nova.model.FloatingIpPools;
import com.woorea.openstack.nova.model.FloatingIpPools.FloatingIpPool;

public class OfflineTestClient extends OpenstackClient {

	OpenstackParser openstackParser;
	String tenantId = "";

	public OfflineTestClient() {
		this.openstackParser = new OpenstackParser();
	}

	public Flavors listFlavors() {

		try {
			String flavorsString = getRessourceString("/flavors.json");
			return openstackParser.parseToFlavors(flavorsString);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Images listImages() {
		String imagesString = getRessourceString("/images.json");
		try {
			return openstackParser.parseToImages(imagesString);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public org.fiteagle.adapter.openstackvmadapter.client.model.Server createServer() {
		String serverString = getRessourceString("/server.json");
		try {
			return openstackParser.parseToServer(serverString);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		// Server server = new Server();
		// Flavors flavors = this.listFlavors();
		// Images images = this.listImages();
		// server.setFlavor(flavors.getList().get(0));
		// server.setImage(images.getList().get(0));

	}

	public org.fiteagle.adapter.openstackvmadapter.client.model.Server createServer(
			String imageId, String flavorId, String serverName) {
		return this.createServer();
	}

	public Server getServerDetails(String id) {
		String serverString = getRessourceString("/serverDetail.json");
		try {
			return openstackParser.parseToServer(serverString);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private Access getAccessWithTenantId() {
//		Keystone keystone = new Keystone(Utils.KEYSTONE_AUTH_URL,
//				new JaxRs20Connector());
		Keystone keystone = new Keystone(Utils.KEYSTONE_AUTH_URL,
				new JerseyConnector());

		TokensResource tokens = keystone.tokens();
		Access access = tokens
				.authenticate(
						new UsernamePassword(Utils.KEYSTONE_USERNAME,
								Utils.KEYSTONE_PASSWORD))
				.withTenantName(Utils.TENANT_NAME).execute();
		keystone.token(access.getToken().getId());

		Tenants tenants = keystone.tenants().list().execute();

		List<Tenant> tenantsList = tenants.getList();

		if (tenants.getList().size() > 0) {
			for (Iterator iterator = tenantsList.iterator(); iterator.hasNext();) {
				Tenant tenant = (Tenant) iterator.next();
				if (tenant.getName().compareTo(Utils.TENANT_NAME) == 0) {
					tenantId = tenant.getId();
					break;
				}
			}

		} else {
			throw new RuntimeException("No tenants found!");
		}

		access = tokens
				.authenticate(
						new TokenAuthentication(access.getToken().getId()))
				.withTenantId(tenantId).execute();

		return access;
	}

	public void allocateFloatingIpForServer(String serverId, String floatingIp) {
		Access access = getAccessWithTenantId();
		Nova novaClient = new Nova(Utils.NOVA_ENDPOINT.concat("/").concat(
				tenantId));
		novaClient.token(access.getToken().getId());

		com.woorea.openstack.nova.model.ServerAction.AssociateFloatingIp action = new com.woorea.openstack.nova.model.ServerAction.AssociateFloatingIp(
				floatingIp);
		AssociateFloatingIp associateFloatingIp = new AssociateFloatingIp(
				serverId, action);
		OpenStackRequest<com.woorea.openstack.nova.model.ServerAction.AssociateFloatingIp> request = new OpenStackRequest<com.woorea.openstack.nova.model.ServerAction.AssociateFloatingIp>(
				novaClient,
				HttpMethod.POST,
				"/servers/" + serverId + "/action",
				associateFloatingIp.json(action),
				com.woorea.openstack.nova.model.ServerAction.AssociateFloatingIp.class);

		try {
			novaClient.execute(request);
		} catch (Exception e) {
//			if ((e instanceof org.glassfish.jersey.message.internal.MessageBodyProviderNotFoundException))
//				throw new RuntimeException(e);

			System.out.println(e);
		}
		//
		// com.woorea.openstack.nova.model.ServerAction.AssociateFloatingIp
		//
		// request.json(action);
		// org.fiteagle.adapter.openstackvmadapter.client.model.Server
		// serverDetail = novaClient
		// .execute(request);

		// com.woorea.openstack.nova.model.ServerAction.AssociateFloatingIp
		// associateFloatingIp =
		// (com.woorea.openstack.nova.model.ServerAction.AssociateFloatingIp)
		// novaClient.servers()
		// .associateFloatingIp(serverId, floatingIp).execute();
		// Object associated = associateFloatingIp.execute();
		// return associated;
		// return response;
	}

	public FloatingIpPools getFloatingIpPools() {
		Access access = getAccessWithTenantId();
		Nova novaClient = new Nova(Utils.NOVA_ENDPOINT.concat("/").concat(
				tenantId));
		novaClient.token(access.getToken().getId());

		// OpenStackRequest<FloatingIpDomains> request = new
		// OpenStackRequest<FloatingIpDomains>(
		// novaClient, HttpMethod.GET, "/os-floating-ip-dns", null,
		// FloatingIpDomains.class);

		OpenStackRequest<FloatingIpPools> request = new OpenStackRequest<FloatingIpPools>(
				novaClient, HttpMethod.GET, "/os-floating-ip-pools", null,
				FloatingIpPools.class);
		FloatingIpPools floatingIpPools = novaClient.execute(request);
		return floatingIpPools;

		// FloatingIpDomains floatingIpDomains = novaClient.execute(request);
		// return floatingIpDomains;
	}

	public FloatingIp addFloatingIp() {

		String floatingIpString = getRessourceString("/floatingIp.json");
		try {
			return openstackParser.parseToFloatingIp(floatingIpString);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	private String getRessourceString(String path) {
		InputStream in = this.getClass().getResourceAsStream(path);
		return convertStreamToString(in);
	}

	  /*
	  Parts (or the complete Code) of the following method(s) were inspired or copied
	  from StackOverFlow/StackExchange.
	  Because of the Licensing of StackOverFlow under the CC-BY-SA [*¹] and MIT[*²] License this comment
	  has to link the original code:
	  
	  Link: https://stackoverflow.com/a/5445161
	  */
	private String convertStreamToString(InputStream is) {
		@SuppressWarnings("resource")
		Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}

/*
[1] https://creativecommons.org/licenses/by-sa/3.0/


[2] https://opensource.org/licenses/MIT
	The MIT License (MIT)
	Copyright (c) 2016 StackExchange

	Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files 
	(the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, 
	publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, 
	subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
	MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
	FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION 
	WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

