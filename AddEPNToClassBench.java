import java.io.*;
import java.util.*;

// class Node
class NodeAddEPNToClassBench
{
    String rule;    
    int eval;

    // Node(String r,int e){
    NodeAddEPNToClassBench(String r,int e){
	rule = r;
	eval = e;
    }
    
    @Override
    public String toString(){

	String str;
	str = rule + "\t" + String.valueOf(eval);
	return str;
    }
}

class AddERelatedRule //評価パケット数と従属関係を求めるのに共通するルールの値を格納
{
    public static int rSize;
    
    public static String[][] rField = new String[rSize][];

    public static void setSize(int size){
	rSize = size;
    }
    
    public static void setRule(List<String> rule,String[] args){
	int sp=0,dp=0;
	
	if(Arrays.asList(args).contains("SP"))
	    sp+=2;
	if(Arrays.asList(args).contains("DP"))
	    dp+=2;
	
	rField = new String[rSize][args.length + 1 - 3 + dp + sp];

	if( Arrays.asList(args).contains("SA") && Arrays.asList(args).contains("DA") ){
	    for(int i=0; i < rSize; i++){
		//  System.out.println(	    
		rField[i] = (rule.get(i)).split("\\s+|\\t+");
		rField[i][1] = rField[i][1].substring(1);

		String[] num1 = rField[i][1].split("\\.|/") ;
		//	System.out.println(num1[0] + "." + num1[1] + "." + num1[2] + "." + num1[3] + "/" + num1[4]);
		int plefix1 = (Integer.parseInt(num1[4]));
		long ten1 = Integer.parseInt(num1[0])*(long)Math.pow(2,24) + Integer.parseInt(num1[1])*(long)Math.pow(2,16) + Integer.parseInt(num1[2])*(long)Math.pow(2,8) + Integer.parseInt(num1[3]);
		//例）198.45.44.44/32 = 198*2^24 + 45*2^16 + 44*2^8 + 44  

		long low,high;

		low = (long)Math.pow(2,32-plefix1) * (long)(ten1/Math.pow(2,32-plefix1));
		high = low + (long)Math.pow(2,32-plefix1) - 1;

		AddERelatedRule.rField[i][1] = low + "-" + high;


		String[] num2 = rField[i][2].split("\\.|/") ;
		int plefix2 = (Integer.parseInt(num2[4]));
		long ten2 = Integer.parseInt(num2[0])*(long)Math.pow(2,24) + Integer.parseInt(num2[1])*(long)Math.pow(2,16) + Integer.parseInt(num2[2])*(long)Math.pow(2,8) + Integer.parseInt(num2[3]);
		
		low = (long)Math.pow(2,32-plefix2) * (long)(ten2/(long)Math.pow(2,32-plefix2));
		high = low + (long)Math.pow(2,32-plefix2) - 1;
		
		AddERelatedRule.rField[i][2] = low + "-" + high;
		

	    }	    
	}
	
	else if( Arrays.asList(args).contains("SA") ){
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

		AddERelatedRule.rField[i][1] = low + "-" + high;

	    }
	}

	else if( Arrays.asList(args).contains("DA") ){
	    
	    for(int i=0; i < rSize; i++){
		rField[i] = (rule.get(i)).split("\\s+|\\t+");
		//rField[i][1] = rField[i][1].substring(1);

		String[] num2 = rField[i][1].split("\\.|/") ;
		int plefix2 = (Integer.parseInt(num2[4]));
		long ten2 = Integer.parseInt(num2[0])*(long)Math.pow(2,24) + Integer.parseInt(num2[1])*(long)Math.pow(2,16) + Integer.parseInt(num2[2])*(long)Math.pow(2,8) + Integer.parseInt(num2[3]);

		long low,high;
		
		low = (long)Math.pow(2,32-plefix2) * (long)(ten2/(long)Math.pow(2,32-plefix2));
		high = low + (long)Math.pow(2,32-plefix2) - 1;
		
		AddERelatedRule.rField[i][1] = low + "-" + high;
	    
	    }
	}
	else{
	    for(int i=0; i < rSize; i++)		
		rField[i] = (rule.get(i)).split("\\s+|\\t+");
	    
	}
	    
    }	
}

