package com.kk.hitplane.response;

import com.kk.hitplane.Response;
import com.kk.hitplane.log.Level;
import com.kk.hitplane.log.Logger;

public class Logout extends Response {
	public String reason;

	@Override
	public boolean exe() {
		Logger.getInstance().print(null, Level.E, reason);
		return true;
	}
}
