package agent.role.exchange;

import java.util.ArrayList;
import java.util.Collections;

import util.MyUtil;
import agent.MiniAgent;
import agent.role.Role;

/**
 * ロールの交換を行うクラス
 *
 * @author kohta
 *
 */
public class RoleExchange {
	/**
	 * アビリティの最大値
	 */
	public static final double maxValueAbility = 10.0;
	/**
	 * アビリティの最小値
	 */
	public static final double minValueAbility = 1.0;

	/**
	 * ロールの数
	 */
	public static final int roleNum = 10;

	/**
	 * エージェント2体による相手の大きい所に同時に渡しあう
	 *
	 * @param a
	 * @param b
	 */
	public static void exchangeRoleBigger(MiniAgent a, MiniAgent b){
		boolean flagA = false;	// aの交換が終ったフラグ
		boolean flagB = false;	// bの交換が終ったフラグ
		ArrayList<Role> rl_a = a.getBiggerRoleList();	// aの降順ロールリスト
		ArrayList<Role> rl_b = b.getBiggerRoleList();	// bの降順ロールリスト
		int indexA = -1;
		int indexB = -1;
		for(int i=0;i<roleNum;i++){
			Role crtRoleA = rl_a.get(i);
			Role crtRoleB = rl_b.get(i);
			// aが受け取るroleindexの決定
			if(!flagA){
				if(crtRoleA.getAbility()>getEqualRole(b, crtRoleA).getAbility()){	// aの作業効率が上回っている時
					if(getEqualRole(b, crtRoleA).getVolume()>0){	// ボリュームが有る時
						indexA = i;
						flagA = true;
					}
				}
			}
			// bが受け取るroleindexの決定
			if(!flagB){
				if(crtRoleB.getAbility()>getEqualRole(a, crtRoleB).getAbility()){	// bの作業効率が上回っている時
					if(getEqualRole(a, crtRoleB).getVolume()>0){	// ボリュームが有る時
						indexB = i;
						flagB = true;
					}
				}
			}
			if(flagA&&flagB){	// indexを取り終わったら終了
				break;
			}
		}

		if(flagA&&flagB){ // 両方共値がある場合
			Role receiveA = rl_a.get(indexA);	// aが受け取るロール
			Role receiveB = rl_b.get(indexB);	// bが受け取るロール

			MiniAgent aClone = (MiniAgent)a.clone();	// エージェントのクローン
			MiniAgent bClone = (MiniAgent)b.clone();

			aClone.receiveRoleVolume(receiveA, bClone.giveRoleVolume(receiveA));	// aが受け取り
			bClone.receiveRoleVolume(receiveB, aClone.giveRoleVolume(receiveB));	// bが受け取り

			if(a.getTotalRoleCost()>aClone.getTotalRoleCost()){	// 効率が良くなった場合交換
				a.receiveRoleVolume(receiveA, b.giveRoleVolume(receiveA));	// aが受け取り
			}
			if(b.getTotalRoleCost()>bClone.getTotalRoleCost()){
				b.receiveRoleVolume(receiveB, a.giveRoleVolume(receiveB));	// bが受け取り
			}
			//System.out.println(receiveA.getName()+"と"+receiveB.getName());
		}
/*
		if(indexA>=0){
			Role receiveA = rl_a.get(indexA);	// aが受け取るロール
			a.receiveRoleVolume(receiveA, b.giveRoleVolume(receiveA));	// aが受け取り
		}
		if(indexB>=0){
			Role receiveB = rl_b.get(indexB);	// aが受け取るロール
			b.receiveRoleVolume(receiveB, a.giveRoleVolume(receiveB));	// bが受け取り
		}
*/
	}

