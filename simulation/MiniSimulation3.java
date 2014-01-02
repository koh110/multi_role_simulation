package simulation;

import agent.MiniAgent;
import agent.role.Role;
import agent.role.exchange.RoleExchange;

import java.text.NumberFormat;
import java.util.ArrayList;

import util.CSVReaderWriter;
import util.MyUtil;

/**
 * 小さなシミュレーションを行うクラス
 *
 * @author kohta
 *
 */
public class MiniSimulation3 {
	// 結果の書き出し先
	public final static String FILE_TOTAL = "miniSim3.csv";
	public final static String FILE_DISPERSION = "minisim3_dispersion.csv";
	public final static String FILE_STEP = "step.csv";
	public final static String STEP_ANALYSIS = "stepAnalysis.csv";

	// シミュレーション回数
	public final static int SIM_NUM = 10000;

	// 交換回数
	public final static int EXCHANGE_NUM = 50;

	// エージェントの数
	public final static int AGENT_NUM = 20;

	// シミュレーション用エージェントリスト
	private ArrayList<MiniAgent> agentList;



	// 途中経過を保存する変数
	private double[][][] step = new double[EXCHANGE_NUM][AGENT_NUM][RoleExchange.roleNum];

	/**
	 * シミュレーション開始地点
	 */
	public void start() {
		ArrayList<String> writeList_total = new ArrayList<String>(); // 書き出し用文字列を保持するリスト
		ArrayList<String> writeList_dispersion = new ArrayList<String>(); // 書き出し用文字列を保持するリスト
		ArrayList<String> writeList_step = new ArrayList<String>();

		for (int i = 0; i < SIM_NUM; i++) {
			simlation(writeList_total, writeList_dispersion, writeList_step, i);
		}
		CSVReaderWriter.write(FILE_TOTAL, writeList_total); // コストをcsv形式で書き出し
		CSVReaderWriter.write(FILE_DISPERSION, writeList_dispersion); // 分散をcsv形式で書き出し
		// CSVReaderWriter.write(FILE_STEP, writeList_step);

		// 分析
		analysis();

		// stepの表示
		String printStep = new String();
		for (int index = 0; index < RoleExchange.roleNum; index++) {
			printStep += "[" + index + "]\n";
			for (int i = 0; i < EXCHANGE_NUM; i++) {
				for (int j = 0; j < AGENT_NUM; j++) {
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

		// 途中経過の分析
		StepAnalysis.miniSim3_analysis(STEP_ANALYSIS);
	}

	/**
	 * シミュレーション本体
	 *
	 * @param writeList_total
	 * @param writeList_dispersion
	 * @param writeList_step
	 * @param num
	 *            シミュレーションが何度目か
	 */
	private void simlation(ArrayList<String> writeList_total,
			ArrayList<String> writeList_dispersion,
			ArrayList<String> writeList_step, int num) {
		init(); // 初期化
		// 書き出し用
		double[] pTotal = new double[AGENT_NUM];
		double[] total = new double[AGENT_NUM];
		double[] pDispersion = new double[AGENT_NUM];
		double[] dispersion = new double[AGENT_NUM];

		// printAgent(agentList.get(0),agentList.get(1),agentList.get(2));
		// 表示

		for (int i = 0; i < agentList.size(); i++) {
			pTotal[i] = agentList.get(i).getTotalRoleCost(); // 交換前のトータルロールコスト
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

			exchange2(); // ロール交換
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
		String writeStr_dispersion = new String();
		for (int i = 0; i < agentList.size(); i++) {
			if (i != 0) {
				writeStr_total += ",";
				writeStr_dispersion += ",";
			}
			writeStr_total += total[i] - pTotal[i];
			writeStr_dispersion += dispersion[i] - pDispersion[i];
		}
		writeList_total.add(writeStr_total); // 書き出し用文字列に追加
		writeList_dispersion.add(writeStr_dispersion);

		// // stepごとの保存をファイルに書き出し
		// String writeStr_step = new String();
		// for(int i=0;i<SIM_NUM;i++){
		// for(int j=0;j<EXCHANGE_NUM;j++){
		// if(j != 0){
		// writeStr_step += ",";
		// }
		// writeStr_step += step[i][j];
		// }
		// }
		// writeList_step.add(writeStr_step);
	}

	/**
	 * 分析メソッド
	 */
	private void analysis() {
		NumberFormat format = NumberFormat.getInstance(); // 表示桁数を設定するインスタンス
		format.setMaximumFractionDigits(3); // 表示桁数を3桁に設定

		double[] total = new double[AGENT_NUM]; // それぞれの短縮時間の合計
		double[] dispersion = new double[AGENT_NUM]; // それぞれの分散

		ArrayList<String[]> read_total = new ArrayList<String[]>(); // 読み込み用リスト
		ArrayList<String[]> read_dispersion = new ArrayList<String[]>(); // 読み込み用リスト
		read_total = CSVReaderWriter.read(FILE_TOTAL); // 読み込み
		read_dispersion = CSVReaderWriter.read(FILE_DISPERSION); // 読み込み

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

		// 分散
		for (String[] strArray : read_dispersion) {
			// 分散の計算
			for (int i = 0; i < agentList.size(); i++) {
				dispersion[i] += Double.valueOf(strArray[i]);
			}
		}
		for (int i = 0; i < agentList.size(); i++) {
			System.out.print(agentList.get(i).getName() + ":"
					+ format.format((dispersion[i] / SIM_NUM)) + "\n");
		}
		double allDispersion = 0;
		for (int i = 0; i < agentList.size(); i++) {
			allDispersion += dispersion[i];
		}
		System.out.println(format.format(allDispersion / AGENT_NUM / SIM_NUM));
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

	private void printDispersion() {
		int counter = 0;
		for (MiniAgent agent : agentList) {
			System.out
					.println(counter + ":" + agent.getRoleList().dispersion());
			counter++;
		}
	}

	/**
	 * ロール交換を行うメソッド
	 */
	private void exchange() {
		// 交換し終わったエージェントの数
		int counter = 0;
		// 交換し終わったエージェントのインデックスのフラグ
		boolean[] changed = new boolean[AGENT_NUM];
		while (true) {
			// 交換indexを決める乱数
			int randNum = (int) MyUtil.random(0, AGENT_NUM - 1);
			// indexをまだ交換していなければ
			if (!changed[randNum]) {
				changed[randNum] = true;
				counter++;
				// まだ交換できるエージェントが残ってる場合
				if (counter < AGENT_NUM - 1) {
					while (true) {
						// 交換相手のindexを決める乱数
						int other = (int) MyUtil.random(0, AGENT_NUM - 1);
						if (!changed[other]) { // まだ交換されてなかったら
							changed[other] = true;
							counter++;
							// 交換
							RoleExchange.exchangeRoleBigger(
									agentList.get(randNum),
									agentList.get(other));
							break;
						}
					}
				}
			}

			// 交換し終わったエージェントの数が全体のエージェントの数-1(奇数の場合)より大きくなった場合交換終了
			if (counter >= AGENT_NUM - 1) {
				break;
			}
		}

		// // 交換を行うindex
		// int[] index = new int[AGENT_NUM];
		// for (int i = 0; i < AGENT_NUM; i++) {
		// index[i] = i;
		// }
		// MyUtil.shuffle(index);
		// // indexをシャッフル
		// for (int i = 0; i < AGENT_NUM / 2; i = i + 2) {
		// // System.out.println("exchange:"+(index[i])+"<->"+(index[i+1]));
		// RoleExchange.exchangeRoleBigger(agentList.get(index[i]),
		// agentList.get(index[i + 1]));
		// }
	}

	/**
	 * エージェント3体での交換
	 */
	private void exchange2() {
		// 交換し終わったエージェントの数
		int counter = 0;
		// 交換し終わったエージェントのインデックスのフラグ
		Boolean[] changed = new Boolean[AGENT_NUM];
		for(int i=0;i<changed.length;i++){
			changed[i] = false;
		}
		while (true) {
			int agentA=-1,agentB=-1,agentC=-1;
			// 交換し終わったエージェントの数が全体のエージェントの数-2より大きくなった場合交換終了
			if (counter > AGENT_NUM - 2) {
				break;
			}
			agentA = changeIndex(counter, changed);
			if(agentA!=-1){
				counter++;
			}
			agentB = changeIndex(counter, changed);
			if(agentB!=-1){
				counter++;
			}
			agentC = changeIndex(counter, changed);
			if(agentC!=-1){
				counter++;
			}
			if(agentA!=-1 && agentB!=-1 && agentC!=-1){
				RoleExchange.exchangeRoleBigger(agentList.get(agentA), agentList.get(agentB), agentList.get(agentC));
			}
		}
	}

	private int changeIndex(Integer counter, Boolean[] changed) {
		int agent = -1;
		while(true){
			// 交換indexを決める乱数
			int randNum = (int) MyUtil.random(0, AGENT_NUM - 1);
			if(!changed[randNum]){	// まだ交換に使われていなければ
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
	 * 初期化
	 */
	private void init() {
		agentList = new ArrayList<MiniAgent>();
		for (int i = 0; i < this.AGENT_NUM; i++) {
			ArrayList<Role> rl = RoleExchange.createRoleList(); // エージェントにセットされるロール変数
			MiniAgent agent = new MiniAgent(String.valueOf(i)); // エージェントの初期化
			agent.setRole(rl); // ロールリストをセット
			agentList.add(agent); // エージェントをリストにセット
		}
	}

	/**
	 * 引数のエージェントをコンソールに表示する
	 *
	 * @param ma
	 */
	private void printAgent(MiniAgent a, MiniAgent b, MiniAgent c) {
		NumberFormat format = NumberFormat.getInstance(); // 表示桁数を設定するインスタンス
		format.setMaximumFractionDigits(3); // 表示桁数を3桁に設定

		ArrayList<Role> rl1 = a.getRoles(); // ロールを取得
		ArrayList<Role> rl2 = b.getRoles(); // ロールを取得
		ArrayList<Role> rl3 = c.getRoles();
		System.out.println(a.getName() + "\t av:tm(cost)\t\t\t" + b.getName()
				+ "\t av:tm(cost)\t\t" + c.getName() + "\t av:tm(cost)\n");
		// ロールの表示
		for (int i = 0; i < rl1.size(); i++) {
			Role r1 = rl1.get(i);
			Role r2 = rl2.get(i);
			Role r3 = rl3.get(i);
			System.out.print(r1.getName() + "\t" + r1.getAbility() + ":"
					+ r1.getVolume() + "(" + format.format(r1.getRoleCost())
					+ ")");
			System.out.print("\t\t");
			System.out.print(r2.getName() + "\t" + r2.getAbility() + ":"
					+ r2.getVolume() + "(" + format.format(r2.getRoleCost())
					+ ")");
			System.out.print("\t\t");
			System.out.print(r3.getName() + "\t" + r3.getAbility() + ":"
					+ r3.getVolume() + "(" + format.format(r3.getRoleCost())
					+ ")");
			System.out.println();
		}
		System.out.println("\t\t(" + format.format(a.getTotalRoleCost())
				+ ")\t\t\t\t(" + format.format(b.getTotalRoleCost())
				+ ")\t\t\t\t(" + format.format(c.getTotalRoleCost()) + ")");
		System.out.println();
	}

	/**
	 * メインメソッド
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		new MiniSimulation3().start();
	}
}
