import java.io.*;
import java.util.*;

class RelatedRule //評価パケット数と従属関係を求めるのに共通するルールの値を格納
{
    public static int rSize;
    
    public static String[][] rField = new String[rSize][];


    public static void setRule(List<String> rule,int fieldnum){

	rField = new String[rSize][fieldnum];
	for(int i=0; i < rSize; i++){
	    //  System.out.println(	    
	    rField[i] = (rule.get(i)).split("\\s+|\\t+");
	    rField[i][1] = rField[i][1].substring(1);

	    String[] num1 = rField[i][1].split("\\.|/") ;
	    int plefix1 = (Integer.parseInt(num1[4]));
	    long ten1 = Integer.parseInt(num1[0])*(long)Math.pow(2,24) + Integer.parseInt(num1[1])*(long)Math.pow(2,16) + Integer.parseInt(num1[2])*(long)Math.pow(2,8) + Integer.parseInt(num1[3]);
	    //例）198.45.44.44/32 = 198*2^24 + 45*2^16 + 44*2^8 + 44  

	    long low,high;

	    low = (long)Math.pow(2,32-plefix1) * (long)(ten1/Math.pow(2,32-plefix1));
	    high = low + (long)Math.pow(2,32-plefix1) - 1;

	    RelatedRule.rField[i][1] = low + "-" + high;

	    if( rField[i].length <= 2 )
	    	continue;
	    
	    String[] num2 = rField[i][2].split("\\.|/") ;
	    int plefix2 = (Integer.parseInt(num2[4]));
	    long ten2 = Integer.parseInt(num2[0])*(long)Math.pow(2,24) + Integer.parseInt(num2[1])*(long)Math.pow(2,16) + Integer.parseInt(num2[2])*(long)Math.pow(2,8) + Integer.parseInt(num2[3]);
	    
	    low = (long)Math.pow(2,32-plefix2) * (long)(ten2/(long)Math.pow(2,32-plefix2));
	    high = low + (long)Math.pow(2,32-plefix2) - 1;
		
	    RelatedRule.rField[i][2] = low + "-" + high;
	    
	}		
    }
}

