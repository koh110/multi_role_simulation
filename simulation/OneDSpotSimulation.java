package simulation;

import java.util.ArrayList;

import util.CSVReaderWriter;
import util.MyUtil;
import agent.OneDSpotAgent;
import agent.role.Role;
import agent.role.exchange.OneDRoleExchange;

/**
 * 1次元をもたせたエージェントによる交換シミュレーション
 *
 * @author kohta
 *
 */
public class OneDSpotSimulation extends SimulationBase2 {
	// 結果を保存するファイル
	public static final String TOTAL_FILE = "oneDsim.csv";
	public static final String STEP_FILE = "oneDStep.csv";
	public static final String DISTANCE_FILE = "oneDDis.csv";


	// エージェントの位置の最大値
	private final double X_MAX = 100;
	// エージェントの位置の最小値
	private final double X_MIN = 0;
	// 交換が行える最大距離
	private final double maxDistance = 30.0;

	// 書き出し用文字列
	private ArrayList<String> distance;

	/**
	 * コンストラクタ
	 */
	public OneDSpotSimulation() {
		super(TOTAL_FILE, STEP_FILE);
		distance = new ArrayList<String>(); // 初期化
	}

	/**
	 * ファイルアウトプット地点
	 */
	@Override
	public void fileOutput() {
		CSVReaderWriter.write(DISTANCE_FILE, distance); // 距離記録ファイルに書き出し
	}

	/**
	 * シミュレーションのスタート地点
	 */
	@Override
	public void startSimulation() {

	}

	/**
	 * シミュレーションの終了地点
	 */
	@Override
	public void endSimulation() {
		// エージェントの距離を書き出し用文字列に入れる
		String printStr = new String(); // 書き出し用変数
		for (int i = 0; i < this.getAgentNum(); i++) {
			// 書き込むエージェントを取得
			OneDSpotAgent agent = (OneDSpotAgent) getAgentList().get(i);
			// エージェントの距離を書き込み文字列に保存
			if (i != 0) {
				printStr += ",";
			}
			printStr += String.valueOf(agent.getX());
		}
		distance.add(printStr); // 書き出し用文字列に追加
	}

	/**
	 * 途中経過の分析
	 */
	@Override
	public void stepAnalysis(ArrayList<String> writeList_step) {
		StepAnalysis.OneDSpotSimAnalysis2(getExchangeNum(),getAgentNum(),getSimNum(),STEP_FILE);
	}

	/**
	 * 交換メソッド
	 */
	public void exchange() {
		//twoAgentExchange();
		threeAgentExchange();
	}

	/**
	 * エージェント3体での交換
	 */
	private void threeAgentExchange() {
		// 交換し終わったエージェントの数
		int counter = 0;
		// 交換し終わったエージェントのインデックスのフラグ
		Boolean[] changed = new Boolean[getAgentNum()];
		for (int i = 0; i < changed.length; i++) {
			changed[i] = false;
		}
		while (true) {
			int agentA = -1, agentB = -1, agentC = -1;
			// 交換し終わったエージェントの数が全体のエージェントの数-2より大きくなった場合交換終了
			if (counter > getAgentNum() - 2) {
				break;
			}
			// 交換するindexの取得
			agentA = changeIndex(counter, changed);
			if (agentA != -1) {
				counter++; // 交換した数を増やす
			}
			// 交換するindexの取得
			agentB = changeIndex(counter, changed);
			if (agentB != -1) {
				counter++; // 交換した数を増やす
			}
			agentC = changeIndex(counter, changed);
			if (agentC != -1) {
				counter++; // 交換した数を増やす
			}
			if (agentA != -1 && agentB != -1 && agentC != -1) {
				// 交換を行うエージェントを取得
				OneDSpotAgent spotAgentA = (OneDSpotAgent) getAgentList().get(
						agentA);
				OneDSpotAgent spotAgentB = (OneDSpotAgent) getAgentList().get(
						agentB);
				OneDSpotAgent spotAgentC = (OneDSpotAgent) getAgentList().get(
						agentC);
				// 交換
				OneDRoleExchange.exchangeRoleBigger(spotAgentA, spotAgentB,
						spotAgentC, maxDistance);
			}
		}
	}

	/**
	 * エージェント2体による交換
	 */
	private void twoAgentExchange() {
		// 交換し終わったエージェントの数
		int counter = 0;
		// 交換し終わったエージェントのインデックスのフラグ
		boolean[] changed = new boolean[getAgentNum()];
		while (true) {
			// 交換indexを決める乱数
			int randNum = (int) MyUtil.random(0, getAgentNum() - 1);
			// indexをまだ交換していなければ
			if (!changed[randNum]) {
				changed[randNum] = true;
				counter++;
				// まだ交換できるエージェントが残ってる場合
				if (counter < getAgentNum() - 1) {
					while (true) {
						// 交換相手のindexを決める乱数
						int other = (int) MyUtil.random(0, getAgentNum() - 1);
						if (!changed[other]) { // まだ交換されてなかったら
							changed[other] = true;
							counter++;
							// 交換するエージェントを取得する
							OneDSpotAgent randAgent = (OneDSpotAgent) getAgentList()
									.get(randNum);
							OneDSpotAgent otherAgent = (OneDSpotAgent) getAgentList()
									.get(other);
							// 交換
							OneDRoleExchange.exchangeRoleBigger(randAgent,
									otherAgent, maxDistance);
							break;
						}
					}
				}
			}

			// 交換し終わったエージェントの数が全体のエージェントの数-1(奇数の場合)より大きくなった場合交換終了
			if (counter >= getAgentNum() - 1) {
				break;
			}
		}
	}

	/**
	 * 交換するインデックスを計算する
	 *
	 * @param counter
	 * @param changed
	 * @return
	 */
	private int changeIndex(Integer counter, Boolean[] changed) {
		int agent = -1;
		while (true) {
			// 交換indexを決める乱数
			int randNum = (int) MyUtil.random(0, getAgentNum() - 1);
			if (!changed[randNum]) { // まだ交換に使われていなければ
				agent = randNum;
				changed[randNum] = true;
				break;
			}
			if (counter > getAgentNum() - 2) {
				break;
			}
		}
		return agent;
	}

	/**
	 * 初期化
	 */
	protected void init() {
		initAgentList(); // エージェントリストの初期化
		// エージェントの生成
		for (int i = 0; i < this.getAgentNum(); i++) {
			ArrayList<Role> rl = OneDRoleExchange.createRoleList(); // エージェントにセットされるロール変数
			double x = MyUtil.random(X_MIN, X_MAX); // エージェントの位置
			//x = MyUtil.nomalDistributionRandNum(X_MIN, X_MAX); // 正規分布で位置を生成(New)
			OneDSpotAgent agent = new OneDSpotAgent(String.valueOf(i), x); // エージェントの初期化
			agent.setRole(rl); // ロールリストをセット
			addAgent(agent); // エージェントをリストに加える
		}
	}

	/**
	 * メインメソッド
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		new OneDSpotSimulation().start();
	}

	@Override
	public void startRoleExchange() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void endRoleExchange() {
		// TODO 自動生成されたメソッド・スタブ

	}
}
