package com.kk.hitplane.response;

import com.kk.hitplane.Response;
import com.kk.hitplane.battle.Battle;

public class UserInfo extends Response {
	public int id;

	@Override
	public boolean exe() {
		Battle.getInstance().onUserInfo(id);
		return true;
	}
}
