package com.inrev.cometd.auth;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.oort.Oort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inrev.cometd.HelloService;

public class AppInitializer extends GenericServlet {
	private static final long serialVersionUID = 1L;
	static final Logger LOG = LoggerFactory.getLogger(AppInitializer.class);
	
    @Override
    public void init() throws ServletException {
    	LOG.info("Initializing");
        BayeuxServer bayeux = (BayeuxServer)getServletContext().getAttribute(BayeuxServer.ATTRIBUTE);
        
        Oort oort = (Oort)getServletContext().getAttribute(Oort.OORT_ATTRIBUTE);
        
        Users u = new Users();
        AppAuthenticator authenticator = new AppAuthenticator(u, oort);
        bayeux.setSecurityPolicy(authenticator);
        new HelloService(bayeux);
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        throw new ServletException();
    }
}