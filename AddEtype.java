import java.io.*;
import java.util.*;

public class AddEtype {//与えられたルールリストの各ルールの最後に評価型を付け加える
    public static void main(String[] args){
	if (args.length != 3){
	    System.out.println("Arguments Error!\nUsage: $ java AddEtype <rulelist> <outputfile> <probability>");   
	    System.exit(1);
	}
	try{
	    File input = new File(args[0]);
	    BufferedReader br = new BufferedReader(new FileReader(input));//入力ファイル
	    File output = new File(args[1]);
	    BufferedWriter bw = new BufferedWriter(new FileWriter(output));//出力ファイル
	    int threshold = (int)(Float.parseFloat(args[2])*100);//しきい値
	    String rule,eType;
	    while((rule = br.readLine()) != null){
	        if(((int)(Math.random() * 100) + 1) <= threshold)
		    eType = "Accept";
		else
		    eType = "Deny";
		bw.write(eType + "\t" + rule);
		bw.newLine();
	    }
	    br.close();
	    bw.close();
	}catch(FileNotFoundException e){
	    System.out.println(e);
	}catch(IOException e){
	    System.out.println(e);
	}
    }
}
