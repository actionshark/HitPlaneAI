package com.kk.hitplane;

import com.kk.hitplane.log.FileLogger;
import com.kk.hitplane.log.Logger;
import com.kk.hitplane.request.GetTime;
import com.kk.hitplane.util.ThreadUtil;

public class Main {
	public static void main(String[] args) {
		String url = "ws://111.231.232.54:10001";
		
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			
			if ("-url".equals(arg)) {
				url = args[++i];
			}
		}
		
		FileLogger logger = new FileLogger();
		logger.setFiles("log1.txt", "log2.txt");
		Logger.setInstance(logger);
		
		Client.url = url;

		ThreadUtil.run(() -> {
			Client client = Client.getInstance();
			
			if (client != null && client.isOpen()) {
				new GetTime().send();
			}
		}, 3000, 20000, -1);
	}
}
