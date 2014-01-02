package agent;

import java.util.ArrayList;

import agent.role.Role;

/**
 * X座標を持つエージェントクラス
 * @author kohta
 *
 */
public class OneDSpotAgent extends MiniAgent{
	/**
	 * X座標
	 */
	private double coordinateX;
	/**
	 * コンストラクタ
	 * @param name
	 * @param x
	 */
	public OneDSpotAgent(String name,double x){
		super(name);
		coordinateX = x;
	}

	/**
	 * エージェントが持つ引数と同じロールを取得する
	 * @param role
	 * @return
	 */
	public Role getEqualRole(Role role){
		ArrayList<Role> rl = getRoles();
		for (Role crtRole : rl) {
			if (crtRole.equals(role)) {
				return crtRole;
			}
		}
		return null;
	}

	/**
	 * x座標のgetter
	 * @return
	 */
	public double getX(){
		return coordinateX;
	}

	/**
	 * x座標のsetter
	 * @param x
	 */
	public void setX(int x){
		this.coordinateX = x;
	}
}
