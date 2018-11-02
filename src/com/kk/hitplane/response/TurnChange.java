package com.kk.hitplane.response;

import com.kk.hitplane.Response;
import com.kk.hitplane.battle.Battle;

import net.sf.json.JSONObject;

public class TurnChange extends Response {
	public int id;

	public int row;
	public int col;

	@Override
	public boolean exe() {
		JSONObject tile = mJson.getJSONObject("tile");
		int status = tile.getInt("status");

		Battle.getInstance().onTurnChange(id, row, col, status);
		return true;
	}
}
