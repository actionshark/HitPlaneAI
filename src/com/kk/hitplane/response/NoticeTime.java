package com.kk.hitplane.response;

import com.kk.hitplane.Response;

public class NoticeTime extends Response {
	public long time;
	
	@Override
	public boolean exe() {
		return false;
	}
}
