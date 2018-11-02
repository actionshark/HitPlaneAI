package com.kk.hitplane.response;

import com.kk.hitplane.Response;
import com.kk.hitplane.log.Level;
import com.kk.hitplane.log.Logger;
import com.kk.hitplane.request.GetBattleInfo;

public class LoginResult extends Response {
	public String error;

	@Override
	public boolean exe() {
		if (error == null) {
			Logger.getInstance().print(null, Level.I, "login success");

			new GetBattleInfo().send();
		} else {
			Logger.getInstance().print(null, Level.E, error);
		}

		return true;
	}
}
