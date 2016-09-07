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
	    String ZOM1 = (ClassBenchToAdjacencyList.tenTotwo(Long.decode(num1[0]),8)) + (ClassBenchToAdjacencyList.tenTotwo(Long.decode(num1[1]),8)) + (ClassBenchToAdjacencyList.tenTotwo(Long.decode(num1[2]),8)) + (ClassBenchToAdjacencyList.tenTotwo(Long.decode(num1[3]),8));

	    StringBuilder sb1 = new StringBuilder(ZOM1);
	    for(int j = plefix1; j < 32; j++)
		sb1.setCharAt(j,'*');

	    RelatedRule.rField[i][1] = sb1.toString();

	    if( rField[i].length <= 2 )
		continue;
	    
	    String[] num2 = rField[i][2].split("\\.|/") ;
	    int plefix2 = (Integer.parseInt(num2[4]));
	    String ZOM2 = (ClassBenchToAdjacencyList.tenTotwo(Long.decode(num2[0]),8)) + (ClassBenchToAdjacencyList.tenTotwo(Long.decode(num2[1]),8)) + (ClassBenchToAdjacencyList.tenTotwo(Long.decode(num2[2]),8)) + (ClassBenchToAdjacencyList.tenTotwo(Long.decode(num2[3]),8));
		
	    StringBuilder sb2 = new StringBuilder(ZOM2);
	    for(int j = plefix2; j < 32; j++)
		sb2.setCharAt(j,'*');
	    
	    RelatedRule.rField[i][2] = sb2.toString();
	}
	
	
    }
}

public class ClassBenchToAdjacencyList {//ClassBench形式のルールリストを評価パケット数と従属関係のルールリスト(竹山法やヒキン法で使う)に変換する
    public static void main(String[] args) {
	if(args.length != 4){
	    System.out.println("Arguments Error!\nUsage: $ java ClassBenchToAdjacencyList <rulelist> <headerlist> <fieldrule> <outputfile>");
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
		//	System.out.print(eval[i] + "\t");
		bw.write(String.valueOf(eval[i]));
	    	if(0 != dep[i].size()){
		    bw.write(" ");
	    	    for(int j = dep[i].size()-1;0 <= j; j--){
			//	System.out.print(dep[i].get(j));
			bw.write(dep[i].get(j));
			if(j == 0){
			    //	System.out.println("");
			    bw.newLine();
			    break;
			}
			//   System.out.print(",");
			bw.write(",");
		    }
	    	}
	    	else{
		    // System.out.println("");
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
		
		    if(RelatedRule.rField[i][ruleFieldSize-1].equals(RelatedRule.rField[j][ruleFieldSize-1]))
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
	
	for(int i=0; i<32; i++){
	    if(rule1.charAt(i)=='*' || rule2.charAt(i)=='*')
		break;
	    else if(rule1.charAt(i)!=rule2.charAt(i))
		return false;
	}
	return true;	
    
    }
    public static boolean includeDA(String rule1,String rule2){
	
	for(int i=0; i<32; i++){
	    if(rule1.charAt(i)=='*' || rule2.charAt(i)=='*')
		break;	    
	    if(rule1.charAt(i)!=rule2.charAt(i))
		return false;
	}
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
	String[] fm1 = rule1.split("/");
	String[] fm2 = rule2.split("/");
	if(fm1[1].equals("0x0000") || fm2[1].equals("0x0000"))
	    return true;

	String FLAG1 = (tenTotwo(Long.decode(fm1[0]),16));
	String MASK1 = (tenTotwo(Long.decode(fm1[1]),16));
	String FLAG2 = (tenTotwo(Long.decode(fm2[0]),16));
	String MASK2 = (tenTotwo(Long.decode(fm2[1]),16));

	for(int i=0; i<16; i++){
	    if( MASK1.charAt(i)=='1' && MASK2.charAt(i)=='1' && FLAG1.charAt(i)!=FLAG2.charAt(i) )
		return false;
	}
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

	header = tenTotwo(Long.parseLong(header),32);
	    
	for(int i=0; i<32; i++){
	    if(rule.charAt(i)=='*')
		break;
	    else if(rule.charAt(i)!=header.charAt(i))
		return false;
	}
	return true;
    }
	
    public static boolean isMatchDA(String rule,String header){

	header = tenTotwo(Long.parseLong(header),32);
	    
	for(int i=0; i<32; i++){
	    if(rule.charAt(i)=='*')
		break;
	    else if(rule.charAt(i)!=header.charAt(i))
		return false;
	}
	return true;
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
	String[] fm = rule.split("/");

	// System.out.println(rule +" "+ header);

	if(fm[1].equals("0x0000"))
	    return true;
	else {
	    String FLAG = (tenTotwo(Long.decode(fm[0]),16));
	    String MASK = (tenTotwo(Long.decode(fm[1]),16));
	    String HEADER = (tenTotwo(Long.parseLong(header),32));//.subString(0,16);
	    for(int i = 0; i < 16; i++){
		if(MASK.charAt(i)=='1' && FLAG.charAt(i) != HEADER.charAt(i))
		    return false;
	    }
	    return true;
	}
	//   return false;    
    }

     
    public static String tenTotwo(long num, int numwidth){//10進表記を2進表記に変換する
	long[] two;
	int i;
	String returnBits = "";
	two = new long[65535];

	for(i=0;i<(numwidth-1);i++) {
	    two[i] = num%2;
	    num = num >> 1;
	}
	two[i] = num;
	while(i>=0){
	    if(two[i]==1)
		returnBits += '1';
	    else
		returnBits += '0';
	    i--;
	}
	return returnBits;
    }







    


}

