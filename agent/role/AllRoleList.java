package agent.role;

import java.util.ArrayList;

import agent.role.roles.*;

import util.MyUtil;

/**
 * ロールのリストクラス
 * @author kohta
 *
 */
public class AllRoleList {
	// 存在するロール全てを保持するリスト
	static ArrayList<Role> roleList;

	/**
	 * コンストラクター
	 */
	public AllRoleList(){
		roleList = new ArrayList<Role>();
		roleList.add(new GrowingRice());
		roleList.add(new GrowingVegetable());
		roleList.add(new Cooking());
		roleList.add(new Fishing());
		roleList.add(new Teaching());
	}

	/**
	 * 存在するロールを返す
	 * @return 存在する全てのロール
	 */
	public ArrayList<Role> getRoleList(){
		return roleList;
	}

	public ArrayList<Role> getRoleList(int size){
		// 必要とするサイズより、存在するサイズの方が小さかったら
		if(size>roleList.size()){
			return roleList;
		}
		else{
			// 返却するリストのインデックスを得るための配列
			int[] index = returnIndex(size);
			ArrayList<Role> returnRoleList = new ArrayList<Role>();
			for(int i=0;i<size;i++){
				returnRoleList.add(roleList.get(index[i]));
			}
			return returnRoleList;
		}
	}

	private int[] returnIndex(int size){
		int[] index = new int[roleList.size()];
		for(int i=0;i<index.length;i++){
			index[i] = i;
		}
		// インデックスの値をシャッフルする
		for(int i=0;i<index.length;i++){
			int random = (int)MyUtil.random(0, index.length-1);
			int tmp = index[0];
			index[0] = index[random];
			index[random] = tmp;
		}
		int[] returnIndex = new int[size];
		for(int i=0;i<size;i++){
			returnIndex[i] = index[i];
		}
		return index;
	}
}
