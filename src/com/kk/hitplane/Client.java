package com.kk.hitplane;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import com.kk.hitplane.log.Level;
import com.kk.hitplane.log.Logger;
import com.kk.hitplane.request.Login;
import com.kk.hitplane.util.ThreadUtil;

public class Client extends WebSocketClient {
	public static final String URL = "ws://111.231.232.54:10001/hitplane";

	private static Client sClient;

	public static synchronized Client getInstance() {
		if (sClient == null) {
			try {
				URI uri = new URI(URL);
				sClient = new Client(uri);
			} catch (Exception e) {
				Logger.getInstance().print(null, Level.E, e);
			}
		}

		return sClient;
	}

	private Client(URI uri) {
		super(uri, new Draft_6455());
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

		ThreadUtil.run(() -> {
			getInstance().connect();
		}, 5000);
	}

	@Override
	public void onError(Exception ex) {
		Logger.getInstance().print(null, Level.E, ex);
	}
}
