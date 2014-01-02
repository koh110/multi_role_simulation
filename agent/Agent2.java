package agent;

import java.util.ArrayList;
import java.util.Collections;

import agent.role.Role;
import agent.role.AllRoleList;
import agent.role.RoleList;


/**
 * エージェントクラス
 * @author kohta
 *
 */
public class Agent2 implements Cloneable{
	/**
	 *  寿命
	 */
	private int life;
	/**
	 *  年齢
	 */
	private int age;
	/**
	 *  ロールのリスト
	 */
	private RoleList havingRoles;
	/**
	 *  死んだロールリスト
	 */
	private RoleList deadRoles;
	// 1度に成長させられるロールの限界数
	private int growRoleNum;

	/**
	 * コンストラクタ
	 * @param life エージェントの寿命
	 */
	public Agent2(int life,int growRoleNum){
		this.life = life;
		this.age = 0;
		this.havingRoles = new RoleList();
		this.deadRoles = new RoleList();
		setRole(new AllRoleList().getRoleList());
		this.growRoleNum = growRoleNum;
	}

	/**
	 * ロールの追加
	 * @param role 追加するロール
	 */
	public void addRole(Role role){
		havingRoles.add(role);
	}

	/**
	 * ロールのリストを加える
	 * @param roleList 追加するロール
	 */
	public void addRole(ArrayList<Role> roleList){
		havingRoles.addAll(roleList);
	}

	/**
	 * ロールのセット
	 * @param roleList セットするロール
	 */
	public void setRole(ArrayList<Role> roleList){
		havingRoles.setRoleList(roleList);
	}

	/**
	 * ロールの交換
	 * @param inputRole 追加されるロール
	 * @param outputRole 手放されるロール
	 */
	public void exchangeRole(Role inputRole,Role outputRole){
		for(Role crtRole:havingRoles.getRoleList()){
			// 手放されるロールがあったらそのロールを殺す
			if(crtRole.getClass() == outputRole.getClass()){
				crtRole.killRole();
				break;
			}
		}
		for(Role crtRole:havingRoles.getRoleList()){
			if(crtRole.getClass() == inputRole.getClass()){
				return;
			}
		}
		// 追加されるロールを持っていなければそのクラスを生成して追加する
		AllRoleList rList = new AllRoleList();
		for(Role crtRole:rList.getRoleList()){
			if(crtRole.getClass() == inputRole.getClass()){
				addRole(crtRole);
			}
		}
	}

	/**
	 * ランダムに成長限界個数までロールを成長させる
	 */
	public void growRoles(){
		ArrayList<Role> shuffledList = (ArrayList<Role>)havingRoles.clone();	// シャッフル用にロールのクローンを生成
		Collections.shuffle(shuffledList);	// をシャッフル
		//ArrayList<Role> shuffledList = MyUtil.shuffle(havingRoles);
		int counter=0;
		for(Role r:shuffledList){
			// ロールが死んでいなかったら
			if(!r.isDead()){
				// ロールを成長させる
				r.growRole();
				counter++;
				// 成長限界個数成長させたら終了
				if(counter >= growRoleNum){
					break;
				}
			}
		}
	}

	/**
	 * ロールの成長を止める
	 * @param r 成長を止めるロール
	 */
	public void killRole(Role killedRole){
		for(Role r : havingRoles.getRoleList()){
			if(r.getClass() == killedRole.getClass()){
				r.killRole();
				break;
			}
		}
	}

	/**
	 * ロールの成長を止める
	 * @param index 成長を止めるロールのインデックス
	 */
	public void killRole(int index){
		havingRoles.get(index).killRole();
	}

	/**
	 * 成長の止まったロールをkilledリストに登録する
	 */
	private void moveDeadRole(){
		for(Role r:havingRoles.getRoleList()){
			if(r.isDead()){
				deadRoles.add(r);
			}
		}
		for(Role r:deadRoles.getRoleList()){
			havingRoles.remove(r);
		}
	}

	/**
	 * エージェントの死亡確認
	 * @return isDead
	 */
	public boolean isDead(){
		// 年齢が寿命を超えていたら死亡
		if(age>=life){
			return true;
		}else{
			return false;
		}
	}


	/**
	 * 1年の開始処理
	 */
	public void startYear(){

	}

	/**
	 * エージェントの1年が過ぎた時の処理
	 */
	public void passYear(){
		aging();
		growRoles();
		moveDeadRole();
	}

	/**
	 * エージェントの年を取らせる
	 */
	private void aging(){
		if(age<life){
			age++;
		}
	}

	/**
	 * ロールリストのgetter
	 * @return havingRoles ロールリスト
	 */
	public ArrayList<Role> getRoles(){
		return havingRoles.getRoleList();
	}
	public RoleList getRoleList(){
		return havingRoles;
	}

	public Role getRole(Role role){
		return havingRoles.getRole(role);
	}

	/**
	 * deadRoleのgetter
	 * @return deadRoles 死んだロールのリスト
	 */
	public ArrayList<Role> getDeadRoles(){
		return deadRoles.getRoleList();
	}
	public RoleList getDeadRoleList(){
		return deadRoles;
	}

	public Object clone(){
		try{
			Agent2 clone = (Agent2)super.clone();
			return clone;
		}catch(CloneNotSupportedException e){
			return null;
		}
	}
}
