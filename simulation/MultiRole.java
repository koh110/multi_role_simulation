package simulation;

import java.util.ArrayList;

import util.MyUtil;

import field.Field;

import agent.Agent;
import agent.role.Role;

public class MultiRole {
	// ループ停止時間
	final int SLEEP = 300;
	// シミュレーション年数
	final int ENDYEAR = 100;
	// シミュレーションフィールド
	Field field;
	// エージェント
	Agent a;
	Agent b;

	public static void main(String[] args){
		new MultiRole().start();
	}

	/**
	 * シミュレーションの中身
	 */
	void start(){
		// 初期化
		init();
		// シミュレーションループ
		while(!field.isEnd()){

			// 見やすくするための停止
			try {
				Thread.sleep(SLEEP);
			} catch (InterruptedException e) {
				System.out.println(e);
			}
			// 場の一年開始
			startYear();
			// 表示
			show();
			// 場の一年経過
			passYear();
		}
	}

	/**
	 * 初期化
	 */
	void init(){
		// フィールドの初期化
		field = new Field(400,200,ENDYEAR);
		// フィールドの表示
		field.showField();
		// エージェントの初期化
		a = new Agent(100,2);
		b = new Agent(120,3);
	}

	/**
	 * 画面表示
	 */
	void show(){
		// 描画をクリアする
		field.clearField();
		// 年数を表示する
		field.showYEAR();
		// エージェントの表示
		field.showAgent(a, 10, 30);
		field.showAgent(b, 150, 30);
	}

	/**
	 * 1年が始まる時の処理
	 */
	void startYear(){
		int year = field.getYEAR();
		// 10年に一度30%の確率で
		boolean flag = MyUtil.probability(30);
		if(year%10==0 && year!=0 && flag){
			/*
			int index = -1;
			// ランダムにロールを殺す
			index = (int)MyLibrary.random(0, a.getRoles().size()-1);
			*/
			// 一番傾きの低いロールを殺す
			ArrayList<Role> rlist = a.getRoles();
			Role min = rlist.get(0);
			for(Role r:rlist){
				if(r.getFunction().getSlope() <= min.getFunction().getSlope()){
					min = r;
				}
			}
			a.killRole(min);
		}
	}

	/**
	 * 1年過ぎた時の処理
	 */
	void passYear(){
		// 場の一年が過ぎる
		field.passYEAR();
		passAgentYear();
	}

	/**
	 * エージェントの歳を進める
	 */
	void passAgentYear(){
		a.passYear();
		b.passYear();
	}
}
