package field;

import java.util.ArrayList;


import agent.Agent;
import agent.role.Role;

/**
 * シミュレーションフィールドクラス
 * シミュレーションを行うフィールドを定義するクラス
 * @author kohta
 *
 */
public class Field {
	// キャンバスサイズ
	private int SIZE_X;
	private int SIZE_Y;
	// 経過年数
	private int YEAR;
	// 終了年数	世界の終わり
	private int ENDYEAR;

	public Field(int canvasSize_X,int canvasSize_Y,int endYear){
		this.YEAR = 0;
		this.SIZE_X = canvasSize_X;
		this.SIZE_Y = canvasSize_Y;
		this.ENDYEAR = endYear;
	}

//============================================================
// 描画系
//============================================================
	/**
	 *  フィールドを表示する
	 */
	public void showField(){
		Canvas.show(SIZE_X,SIZE_Y);
	}

	/**
	 *  フィールドに描画されたものをクリアする
	 */
	public void clearField(){
		// 描画する前にかかれてたところ消す
		Canvas.clear();
	}

	/**
	 *  フィールドに年数を表示
	 */
	public void showYEAR(){
		int x = 10;
		int y = 10;
		Canvas.setColor(0, 0, 0);
		Canvas.fillRect(x, y, 100, 10);
		Canvas.setColor(255, 255, 255);
		Canvas.drawString(x+10, y+10, "now year:"+YEAR);
	}

	/**
	 *  エージェントを表示する
	 * @param agent
	 * @param x
	 * @param y
	 */
	public void showAgent(Agent agent,int x,int y){
		ArrayList<Role> roles = agent.getRoles();
		// 箱の大きさ
		int length = 130, hight = 15;
		// 表示色
		Canvas.setColor(0, 0, 0);
		// 表示調整用
		int index=0;
		for(int i=0;i<roles.size();i++){
			// 参照しているエージェントのロールが死んでいなかったら
			if(!agent.getRoles().get(i).isDead()){
				Canvas.drawRect(x, y+index*hight, length, hight);
				Role r = roles.get(i);
				// 役割の名前と評価関数の表示
				Canvas.drawString(x+3, y+index*hight+hight-2,r.getRoleName()+r.getFunction());
				index++;
			}
		}
	}

//============================================================

	/**
	 *  フィールドの年数を進める
	 */
	public void passYEAR(){
		YEAR++;
	}

	/**
	 *  世界が終わったかどうか
	 * @return 終了年数を超えていたらtrue
	 */
	public boolean isEnd(){
		if(this.YEAR>this.ENDYEAR)
			return true;
		else
			return false;
	}



	// こっから下getter,setter
	public int getSIZE_X() {
		return SIZE_X;
	}

	public void setSIZE_X(int size) {
		SIZE_X = size;
	}

	public int getSIZE_Y() {
		return SIZE_Y;
	}

	public void setSIZE_Y(int size) {
		SIZE_Y = size;
	}

	public int getYEAR() {
		return YEAR;
	}

	public void setYEAR(int year) {
		YEAR = year;
	}

	public int getENDYEAR() {
		return ENDYEAR;
	}

	public void setENDYEAR(int endyear) {
		ENDYEAR = endyear;
	}
}
