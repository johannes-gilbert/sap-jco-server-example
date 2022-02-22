package com.sap;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

public class HttpCaller {
	
	public static Logger logger = Logger.getLogger(HttpCaller.class); 
	
	private SSLContext context;
	
	private Client client;
	
	public HttpCaller() {
	}
	
	public void initializeSslContext() {
		// Initialize an SSL context.
		try {			
			/*
			 * http://stackoverflow.com/questions/6047996/ignore-self-signed-ssl-cert-using-jersey-client
			 * for
			 * javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: PKIX path building failed: 
			 * sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
			 */
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
			    public X509Certificate[] getAcceptedIssuers(){return null;}
			    public void checkClientTrusted(X509Certificate[] certs, String authType){}
			    public void checkServerTrusted(X509Certificate[] certs, String authType){}
			}};

			context = SSLContext.getInstance("TLS");
			context.init(null/*keyManagerFactory.getKeyManagers()*/, trustAllCerts, new SecureRandom());
		
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			logger.error(e);
		}
	}
	
	public void initializeClient() {	
		// In case you are not using any proxy delete these lines.
        // In case you are using a proxy replace <host> and <port>.
		System.setProperty ("https.proxyHost", "<host>");
		System.setProperty ("https.proxyPort", "<port>");
		System.setProperty ("http.proxyHost", "<host>");
		System.setProperty ("http.proxyPort", "<port>");
		    
		// Build a client.
		client = ClientBuilder.newBuilder()
				.sslContext(context)
				.hostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String hostname, SSLSession session) {
						// Just accept any host. Do not do an actual verification.
						return true;
					}
				})
				.build();
	}
	
	public String invokeGet(String uri) {
		logger.info("URL: "+uri);
		WebTarget target = client.target(uri);
		Invocation.Builder invocationBuilder = target.request();
		// Set necessary headers.
		invocationBuilder.header("Accept","*/*");
		invocationBuilder.header("Accept-Encoding","gzip, deflate, sdch, br");
		invocationBuilder.header("Accept-Language","de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4");
		Response response = invocationBuilder.get();
		return response.readEntity(String.class);
	}
}