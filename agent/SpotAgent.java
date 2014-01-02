package agent;

import spot.Spot;

public class SpotAgent extends Agent {

	/**
	 * 今居るSpot
	 */
	private Spot nowSpot;

	/**
	 *
	 */
	private Spot homeSpot;

	/**
	 * コンストラクタ
	 * @param life
	 * @param growRoleNum
	 * @param spot
	 */
	public SpotAgent(int life, int growRoleNum,Spot spot,Spot home) {
		super(life, growRoleNum);
		this.nowSpot = spot;
		this.homeSpot = home;
	}

	/**
	 * スポットを移動させる
	 * @param spot
	 */
	public void moveSpot(Spot spot){
		this.nowSpot = spot;
	}

	/**
	 * 現在居るSpotのgetter
	 * @return nowSpot
	 */
	public Spot getSpot(){
		return nowSpot;
	}
}
