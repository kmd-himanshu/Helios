package com.helio.boomer.rap.service;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class WSHandler {

	public static String BASEURISTRING = 
		"http://helioenergysolutions.virtual.vps-host.net:6060/serviceapi/restful-services/service";

	public static WebResource service;
	
	public static URI getBaseURI() {
		return UriBuilder.fromUri(BASEURISTRING).build();
	}

	public static WebResource getWebResourceService() {
		if (service == null) {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			service = client.resource(getBaseURI());
		}
		return service;
	}

}
