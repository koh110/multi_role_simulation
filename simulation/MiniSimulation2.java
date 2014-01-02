package simulation;

import agent.MiniAgent;
import agent.role.Role;

import java.util.ArrayList;
import java.util.Collections;

import util.CSVReaderWriter;

/**
 * 小さなシミュレーションを行うクラス
 *
 * @author kohta
 *
 */
public class MiniSimulation2 {
	// 結果の書き出し先
	final String FILE = "miniSim2.csv";

	// シミュレーション回数
	final int SIM_NUM = 10000;

	private MiniAgent a; // シミュレーション用エージェント
	private MiniAgent b;
	private MiniAgent c;

	/**
	 * シミュレーション開始地点
	 */
	public void start() {
		ArrayList<String> writeList = new ArrayList<String>(); // 書き出し用文字列を保持するリスト
		for (int i = 0; i < SIM_NUM; i++) {
			init(); // 初期化
			// 書き出し用
			double pTotal_a = 0, pTotal_b = 0, pTotal_c = 0;
			double total_a = 0, total_b = 0, total_c = 0;
			// printAgent(a,b); // 表示
			pTotal_a = a.getTotalRoleCost(); // 交換前のaのトータルロールコスト
			pTotal_b = b.getTotalRoleCost(); // 交換前のbのトータルロールコスト
			pTotal_c = c.getTotalRoleCost(); // 交換前のcのトータルロールコスト

			exchangeRoleBigger(a, b, c); // ロールの交換

			// printAgent(a,b); // 表示
			total_a = a.getTotalRoleCost(); // 交換後のaのトータルロールコスト
			total_b = b.getTotalRoleCost(); // 交換後のbのトータルロールコスト
			total_c = c.getTotalRoleCost(); // 交換後のcのトータルロールコスト
			System.out.println("a:" + (total_a - pTotal_a) + "\nb:"
					+ (total_b - pTotal_b) + "\nc:" + (total_c - pTotal_c)
					+ "\n");
			writeList.add((total_a - pTotal_a) + "," + (total_b - pTotal_b)
					+ "," + (total_c - pTotal_c)); // 書き出し用文字列に追加
		}
		CSVReaderWriter.write(FILE, writeList); // csv形式で書き出し

		// 分析
		double total_a = 0, total_b = 0, total_c = 0; // それぞれの短縮時間の合計
		ArrayList<String[]> read = new ArrayList<String[]>(); // 読み込み用リスト
		read = CSVReaderWriter.read(FILE); // 読み込み
		for (String[] strArray : read) {
			// totalの計算
			total_a += Double.valueOf(strArray[0]);
			total_b += Double.valueOf(strArray[1]);
			total_c += Double.valueOf(strArray[2]);
		}
		System.out.println("\na:" + (total_a / SIM_NUM) + "\nb:"
				+ (total_b / SIM_NUM) + "\nc:" + (total_c / SIM_NUM));
		System.out.println(((total_a / SIM_NUM)+(total_b/SIM_NUM)+(total_c/SIM_NUM))/3);
	}

	/**
	 * 相手の大きい所に渡しあう
	 *
	 * @param a
	 * @param b
	 * @param c
	 */
	private void exchangeRoleBigger(MiniAgent a, MiniAgent b, MiniAgent c) {
		ArrayList<Role> rl_a = a.getBiggerRoleList(); // aの降順ロールリスト
		ArrayList<Role> rl_b = b.getBiggerRoleList(); // bの降順ロールリスト
		ArrayList<Role> rl_c = c.getBiggerRoleList(); // cの降順ロールリスト

		// 1体目の交換
		Role roleBtoA = getEqualRole(rl_b,rl_a.get(0));	// bからaへ渡すロールを取得
		Role roleCtoA = getEqualRole(rl_c,rl_a.get(0));	// cからaへ渡すロールを取得
		a.receiveRoleVolume(roleBtoA, b.giveRoleVolume(roleBtoA));	// bからaにボリュームを渡す
		a.receiveRoleVolume(roleCtoA, c.giveRoleVolume(roleCtoA));	// cからaにボリュームを渡す

		// 2体目の交換
		Role roleAtoB = getEqualRole(rl_a,rl_b.get(0));	// aからbへ渡すロールを取得
		Role roleCtoB = getEqualRole(rl_c,rl_b.get(0));	// cからbへ渡すロールを取得
		// aに関する交換
		if(roleAtoB.equals(roleBtoA)){
			roleAtoB = getEqualRole(rl_a,rl_b.get(1));	// すでに渡しているロールならば次のロールを渡す
		}
		// cに関する交換
		roleCtoB = getEqualRole(rl_c,roleAtoB);

		// トータルロールコストが今の状態よりよくなるならば交換をする
		// ロールを受け取る
		b.receiveRoleVolume(roleAtoB, a.giveRoleVolume(roleAtoB));
		b.receiveRoleVolume(roleCtoB, c.giveRoleVolume(roleCtoB));

		// 3体目の交換
		Role roleAtoC = getEqualRole(rl_a,rl_c.get(0));	// aからcへ渡すロールを取得
		Role roleBtoC = getEqualRole(rl_b,rl_c.get(0));	// bからcへ渡すロールを取得
		if(roleAtoC.equals(roleCtoA)){	// cがaに渡したロールと等しかったら次のロールを渡す
			roleAtoC = getEqualRole(rl_a,rl_c.get(1));
			if(roleAtoC.equals((roleAtoB))){	//aがbに渡したロールと等しかったらさらに次のロールを渡す
				roleAtoC = getEqualRole(rl_a,rl_c.get(2));
			}
		}
		roleBtoC = getEqualRole(rl_b,roleAtoC);

		// ロールを受け取る
		c.receiveRoleVolume(roleAtoC, a.giveRoleVolume(roleAtoC));
		c.receiveRoleVolume(roleBtoC, b.giveRoleVolume(roleBtoC));

		System.out.print("");
	}

