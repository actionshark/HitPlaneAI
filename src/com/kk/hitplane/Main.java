package com.kk.hitplane;

import com.kk.hitplane.log.FileLogger;
import com.kk.hitplane.log.Logger;
import com.kk.hitplane.request.GetTime;
import com.kk.hitplane.util.ThreadUtil;

public class Main {
	public static void main(String[] args) {
		FileLogger logger = new FileLogger();
		logger.setFiles("log1.txt", "log2.txt");
		Logger.setInstance(logger);

		ThreadUtil.run(() -> {
			if (Client.getInstance().isOpen()) {
				new GetTime().send();
			} else {
				Client.getInstance().connect();
			}
		}, 3000, 20000, -1);
	}
}
