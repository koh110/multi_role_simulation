package agent.role;

import java.util.Comparator;

import util.MyUtil;

/**
 * ロールクラス
 *
 * @author kohta
 *
 */
public class Role implements Comparable,Cloneable {
	/**
	 * ロールの名前
	 */
	private String roleName;
	/**
	 * ロールの評価関数
	 */
	private Function f;
	/**
	 * ロールの才能
	 */
	private double ability;
	/**
	 * ロールの使用量
	 */
	private double volume;
	/**
	 * ロールの死亡フラグ
	 */
	private boolean isDead;
	/**
	 * ロールの持続期間
	 */
	private int length;

	/**
	 * コンストラクタ
	 *
	 * @param name
	 *            役割の名前
	 */
	public Role(String name) {
		this.roleName = name;
		double slope = MyUtil.random(0, 5);
		double intercept = MyUtil.random(0, 5);
		this.f = new Function(slope, intercept);
		this.ability = MyUtil.random(0, 100);
		this.volume = MyUtil.random(0, 10);
		this.isDead = false;
		this.length = 1;
	}

	/**
	 * 評価関数を才能にしたがって成長させる
	 */
	public void growRole() {
		double slope = f.getSlope();
		double intercept = f.getIntercept();
		if (MyUtil.probability(ability)) {
			slope += 0.5;
			intercept += 0.5;
		} else {
			slope += 0.1;
			intercept += 0.1;
		}
		this.f = new Function(slope, intercept);
	}

	/**
	 * 評価関数の計算を行う
	 *
	 * @param x
	 *            評価に与える値
	 */
	public void answer(double x) {
		f.answer(x);
	}

	/**
	 * ボリュームを使用する
	 *
	 * @param use
	 */
	public void useVolume(double use) {
		this.volume -= use;
	}

	/**
	 * ボリュームを回復させる
	 *
	 * @param cure
	 */
	public void cureVolume(double cure) {
		this.volume += cure;
	}

	/**
	 * ロールのコスト
	 *
	 * @return 受け取り手が支払うべきロールのコスト
	 */
	public double getRoleCost() {
		return this.volume / this.ability;
	}

	/**
	 * 役割を殺す
	 */
	public void killRole() {
		this.isDead = true;
	}

	/**
	 * 役割を生き返す
	 */
	public void reliveRole() {
		this.isDead = false;
	}

	/**
	 * 役割が生きているかどうか
	 *
	 * @return isDead
	 */
	public boolean isDead() {
		return isDead;
	}

	// =============================================================
	// setter,getter
	// =============================================================
	/**
	 * ロールのgetter
	 *
	 * @return ロールの名前
	 */
	public String getName() {
		return roleName;
	}

	/**
	 * 評価関数のgetter
	 *
	 * @return 評価関数
	 */
	public Function getFunction() {
		return f;
	}

	/**
	 * 名前のgetter
	 *
	 * @return 役割の名前
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * ボリュームのgetter
	 *
	 * @return ボリュームの量
	 */
	public double getVolume() {
		return volume;
	}

	/**
	 * ボリュームのsetter
	 *
	 * @param value
	 *            setするリュームの値
	 */
	public void setVolume(double value) {
		this.volume = value;
	}

	/**
	 * 才能(ability)のgetter
	 *
	 * @return ability 才能の値
	 */
	public double getAbility() {
		return this.ability;
	}

	/**
	 * 才能(ability)のsetter
	 *
	 * @param value
	 *            setする才能の値
	 */
	public void setAbility(double value) {
		this.ability = value;
	}

	/**
	 * ロールが同じものかどうかを取得する
	 *
	 * @param role
	 * @return
	 */
	public boolean equals(Role role) {
		// 名前が等しければtrue
		if (this.getName().equals(role.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * ロールの持続期間を取得する
	 * @return
	 */
	public int getLength(){
		return length;
	}

	/**
	 * ロールの持続期間を設定する
	 * @param setLength	設定する持続期間
	 */
	public void setLength(int setLength){
		this.length = setLength;
	}

	/**
	 * 文字列化
	 */
	@Override
	public String toString(){
		return "[name:"+this.roleName+",ablity:"+this.ability+",volume:"+this.volume+"]";
	}

	// =============================================================
	// implements
	// =============================================================
	/**
	 * 比較メソッド
	 *
	 * @return 大きい時1。小さい時-1。同じ時0
	 */
	@Override
	public int compareTo(Object object) {
		Role operand = (Role) object;
		if (ability > operand.ability) {
			return 1;
		} else if (ability < operand.ability) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * クローンメソッド
	 * @reutn コピーを返す
	 */
	public Object clone(){
		try{
			Role clone = (Role)super.clone();
			clone.f = (Function)this.f.clone();
			return clone;
		}catch(CloneNotSupportedException e){
			return null;
		}
	}
}
