package simulation;

import java.text.NumberFormat;
import java.util.ArrayList;

import util.CSVReaderWriter;
import util.MyUtil;

import agent.MiniAgent;
import agent.role.Role;
import agent.role.exchange.RoleExchange;

/**
 * シミュレーションのベースクラス
 *
 * @author kohta
 *
 */
public abstract class SimulationBase {
	public SimulationBase(){
		FILE_TOTAL = "total.csv";
		FILE_STEP = "step.csv";
		step = new double[EXCHANGE_NUM][AGENT_NUM][RoleExchange.roleNum];
	}

	/**
	 * コンストラクタ
	 *
	 * @param totalFileName
	 * @param stepFileName
	 */
	public SimulationBase(String totalFileName, String stepFileName) {
		FILE_TOTAL = totalFileName;
		FILE_STEP = stepFileName;
		step = new double[EXCHANGE_NUM][AGENT_NUM][RoleExchange.roleNum];
	}

	/**
	 * コンストラクタ
	 *
	 * @param totalFileName
	 * @param stepFileName
	 * @param agentNum
	 */
	public SimulationBase(String totalFileName, String stepFileName,int agentNum) {
		FILE_TOTAL = totalFileName;
		FILE_STEP = stepFileName;
		AGENT_NUM = agentNum;
		step = new double[EXCHANGE_NUM][AGENT_NUM][RoleExchange.roleNum];
	}

	/**
	 * コンストラクタ
	 *
	 * @param totalFileName
	 * @param stepFileName
	 * @param simNum
	 * @param exchangeNum
	 */
	public SimulationBase(String totalFileName, String stepFileName,int agentNum,int exchangeNum) {
		FILE_TOTAL = totalFileName;
		FILE_STEP = stepFileName;
		AGENT_NUM = agentNum;
		EXCHANGE_NUM = exchangeNum;
		step = new double[EXCHANGE_NUM][AGENT_NUM][RoleExchange.roleNum];
	}
	/**
	 * コンストラクタ
	 *
	 * @param totalFileName
	 * @param stepFileName
	 * @param agentNum
	 * @param exchangeNum
	 * @param simNum
	 */
	public SimulationBase(String totalFileName, String stepFileName,int agentNum,int exchangeNum,int simNum) {
		FILE_TOTAL = totalFileName;
		FILE_STEP = stepFileName;
		SIM_NUM = simNum;
		EXCHANGE_NUM = exchangeNum;
		AGENT_NUM = agentNum;
		step = new double[EXCHANGE_NUM][AGENT_NUM][RoleExchange.roleNum];
	}

	// 結果の書き出し先
	private String FILE_TOTAL;
	private String FILE_STEP;
	// シミュレーション回数
	private int SIM_NUM = 10000;

	// 交換回数
	private int EXCHANGE_NUM = 20;

	// エージェントの数
	private int AGENT_NUM = 20;

	// シミュレーションの現在ステップ
	private int crtStepNum = 0;

	// シミュレーション用エージェントリスト
	private ArrayList<MiniAgent> agentList;

	// 途中経過を保存する変数
	private double[][][] step;

	/**
	 * 初期化
	 */
	protected void init() {
		initAgentList();	// エージェントリストの初期化
		for (int i = 0; i < this.AGENT_NUM; i++) {
			ArrayList<Role> rl = RoleExchange.createRoleList(); // エージェントにセットされるロール変数
			MiniAgent agent = new MiniAgent(String.valueOf(i)); // エージェントの初期化
			agent.setRole(rl); // ロールリストをセット
			agentList.add(agent); // エージェントをリストにセット
		}
	}

	/**
	 * エージェントリストの初期化
	 */
	public void initAgentList() {
		agentList = new ArrayList<MiniAgent>();	// エージェントリストの初期化
	}

	/**
	 * 交換メソッド
	 */
	protected void exchange() {
		threeAgentExchange();
	}

