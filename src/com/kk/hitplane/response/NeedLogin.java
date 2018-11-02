package com.kk.hitplane.response;

import com.kk.hitplane.Response;
import com.kk.hitplane.request.Login;

public class NeedLogin extends Response {
	@Override
	public boolean exe() {
		new Login().send();
		return true;
	}
}