public class ClassBenchToAdjacencyList {//ClassBench形式のルールリストを評価パケット数と従属関係のルールリスト(竹山法やヒキン法で使う)に変換する
    public static void main(String[] args) {
	if(args.length != 4){
	    System.out.println("Arguments Error!\nUsage: $ java ClassBenchToAdjacencyList <rulelist> <headerlist> <fieldnumber> <outputfile>");
	    System.exit(1);
	}
	try {
	    int fieldnum;
	    List<String> rule = new ArrayList<String>();
	    List<String> header = new ArrayList<String>();
		   
	    File rInput = new File(args[0]);
	    BufferedReader br1 = new BufferedReader(new FileReader(rInput));//入力ファイル
	    File hInput = new File(args[1]);
	    BufferedReader br2 = new BufferedReader(new FileReader(hInput));//入力ファイル
	    File output = new File(args[3]);
	    BufferedWriter bw = new BufferedWriter(new FileWriter(output));//出力ファイル
	    String str;
		    
	    while((str = br1.readLine()) != null)
		rule.add(str);
		    	  
	    while((str = br2.readLine()) != null)
		header.add(str);

	    RelatedRule.rSize = rule.size();
	    if(Integer.parseInt(args[2]) <= 2)
		fieldnum = Integer.parseInt(args[2]) + 1;
	    else if(Integer.parseInt(args[2]) == 3)
		fieldnum = Integer.parseInt(args[2]) + 3;
	    else
		fieldnum = Integer.parseInt(args[2]) + 5;
	    RelatedRule.setRule(rule,fieldnum);
	  
	    int[] eval = makeEvaluation(header);
	    List<String>[] dep = makeDependence();
	    
	    for(int i = 0; i < dep.length; i++){   //結果の表示
	    	bw.write("R" + (i+1) + " : ");
	    	bw.write(String.valueOf(eval[i]));
	    	if(0 != dep[i].size()){
	    	    bw.write(" ");
	    	    for(int j = dep[i].size()-1;0 <= j; j--){
			bw.write(dep[i].get(j));
	    		if(j == 0){
			    bw.newLine();
	    		    break;
	    		}
			bw.write(",");
	    	    }
	    	}
	    	else{
		    bw.newLine();
	    	}
	    }

	    
	    
	    br1.close();
	    br2.close();
	    bw.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public static List<String>[] makeDependence(){ //従属関係を生成

	int ruleFieldSize = RelatedRule.rField[0].length;	
	ArrayList<String>[] dep = new ListString[RelatedRule.rSize]; //従属関係を格納する配列
	for(int i = 0; i < dep.length; i++)
	    dep[i] = new ListString();

	switch(ruleFieldSize){

	case 2://評価型 送信元アドレス
	    for(int i=RelatedRule.rSize-1; 0<=i; i--){
		for(int j=i-1; 0<=j; j--){
		
		    if(RelatedRule.rField[i][0].equals(RelatedRule.rField[j][0]))
			continue;

		    if( includeSA(RelatedRule.rField[i][1],RelatedRule.rField[j][1]) ){
		    
			// System.out.println((i+1) +" "+ (j+1));
		    
			dep[i].add(String.valueOf(j+1));
			//   System.out.println(j+1);
		    }
		}
	    }
	    break;
		
	case 3://評価型 送信元アドレス　送信先アドレス
	    for(int i=RelatedRule.rSize-1; 0<=i; i--){
		for(int j=i-1; 0<=j; j--){
		
		    if(RelatedRule.rField[i][0].equals(RelatedRule.rField[j][0]))
			continue;

		    if( includeSA(RelatedRule.rField[i][1],RelatedRule.rField[j][1]) && includeDA(RelatedRule.rField[i][2],RelatedRule.rField[j][2]) ){
		    
			// System.out.println((i+1) +" "+ (j+1));
		    
			dep[i].add(String.valueOf(j+1));
			//   System.out.println(j+1);
		    }
		}
	    }
	    break;
		
	case 6://評価型 送信元アドレス　送信先アドレス　送信元ポート
	    for(int i=RelatedRule.rSize-1; 0<=i; i--){
		for(int j=i-1; 0<=j; j--){
		
		    if(RelatedRule.rField[i][0].equals(RelatedRule.rField[j][0]))
			continue;

		    if(includeSP(RelatedRule.rField[i][3],RelatedRule.rField[i][5],RelatedRule.rField[j][3],RelatedRule.rField[j][5]) && includeSA(RelatedRule.rField[i][1],RelatedRule.rField[j][1]) && includeDA(RelatedRule.rField[i][2],RelatedRule.rField[j][2]) ){
		    
			// System.out.println((i+1) +" "+ (j+1));
		    
			dep[i].add(String.valueOf(j+1));
			//   System.out.println(j+1);
		    }
		}
	    }
	    break;
		
	case 9://評価型 送信元アドレス　送信先アドレス　送信元ポート　送信先ポート
	    for(int i=RelatedRule.rSize-1; 0<=i; i--){
		for(int j=i-1; 0<=j; j--){
		
		    if(RelatedRule.rField[i][0].equals(RelatedRule.rField[j][0]))
			continue;

		    if(includeSP(RelatedRule.rField[i][3],RelatedRule.rField[i][5],RelatedRule.rField[j][3],RelatedRule.rField[j][5]) && includeDP(RelatedRule.rField[i][6],RelatedRule.rField[i][8],RelatedRule.rField[j][6],RelatedRule.rField[j][8]) && includeSA(RelatedRule.rField[i][1],RelatedRule.rField[j][1]) && includeDA(RelatedRule.rField[i][2],RelatedRule.rField[j][2]) ){
		    
			// System.out.println((i+1) +" "+ (j+1));
		    
			dep[i].add(String.valueOf(j+1));
			//   System.out.println(j+1);
		    }
		}
	    }
	    break;
		
	case 10://評価型 送信元アドレス　送信先アドレス　送信元ポート　送信先ポート　プロトコル
	    for(int i=RelatedRule.rSize-1; 0<=i; i--){
		for(int j=i-1; 0<=j; j--){
		
		    if(RelatedRule.rField[i][0].equals(RelatedRule.rField[j][0]))
			continue;

		    if(includeSP(RelatedRule.rField[i][3],RelatedRule.rField[i][5],RelatedRule.rField[j][3],RelatedRule.rField[j][5]) && includeDP(RelatedRule.rField[i][6],RelatedRule.rField[i][8],RelatedRule.rField[j][6],RelatedRule.rField[j][8]) && includePROT(RelatedRule.rField[i][9],RelatedRule.rField[j][9]) && includeSA(RelatedRule.rField[i][1],RelatedRule.rField[j][1]) && includeDA(RelatedRule.rField[i][2],RelatedRule.rField[j][2]) ){
		    
			// System.out.println((i+1) +" "+ (j+1));
		    
			dep[i].add(String.valueOf(j+1));
			//   System.out.println(j+1);
		    }
		}
	    }
	    break;
		
	case 11://評価型 送信元アドレス　送信先アドレス　送信元ポート　送信先ポート　プロトコル　フラグ
	    for(int i=RelatedRule.rSize-1; 0<=i; i--){
		for(int j=i-1; 0<=j; j--){
		
		    if(RelatedRule.rField[i][0].equals(RelatedRule.rField[j][0]))
			continue;

		    if(includeSP(RelatedRule.rField[i][3],RelatedRule.rField[i][5],RelatedRule.rField[j][3],RelatedRule.rField[j][5]) && includeDP(RelatedRule.rField[i][6],RelatedRule.rField[i][8],RelatedRule.rField[j][6],RelatedRule.rField[j][8]) && includePROT(RelatedRule.rField[i][9],RelatedRule.rField[j][9]) && includeFLAG(RelatedRule.rField[i][10],RelatedRule.rField[j][10]) && includeSA(RelatedRule.rField[i][1],RelatedRule.rField[j][1]) && includeDA(RelatedRule.rField[i][2],RelatedRule.rField[j][2])){
		    
			// System.out.println((i+1) +" "+ (j+1));
		    
			dep[i].add(String.valueOf(j+1));
			//   System.out.println(j+1);
		    }
		}
	    }

	}

	return dep;	       
    }
    
    public static boolean includeSA(String rule1,String rule2){

	String[] str1 = rule1.split("-");
	String[] str2 = rule2.split("-");
	
	if( Long.parseLong(str1[1]) < Long.parseLong(str2[0]) )
	    return false;
	else if( Long.parseLong(str2[1]) <  Long.parseLong(str1[0]) )
	    return false;
	return true;
	
    
    }
    public static boolean includeDA(String rule1,String rule2){

	String[] str1 = rule1.split("-");
	String[] str2 = rule2.split("-");
	
	if( Long.parseLong(str1[1]) <  Long.parseLong(str2[0]) )
	    return false;
	else if( Long.parseLong(str2[1]) <  Long.parseLong(str1[0]) )
	    return false;
	return true;
	
	
    }
    public static boolean includeSP(String sr1_1,String sr1_2,String sr2_1,String sr2_2){
	int rule1_1 = Integer.parseInt(sr1_1);
	int rule1_2 = Integer.parseInt(sr1_2);
	int rule2_1 = Integer.parseInt(sr2_1);
	int rule2_2 = Integer.parseInt(sr2_2);
	if(rule1_2 < rule2_1)
	    return false;
	else if(rule2_2 < rule1_1)
	    return false;
	return true;
    }
    public static boolean includeDP(String sr1_1,String sr1_2,String sr2_1,String sr2_2){
	int rule1_1 = Integer.parseInt(sr1_1);
	int rule1_2 = Integer.parseInt(sr1_2);
	int rule2_1 = Integer.parseInt(sr2_1);
	int rule2_2 = Integer.parseInt(sr2_2);
	if(rule1_2 < rule2_1)
	    return false;
	else if(rule2_2 < rule1_1)
	    return false;
	return true;
    }
    public static boolean includePROT(String rule1,String rule2){
	String[] pm1 = rule1.split("/");
	String[] pm2 = rule2.split("/");
	if(pm1[1].equals("0x00") || pm2[1].equals("0x00"))
	    return true;
	else if(pm1[0].equals(pm2[0]))
	    return true;
	return false;
	    
    }
    public static boolean includeFLAG(String rule1,String rule2){

	if(rule1.endsWith("0x0000") || rule2.endsWith("0x0000"))
	    return true;
	else if(rule1.equals(rule2))
	    return true;	
	else if(rule1.endsWith("0xffff") || rule2.endsWith("0xffff"))
	    return false;
	else if( (rule1.equals("0x0200/0x1200") && rule2.equals("0x0000/0x0200")) || (rule2.equals("0x0200/0x1200") && rule1.equals("0x0000/0x0200")) )
	    return false;
	else if( (rule1.equals("0x0200/0x1200") && rule2.equals("0x1000/0x1000")) || (rule2.equals("0x0200/0x1200") && rule1.equals("0x1000/0x1000")) )
	    return false;		
	else if( (rule1.substring(7)).equals(rule2.substring(7)) )
	    return false;
	else
	    return true;
    }
    
    public static int[] makeEvaluation(List<String> header){
	
	int hSize = header.size();	
	int[] eval = new int[RelatedRule.rSize];//評価パケット数を格納する配列
	String[] hField = header.get(0).split("\\s+|\\t+");
	//	System.out.println(hField.length);
	switch(hField.length){
	    
	case 1://送信元アドレス
	    for(int j=0; j < hSize; j++){
		hField = header.get(j).split("\\s+|\\t+");
		for(int i=0; i < RelatedRule.rSize; i++){
		    if( isMatchSA(RelatedRule.rField[i][1],hField[0]) ){    
			eval[i]++;
			break;
		    }
		}
	    }
	    break;
	    
	case 2://送信元アドレス　送信先アドレス
	    for(int j=0; j < hSize; j++){
		hField = header.get(j).split("\\s+|\\t+");
		for(int i=0; i < RelatedRule.rSize; i++){		
		    if(isMatchSA(RelatedRule.rField[i][1],hField[0]) && isMatchDA(RelatedRule.rField[i][2],hField[1]) ){    
			eval[i]++;
			break;
		    }
		}
	    }
	    break;
	    
	case 3://送信元アドレス　送信先アドレス　送信元ポート
	    for(int j=0; j < hSize; j++){
		hField = header.get(j).split("\\s+|\\t+");	    
		for(int i=0; i < RelatedRule.rSize; i++){
		    if(isMatchSP(RelatedRule.rField[i][3],RelatedRule.rField[i][5],hField[2]) && isMatchSA(RelatedRule.rField[i][1],hField[0]) && isMatchDA(RelatedRule.rField[i][2],hField[1])){    
			eval[i]++;
			break;
		    }
		}
	    }
	    break;
	    
	case 4://送信元アドレス　送信先アドレス　送信元ポート　送信先ポート
	    for(int j=0; j < hSize; j++){
		hField = header.get(j).split("\\s+|\\t+");
		for(int i=0; i < RelatedRule.rSize; i++){		
		    if(isMatchSP(RelatedRule.rField[i][3],RelatedRule.rField[i][5],hField[2]) && isMatchDP(RelatedRule.rField[i][6],RelatedRule.rField[i][8],hField[3]) && isMatchSA(RelatedRule.rField[i][1],hField[0]) && isMatchDA(RelatedRule.rField[i][2],hField[1])){    
			eval[i]++;
			break;
		    }
		}
	    }
	    break;
	    
	case 5://送信元アドレス　送信先アドレス　送信元ポート　送信先ポート　プロトコル
	    for(int j=0; j < hSize; j++){
		hField = header.get(j).split("\\s+|\\t+");	    
		for(int i=0; i < RelatedRule.rSize; i++){		
		    if(isMatchSP(RelatedRule.rField[i][3],RelatedRule.rField[i][5],hField[2]) && isMatchDP(RelatedRule.rField[i][6],RelatedRule.rField[i][8],hField[3]) && isMatchPROT(RelatedRule.rField[i][9],hField[4]) && isMatchSA(RelatedRule.rField[i][1],hField[0]) && isMatchDA(RelatedRule.rField[i][2],hField[1])){    
			eval[i]++;
			break;
		    }
		}
	    }
	    break;
	    
	case 6://送信元アドレス　送信先アドレス　送信元ポート　送信先ポート　プロトコル　フラグ　
	    for(int j=0; j < hSize; j++){
		hField = header.get(j).split("\\s+|\\t+");
		for(int i=0; i < RelatedRule.rSize; i++){
		    if(isMatchSP(RelatedRule.rField[i][3],RelatedRule.rField[i][5],hField[2]) && isMatchDP(RelatedRule.rField[i][6],RelatedRule.rField[i][8],hField[3]) && isMatchPROT(RelatedRule.rField[i][9],hField[4]) && isMatchFLAG(RelatedRule.rField[i][10],hField[5]) && isMatchSA(RelatedRule.rField[i][1],hField[0]) && isMatchDA(RelatedRule.rField[i][2],hField[1])){
	        
			eval[i]++;
			break;
		    }
		}
	    }
	    break;	    
	}
	
	return eval;
    }
    
    public static boolean isMatchSA(String rule,String header){

	String[] str = rule.split("-");
	//System.out.println(rule);
	//System.out.println(low + " : " + high +" "+ header);
	if(Long.parseLong(str[0]) <= Long.parseLong(header) && Long.parseLong(header) <= Long.parseLong(str[1]) )
	    return true;
	else
	    return false;

    }
	
    public static boolean isMatchDA(String rule,String header){
	
	String[] str = rule.split("-");
	//System.out.println(low + " : " + high +" "+ header);
	if(Long.parseLong(str[0]) <= Long.parseLong(header) && Long.parseLong(header) <= Long.parseLong(str[1]) )
	    return true;
	else
	    return false;

    }
	
    public static boolean isMatchSP(String low,String high,String header){

	//System.out.println(low + " : " + high +" "+ header);
	if(Integer.parseInt(low)<=Integer.parseInt(header) && Integer.parseInt(header) <= Integer.parseInt(high))
	    return true;
	else
	    return false;
	    
    }
	
    public static boolean isMatchDP(String low,String high,String header){
	    
	//System.out.println(low + " : " + high +" "+ header);
	if(Integer.parseInt(low)<=Integer.parseInt(header) && Integer.parseInt(header) <= Integer.parseInt(high))
	    return true;
	else
	    return false;

    }
	
    public static boolean isMatchPROT(String rule,String header){//プロトコルが合致しているかの比較
	String[] pm = rule.split("/");

	//System.out.println(rule +" "+ header);
	    
	if(pm[1].equals("0x00"))
	    return true;

	if(pm[1].equals("0xFF") && Integer.decode(pm[0]) == Integer.parseInt(header))
	    return true;

	return false;
    }
	
    public static boolean isMatchFLAG(String rule,String header){//フラグが合致しているかの比較

	if(rule.endsWith("0x0000"))
	    return true;
	else {
	    if( (header.equals("0") && rule.equals("0x0000/0x0200") ) || ( header.equals("4294967295") && rule.equals("0x1000/0x1000") ) ) //acl_seed
		return true;
	    else if( (header.equals("0") && rule.equals("0x0000/0xff00") ) || (header.equals("4294967295") && ( rule.equals("0x0400/0x0400") || rule.equals("0x0100/0x0100") ) ) || (header.equals("33559040") && rule.equals("0x0400/0x0400") ) || (header.equals("67109888") && rule.equals("0x0200/0x1200") ) )//fw_seed
		return true;
	    else if( (header.equals("4294967295") && rule.equals("0x0200/0x0200") ) )//ipc_seed
		return true;
	    else{
		StringBuilder sb = new StringBuilder(rule);	       
		sb.delete(6,9);
		if( Long.parseLong(header) == Long.decode( sb.toString() ) )
		    return true;
	    }
	}
	return false;
    }

}

