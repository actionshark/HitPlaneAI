package com.kk.hitplane.response;

import com.kk.hitplane.Response;
import com.kk.hitplane.log.Level;
import com.kk.hitplane.log.Logger;

public class ShowToast extends Response {
	public String text;

	@Override
	public boolean exe() {
		Logger.getInstance().print(null, Level.I, text);

		return true;
	}
}
