package agent.role.exchange;

import java.util.ArrayList;
import agent.OneDSpotAgent;
import agent.role.Role;

/**
 * x座標を持つエージェントのロール交換
 *
 * @author kohta
 *
 */
public class OneDRoleExchange extends RoleExchange {
	/**
	 * 交換が行える最大距離
	 */
	//public static final double maxDistance = 10.0;

	/**
	 * エージェント2体による相手の大きい所に同時に渡しあう
	 *
	 * @param a
	 * @param b
	 * @param distance 交換が行える距離
	 */
	public static void exchangeRoleBigger(OneDSpotAgent a, OneDSpotAgent b, double distance) {
		// 交換できる最大距離を越えていたら交換しない
		if (Math.abs(a.getX() - b.getX()) > distance) {
			return;
		}

		boolean flagA = false; // aの交換が終ったフラグ
		boolean flagB = false; // bの交換が終ったフラグ
		ArrayList<Role> rl_a = a.getBiggerRoleList(); // aの降順ロールリスト
		ArrayList<Role> rl_b = b.getBiggerRoleList(); // bの降順ロールリスト
		int indexA = -1;
		int indexB = -1;
		for (int i = 0; i < roleNum; i++) {
			Role crtRoleA = rl_a.get(i);
			Role crtRoleB = rl_b.get(i);
			// aが受け取るroleindexの決定
			if (!flagA) {
				if (crtRoleA.getAbility() > b.getEqualRole(crtRoleA)
						.getAbility()) { // aの作業効率が上回っている時
					if (b.getEqualRole(crtRoleA).getVolume() > 0) { // ボリュームが有る時
						indexA = i;
						flagA = true;
					}
				}
			}
			// bが受け取るroleindexの決定
			if (!flagB) {
				if (crtRoleB.getAbility() > a.getEqualRole(crtRoleB)
						.getAbility()) { // bの作業効率が上回っている時
					if (a.getEqualRole(crtRoleB).getVolume() > 0) { // ボリュームが有る時
						indexB = i;
						flagB = true;
					}
				}
			}
			if (flagA && flagB) { // indexを取り終わったら終了
				break;
			}
		}

		if (flagA && flagB) { // 両方共値がある場合
			Role receiveA = rl_a.get(indexA); // aが受け取るロール
			Role receiveB = rl_b.get(indexB); // bが受け取るロール

			OneDSpotAgent aClone = (OneDSpotAgent) a.clone(); // エージェントのクローン
			OneDSpotAgent bClone = (OneDSpotAgent) b.clone();

			aClone.receiveRoleVolume(receiveA, bClone.giveRoleVolume(receiveA)); // aが受け取り
			bClone.receiveRoleVolume(receiveB, aClone.giveRoleVolume(receiveB)); // bが受け取り

			if (a.getTotalRoleCost() > aClone.getTotalRoleCost()) { // 効率が良くなった場合交換
				a.receiveRoleVolume(receiveA, b.giveRoleVolume(receiveA)); // aが受け取り
			}
			if (b.getTotalRoleCost() > bClone.getTotalRoleCost()) {
				b.receiveRoleVolume(receiveB, a.giveRoleVolume(receiveB)); // bが受け取り
			}
			// System.out.println(receiveA.getName()+"と"+receiveB.getName());
		}
	}

	/**
	 * エージェント3体による相手の大きい所に渡しあう処理
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param distance 交換が行える距離
	 */
	public static void exchangeRoleBigger(OneDSpotAgent a, OneDSpotAgent b, OneDSpotAgent c, double distance) {
		// 交換できる最大距離以内の場合交換を行う

		if (Math.abs(a.getX() - b.getX()) <= distance) {
			if(Math.abs(a.getX() - c.getX()) <= distance){
				if(Math.abs(b.getX()-c.getX()) <= distance){

					// 交換処理
					threeExchangeSubstance(a, b, c);

				}
			}
		}

	}