public class AddEPNToClassBench {//ClassBenchに評価パケット数と従属関係のルールリスト(竹山法やヒキン法で使う)に変換する
    public static void main(String[] args) {
	if(args.length < 4 || args.length > 9){
	    System.out.println("Arguments Error!\nUsage: $ java ClassBenchToAdjacencyList <rulelist> <headerlist> <outputfile> [fields] ");
	    System.exit(1);
	}
	for(int i = 3; i < args.length; i++){
	    if( !(args[i].equals("SA") ||  args[i].equals("DA") ||  args[i].equals("SP") ||  args[i].equals("DP") ||  args[i].equals("PROT") ||  args[i].equals("FLAG") ) ){
		System.out.println("Arguments Error! : " + args[i]  + "\nSelect field arguments in SA,DA,SP,DP,PROT and FLAG");
		System.exit(1);
	    }
	}
	
	try {
	    int fieldnum;
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

	    AddERelatedRule.setSize( rule.size() );
	    AddERelatedRule.setRule(rule,args);
	    
	    int[] eval = makeEvaluation(header,args);

	    // ArrayList<Node> AList = new ArrayList<Node>();
	    ArrayList<NodeAddEPNToClassBench> AList = new ArrayList<NodeAddEPNToClassBench>();

	    for(int i = 0; i < eval.length; i++){
		// Node node = new Node(rule.get(i),eval[i]);
		NodeAddEPNToClassBench node = new NodeAddEPNToClassBench(rule.get(i),eval[i]);
		AList.add(node);
	    }

	    // for(Node n : AList){ //結果の表示
	    for(NodeAddEPNToClassBench n : AList){ //結果の表示
	    	bw.write( n.toString() );
	    	bw.newLine();
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
    
    public static int[] makeEvaluation(List<String> header,String[] args){//　評価パケット数を生成

	int SPadjust = Arrays.asList(args).contains("SP") ? 2 : 0;
	int DPadjust = Arrays.asList(args).contains("DP") ? 2 : 0;
	int hSize = header.size();	
	int[] eval = new int[AddERelatedRule.rSize];//評価パケット数を格納する配列
	String[] hField = header.get(0).split("\\s+|\\t+");
	HashMap<String,Integer> map = new HashMap<String,Integer>();
	//	System.out.println(hField.length);
	switch(args.length){
	    
	case 4://　評価型＋フィールド数１　のルール
	    for(int j=0; j < hSize; j++){
		//		hField = header.get(j).split("\\s+|\\t+");
		if(map.containsKey(header.get(j)) ){
		    eval[map.get(header.get(j))]++;
		    continue;
		}
		outside: for(int i=0; i < AddERelatedRule.rSize; i++){
		    switch(args[3]){

		    case "SA":
			if( isMatchSA(AddERelatedRule.rField[i][1],header.get(j)) ) {			   
			    eval[i]++;
			    map.put(header.get(j),i);
			    break outside;
			}
			break;
		    case "DA":
			if( isMatchDA(AddERelatedRule.rField[i][1],header.get(j)) )    {
			    eval[i]++;
			    map.put(header.get(j),i);
			    break outside;
			}
			break;
		    case "SP":
			if( isMatchSP(AddERelatedRule.rField[i][1],AddERelatedRule.rField[i][3],header.get(j)) ){
			    eval[i]++;
			    map.put(header.get(j),i);
			    break outside;
			}
			break;
		    case "DP":
			if( isMatchDP(AddERelatedRule.rField[i][1],AddERelatedRule.rField[i][3],header.get(j)) ){ 
			    eval[i]++;
			    map.put(header.get(j),i);
			    break outside;
			}
			break;
		    case "PROT":
			if( isMatchPROT(AddERelatedRule.rField[i][1],header.get(j)) ){
			    eval[i]++;
			    map.put(header.get(j),i);
			    break outside;
			}
			break;		    
		    }
		}
	    }
	    break;
	    
	case 5://　評価型＋フィールド数２　のルール
	    for(int j=0; j < hSize; j++){
		if(map.containsKey(header.get(j)) ){
		    eval[map.get(header.get(j))]++;
		    continue;
		}
		hField = header.get(j).split("\\s+|\\t+");
		outside: for(int i=0; i < AddERelatedRule.rSize; i++){
		    for(int k=4; k >= 3; k--){
			switch(args[k]){
			case "SA":
			    if( isMatchSA(AddERelatedRule.rField[i][1],hField[0]) ) {
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "DA":
			    if( isMatchDA(AddERelatedRule.rField[i][k-2],hField[k-3]) ){				
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "SP":
			    if( isMatchSP(AddERelatedRule.rField[i][k-2],AddERelatedRule.rField[i][k],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "DP":
			    if( isMatchDP(AddERelatedRule.rField[i][k-2 + SPadjust],AddERelatedRule.rField[i][k + SPadjust],hField[k-3]) ){
				if(k == 3){				    
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "PROT":
			    if( isMatchPROT(AddERelatedRule.rField[i][k-2 + SPadjust + DPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "FLAG":
			    //System.out.println( AddERelatedRule.rField[i][0] + " " +  AddERelatedRule.rField[i][1] + " " + AddERelatedRule.rField[i][2] + " " + AddERelatedRule.rField[i][k-2 + SPadjust + DPadjust] + " " + hField[k-3]);
			    if( isMatchFLAG(AddERelatedRule.rField[i][k-2 + SPadjust + DPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;		    		    
			}
		    }
		}
	    }
	    break;
	    
	case 6://　評価型＋フィールド数３　のルール
	    for(int j=0; j < hSize; j++){
		if(map.containsKey(header.get(j)) ){
		    eval[map.get(header.get(j))]++;
		    continue;
		}
		hField = header.get(j).split("\\s+|\\t+");	    
		outside: for(int i=0; i < AddERelatedRule.rSize; i++){
		    for(int k=5; k >= 3; k--){
			switch(args[k]){
			case "SA":
			    if( isMatchSA(AddERelatedRule.rField[i][1],hField[0]) ) {
				if(k == 3 ){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "DA":
			    if( isMatchDA(AddERelatedRule.rField[i][k-2],hField[k-3]) )    {
				if(k == 3 ){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "SP":
			    if( isMatchSP(AddERelatedRule.rField[i][k-2],AddERelatedRule.rField[i][k],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "DP":
			    if( isMatchDP(AddERelatedRule.rField[i][k-2 + SPadjust],AddERelatedRule.rField[i][k + SPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "PROT":
			    if( isMatchPROT(AddERelatedRule.rField[i][k-2 + SPadjust + DPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "FLAG":			    
			    if( isMatchFLAG(AddERelatedRule.rField[i][k-2 + SPadjust + DPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;		    		    
			}
		    }		    
		}
	    }
	    break;
	    
	case 7://　評価型＋フィールド数４　のルール
	    for(int j=0; j < hSize; j++){
		if(map.containsKey(header.get(j)) ){
		    eval[map.get(header.get(j))]++;
		    continue;
		}
		hField = header.get(j).split("\\s+|\\t+");
		outside: for(int i=0; i < AddERelatedRule.rSize; i++){		
		    for(int k=6; k >= 3; k--){
			switch(args[k]){
			case "SA":
			    if( isMatchSA(AddERelatedRule.rField[i][k-2],hField[k-3]) ) {
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "DA":
			    if( isMatchDA(AddERelatedRule.rField[i][k-2],hField[k-3]) )    {
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "SP":
			    if( isMatchSP(AddERelatedRule.rField[i][k-2],AddERelatedRule.rField[i][k],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "DP":
			    if( isMatchDP(AddERelatedRule.rField[i][k-2 + SPadjust],AddERelatedRule.rField[i][k + SPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "PROT":
			    if( isMatchPROT(AddERelatedRule.rField[i][k-2 + SPadjust + DPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "FLAG":			    
			    if( isMatchFLAG(AddERelatedRule.rField[i][k-2 + SPadjust + DPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;		    		    
			}
		    }
        
		}
	    }
	    break;
	    
	case 8://　評価型＋フィールド数５　のルール
	    for(int j=0; j < hSize; j++){
		if(map.containsKey(header.get(j)) ){
		    eval[map.get(header.get(j))]++;
		    continue;
		}
		hField = header.get(j).split("\\s+|\\t+");	    
		outside: for(int i=0; i < AddERelatedRule.rSize; i++){		
		    for(int k=7; k >= 3; k--){
			switch(args[k]){
			case "SA":
			    if( isMatchSA(AddERelatedRule.rField[i][k-2],hField[k-3]) ) {
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "DA":
			    if( isMatchDA(AddERelatedRule.rField[i][k-2],hField[k-3]) )    {
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "SP":
			    if( isMatchSP(AddERelatedRule.rField[i][k-2],AddERelatedRule.rField[i][k],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "DP":
			    if( isMatchDP(AddERelatedRule.rField[i][k-2 + SPadjust],AddERelatedRule.rField[i][k + SPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "PROT":
			    if( isMatchPROT(AddERelatedRule.rField[i][k-2 + SPadjust + DPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "FLAG":			    
			    if( isMatchFLAG(AddERelatedRule.rField[i][k-2 + SPadjust + DPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    map.put(header.get(j),i);
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;		    		    
			}
		    }

		}
	    }
	    break;
	    
	case 9://　評価型＋フィールド数６　のルール
	    for(int j=0; j < hSize; j++){
		if(map.containsKey(header.get(j)) ){
		    eval[map.get(header.get(j))]++;
		    continue;
		}
		hField = header.get(j).split("\\s+|\\t+");
		for(int i=0; i < AddERelatedRule.rSize; i++){

		    if(isMatchSP(AddERelatedRule.rField[i][3],AddERelatedRule.rField[i][5],hField[2]) && isMatchDP(AddERelatedRule.rField[i][6],AddERelatedRule.rField[i][8],hField[3]) && isMatchPROT(AddERelatedRule.rField[i][9],hField[4]) && isMatchFLAG(AddERelatedRule.rField[i][10],hField[5]) && isMatchSA(AddERelatedRule.rField[i][1],hField[0]) && isMatchDA(AddERelatedRule.rField[i][2],hField[1])){

			eval[i]++;
			map.put(header.get(j),i);
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
	    if( ( (header.equals("0") || header.equals("268439552")) && rule.equals("0x0000/0x0200") ) || ( header.equals("4294967295") && rule.equals("0x1000/0x1000") ) ) //acl_seed
		return true;
	    else if( (header.equals("0") && rule.equals("0x0000/0xff00") ) || (header.equals("4294967295") && ( rule.equals("0x0400/0x0400") || rule.equals("0x0100/0x0100") ) ) )//fw_seed
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