	/**
	 * ロールのリストからroleに対応する要素を取ってくる
	 * @param rl
	 * @param role
	 * @return
	 */
	private Role getEqualRole(ArrayList<Role> rl, Role role) {
		for (Role crtRole : rl) {
			if (crtRole.equals(role)) {
				return crtRole;
			}
		}
		return null;
	}

	/**
	 * 初期化
	 */
	private void init() {
		ArrayList<Role> rl = createRoleList(); // エージェントにセットされるロール変数
		a = new MiniAgent("a"); // エージェントの初期化
		a.setRole(rl); // ロールリストをセット
		b = new MiniAgent("b");
		rl = createRoleList(); // ロールリストの生成
		b.setRole(rl); // ロールリストをセット
		c = new MiniAgent("c");
		rl = createRoleList();
		c.setRole(rl);
	}

	/**
	 * 引数のエージェントをコンソールに表示する
	 *
	 * @param ma
	 */
	private void printAgent(MiniAgent a, MiniAgent b, MiniAgent c) {
		ArrayList<Role> rl1 = a.getRoles(); // ロールを取得
		ArrayList<Role> rl2 = b.getRoles(); // ロールを取得
		ArrayList<Role> rl3 = c.getRoles();
		System.out.println(a.getName() + "\t av:tm(cost)\t\t" + b.getName()
				+ "\t av:tm(cost)\n" + c.getName() + "\t av:tm(cost)\n");
		// ロールの表示
		for (int i = 0; i < rl1.size(); i++) {
			Role r1 = rl1.get(i);
			Role r2 = rl2.get(i);
			Role r3 = rl3.get(i);
			System.out.print(r1.getName() + "\t" + r1.getAbility() + ":"
					+ r1.getVolume() + "(" + r1.getRoleCost() + ")");
			System.out.print("\t\t");
			System.out.print(r2.getName() + "\t" + r2.getAbility() + ":"
					+ r2.getVolume() + "(" + r2.getRoleCost() + ")");
			System.out.println();
			System.out.print(r3.getName() + "\t" + r3.getAbility() + ":"
					+ r3.getVolume() + "(" + r3.getRoleCost() + ")");
			System.out.println();
		}
		System.out.println("\t\t(" + a.getTotalRoleCost() + ")\t\t\t\t("
				+ b.getTotalRoleCost() + ")\t\t\t\t(" + c.getTotalRoleCost()
				+ ")");
		System.out.println();
	}

	/**
	 * ロールリストの生成
	 *
	 * @return 生成されたロールリスト
	 */
	private ArrayList<Role> createRoleList() {
		ArrayList<Role> rl = new ArrayList<Role>(); // エージェントにセットされるロール変数
		ArrayList<Double> abilityList = new ArrayList<Double>(); // アビリティを保持するリスト
		abilityList.add(1.0);
		abilityList.add(2.0);
		abilityList.add(3.0);
		abilityList.add(6.0);
		Collections.shuffle(abilityList); // アビリティリストのシャッフル
		for (int i = 0; i < abilityList.size(); i++) {
			// ランダムなアビリティのロールをロールリストに加える
			rl.add(createRole(String.valueOf(i), abilityList.get(i), 6));
		}
		return rl;
	}

	/**
	 * ロールを生成する
	 *
	 * @param name
	 * @param ability
	 * @param volume
	 * @return
	 */
	private Role createRole(String name, double ability, double volume) {
		Role r = new Role(name);
		r.setAbility(ability);
		r.setVolume(volume);
		return r;
	}

	/**
	 * メインメソッド
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		new MiniSimulation2().start();
	}
}