	/**
	 * エージェント3体交換の処理
	 * @param a
	 * @param b
	 * @param c
	 */
	private static void threeExchangeSubstance(OneDSpotAgent a,
			OneDSpotAgent b, OneDSpotAgent c) {
		boolean flagA = false; // aの交換が終ったフラグ
		boolean flagB = false; // bの交換が終ったフラグ
		boolean flagC = false; // cの交換が終ったフラグ
		ArrayList<Role> rl_a = a.getBiggerRoleList(); // aの降順ロールリスト
		ArrayList<Role> rl_b = b.getBiggerRoleList(); // bの降順ロールリスト
		ArrayList<Role> rl_c = c.getBiggerRoleList(); // cの降順ロールリスト
		int indexA = -1;
		int indexB = -1;
		int indexC = -1;
		for (int i = 0; i < roleNum; i++) {
			Role crtRoleA = rl_a.get(i);
			Role crtRoleB = rl_b.get(i);
			Role crtRoleC = rl_c.get(i);

			// aが受け取るroleindexの決定
			if (!flagA) {
				double ability = crtRoleA.getAbility();
				// aの作業効率が上回っている時
				if (ability > b.getEqualRole(crtRoleA).getAbility()
						&& ability > c.getEqualRole(crtRoleA).getAbility()) {
					// ボリュームが有る時
					if (b.getEqualRole(crtRoleA).getVolume() > 0
							|| c.getEqualRole(crtRoleA).getVolume() > 0) {
						indexA = i;
						flagA = true;
					}
				}
			}
			// bが受け取るroleindexの決定
			if (!flagB) {
				double ability = crtRoleB.getAbility();
				// bの作業効率が上回っている時
				if (ability > a.getEqualRole(crtRoleB).getAbility()
						&& ability > c.getEqualRole(crtRoleB).getAbility()) {
					// ボリュームが有る時
					if (a.getEqualRole(crtRoleB).getVolume() > 0
							|| c.getEqualRole(crtRoleB).getVolume() > 0) {
						indexB = i;
						flagB = true;
					}
				}
			}
			// cが受け取るroleindexの決定
			if (!flagC) {
				double ability = crtRoleC.getAbility();
				// cの作業効率が上回っている時
				if (ability > a.getEqualRole(crtRoleC).getAbility()
						&& ability > b.getEqualRole(crtRoleC).getAbility()) {
					// ボリュームが有る時
					if (a.getEqualRole(crtRoleC).getVolume() > 0
							|| b.getEqualRole(crtRoleC).getVolume() > 0) {
						indexC = i;
						flagC = true;
					}
				}
			}
			if (flagA && flagB && flagC) { // indexを取り終わったら終了
				break;
			}
		}

		if (flagA) {
			Role receiveA = rl_a.get(indexA); // aが受け取るロール

			// 値があれば渡す
			if (b.getRole(receiveA).getVolume() > 0) {
				a.receiveRoleVolume(receiveA, b.giveRoleVolume(receiveA)); // aが受け取り
			}
			if (c.getRole(receiveA).getVolume() > 0) {
				a.receiveRoleVolume(receiveA, c.giveRoleVolume(receiveA));
			}
		}
		if (flagB) {
			Role receiveB = rl_b.get(indexB); // bが受け取るロール

			// 値があれば渡す
			if (a.getRole(receiveB).getVolume() > 0) {
				b.receiveRoleVolume(receiveB, a.giveRoleVolume(receiveB));
			}
			if (c.getRole(receiveB).getVolume() > 0) {
				b.receiveRoleVolume(receiveB, c.giveRoleVolume(receiveB));
			}
		}
		if (flagC) {
			Role receiveC = rl_c.get(indexC);

			// 値があれば渡す
			if (a.getRole(receiveC).getVolume() > 0) {
				c.receiveRoleVolume(receiveC, a.giveRoleVolume(receiveC));
			}
			if (b.getRole(receiveC).getVolume() > 0) {
				c.receiveRoleVolume(receiveC, b.giveRoleVolume(receiveC));
			}
		}
	}
}
