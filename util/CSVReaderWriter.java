package util;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * CSV形式ファイルをRead,Writeするクラス
 * @author kohta
 *
 */
public class CSVReaderWriter {
	/**
	 * ファイル出力
	 * @param writeFileName 出力先のファイル名
	 * @param strlistの内容をファイルに書き出す
	 */
	public static void write(String writeFileName,ArrayList<String> strlist){
		try{
			// 出力ファイルを開く
			BufferedWriter out = new BufferedWriter(
					new FileWriter(writeFileName));
			for(String str:strlist){
				// 書き出し
				out.write(str);
				// 改行
				out.newLine();
			}
			// 終了
			out.close();
		}catch(IOException e){
			JOptionPane.showMessageDialog(null, e);
		}
	}
	public static void write(String writeFileName,String str){
		ArrayList<String> strlist = new ArrayList<String>();
		strlist.add(str);
		write(writeFileName,strlist);
	}

	/**
	 * ファイル入力
	 * @param readFileName 入力先のファイル名
	 * @return カンマ区切りにされたString
	 */
	public static ArrayList<String[]> read(String readFileName){
		ArrayList<String[]> readStr = new ArrayList<String[]>();
		try{
			// 入力ファイルを開く
			BufferedReader in = new BufferedReader(
					new FileReader(readFileName));
			// 読み出されるString
			String input;
			// 読み出された行をカンマ区切りにしたもの
			String[] inputline;
			// ファイルの終端まで読み出す
			while((input = in.readLine()) != null){
				inputline = input.split(",");
				readStr.add(inputline);
			}
			// 終了
			in.close();
		}catch(IOException e){
			JOptionPane.showMessageDialog(null, readFileName+"がありません");
		}
		return readStr;
	}
}
