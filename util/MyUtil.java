package util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class MyUtil {
	/**
	 * min~maxまでの指定範囲乱数の取得
	 *
	 * @param min
	 * @param max
	 * @return min~maxまでの乱数
	 */
	public static double random(double min, double max) {
		return Math.floor(Math.random() * (max - min + 1.0)) + min;
	}

	/**
	 * 引数(%)の割合でtrueを返す
	 *
	 * @param percent
	 * @return percentの確率でtrue
	 */
	public static boolean probability(double percent) {
		double rand_num = random(0, 100);
		if (rand_num < percent) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 正規分布に従う乱数
	 * @param omega 平均値
	 * @param sigma 標準偏差
	 * @return
	 */
	public static double normalDistributionRandNum(double omega,double sigma){
		Random random = new Random();
		double rand = random.nextGaussian();
		rand = rand*sigma+omega;
		return rand;
	}
	/**
	 * 正規分布に従う乱数
	 * @param min 最小値
	 * @param max 最大値
	 * @return
	 */
	public static double nomalDistributionRandNum(double min,double max){
		Random random = new Random();
		double randNum = -1;
		do{
			randNum = random.nextGaussian();
			if(min<=randNum && randNum<=max){
				break;
			}
		}while(true);
		return randNum;
	}
//
//	/**
//	 * 正規分布に従う乱数の発生
//	 * @param standardDeviation 標準偏差
//	 * @param averageValue 平均値
//	 * @return
//	 */
//	static int switchNum = 0;	// リターン切り替え用変数
//	static double whiteGaussianNoise2 = 0;	// ホワイトガウスノイズ2
//	public static double gaussianRand(double standardDeviation, double averageValue){
//		double returnNum = 0.0;	// リターン用変数
//
//		double randNum1 = 0;	// 乱数1
//		double randNum2 = 0;	// 乱数2
//		double whiteGaussianNoise1 = 0;	// ホワイトガウスノイズ1
//
//		if(switchNum == 0){	// スイッチが0なら
//			switchNum = 1;	// スイッチの切り替え
//			randNum1 = random(0,9);	// [0,9]の乱数取得
//			randNum2 = random(0,9);	// [0,9]の乱数取得
//
//			// 正規乱数1
//			whiteGaussianNoise1 = standardDeviation * Math.sqrt(-2.0 * Math.log(randNum1)) * Math.cos(2.0 * Math.PI * randNum2) + averageValue;
//			// 正規乱数2
//			whiteGaussianNoise2 = standardDeviation * Math.sqrt(-2.0 * Math.log(randNum1)) * Math.cos(2.0 * Math.PI * randNum2) + averageValue;
//
//			// 正規乱数1を返す
//			returnNum = whiteGaussianNoise1;
//		}
//		else{
//			switchNum = 0;	// スイッチ切り替え
//			// 正規乱数2を返す
//			returnNum = whiteGaussianNoise2;
//		}
//
//		return returnNum;
//	}

	/**
	 * ArrayListをシャッフルする
	 *
	 * @param list
	 * @return シャッフルしたArrayList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList shuffle(ArrayList list) {
		ArrayList shuffle = (ArrayList) list.clone();
		for (int i = 0; i < shuffle.size(); i++) {
			int random = (int) MyUtil.random(0, shuffle.size() - 1);
			Object tmp = shuffle.get(0);
			shuffle.set(0, shuffle.get(random));
			shuffle.set(random, tmp);
		}
		return shuffle;
	}

	/**
	 *  引数で指定した配列をシャッフルする
	 * @param arr
	 */
	public static int[] shuffle(int[] arr) {
		for (int i = arr.length - 1; i > 0; i--) {
			int t = (int) (Math.random() * i); // 0～i-1の中から適当に選ぶ

			// 選ばれた値と交換する
			int tmp = arr[i];
			arr[i] = arr[t];
			arr[t] = tmp;
		}
		return arr;
	}
	public static double[] shuffle(double[] arr){
		for (int i = arr.length - 1; i > 0; i--) {
			int t = (int) (Math.random() * i); // 0～i-1の中から適当に選ぶ

			// 選ばれた値と交換する
			double tmp = arr[i];
			arr[i] = arr[t];
			arr[t] = tmp;
		}
		return arr;
	}

	/**
	 * LinkedListをシャッフルする
	 *
	 * @param list
	 * @return シャッフルしたLinkedList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static LinkedList shuffle(LinkedList list) {
		LinkedList shuffle = (LinkedList) list.clone();
		for (int i = 0; i < shuffle.size(); i++) {
			int random = (int) MyUtil.random(0, shuffle.size() - 1);
			Object tmp = shuffle.get(0);
			shuffle.set(0, shuffle.get(random));
			shuffle.set(random, tmp);
		}
		return shuffle;
	}
}
