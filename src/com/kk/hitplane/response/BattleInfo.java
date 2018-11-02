package com.kk.hitplane.response;

import com.kk.hitplane.Response;
import com.kk.hitplane.battle.Battle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BattleInfo extends Response {
	public int turn;

	@Override
	public boolean exe() {
		int[][] status = new int[Battle.ROW_NUM][Battle.COL_NUM];

		JSONArray tiles = mJson.getJSONArray("tiles");

		for (int row = 0; row < Battle.ROW_NUM; row++) {
			for (int col = 0; col < Battle.COL_NUM; col++) {
				JSONObject tile = tiles.getJSONObject(Battle.COL_NUM * row + col);
				status[row][col] = tile.getInt("status");
			}
		}

		Battle.getInstance().onBattleInfo(status, turn);
		return true;
	}
}
