package com.kk.hitplane.response;

import com.kk.hitplane.Response;
import com.kk.hitplane.request.AcceptRequest;

public class RequestBegin extends Response {
	public int id;
	public String nickname;
	public boolean active;

	@Override
	public boolean exe() {
		if (!active) {
			AcceptRequest ar = new AcceptRequest();
			ar.enemy = id;
			ar.send();
		}

		return true;
	}
}
