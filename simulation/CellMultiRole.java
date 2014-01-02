package simulation;

import agent.*;
import agent.role.Role;

import java.util.ArrayList;
import java.util.Collections;


import field.Field;

public class CellMultiRole {
	// ループ停止時間
	static final int SLEEP = 300;
	// シミュレーション年数
	static final int ENDYEAR = 100;
	// シミュレーションフィールド
	Field field;

	// 8近傍を検索するためのクラス
	class DXY{
		public int x;
		public int y;
		public DXY(int x,int y){this.x = x;this.y = y;}
	}
	// 8近傍を検索するための配列
	final DXY[] dxy = {
		new DXY(-1,0),// 左
		new DXY(-1,-1),// 左上
		new DXY(0,-1),// 上
		new DXY(1,-1),// 右上
		new DXY(1,0),// 右
		new DXY(1,1),// 右下
		new DXY(0,1),// 下
		new DXY(-1,1)// 左下
	};

	// セルの大きさ
	static final int WIDTH = 10;
	static final int HIGHT = 10;
	// セルオートマトン用のボード。周囲は壁
	Agent[][] board = new Agent[HIGHT+2][WIDTH+2];

	public static void main(String[] args){
		new CellMultiRole().start();
	}

	public void start(){
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
			// 周囲に合わせてロールの成長
			changeRole();
		}
	}

	// 初期化処理
	private void init(){
		// フィールドの初期化
		field = new Field(400,200,ENDYEAR);
		// フィールドの表示
		field.showField();
		// エージェントの配置
		setAgent();
	}

	// ボードにエージェントの配置
	private void setAgent(){
		for(int i=1;i<WIDTH+1;i++){
			for(int j=1;j<HIGHT+1;j++){
				board[i][j] = new Agent(ENDYEAR,3);
			}
		}
		// 周囲にはnullを配置
		for(int i=0;i<WIDTH+2;i++){
			board[i][0] = null;
			board[i][WIDTH+1]=null;
			board[0][i] = null;
			board[WIDTH+1][i] = null;
		}
	}

	// 周囲のエージェントのロールにあわせてエージェントのロールを変動させる
	public void changeRole(){
		for(int i=1;i<WIDTH+1;i++){
			for(int j=1;j<HIGHT+1;j++){
				circuitCheck(i,j);
			}
		}
	}

	// 8近傍のチェック
	private void circuitCheck(int x,int y){
		// 調べているエージェント
		Agent crtAgent = board[x][y];
		// 8近傍のエージェントを保持
		ArrayList<Agent> circuit = new ArrayList<Agent>();
		// すべてのロールを保持するリスト
		ArrayList<Role> allRoles = new ArrayList<Role>();
		// 左から時計回りに検索
		for(int i=0;i<dxy.length;i++){
			// 壁じゃなかったら
			if(board[x+dxy[i].x][y+dxy[i].y] != null){
				Agent addAgent = board[x+dxy[i].x][y+dxy[i].y];
				circuit.add(addAgent);
				for(Role tmpRole:addAgent.getRoles()){
					if(!tmpRole.isDead()){
						allRoles.add(tmpRole);
					}
				}
			}
		}
		// すべてのロールリストをソート
		Collections.sort(allRoles);
		BREAK:for(Role tmpRole:allRoles){
			for(Role crtRole:crtAgent.getRoles()){
				if(!crtRole.isDead() && tmpRole.getClass()==crtRole.getClass()){
					crtRole.growRole();
					break BREAK;
				}
			}
		}
	}
}