	/**
	 * エージェント3体による相手の大きい所に渡しあう処理
	 *
	 * @param a
	 * @param b
	 * @param c
	 */
	public static void exchangeRoleBigger(MiniAgent a, MiniAgent b, MiniAgent c) {
		boolean flagA = false;	// aの交換が終ったフラグ
		boolean flagB = false;	// bの交換が終ったフラグ
		boolean flagC = false;	// cの交換が終ったフラグ
		ArrayList<Role> rl_a = a.getBiggerRoleList();	// aの降順ロールリスト
		ArrayList<Role> rl_b = b.getBiggerRoleList();	// bの降順ロールリスト
		ArrayList<Role> rl_c = c.getBiggerRoleList();	// cの降順ロールリスト
		int indexA = -1;
		int indexB = -1;
		int indexC = -1;
		for(int i=0;i<roleNum;i++){
			Role crtRoleA = rl_a.get(i);
			Role crtRoleB = rl_b.get(i);
			Role crtRoleC = rl_c.get(i);

			// aが受け取るroleindexの決定
			if(!flagA){
				double ability = crtRoleA.getAbility();
				// aの作業効率が上回っている時
				if(ability>getEqualRole(b, crtRoleA).getAbility() && ability>getEqualRole(c, crtRoleA).getAbility()){
					// ボリュームが有る時
					if(getEqualRole(b, crtRoleA).getVolume()>0 || getEqualRole(c,crtRoleA).getVolume()>0){
						indexA = i;
						flagA = true;
					}
				}
			}
			// bが受け取るroleindexの決定
			if(!flagB){
				double ability = crtRoleB.getAbility();
				// bの作業効率が上回っている時
				if(ability>getEqualRole(a, crtRoleB).getAbility() && ability>getEqualRole(c,crtRoleB).getAbility()){
					// ボリュームが有る時
					if(getEqualRole(a, crtRoleB).getVolume()>0 || getEqualRole(c,crtRoleB).getVolume()>0){
						indexB = i;
						flagB = true;
					}
				}
			}
			// cが受け取るroleindexの決定
			if(!flagC){
				double ability = crtRoleC.getAbility();
				// cの作業効率が上回っている時
				if(ability>getEqualRole(a,crtRoleC).getAbility() && ability>getEqualRole(b,crtRoleC).getAbility()){
					// ボリュームが有る時
					if(getEqualRole(a,crtRoleC).getVolume()>0 || getEqualRole(b,crtRoleC).getVolume()>0){
						indexC = i;
						flagC = true;
					}
				}
			}
			if(flagA&&flagB&&flagC){	// indexを取り終わったら終了
				break;
			}
		}

		if(flagA){
			Role receiveA = rl_a.get(indexA);	// aが受け取るロール

			// 値があれば渡す
			if(b.getRole(receiveA).getVolume()>0){
				a.receiveRoleVolume(receiveA, b.giveRoleVolume(receiveA));	// aが受け取り
			}
			if(c.getRole(receiveA).getVolume()>0){
				a.receiveRoleVolume(receiveA, c.giveRoleVolume(receiveA));
			}
		}
		if(flagB){
			Role receiveB = rl_b.get(indexB);	// bが受け取るロール

			// 値があれば渡す
			if(a.getRole(receiveB).getVolume()>0){
				b.receiveRoleVolume(receiveB, a.giveRoleVolume(receiveB));
			}
			if(c.getRole(receiveB).getVolume()>0){
				b.receiveRoleVolume(receiveB, c.giveRoleVolume(receiveB));
			}
		}
		if(flagC){
			Role receiveC = rl_c.get(indexC);

			// 値があれば渡す
			if(a.getRole(receiveC).getVolume()>0){
				c.receiveRoleVolume(receiveC, a.giveRoleVolume(receiveC));
			}
			if(b.getRole(receiveC).getVolume()>0){
				c.receiveRoleVolume(receiveC, b.giveRoleVolume(receiveC));
			}
		}
	}

	/**
	 * ロールのリストからroleに対応する要素を取ってくる
	 *
	 * @param rl
	 * @param role
	 * @return
	 */
	private static Role getEqualRole(ArrayList<Role> rl, Role role) {
		for (Role crtRole : rl) {
			if (crtRole.equals(role)) {
				return crtRole;
			}
		}
		return null;
	}
	private static Role getEqualRole(MiniAgent agent, Role role){
		ArrayList<Role> rl = agent.getRoles();
		for (Role crtRole : rl) {
			if (crtRole.equals(role)) {
				return crtRole;
			}
		}
		return null;
	}