	/**
	 * エージェント3体での交換
	 */
	private void threeAgentExchange() {
		// 交換し終わったエージェントの数
		int counter = 0;
		// 交換し終わったエージェントのインデックスのフラグ
		Boolean[] changed = new Boolean[AGENT_NUM];
		for (int i = 0; i < changed.length; i++) {
			changed[i] = false;
		}
		while (true) {
			int agentA = -1, agentB = -1, agentC = -1;
			// 交換し終わったエージェントの数が全体のエージェントの数-2より大きくなった場合交換終了
			if (counter > AGENT_NUM - 2) {
				break;
			}
			agentA = changeIndex(counter, changed);
			if (agentA != -1) {
				counter++;
			}
			agentB = changeIndex(counter, changed);
			if (agentB != -1) {
				counter++;
			}
			agentC = changeIndex(counter, changed);
			if (agentC != -1) {
				counter++;
			}
			if (agentA != -1 && agentB != -1 && agentC != -1) {
				RoleExchange.exchangeRoleBigger(agentList.get(agentA),
						agentList.get(agentB), agentList.get(agentC));
			}
		}
	}

	/**
	 * ファイルをアウトプットするタイミング
	 */
	abstract public void fileOutput();

	/**
	 * シミュレーション開始地点
	 */
	public void start() {
		ArrayList<String> writeList_total = new ArrayList<String>(); // 書き出し用文字列を保持するリスト
		ArrayList<String> writeList_step = new ArrayList<String>();

		// シミュレーションを繰り返す
		for (int i = 0; i < SIM_NUM; i++) {
			crtStepNum = i;	// 現在のステップ数
			simlation(writeList_total, writeList_step);
		}

		// ファイルアウトプット
		fileOutput();	// ファイルのアウトプット

		CSVReaderWriter.write(FILE_TOTAL, writeList_total); // コストをcsv形式で書き出し

		// 分析
		//analysis();

		// stepの表示、書き出し
		writeStep(writeList_step);

		// 途中経過の分析、書き出し
		stepAnalysis();

	}

	/**
	 * stepの表示、ファイルに書き出し
	 * @param writeList_step
	 */
	public void writeStep(ArrayList<String> writeList_step) {
		// stepの表示
		String printStep = new String();
		for (int index = 0; index < RoleExchange.roleNum; index++) {
			printStep += "[" + index + "]\n";
			for (int i = 0; i < getExchangeNum(); i++) {
				for (int j = 0; j < getAgentNum(); j++) {
					if (j != 0) {
						printStep += ",";
					}
					printStep += String.valueOf(step[i][j][index]);
				}
				printStep += "\n";
			}
		}
		System.out.println(printStep);
		writeList_step.add(printStep);
		CSVReaderWriter.write(FILE_STEP, writeList_step);
	}

