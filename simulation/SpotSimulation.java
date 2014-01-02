package simulation;

import field.Field;
import agent.Agent;
import spot.Spot;
import spot.Home;
import spot.SpotList;
import java.util.ArrayList;

/**
 * スポットでシミュレーションを行うクラス
 * @author kohta
 *
 */
public class SpotSimulation {
	// ループ停止時間
	final int SLEEP = 300;
	// シミュレーション年数
	final int ENDYEAR = 100;
	// エージェントの人数
	final int AGENT_NUM = 10;
	/**
	 *  シミュレーションフィールド
	 */
	Field field;
	/**
	 *  エージェントのリスト
	 */
	ArrayList<Agent> agentList;
	/**
	 *  スポットのリスト
	 */
	SpotList spotList;

	public static void main(String[] args){
		new SpotSimulation().start();
	}

	/**
	 * シミュレーションの中身
	 */
	void start(){
		// 初期化
		init();

		// シミュレーションループ
		while(!field.isEnd()){
			// 見やすくするための停止
			try {
				Thread.sleep(SLEEP);
			} catch (InterruptedException e) {
				System.out.println(e);
			}
			// 場の一年開始
			startYear();
			// 行動
			act();
			// 表示
			show();
			// 場の一年経過
			passYear();
		}
	}

//======================================================================
// 初期化
//======================================================================
	/**
	 * 総合初期化処理
	 */
	void init(){
		// フィールドの初期化
		field = new Field(400,200,ENDYEAR);
		// フィールドの表示
		field.showField();
		// スポットの初期化
		initSpot();
		// エージェンの初期化
		initAgent();
	}

	/**
	 * スポットの初期化
	 */
	void initSpot(){
		// スポットリストの初期化
		spotList = new SpotList(Home.class);
	}

	/**
	 * エージェントの初期化
	 */
	void initAgent(){
		// エージェントリストの初期化
		agentList = new ArrayList<Agent>();
		// エージェントの寿命
		int life = 100;
		// エージェントが成長させられるロールの数
		int roleNum = 3;
		for(int i=0;i<AGENT_NUM;i++){
			// エージェントの生成
			Agent agent = new Agent(life,roleNum);
			// エージェントリストに配置
			agentList.add(agent);
			// エージェントを初期スポットに配置
			spotList.getHomeSpot().addAgent(agent);
		}
	}

//======================================================================
// 年間開始処理
//======================================================================
	/**
	 * 総合年間開始処理
	 */
	void startYear(){
		// ホームスポットからエージェントを移動する
		for(Agent agent:agentList){
			spotList.getHomeSpot().moveAgent(agent, spotList.getRandomSpot());
		}
		// エージェントの年間開始処理
		startAgentYear();
	}

	/**
	 * エージェント年間開始処理
	 */
	void startAgentYear(){
		for(Agent agent:agentList){
			agent.startYear();
		}
	}

//======================================================================

	/**
	 * エージェントの総合行動処理
	 */
	void act(){
		// スポットごとに処理
		for(Spot spot:spotList.getSpotList()){
			// スポットにいるすべてのエージェントの処理
			for(Agent agent:spot.getAgentList()){
				actAgent(agent,spot);
			}
		}
	}

	/**
	 * スポット内でのエージェントの行動処理
	 * @param agent
	 * @param spot
	 */
	void actAgent(Agent agent,Spot spot){

	}

//======================================================================
// 年間終了時の処理
//======================================================================
	/**
	 * 総合年間終了処理
	 */
	void passYear(){
		// 場の一年が過ぎる
		field.passYEAR();
		// 全てのスポットにいるエージェントがホームスポットに戻る
		spotList.moveAgentToHomeSpot();
		// 全てのエージェントの一年が過ぎる
		for(Agent agent:agentList){
			// エージェントの一年が過ぎる
			agent.passYear();
		}
	}

	/**
	 * エージェントの年間終了処理
	 */
	void passAgentYear(){
		for(Agent agent:agentList){
			agent.passYear();
		}
	}
//======================================================================

	/**
	 * 画面表示
	 */
	void show(){
		// 描画をクリアする
		field.clearField();
		// 年数を表示する
		field.showYEAR();
	}
}
