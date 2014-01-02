package agent.role;

import java.text.*;

/**
 * 関数クラス
 * @author kohta
 */
public class Function implements Comparable,Cloneable{
	/**
	 *  関数の傾き
	 */
	private double slope;
	/**
	 *  関数の切片
	 */
	private double intercept;

	/**
	 * コンストラクタ
	 * @param slope 関数の傾き
	 * @param intercept 関数の切片
	 */
	Function(double slope,double intercept){
		this.slope = slope;
		this.intercept = intercept;
	}

	/**
	 * 関数の計算結果
	 * @param x 関数に与える数値
	 */
	public double answer(double x){
		return slope*x + intercept;
	}

	/**
	 * 傾きのgetter
	 * @return 関数の傾き
	 */
	public double getSlope() {
		return slope;
	}

	/**
	 * 切片のgetter
	 * @return 切片の傾き
	 */
	public double getIntercept() {
		return intercept;
	}

	/**
	 * 関数のtoString
	 * @return 表示用関数
	 */
	public String toString(){
		// 表示桁数合わせ
		NumberFormat format = NumberFormat.getInstance();
		// 表示桁数2桁
		format.setMaximumFractionDigits(2);
		return format.format(slope)+"x+"+format.format(intercept);
	}

	/**
	 * 比較用
	 * @return 大きい時1。小さい時-1。同じ時0
	 */
	@Override
	public int compareTo(Object object) {
		// TODO 自動生成されたメソッド・スタブ
		Function operand = (Function) object;
		if(slope > operand.slope){
			return 1;
		}else if(slope < operand.slope){
			return -1;
		}else{
			return 0;
		}
	}
	/**
	 * コピー用
	 * @return コピーされたオブジェクト
	 */
	@Override
	public Object clone(){
		try{
			Function clone = (Function)super.clone();
			return clone;
		}catch(CloneNotSupportedException e){
			return null;
		}
	}
}
