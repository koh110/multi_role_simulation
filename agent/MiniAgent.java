package agent;


import java.util.ArrayList;
import java.util.Collections;

import agent.role.Role;
import agent.role.RoleComparator;

/**
 * スモールシミュレーションのためのエージェントクラス
 * @author kohta
 *
 */
public class MiniAgent extends Agent2{
	private String name;	// 名前
	/**
	 * コンストラクタ
	 * @param name 名前
	 */
	public MiniAgent(String name){
		super(0,0);
		this.name = name;
	}

	/**
	 * 引数に与えられたロールと同じ名前のロールのボリュームを全て取ってくる
	 * @param role
	 * @return 渡されるボリュームの値
	 */
	public double giveRoleVolume(Role role){
		ArrayList<Role> tmp = this.getRoles();	// 所持しているロールのリスト
		Role returnRole = null;	// 返すボリュームもつロール
		for(Role crt:tmp){
			if(role.getName().equals(crt.getName())){	// ロールの名前が一致したら
				returnRole = crt;	// 返す
				break;
			}
		}
		double volume = returnRole.getVolume();	// ボリュームを取得
		returnRole.setVolume(0);	// 渡すのでボリュームは0になる
		return volume;
	}

	/**
	 * 引数に与えられたロールと同じ名前のロールに引数のボリュームを足す
	 * @param role
	 * @param volume
	 */
	public void receiveRoleVolume(Role role,double volume){
		ArrayList<Role> tmp = this.getRoles();	// 所持しているロールのリスト
		for(Role crt:tmp){
			if(role.getName().equals(crt.getName())){	// ロールの名前が一致したら
				double formerVolume = crt.getVolume();	// 元のボリュームを取得
				crt.setVolume(formerVolume+volume);	// 引数のボリュームを足してセットする
				break;
			}
		}
	}

	/**
	 * ロールコストのトータルを取得する
	 * @return ロールコストのトータル
	 */
	public double getTotalRoleCost(){
		ArrayList<Role> tmp = this.getRoles();	// 所持しているロールのリスト
		double total = 0;	// ロールのコストの合計
		for(Role role:tmp){
			total += role.getRoleCost();
		}
		return total;
	}

	/**
	 * ロールのアビリティが高い順番に並んだリストを返す
	 * @return
	 */
	public ArrayList<Role> getBiggerRoleList(){
		ArrayList<Role> roleList = (ArrayList<Role>)getRoles().clone();
		Collections.sort(roleList, new RoleComparator(RoleComparator.DESC));
		return roleList;
	}

	/**
	 * ロールのアビリティが低い順番に並んだリストを返す
	 * @return
	 */
	public ArrayList<Role> getSmallerRoleList(){
		ArrayList<Role> roleList = (ArrayList<Role>)getRoles().clone();
		Collections.sort(roleList);
		return roleList;
	}

	/**
	 *  名前のgetter
	 * @return 名前
	 */
	public String getName(){
		return this.name;
	}
}
