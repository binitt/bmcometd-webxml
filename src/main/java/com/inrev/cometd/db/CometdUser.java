package com.inrev.cometd.db;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inrev.utils.db.IRDal;
import com.inrev.utils.db.IRDataTable;
import com.inrev.utils.db.IRRow;

public class CometdUser {
	static final Logger LOG = LoggerFactory.getLogger(CometdUser.class);
	
	public String user;
	public String token;
	public boolean active;
	
	private static Map<String, CometdUser> users;
	private static long lastLoaded = 0;
	/**
	 * Loads every 5 mins
	 * @return
	 * @throws Exception
	 */
	public static synchronized Map<String, CometdUser> getUsers() throws Exception {
		long elapsedMs = System.currentTimeMillis() - lastLoaded;
		if (elapsedMs > 5 * 60 * 1000) {
			// reload
			users = readAll();
			lastLoaded = System.currentTimeMillis();
		} 

		return users;
	}
	
	public static Map<String, CometdUser> readAll() throws Exception {
		Map<String, CometdUser> lst = new HashMap<String, CometdUser>();
		
		IRDataTable dt = new IRDal().read("select * from t_cometd_user where active=true",
				new Object[0]);
		for (IRRow dr : dt.rows) {
			CometdUser obj = getObjFromDR(dr);
			lst.put(obj.user, obj);
		}
		return lst;
	}
	
	public static CometdUser getObjFromDR(IRRow dr) {
		CometdUser obj = new CometdUser();
		obj.user = dr.getString("user");
		obj.token = dr.getString("token");
		obj.active = dr.getBoolean("active");
		
		return obj;
	}
}
