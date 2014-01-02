package spot;

import java.util.ArrayList;

import util.MyUtil;

/**
 * スポットのリストクラス
 * @author kohta
 */
public class SpotList {
	/**
	 *  存在するすべてのスポットを保持するリスト
	 */
	private ArrayList<Spot> spotList;

	/**
	 * ホームスポット
	 */
	private Spot home;

	/**
	 * コンストラクタ
	 */
	public SpotList(Class<?> homeSpot){
		spotList = new ArrayList<Spot>();
		spotList.add(new Home());
		spotList.add(new Company());
		spotList.add(new School());
		for(Spot spot:spotList){
			if(spot.getClass() == homeSpot){
				this.home = spot;
			}
		}
	}

	/**
	 * スポットリストを取得
	 * @return spotList
	 */
	public ArrayList<Spot> getSpotList(){
		return spotList;
	}

	/**
	 * ホームスポットを取得
	 * @return home
	 */
	public Spot getHomeSpot(){
		return home;
	}

	/**
	 * spotリストからランダムにspotを取得
	 * @return random
	 */
	public Spot getRandomSpot(){
		int random = (int)MyUtil.random(0, spotList.size()-1);
		return spotList.get(random);
	}

	/**
	 * すべてのspotにいるエージェントをホームスポットに移動する
	 */
	public void moveAgentToHomeSpot(){
		for(Spot spot:spotList){
			if(spot!=home){
				spot.moveAllAgent(home);
			}
		}
	}
}
