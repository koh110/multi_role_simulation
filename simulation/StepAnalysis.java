package simulation;

import java.util.ArrayList;

import agent.role.exchange.RoleExchange;
import util.CSVReaderWriter;

/**
 * 途中経過を分析するクラス
 *
 * @author kohta
 *
 */
public class StepAnalysis {
	/**
	 * 1次元座標エージェント用分析メソッド
	 * @param stepFile	ステップ情報を保存したファイル
	 * @param distanceFile	距離情報を保存したファイル
	 */
	public static void OneDSpotSimAnalysis(OneDSpotSimulation simulation,String stepFile,String distanceFile){
		// ファイル読み込み
		ArrayList<String[]> step = new ArrayList<String[]>();	// ステップファイル読み込み変数
		step = CSVReaderWriter.read(stepFile);	// 読み込み
		ArrayList<String[]> distanceFileStr = new ArrayList<String[]>();	// 距離ファイル読み込み
		distanceFileStr = CSVReaderWriter.read(distanceFile);	// 読み込み

		// ファイル処理
		int[][][] counter = new int[RoleExchange.roleNum][simulation.getExchangeNum()][simulation.getAgentNum()+1];
		ArrayList<Double>[][][] distanceHistgram = new ArrayList[RoleExchange.roleNum][simulation.getExchangeNum()][simulation.getAgentNum()+1];	// それぞれのロール数に応じたエージェントの距離
		for(int i=0;i<RoleExchange.roleNum;i++){	// 初期化
			for(int j=0;j<simulation.getExchangeNum();j++){
				for(int k=0;k<simulation.getAgentNum()+1;k++){
					distanceHistgram[i][j][k] = new ArrayList<Double>();
				}
			}
		}
		// ファイルから読みだした行全てに対する処理
		int roleCounter = 0;
		int index=0;
		String[] distanceStrArray = distanceFileStr.get(distanceFileStr.size()-1);	// 距離ファイルの最後の結果を取得
		for(int i=0;i<step.size();i++){	// 行全てに対するループ
			String[] stepStrArray = step.get(i);	// ステップファイルの行を取得

			if(stepStrArray.length<=1){	// ロールインデックス表示行だったら飛ばす
				if(i!=0){
					roleCounter++;
					index=0;
				}
			}else{
				for(int j=0;j<stepStrArray.length;j++){	// 列に対する処理
					double value = Double.valueOf(stepStrArray[j]);	// 対象のエージェントのロールコストを取得
					int valueIndex = (int) (value / 10);	// 対応するインデックスを取得
					counter[roleCounter][index][valueIndex]++;

					double distance = Double.valueOf(distanceStrArray[j]);	// 対応するエージェントの距離を取得

					distanceHistgram[roleCounter][index][valueIndex].add(distance);	// 対応するインデックスに距離を追加
				}
				index++;
			}
		}

		// 表示
		String printStr = new String();
		for (int roleIndex = 0; roleIndex < RoleExchange.roleNum; roleIndex++) {
			printStr += "["+roleIndex+"]\n";
			for (int i = 0; i < simulation.getExchangeNum(); i++) {
				for (int j = 0; j < simulation.getAgentNum(); j++) {
					if (j != 0) {
						printStr += ",";
					}
					printStr += counter[roleIndex][i][j];
				}
				printStr += "\n";
			}
		}
		printStr += "\n";

		// 平均値
		double[][] total = new double[simulation.getExchangeNum()][simulation.getAgentNum()+1];
		for(int stepNum=0;stepNum<simulation.getExchangeNum();stepNum++){
			for(int agentNum=0;agentNum<simulation.getAgentNum();agentNum++){
				for(int roleIndex=0;roleIndex<RoleExchange.roleNum;roleIndex++){
					total[stepNum][agentNum] += counter[roleIndex][stepNum][agentNum];
				}
			}
		}
		// 表示
		for(int i=0;i<simulation.getExchangeNum();i++){
			printStr += "\n";
			for(int j=0;j<simulation.getAgentNum();j++){
				if(j != 0){
					printStr += ",";
				}
				printStr += total[i][j]/RoleExchange.roleNum;
			}
		}

		printStr += "\n\n";

		// 距離情報の表示
		String[] lastDistance = distanceFileStr.get(distanceFileStr.size()-1);
		for(int i=0;i<lastDistance.length;i++){
			if(i != 0){
				printStr += ",";
			}
			printStr += lastDistance[i];
		}

		printStr += "\n\n";

		// 距離情報ヒストグラムの表示
		for(int i=0;i<distanceHistgram.length;i++){
			printStr += "role["+i+"]\n";
			for(int j=0;j<distanceHistgram[i].length;j++){
				printStr += "step:"+j+"\n";
				for(int k=0;k<distanceHistgram[i][j].length;k++){
					printStr += "roleNum:"+k;
					for(Double distance:distanceHistgram[i][j][k]){
						printStr += ","+distance;
					}
					printStr += "\n";
				}
				printStr += "\n";
			}
			printStr += "\n";
		}

		// コンソール表示
		//System.out.print(printStr);
		// ファイルに書き出し
		CSVReaderWriter.write(stepFile, printStr);
	}

