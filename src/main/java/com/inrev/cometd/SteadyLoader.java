package com.inrev.cometd;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inrev.cometd.db.CometdUser;
import com.inrev.utils.BMConfig;
import com.inrev.utils.Utility;
import com.inrev.utils.db.IRDal;

public class SteadyLoader implements ServletContextListener {
	static final Logger LOG = LoggerFactory.getLogger(SteadyLoader.class);
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			IRDal.DefaultConnStr = BMConfig.ConnectionString;
			LOG.info("Loaded {} active cometd users", CometdUser.getUsers().size());
		} catch (Exception ex) {
			LOG.error("Got exception: {}", Utility.stringifyException(ex));
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