	/**
	 * 途中経過の分析
	 */
	public void stepAnalysis() {
		// 途中経過の分析
		StepAnalysis.miniSim3_analysis(FILE_STEP);
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
			int randNum = (int) MyUtil.random(0, AGENT_NUM - 1);
			if (!changed[randNum]) { // まだ交換に使われていなければ
				agent = randNum;
				changed[randNum] = true;
				break;
			}
			if (counter > AGENT_NUM - 2) {
				break;
			}
		}
		return agent;
	}

	/**
	 * シミュレーションスタート時の処理
	 */
	abstract public void startSimulation();

	/**
	 * シミュレーションエンド時の処理
	 */
	abstract public void endSimulation();

	/**
	 * ロール交換スタート時の処理
	 */
	abstract public void startRoleExchange();

	/**
	 * ロール交換終了時の処理
	 */
	abstract public void endRoleExchange();

	/**
	 * シミュレーション本体
	 *
	 * @param writeList_total
	 * @param writeList_dispersion
	 * @param writeList_step
	 */
	private void simlation(ArrayList<String> writeList_total,
			ArrayList<String> writeList_step) {
		init(); // 初期化
		// 書き出し用
		double[] pTotal = new double[AGENT_NUM];
		double[] total = new double[AGENT_NUM];
		double[] pDispersion = new double[AGENT_NUM];
		double[] dispersion = new double[AGENT_NUM];

		// シミュレーションスタート時の処理
		startSimulation();

		// printAgent(agentList.get(0),agentList.get(1),agentList.get(2));
		// 表示

		for (int i = 0; i < agentList.size(); i++) {
			MiniAgent agent = agentList.get(i);	// エージェントの取得
			double crtTotal = agent.getTotalRoleCost();	// トータルロールコストの取得
			pTotal[i] = crtTotal; // 交換前のトータルロールコスト
		}

		for (int i = 0; i < agentList.size(); i++) {
			pDispersion[i] = agentList.get(i).getRoleList().dispersion(); // 分散値
		}

		// 交換前の分散値
		// System.out.println("交換前の分散値");
		// printDispersion(); // 分散値の表示

		// 複数回交換
		for (int i = 0; i < EXCHANGE_NUM; i++) {
			// 交換1回毎の作業時間を記録
			for (int j = 0; j < AGENT_NUM; j++) {
				for (int k = 0; k < RoleExchange.roleNum; k++) {
					step[i][j][k] = agentList.get(j).getRoleList().get(k)
							.getVolume();
				}
			}

			startRoleExchange();	// ロール交換開始時の処理
			exchange();	// ロール交換
			endRoleExchange();	// ロール交換終了時の処理
		}

		// 交換前の分散値
		// System.out.println("交換後の分散値");
		// printDispersion(); // 分散値の表示

		// printAgent(agentList.get(0),agentList.get(1),agentList.get(2));
		// // 表示

		for (int i = 0; i < agentList.size(); i++) {
			total[i] = agentList.get(i).getTotalRoleCost(); // 交換後のトータルロールコスト
		}
		for (int i = 0; i < agentList.size(); i++) {
			dispersion[i] = agentList.get(i).getRoleList().dispersion(); // 交換後の分散値
		}

		// 表示
		// for(int j=0;j<agentList.size();j++){
		// System.out.print(agentList.get(j).getName()+":"+(total[j]-pTotal[j])+"\n");
		// }
		String writeStr_total = new String();
		for (int i = 0; i < agentList.size(); i++) {
			if (i != 0) {
				writeStr_total += ",";
			}
			writeStr_total += total[i] - pTotal[i];
		}
		writeList_total.add(writeStr_total); // 書き出し用文字列に追加

		// シミュレーション終了時の処理
		endSimulation();
	}

	/**
	 * 分析メソッド
	 */
	private void analysis() {
		NumberFormat format = NumberFormat.getInstance(); // 表示桁数を設定するインスタンス
		format.setMaximumFractionDigits(3); // 表示桁数を3桁に設定

		double[] total = new double[AGENT_NUM]; // それぞれの短縮時間の合計

		ArrayList<String[]> read_total = new ArrayList<String[]>(); // 読み込み用リスト
		read_total = CSVReaderWriter.read(FILE_TOTAL); // 読み込み

		// トータル
		for (String[] strArray : read_total) {
			// totalの計算
			for (int i = 0; i < agentList.size(); i++) {
				total[i] += Double.valueOf(strArray[i]);
			}
		}
		for (int i = 0; i < agentList.size(); i++) {
			System.out.print(agentList.get(i).getName() + ":"
					+ format.format((total[i] / SIM_NUM)) + "\n");
		}
		double allTotal = 0;
		for (int i = 0; i < agentList.size(); i++) {
			allTotal += total[i];
		}
		System.out.println(format.format(allTotal / AGENT_NUM / SIM_NUM));

		System.out.println();

		// totalのヒストグラム
		int[] histogram = new int[15];
		for (int i = 0; i < agentList.size(); i++) {
			System.out.println((-1) * (total[i] / SIM_NUM));
			int totalInt = (int) (-total[i] / SIM_NUM);
			if (1 <= totalInt && totalInt < histogram.length) {
				histogram[totalInt]++;
			} else if (totalInt < 1) {
				histogram[0]++;
			} else if (histogram.length <= totalInt) {
				histogram[histogram.length - 1]++;
			}
		}
		for (int i = 0; i < histogram.length; i++) {
			System.out.println("[" + i + "]:" + histogram[i]);
		}
	}

	//===============================================================================
	// getter,setter
	//===============================================================================
	/**
	 * シミュレーションの回数を取得
	 * @return シミュレーションの繰り返し回数
	 */
	public int getSimNum(){
		return this.SIM_NUM;
	}

	/**
	 * 交換回数を取得
	 * @return 交換回数
	 */
	public int getExchangeNum(){
		return this.EXCHANGE_NUM;
	}

	/**
	 * エージェントの個数を取得
	 * @return エージェントの個数
	 */
	public int getAgentNum(){
		return this.AGENT_NUM;
	}

	/**
	 * エージェントのリストを取得
	 * @return
	 */
	public ArrayList<MiniAgent> getAgentList(){
		return this.agentList;
	}

	/**
	 * エージェントをリストに加える
	 * @param agent
	 */
	public void addAgent(MiniAgent agent){
		this.agentList.add(agent);
	}

	/**
	 * 現在のステップが何ターン目かを返す
	 * @return
	 */
	public int getCurrentStepNum(){
		return this.crtStepNum;
	}
}