	/**
	 * 1次元座標エージェント用分析メソッド
	 * @param exchangeNum	交換回数
	 * @param agentNum		エージェントの個数
	 * @param simNum		シミュレーション回数
	 * @param stepFile	ステップ情報を保存したファイル
	 */
	public static void OneDSpotSimAnalysis2(int exchangeNum,int agentNum,int simNum,String stepFile){
		// ファイル読み込み
		ArrayList<String[]> step = new ArrayList<String[]>();	// ステップファイル読み込み変数
		step = CSVReaderWriter.read(stepFile);	// 読み込み

		// ファイル処理
		int[][][] counter = new int[RoleExchange.roleNum][exchangeNum][agentNum+1];

		// ファイルから読みだした行全てに対する処理
		int roleCounter = 0;
		int index=0;
		int stepCount=0;
		for(int i=0;i<step.size();i++){	// 行全てに対するループ
			String[] stepStrArray = step.get(i);	// ステップファイルの行を取得
			if(stepStrArray[0].length()<=0){

			}else if(stepStrArray[0].indexOf("step")>=0){	// step行の場合
				stepCount++;
				roleCounter=0;
				index=0;
			}else if(stepStrArray[0].indexOf("[")>=0){	// ロールインデックス表示行だったら飛ばす
				char charNum = stepStrArray[0].charAt(1);
				roleCounter=Integer.valueOf(""+charNum);
				index=0;
			}else{
				if(stepStrArray.length>0){
					for(int j=0;j<stepStrArray.length;j++){	// 列に対する処理
						String str = stepStrArray[j];
						double value = Double.valueOf(str);	// 対象のエージェントのロールコストを取得
						int valueIndex = (int) (value / 10);	// 対応するインデックスを取得
						counter[roleCounter][index][valueIndex]++;
					}
					index++;
				}
			}
		}

		// 表示
		String printStr = new String();
		for (int roleIndex = 0; roleIndex < RoleExchange.roleNum; roleIndex++) {
			printStr += "["+roleIndex+"]\n";
			for (int i = 0; i < exchangeNum; i++) {
				for (int j = 0; j < agentNum; j++) {
					if (j != 0) {
						printStr += ",";
					}
					double value = counter[roleIndex][i][j]/(double)simNum;
					printStr += value;
				}
				printStr += "\n";
			}
		}
		printStr += "\n";

		double[][] stepAve = new double[exchangeNum][agentNum+1];
		for(int roleIndex=0;roleIndex<RoleExchange.roleNum;roleIndex++){
			for(int exchangeIdx=0;exchangeIdx<exchangeNum;exchangeIdx++){
				for(int agentIdx=0;agentIdx<agentNum;agentIdx++){
					stepAve[exchangeIdx][agentIdx] += counter[roleIndex][exchangeIdx][agentIdx]/(double)simNum;
				}
			}
		}
		for(int exchangeIdx=0;exchangeIdx<exchangeNum;exchangeIdx++){
			for(int agentIdx=0;agentIdx<agentNum;agentIdx++){
				stepAve[exchangeIdx][agentIdx] /= RoleExchange.roleNum;
				if (agentIdx != 0) {
					printStr += ",";
				}
				printStr += stepAve[exchangeIdx][agentIdx];
			}
			printStr+="\n";
		}
		printStr+="\n";


		// コンソール表示
		System.out.print(printStr);
		// ファイルに書き出し
		CSVReaderWriter.write("Analysis"+stepFile, printStr);
	}

	/**
	 * 分析するメソッド
	 */
	public static void miniSim3_analysis(String filename) {
		// ファイル読み込み
		ArrayList<String[]> step = new ArrayList<String[]>();
		step = CSVReaderWriter.read(filename);
		// ファイル処理
		int[][][] counter = new int[RoleExchange.roleNum][MiniSimulation3.EXCHANGE_NUM][MiniSimulation3.AGENT_NUM+1];
		// ファイルから読みだした行全てに対する処理
		int roleCounter = 0;
		int index=0;
		for(int i=0;i<step.size();i++){
			String[] strArray = step.get(i);
			if(strArray.length<=1){
				if(i!=0){
					roleCounter++;
					index=0;
				}
			}else{
				for(int j=0;j<strArray.length;j++){
					int value = (int) (Double.valueOf(strArray[j]) / 10);
					counter[roleCounter][index][value]++;
				}
				index++;
			}
		}

		// 表示
		String printStr = new String();
		for (int roleIndex = 0; roleIndex < RoleExchange.roleNum; roleIndex++) {
			printStr += "["+roleIndex+"]\n";
			for (int i = 0; i < MiniSimulation3.EXCHANGE_NUM; i++) {
				for (int j = 0; j < MiniSimulation3.AGENT_NUM; j++) {
					if (j != 0) {
						printStr += ",";
					}
					printStr += counter[roleIndex][i][j];
				}
				printStr += "\n";
			}
		}
		printStr += "\n";

		// 平均値
		double[][] total = new double[MiniSimulation3.EXCHANGE_NUM][MiniSimulation3.AGENT_NUM+1];
		for(int stepNum=0;stepNum<MiniSimulation3.EXCHANGE_NUM;stepNum++){
			for(int agentNum=0;agentNum<MiniSimulation3.AGENT_NUM;agentNum++){
				for(int roleIndex=0;roleIndex<RoleExchange.roleNum;roleIndex++){
					total[stepNum][agentNum] += counter[roleIndex][stepNum][agentNum];
				}
			}
		}
		// 表示
		for(int i=0;i<MiniSimulation3.EXCHANGE_NUM;i++){
			printStr += "\n";
			for(int j=0;j<MiniSimulation3.AGENT_NUM;j++){
				if(j != 0){
					printStr += ",";
				}
				printStr += total[i][j]/RoleExchange.roleNum;
			}
		}
		System.out.print(printStr);
		// ファイルに書き出し
		CSVReaderWriter.write(filename, printStr);
	}

	public static void main(String[] args) {
		//miniSim3_analysis("stepAnalysis.csv");
		OneDSpotSimAnalysis2(20,20,100,"oneDStep.csv");
	}
}
