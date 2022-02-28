package com.sap;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.server.JCoServerContext;
import com.sap.conn.jco.server.JCoServerFunctionHandler;

public class AbapCallHandler implements JCoServerFunctionHandler {
	
	/**
	 * This handler only supports one function with name {@code Z_SAMPLE_ABAP_CONNECTOR_CALL}.
	 */
	public static final String FUNCTION_NAME = "Z_SAMPLE_ABAP_CONNECTOR_CALL";
	
	private static Logger logger = Logger.getLogger(AbapCallHandler.class);
	
	private void printRequestInformation(JCoServerContext serverCtx, JCoFunction function) {
		logger.info("----------------------------------------------------------------");
        logger.info("call              : " + function.getName());
        logger.info("ConnectionId      : " + serverCtx.getConnectionID());
        logger.info("SessionId         : " + serverCtx.getSessionID());
        logger.info("TID               : " + serverCtx.getTID());
        logger.info("repository name   : " + serverCtx.getRepository().getName());
        logger.info("is in transaction : " + serverCtx.isInTransaction());
        logger.info("is stateful       : " + serverCtx.isStatefulSession());
        logger.info("----------------------------------------------------------------");
        logger.info("gwhost: " + serverCtx.getServer().getGatewayHost());
        logger.info("gwserv: " + serverCtx.getServer().getGatewayService());
        logger.info("progid: " + serverCtx.getServer().getProgramID());
        logger.info("----------------------------------------------------------------");
        logger.info("attributes  : ");
        logger.info(serverCtx.getConnectionAttributes().toString());
        logger.info("----------------------------------------------------------------");
	}

	public void handleRequest(JCoServerContext serverCtx, JCoFunction function) {
		// Check if the called function is the supported one.
		if(!function.getName().equals(FUNCTION_NAME)) {
			logger.error("Function '"+function.getName()+"' is no supported to be handled!");
			return;
		}
        printRequestInformation(serverCtx, function);

        // Get the URI provided from Abap.
        String url = function.getImportParameterList().getString("URL");
        
		HttpCaller main = new HttpCaller();
		main.initializeSslContext();
		main.initializeClient();
		String payload = null;
		try {
			payload = main.invokeGet(url);
			System.out.println(payload);
		} catch(IOException | InterruptedException e) {
			// Provide the exception as payload.
			payload = e.getMessage();
		}
		// Provide the payload as exporting parameter.
        function.getExportParameterList().setValue("RESPONSE_PAYLOAD", payload);
    }
}