	/**
	 * ロールリストの生成
	 *
	 * @return 生成されたロールリスト
	 */
	public static ArrayList<Role> createRoleList() {
		ArrayList<Role> rl = new ArrayList<Role>(); // エージェントにセットされるロール変数
		ArrayList<Double> abilityList = new ArrayList<Double>(); // アビリティを保持するリスト
		// ロールの数だけロールを産む
		for(int i=0;i<roleNum;i++){
			// minからmaxまでの乱数でアビリティをセット
			abilityList.add(MyUtil.random(minValueAbility, maxValueAbility));
		}
		Collections.shuffle(abilityList); // アビリティリストのシャッフル
		for (int i = 0; i < abilityList.size(); i++) {
			// ランダムなアビリティのロールをロールリストに加える
			rl.add(createRole(String.valueOf(i), abilityList.get(i), maxValueAbility));
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
	private static Role createRole(String name, double ability, double volume) {
		Role r = new Role(name);
		r.setAbility(ability);
		r.setVolume(volume);
		return r;
	}

//
//	public static void exchangeRoleBigger(MiniAgent a, MiniAgent b) {
//		// 降順ロールリストのリスト
//		ArrayList<ArrayList<Role>> rlistList = new ArrayList<ArrayList<Role>>();
//		rlistList.add(a.getBiggerRoleList()); // aの降順ロールリスト
//		rlistList.add(b.getBiggerRoleList()); // bの降順ロールリスト
//		// 今見ているロールのindexのマップ
//		HashMap<ArrayList<Role>, Integer> indexMap = new HashMap<ArrayList<Role>, Integer>();
//		for (ArrayList<Role> roleList : rlistList) { // ロールリストをマップに加えて初期化
//			indexMap.put(roleList, 0);
//		}
//
//		// ボリュームがすでにない時にはインデックスを進める
//		for (ArrayList<Role> rlist : rlistList) {
//			// ロールの数だけ処理
//			for (Role role : rlist) {
//				if (role.getVolume() <= 0) { // ボリュームが無い時
//					int index = indexMap.get(rlist); // 対応するindexを取得
//					indexMap.put(rlist, index + 1); // indexの値を増やしてセット
//				} else {
//					break;
//				}
//			}
//		}
//
//		int index_a = indexMap.get(rlistList.get(0)); // aが受け取るべきindex
//		int index_b = indexMap.get(rlistList.get(1)); // bが受け取るべきindex
//		// 交換するべきロールがかぶっている時
//		if (index_a == index_b) {
//			// ロールの数を超えていない限り、ひとつ次のものを交換
//			if ((index_b + 1) < roleNum) {
//				// aの交換
//				Role receiveA = a.getBiggerRoleList().get(index_a); // aが受け取るべきロール
//				a.receiveRoleVolume(receiveA, b.giveRoleVolume(receiveA));
//				// bの交換
//				Role receiveB = b.getBiggerRoleList().get(index_b + 1); // bが受け取るべきロール
//				b.receiveRoleVolume(receiveB, a.giveRoleVolume(receiveB));
//			}
//		} else {
//			// そのまま交換
//			// aの交換
//			Role receiveA = a.getBiggerRoleList().get(index_a); // aが受け取るべきロール
//			a.receiveRoleVolume(receiveA, b.giveRoleVolume(receiveA));
//			// bの交換
//			Role receiveB = b.getBiggerRoleList().get(index_b); // bが受け取るべきロール
//			b.receiveRoleVolume(receiveB, a.giveRoleVolume(receiveB));
//		}
//	}
//
//	public static void exchangeRoleBigger(MiniAgent a, MiniAgent b){
//		ArrayList<Role> rl_a = a.getBiggerRoleList();	// aの降順ロールリスト
//		ArrayList<Role> rl_b = b.getBiggerRoleList();	// bの降順ロールリスト
//		int index_a = getExchangeIndex(b, rl_a,0);	// aが受け取るロールのindex
//		int index_b = getExchangeIndex(a, rl_b,0);	// bが受け取るロールのindex
//		Role roleToA = getEqualRole(b.getRoles(), rl_a.get(index_a));	// aが受け取るロール
//		Role roleToB = getEqualRole(a.getRoles(), rl_b.get(index_b));	// bが受け取るロール
//		if(!roleToA.equals(roleToB)){	// ロールがかぶって無い場合
//			a.receiveRoleVolume(roleToA, b.giveRoleVolume(rl_a.get(index_a)));	// aがロールの受け取り
//			b.receiveRoleVolume(roleToB, a.giveRoleVolume(rl_b.get(index_b)));	// bがロールの受け取り
//		}else{	// かぶってた場合
//			a.receiveRoleVolume(roleToA, b.giveRoleVolume(rl_a.get(index_a)));	// aがロールの受け取り
//			index_b = getExchangeIndex(a, rl_b, index_b+1);	// 次のindexを調べる
//			b.receiveRoleVolume(roleToB, a.giveRoleVolume(rl_b.get(index_b)));	// bがロールの受け取り
//		}
//	}
//
//	/**
//	 * 交換するindexを取得する
//	 * @param agent ボリュームを受け取ってくる相手のエージェント
//	 * @param biggerList
//	 * @param index 開始地点
//	 * @return
//	 */
//	private static int getExchangeIndex(MiniAgent agent, ArrayList<Role> biggerList,int index) {
//		for(int i=index;i<biggerList.size();i++){
//			Role role = getEqualRole(agent.getRoles(), biggerList.get(i));
//			// 受け取るロールのボリュームを相手が持ってなかったら
//			if(role.getVolume()<=0){
//				index++;
//			}else{
//				// 作業効率が相手より高かった場合
//				if(role.getAbility()<biggerList.get(index).getAbility()){
//					break;
//				}
//			}
//		}
//		return index;
//	}

}
