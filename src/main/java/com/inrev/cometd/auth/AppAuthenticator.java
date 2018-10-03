package com.inrev.cometd.auth;

import java.util.HashMap;
import java.util.Map;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.oort.Oort;
import org.cometd.server.DefaultSecurityPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppAuthenticator extends DefaultSecurityPolicy implements
		ServerSession.RemoveListener {
	static final Logger LOG = LoggerFactory.getLogger(AppAuthenticator.class);
	private final Users users;
	private final Oort oort;

	public AppAuthenticator(Users users, Oort oort) {
		this.users = users;
		this.oort = oort;
	}

	@Override
	public boolean canHandshake(BayeuxServer server, ServerSession session,
			ServerMessage message) {
		boolean promise;
		if (session.isLocalSession()) {
			LOG.info("true: local session allowed");
			promise = true;
			return promise;
		}
		
		Map<String, Object> ext = message.getExt();
		if (ext == null) {
			LOG.info("false: ext not received");
			promise = false;
			return promise;
		}

		// Remote Oort nodes are allowed to handshake
        if (oort.isOortHandshake(message)) {
        	LOG.info("true: oort handshake");
            promise = true;
            return promise;
        }

		@SuppressWarnings("unchecked")
		Map<String, Object> authentication = (Map<String, Object>) ext
				.get("com.inrev.authn");
		if (authentication == null) {
			LOG.info("false: auth null");
			promise = false;
			return promise;
		}

		boolean authenticationData = verify(authentication);
		if (authenticationData == false) {
			LOG.info("false: invalid auth tokens");
			// customize msg
			sendAuthFailedMsg(message);
			
			promise = false;
			return promise;
		} 

		// Authentication successful

		// Link authentication data to the session
		users.put(session, authentication);
		
		// Be notified when the session disappears
		session.addListener(this);

		promise = true;
		return promise;
	}

	public void removed(ServerSession session, boolean expired) {
		// Unlink authentication data from the remote client
	}

	boolean verify(Map<String, Object> auth) {
		if (auth.containsKey("user") && auth.containsKey("token")) {
			String user = (String)auth.get("user");
			String token = (String)auth.get("token");
			if (user.equals("cometuser1") && token.equals("adfwew283292")) {
				LOG.info("Verify succeeded for user: {}", user);
				return true;
			} else {
				LOG.info("Verify failed for user: {}, token: {}", user, token);
				return false;
			}
		}
		LOG.info("Verify failed, user / token absent");
		return false;
	}
	
	void sendAuthFailedMsg(ServerMessage message) {
		LOG.info("Sending failed auth msg");
        // Retrieve the handshake response
        ServerMessage.Mutable handshakeReply = message.getAssociated();

        // Modify the advice, in this case tell to try again
        // If the advice is not modified it will default to disconnect the client
        Map<String, Object> advice = handshakeReply.getAdvice(true);
        advice.put(Message.RECONNECT_FIELD, Message.RECONNECT_HANDSHAKE_VALUE);

        // Modify the ext field with extra information on the authentication failure
        Map<String, Object> ext = handshakeReply.getExt(true);
        Map<String, Object> authentication = new HashMap<String, Object>();
        ext.put("com.inrev.authn", authentication);
        authentication.put("failureReason", "invalid_credentials");
	}
}


