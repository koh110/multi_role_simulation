package agent.role;

import java.util.ArrayList;

/**
 * エージェントが持つロールのリスト
 * @author kohta
 *
 */
public class RoleList {
	/**
	 * 所持するロールのリスト
	 */
	private ArrayList<Role> roleList;

	/**
	 * コンストラクタ
	 */
	public RoleList(){
		// ロールリストの初期化
		roleList = new ArrayList<Role>();
	}

	/**
	 * ロールリストのgetter
	 * @return
	 */
	public ArrayList<Role> getRoleList(){
		return this.roleList;
	}

	/**
	 * indexで示されるロールを取得
	 * @param index
	 * @return
	 */
	public Role get(int index){
		return roleList.get(index);
	}

	/**
	 * 要素の追加
	 * @param role
	 */
	public void add(Role role) {
		this.roleList.add(role);
	}

	/**
	 * すべての要素を追加
	 * @param roleList
	 */
	public void addAll(ArrayList<Role> roleList) {
		this.roleList.addAll(roleList);
	}
	public void addAll(RoleList roleList){
		this.roleList.addAll(roleList.getRoleList());
	}

	/**
	 * 要素をセットする
	 * @param roleList
	 */
	public void setRoleList(ArrayList<Role> roleList){
		this.roleList = roleList;
	}
	public void setRoleList(RoleList roleList){
		this.roleList = roleList.getRoleList();
	}

	/**
	 * 要素を削除
	 * @param index
	 */
	public void remove(int index){
		this.roleList.remove(index);
	}
	/**
	 * 要素を削除
	 * @param role
	 */
	public void remove(Role role){
		this.roleList.remove(role);
	}

	/**
	 * ロールリストのサイズを取得
	 * @return
	 */
	public int size(){
		return roleList.size();
	}

	/**
	 * ロールの取得
	 * @param role
	 * @return
	 */
	public Role getRole(Role role) {
		for (Role crtRole : roleList) {
			if (crtRole.equals(role)) {
				return crtRole;
			}
		}
		return null;
	}

	/**
	 * ロールコストの分散
	 * @return
	 */
	public double dispersion(){
		// 相加平均
		final double average = this.average();
		// 合計
		double total = 0;
		for(Role role:this.roleList){
			// ロールコスト
			double roleCost = role.getRoleCost();
			total += (average - roleCost)*(average - roleCost);
		}
		return total/roleList.size();
	}

	/**
	 * ロールコストの平均値
	 * @return
	 */
	public double average(){
		double total = 0;	// 合計値
		for(Role role:this.roleList){
			total += role.getRoleCost();
		}
		return total/this.roleList.size();
	}

	public Object clone(){
		try{
			RoleList clone = (RoleList)super.clone();
			// 参照型のフィールドにもclone
			clone.roleList = new ArrayList<Role>();
			for(Role role:this.roleList){
				clone.roleList.add((Role)role.clone());
			}
			return clone;
		}catch(CloneNotSupportedException e){
			return null;
		}
	}
}
