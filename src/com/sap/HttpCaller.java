package com.sap;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

public class HttpCaller {
	
	public static Logger logger = Logger.getLogger(HttpCaller.class); 
	
	private SSLContext context;
	
	private HttpClient client;
	
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
		/*System.setProperty ("https.proxyHost", "<host>");
		System.setProperty ("https.proxyPort", "<port>");
		System.setProperty ("http.proxyHost", "<host>");
		System.setProperty ("http.proxyPort", "<port>");*/
		    
		// Build a client.
		client = HttpClient.newBuilder()
				.followRedirects(HttpClient.Redirect.ALWAYS)
	            .version(HttpClient.Version.HTTP_1_1)
				.sslContext(context).build();
	}
	
	public String invokeGet(String url) throws IOException, InterruptedException {
		logger.info("URL: "+url);
		HttpRequest request = HttpRequest.newBuilder(URI.create(url))
				.header("Accept","*/*")
				.header("Accept-Language","de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4")
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		return response.body();
	}
}