package spot;

import java.util.ArrayList;
import agent.Agent;

/**
 * 場所を示すための親クラス
 */
public class Spot {
	/**
	 *  スポットにいるエージェントのリスト
	 */
	private ArrayList <Agent> agentList;
	/**
	 *  コンストラクタ
	 */
	public Spot(){
		agentList = new ArrayList<Agent>();
	}

	/**
	 * エージェントをリストに加える
	 * @param agent
	 */
	public void addAgent(Agent agent){
		// エージェントリストに加える
		agentList.add(agent);
	}

	/**
	 * エージェントのリストを全てスポットのエージェントリストに加える
	 * @param agList
	 */
	public void addAllAgent(ArrayList<Agent> agList){
		// エージェントリストに加える
		this.agentList.addAll(agList);
	}

	/**
	 * スポットのエージェントリストをゲットする
	 * @return agnetList
	 */
	public ArrayList<Agent> getAgentList(){
		return agentList;
	}

	/**
	 * エージェントを移動させる
	 * @param agent
	 * @param toSpot
	 */
	public void moveAgent(Agent agent,Spot toSpot){
		toSpot.addAgent(agent);
		agentList.remove(agent);
	}

	/**
	 * 全てのエージェントを移動させる
	 * @param toSpot
	 */
	public void moveAllAgent(Spot toSpot){
		toSpot.addAllAgent(agentList);
		agentList.clear();
	}
}
