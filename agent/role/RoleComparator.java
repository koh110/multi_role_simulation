package agent.role;

import java.util.Comparator;

/**
 * ロール比較用クラス
 * @author kohta
 *
 */
public class RoleComparator implements Comparator {
	public static final int ASC = 1; // 昇順
	public static final int DESC = -1; // 降順
	private int sort = ASC; // デフォルトは昇順

	public RoleComparator() {
	};

	public RoleComparator(int sort) {
		this.sort = sort;
	}

	public int compare(Object arg0, Object arg1) {
		if (!(arg0 instanceof Comparable) || !(arg1 instanceof Comparable)) {
			throw new IllegalArgumentException(
					"arg0 & arg1 must implements interface of java.lang.Comparable.");
		}

		if (arg0 == null && arg1 == null) {
			return 0; // arg0 = arg1
		} else if (arg0 == null) {
			return 1 * sort; // arg1 > arg2
		} else if (arg1 == null) {
			return -1 * sort; // arg1 < arg2
		}

		return ((Comparable) arg0).compareTo((Comparable) arg1) * sort;
	}
}