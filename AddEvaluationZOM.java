import java.io.*;
import java.util.*;

public class AddEvaluationZOM {//0,1,*のルールに評価パケット数を付与する
    public static void main(String[] args){
	if(args.length != 3){
	    System.out.println("Arguments Error!\nUsage: $ java AddEvaluationZOM  <rulelist> <headerlist> <outputfile>");
	    System.exit(1);
	}	
	try{
	    List<String> rule = new ArrayList<String>();
	    List<String> header = new ArrayList<String>();
	    
	    File rInput = new File(args[0]);
	    BufferedReader br1 = new BufferedReader(new FileReader(rInput));//入力ファイル
	    File hInput = new File(args[1]);
	    BufferedReader br2 = new BufferedReader(new FileReader(hInput));//入力ファイル
	    File output = new File(args[2]);
	    BufferedWriter bw = new BufferedWriter(new FileWriter(output));//出力ファイル
	    String str;

	    while((str = br1.readLine()) != null)
		rule.add(str);
		    	  
	    while((str = br2.readLine()) != null)
		header.add(str);
	    
	    int rSize = rule.size();
	    int hSize = header.size();
	    int[] eval = new int[rSize];

	    if( rule.get(0).contains(" ") || rule.get(0).contains("\t") )
		eval = makeFieldEvaluation(rule,header);
	    else {
		for(int j=0; j < hSize; j++){
		    for(int i=0; i < rSize; i++){
			if( isMatch(rule.get(i),header.get(j),rule.get(0).length()) ){    
			    eval[i]++;
			    break;
			}
		    }
		}
			       		
	    }


	    
	    for(int i = 0; i < rSize; i++){   //結果の表示
		bw.write(rule.get(i));		
		bw.write(" " + String.valueOf(eval[i]));
		bw.newLine();
	    }
	    
	    
	    br1.close();
	    br2.close();
	    bw.close();
	}catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
    }



    public static int[] makeFieldEvaluation(List<String> fieldRule,List<String> fieldHeader){
	
	int rSize = fieldRule.size();	
	int hSize = fieldHeader.size();
	int[] eval = new int[rSize];//評価パケット数を格納する配列
	
	List<String> rule = new ArrayList<String>();
	List<String> header = new ArrayList<String>();

	int strSize = fieldRule.get(0).length();	
	
	for(int i = 0; i < rSize; i++){	    	    
	    StringBuilder sb = new StringBuilder();
	    for(int j = 0; j < strSize; j++){
		if(fieldRule.get(i).charAt(j) == ' ' || fieldRule.get(i).charAt(j) == '\t')
		    continue;
		sb.append( fieldRule.get(i).charAt(j) );
	    }
	    rule.add(sb.toString());
	}
	
	for(int i = 0; i < hSize; i++){	    	    
	    StringBuilder sb = new StringBuilder();       
	    for(int j = 0; j < strSize; j++){
		if(fieldHeader.get(i).charAt(j) == ' ' || fieldHeader.get(i).charAt(j) == '\t')
		    continue;
		sb.append( fieldHeader.get(i).charAt(j) );
	    }
	    header.add(sb.toString());
	}
	
	for(int j=0; j < hSize; j++){
	    for(int i=0; i < rSize; i++){
		if( isMatch(rule.get(i),header.get(j),rule.get(0).length()) ){    
		    eval[i]++;
		    break;
		}
	    }
	}
	
	return eval;
    }

    
    public static boolean isMatch(String rule,String header,int size){
	    
	for(int i=0; i < size; i++){
	    if( rule.charAt(i) != '*' && rule.charAt(i) != header.charAt(i) )
		return false;
	}
	return true;
    }
    
}
