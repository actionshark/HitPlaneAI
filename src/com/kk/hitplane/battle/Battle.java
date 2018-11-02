package com.kk.hitplane.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.kk.hitplane.request.PlayBattle;

public class Battle {
	private static final Battle sInstance = new Battle();

	public static Battle getInstance() {
		return sInstance;
	}

	public static final int ROW_NUM = 8;
	public static final int COL_NUM = 8;

	public static final int[][] PLANE = new int[][] {
			{ Tile.TILE_EMPTY, Tile.TILE_EMPTY, Tile.TILE_HEAD, Tile.TILE_EMPTY, Tile.TILE_EMPTY, },
			{ Tile.TILE_BODY, Tile.TILE_BODY, Tile.TILE_BODY, Tile.TILE_BODY, Tile.TILE_BODY, },
			{ Tile.TILE_EMPTY, Tile.TILE_EMPTY, Tile.TILE_BODY, Tile.TILE_EMPTY, Tile.TILE_EMPTY, },
			{ Tile.TILE_EMPTY, Tile.TILE_BODY, Tile.TILE_BODY, Tile.TILE_BODY, Tile.TILE_EMPTY, }, };

	private int[][] mMap;

	private int mInjuryTop;
	private int mInjuryBtm;
	private int mInjuryLeft;
	private int mInjuryRight;

	private int mId;

	private Random mRandom = new Random();

	public void onUserInfo(int id) {
		mId = id;
	}

	protected boolean isMyTurn(int turn) {
		return mId == turn;
	}

	public void onBattleInfo(int[][] status, int turn) {
		mMap = new int[ROW_NUM][COL_NUM];

		mInjuryTop = ROW_NUM;
		mInjuryBtm = 0;
		mInjuryLeft = COL_NUM;
		mInjuryRight = 0;

		for (int row = 0; row < ROW_NUM; row++) {
			for (int col = 0; col < COL_NUM; col++) {
				int st = status[row][col];

				mMap[row][col] = st;

				if (st == Tile.STATUS_INJURY) {
					mInjuryTop = Math.min(mInjuryTop, row);
					mInjuryBtm = Math.max(mInjuryBtm, row);
					mInjuryLeft = Math.min(mInjuryLeft, col);
					mInjuryRight = Math.max(mInjuryRight, col);
				}
			}
		}

		if (isMyTurn(turn)) {
			play();
		}
	}

	public void onTurnChange(int turn, int row, int col, int status) {
		mMap[row][col] = status;

		if (status == Tile.STATUS_INJURY) {
			mInjuryTop = Math.min(mInjuryTop, row);
			mInjuryBtm = Math.max(mInjuryBtm, row);
			mInjuryLeft = Math.min(mInjuryLeft, col);
			mInjuryRight = Math.max(mInjuryRight, col);
		}

		if (isMyTurn(turn)) {
			play();
		}
	}

	protected void play() {
		int max = 0;
		int[][] counts = count();

		for (int row = 0; row < ROW_NUM; row++) {
			for (int col = 0; col < COL_NUM; col++) {
				max = Math.max(max, counts[row][col]);
			}
		}

		List<Integer> result = new ArrayList<>();

		for (int row = 0; row < ROW_NUM; row++) {
			for (int col = 0; col < COL_NUM; col++) {
				if (counts[row][col] == max) {
					int idx = COL_NUM * row + col;
					result.add(idx);
				}
			}
		}

		int index = mRandom.nextInt(result.size());
		index = result.get(index);

		PlayBattle pb = new PlayBattle();
		pb.row = index / COL_NUM;
		pb.col = index % COL_NUM;
		pb.send();
	}

	protected int[][] count() {
		int[][] counts = new int[ROW_NUM][COL_NUM];
		for (int row = 0; row < ROW_NUM; row++) {
			for (int col = 0; col < COL_NUM; col++) {
				counts[row][col] = 0;
			}
		}

		///////////////////////////////////////////////////////////////////////////

		int[][] plane = new int[PLANE.length][PLANE[0].length];
		for (int row = 0; row < PLANE.length; row++) {
			for (int col = 0; col < PLANE[0].length; col++) {
				plane[row][col] = PLANE[row][col];
			}
		}

		countOne(counts, plane);

		///////////////////////////////////////////////////////////////////////////

		plane = new int[PLANE.length][PLANE[0].length];
		for (int row = 0; row < PLANE.length; row++) {
			for (int col = 0; col < PLANE[0].length; col++) {
				plane[row][col] = PLANE[PLANE.length - 1 - row][col];
			}
		}

		countOne(counts, plane);

		///////////////////////////////////////////////////////////////////////////

		plane = new int[PLANE[0].length][PLANE.length];
		for (int row = 0; row < PLANE[0].length; row++) {
			for (int col = 0; col < PLANE.length; col++) {
				plane[row][col] = PLANE[col][row];
			}
		}

		countOne(counts, plane);

		///////////////////////////////////////////////////////////////////////////

		plane = new int[PLANE[0].length][PLANE.length];
		for (int row = 0; row < PLANE[0].length; row++) {
			for (int col = 0; col < PLANE.length; col++) {
				plane[row][col] = PLANE[PLANE.length - 1 - col][row];
			}
		}

		countOne(counts, plane);

		return counts;
	}

	protected void countOne(int[][] counts, int[][] plane) {
		int rowNum = plane.length;
		int colNum = plane[0].length;

		int hr = -1;
		int hc = -1;

		for (int row = 0; row < rowNum; row++) {
			for (int col = 0; col < colNum; col++) {
				if (plane[row][col] == Tile.TILE_HEAD) {
					hr = row;
					hc = col;
					break;
				}
			}

			if (hr != -1) {
				break;
			}
		}

		int top = Math.max(mInjuryBtm - rowNum + 1, 0);
		int btm = Math.min(mInjuryTop, ROW_NUM - rowNum);
		int left = Math.max(mInjuryRight - colNum + 1, 0);
		int right = Math.min(mInjuryLeft, COL_NUM - colNum);

		for (int row = top; row <= btm; row++) {
			for (int col = left; col <= right; col++) {
				if (match(plane, row, col)) {
					counts[hr + row][hc + col]++;
				}
			}
		}
	}

	protected boolean match(int[][] plane, int row, int col) {
		for (int r = 0; r < plane.length; r++) {
			for (int c = 0; c < plane[0].length; c++) {
				int status = mMap[r + row][c + col];
				int tile = plane[r][c];

				if (status == Tile.STATUS_NORMAL) {
					continue;
				}

				if (status == Tile.STATUS_MISS && tile == Tile.TILE_EMPTY) {
					continue;
				}

				if (status == Tile.STATUS_INJURY && tile == Tile.TILE_BODY) {
					continue;
				}

				return false;
			}
		}

		return true;
	}
}
