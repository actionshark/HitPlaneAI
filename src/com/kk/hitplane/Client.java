package com.kk.hitplane;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.kk.hitplane.log.Level;
import com.kk.hitplane.log.Logger;
import com.kk.hitplane.request.Login;

public class Client extends WebSocketClient {
	private static Client sClient;
	
	public static String url;

	public static synchronized Client getInstance() {
		if (sClient == null || !sClient.isOpen()) {
			try {
				URI uri = new URI(url);
				sClient = new Client(uri);
				sClient.connect();
			} catch (Exception e) {
				Logger.getInstance().print(null, Level.E, e);

				sClient = null;
			}
		}

		return sClient;
	}

	private Client(URI uri) {
		super(uri);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		Logger.getInstance().print(null, Level.I, "onOpen");

		new Login().send();
	}

	@Override
	public void onMessage(String message) {
		Logger.getInstance().print(null, Level.I, "onMessage", message);

		Response.dispatch(message);
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		Logger.getInstance().print(null, Level.I, "onClose", code, reason, remote);
	}

	@Override
	public void onError(Exception ex) {
		Logger.getInstance().print(null, Level.E, ex);
	}
}
