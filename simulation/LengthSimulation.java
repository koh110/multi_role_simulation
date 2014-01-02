package simulation;

import java.util.ArrayList;

import agent.LengthAgent;
import agent.role.Role;
import agent.role.exchange.LengthRoleExchange;

import util.CSVReaderWriter;
import util.MyUtil;

public class LengthSimulation extends SimulationBase {
	// 結果を保存するファイル
	public static final String TOTAL_FILE = "lengthsim.csv";
	public static final String STEP_FILE = "lengthStep.csv";
	public static final String LENGTH_FILE = "lengthDis.csv";

	// エージェント数
	private static int AGENT_NUM = 100;

	// 交換回数
	private static int EXCHANGE_NUM = 50;

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
	public LengthSimulation() {
		super(TOTAL_FILE, STEP_FILE, AGENT_NUM, EXCHANGE_NUM);
		distance = new ArrayList<String>(); // 初期化
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
				LengthAgent spotAgentA = (LengthAgent) getAgentList().get(
						agentA);
				LengthAgent spotAgentB = (LengthAgent) getAgentList().get(
						agentB);
				LengthAgent spotAgentC = (LengthAgent) getAgentList().get(
						agentC);
				// 交換
				LengthRoleExchange.exchangeRoleBigger(spotAgentA, spotAgentB,
						spotAgentC, maxDistance);
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
			ArrayList<Role> rl = LengthRoleExchange.createRoleList(); // エージェントにセットされるロール変数
			double x = MyUtil.random(X_MIN, X_MAX); // エージェントの位置
			//x = MyUtil.nomalDistributionRandNum(X_MIN, X_MAX); // 正規分布で位置を生成(New)
			LengthAgent agent = new LengthAgent(String.valueOf(i), x); // エージェントの初期化
			agent.setRole(rl); // ロールリストをセット
			addAgent(agent); // エージェントをリストに加える
		}
	}

	/**
	 * ファイルアウトプット地点
	 */
	@Override
	public void fileOutput() {
		CSVReaderWriter.write(LENGTH_FILE, distance); // 距離記録ファイルに書き出し
	}

	@Override
	public void startSimulation() {

	}

	@Override
	public void endSimulation() {

	}

	@Override
	public void startRoleExchange() {

	}

	@Override
	public void endRoleExchange() {

	}

}
