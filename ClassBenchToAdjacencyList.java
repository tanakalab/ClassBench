import java.io.*;
import java.util.*;

class Node
{
    int eval;
    List<String> dep;

    Node(int e,List<String> d){
	eval = e;
	dep = d;
    }
    
    @Override
    public String toString(){

	if(dep.isEmpty())
	    return String.valueOf(eval);

	else{
	    String str;
	    str = eval + " ";
	    for(int i = dep.size()-1;0 <= i; i--){
		str += dep.get(i);
		if(i == 0)		    
		    break;	       
		str += ",";
	    }		    
	    return str;
	}
    }
}   
class RelatedRule //評価パケット数と従属関係を求めるのに共通するルールの値を格納
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
		int plefix1 = (Integer.parseInt(num1[4]));
		long ten1 = Integer.parseInt(num1[0])*(long)Math.pow(2,24) + Integer.parseInt(num1[1])*(long)Math.pow(2,16) + Integer.parseInt(num1[2])*(long)Math.pow(2,8) + Integer.parseInt(num1[3]);
		//例）198.45.44.44/32 = 198*2^24 + 45*2^16 + 44*2^8 + 44  

		long low,high;

		low = (long)Math.pow(2,32-plefix1) * (long)(ten1/Math.pow(2,32-plefix1));
		high = low + (long)Math.pow(2,32-plefix1) - 1;

		RelatedRule.rField[i][1] = low + "-" + high;


		String[] num2 = rField[i][2].split("\\.|/") ;
		int plefix2 = (Integer.parseInt(num2[4]));
		long ten2 = Integer.parseInt(num2[0])*(long)Math.pow(2,24) + Integer.parseInt(num2[1])*(long)Math.pow(2,16) + Integer.parseInt(num2[2])*(long)Math.pow(2,8) + Integer.parseInt(num2[3]);
		
		low = (long)Math.pow(2,32-plefix2) * (long)(ten2/(long)Math.pow(2,32-plefix2));
		high = low + (long)Math.pow(2,32-plefix2) - 1;
		
		RelatedRule.rField[i][2] = low + "-" + high;
		

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

		RelatedRule.rField[i][1] = low + "-" + high;

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
		
		RelatedRule.rField[i][1] = low + "-" + high;
	    
	    }
	}
	else{
	    for(int i=0; i < rSize; i++)		
		rField[i] = (rule.get(i)).split("\\s+|\\t+");
	    
	}
	    
    }	
}

public class ClassBenchToAdjacencyList {//ClassBench形式のルールリストを評価パケット数と従属関係のルールリスト(竹山法やヒキン法で使う)に変換する
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

	    RelatedRule.setSize( rule.size() );
	    RelatedRule.setRule(rule,args);

	    // String[] argcopy = new String[args.length-3];
	    // System.arraycopy(args,3,argcopy,0,args.length-3);
	    
	    int[] eval = makeEvaluation(header,args);
	    List<String>[] dep = makeDependence(args);

	    ArrayList<Node> AList = new ArrayList<Node>();

	    for(int i = 0; i < eval.length; i++){
		Node node = new Node(eval[i],dep[i]);
		AList.add(node);
	    }

	    for(Node n : AList){ //結果の表示
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
    
    public static List<String>[] makeDependence(String[] args){ //従属関係を生成

	int SPadjust = Arrays.asList(args).contains("SP") ? 2 : 0;
	int DPadjust = Arrays.asList(args).contains("DP") ? 2 : 0;
	int ruleFieldSize = RelatedRule.rField[0].length;	
	ArrayList<String>[] dep = new ListString[RelatedRule.rSize]; //従属関係を格納する配列
	for(int i = 0; i < dep.length; i++)
	    dep[i] = new ListString();

	switch(args.length){

	case 4://　評価型＋フィールド数１　のルール
	    for(int i=RelatedRule.rSize-1; 0<=i; i--){
		outside: for(int j=i-1; 0<=j; j--){
		    if(RelatedRule.rField[i][0].equals(RelatedRule.rField[j][0]))
			continue outside;
		    
		    switch(args[3]){

		    case "SA":
			if( includeSA(RelatedRule.rField[i][1],RelatedRule.rField[j][1]) ){
			    
			    // System.out.println((i+1) +" "+ (j+1));
			    
			    dep[i].add(String.valueOf(j+1));
			    //   System.out.println(j+1);
			}
			break;
		    case "DA":
			if( includeDA(RelatedRule.rField[i][1],RelatedRule.rField[j][1]) ){
			    
			    // System.out.println((i+1) +" "+ (j+1));
			    
			    dep[i].add(String.valueOf(j+1));
			    //   System.out.println(j+1);
			}
			break;
		    case "SP":
			if( includeSP(RelatedRule.rField[i][1],RelatedRule.rField[i][3],RelatedRule.rField[j][1],RelatedRule.rField[j][3]) ){
			    
			    // System.out.println((i+1) +" "+ (j+1));
			    
			    dep[i].add(String.valueOf(j+1));
			    //   System.out.println(j+1);
			}
			break;
		    case "DP":
			if(includeDP(RelatedRule.rField[i][1],RelatedRule.rField[i][3],RelatedRule.rField[j][1],RelatedRule.rField[j][3]) ){
			    
			    // System.out.println((i+1) +" "+ (j+1));
			    
			    dep[i].add(String.valueOf(j+1));
			    //   System.out.println(j+1);
			}
			break;
		    case "PROT":
			if( includePROT(RelatedRule.rField[i][1],RelatedRule.rField[j][1]) ){
			    
			    // System.out.println((i+1) +" "+ (j+1));
			    
			    dep[i].add(String.valueOf(j+1));
			    //   System.out.println(j+1);
			}
			break;
		    }
		}
	    }
	
	    break;
	    			
	case 5://　評価型＋フィールド数２　のルール
	    for(int i=RelatedRule.rSize-1; 0<=i; i--){
		outside: for(int j=i-1; 0<=j; j--){
		    for(int k=4; k >= 3; k--){
			if(k==4 && RelatedRule.rField[i][0].equals(RelatedRule.rField[j][0]))
			    continue outside;			
			switch(args[k]){
			case "SA":
			    if( includeSA(RelatedRule.rField[i][1],RelatedRule.rField[j][1]) ){
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;			
			    break;
			case "DA":
			    if( includeDA(RelatedRule.rField[i][k-2],RelatedRule.rField[j][k-2]) ){
			    
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "SP":
			    if( includeSP(RelatedRule.rField[i][k-2],RelatedRule.rField[i][k],RelatedRule.rField[j][k-2],RelatedRule.rField[j][k]) ){
			    
				// System.out.println((i+1) +" "+ (j+1));
				if(k==3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "DP":
			    if(includeDP(RelatedRule.rField[i][k-2 + SPadjust],RelatedRule.rField[i][k + SPadjust],RelatedRule.rField[j][k-2 + SPadjust],RelatedRule.rField[j][k + SPadjust]) ){
				
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "PROT":
			    if( includePROT(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],RelatedRule.rField[j][k-2 + SPadjust + DPadjust]) ){
				
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "FLAG":
			    if( includeFLAG(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],RelatedRule.rField[j][k-2 + SPadjust + DPadjust]) ){
			    
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
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
	    for(int i=RelatedRule.rSize-1; 0<=i; i--){
		outside: for(int j=i-1; 0<=j; j--){
		    for(int k=5; k >= 3; k--){
			if(k==5 && RelatedRule.rField[i][0].equals(RelatedRule.rField[j][0]))
			    continue outside;			
			switch(args[k]){
			case "SA":
			    if( includeSA(RelatedRule.rField[i][1],RelatedRule.rField[j][1]) ){
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;			
			    break;
			case "DA":
			    if( includeDA(RelatedRule.rField[i][k-2],RelatedRule.rField[j][k-2]) ){
			    
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "SP":
			    if( includeSP(RelatedRule.rField[i][k-2],RelatedRule.rField[i][k],RelatedRule.rField[j][k-2],RelatedRule.rField[j][k]) ){
			    
				// System.out.println((i+1) +" "+ (j+1));
				if(k==3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "DP":
			    if(includeDP(RelatedRule.rField[i][k-2 + SPadjust],RelatedRule.rField[i][k + SPadjust],RelatedRule.rField[j][k-2 + SPadjust],RelatedRule.rField[j][k + SPadjust]) ){
				
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "PROT":
			    if( includePROT(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],RelatedRule.rField[j][k-2 + SPadjust + DPadjust]) ){
				
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "FLAG":
			    if( includeFLAG(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],RelatedRule.rField[j][k-2 + SPadjust + DPadjust]) ){
			    
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
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
	    for(int i=RelatedRule.rSize-1; 0<=i; i--){
		outside:for(int j=i-1; 0<=j; j--){
		    for(int k=6; k >= 3; k--){
			if(k==6 && RelatedRule.rField[i][0].equals(RelatedRule.rField[j][0]))
			    continue outside;			
			switch(args[k]){
			case "SA":
			    if( includeSA(RelatedRule.rField[i][1],RelatedRule.rField[j][1]) ){
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;			
			    break;
			case "DA":
			    if( includeDA(RelatedRule.rField[i][k-2],RelatedRule.rField[j][k-2]) ){
			    
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "SP":
			    if( includeSP(RelatedRule.rField[i][k-2],RelatedRule.rField[i][k],RelatedRule.rField[j][k-2],RelatedRule.rField[j][k]) ){
			    
				// System.out.println((i+1) +" "+ (j+1));
				if(k==3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "DP":
			    if(includeDP(RelatedRule.rField[i][k-2 + SPadjust],RelatedRule.rField[i][k + SPadjust],RelatedRule.rField[j][k-2 + SPadjust],RelatedRule.rField[j][k + SPadjust]) ){
				
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "PROT":
			    if( includePROT(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],RelatedRule.rField[j][k-2 + SPadjust + DPadjust]) ){
				
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "FLAG":
			    if( includeFLAG(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],RelatedRule.rField[j][k-2 + SPadjust + DPadjust]) ){
			    
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
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
	    for(int i=RelatedRule.rSize-1; 0<=i; i--){
		outside: for(int j=i-1; 0<=j; j--){
		    for(int k=7; k >= 3; k--){
			if(k==7 && RelatedRule.rField[i][0].equals(RelatedRule.rField[j][0]))
			    continue outside;			
			switch(args[k]){
			case "SA":
			    if( includeSA(RelatedRule.rField[i][1],RelatedRule.rField[j][1]) ){
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;			
			    break;
			case "DA":
			    if( includeDA(RelatedRule.rField[i][k-2],RelatedRule.rField[j][k-2]) ){
			    
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "SP":
			    if( includeSP(RelatedRule.rField[i][k-2],RelatedRule.rField[i][k],RelatedRule.rField[j][k-2],RelatedRule.rField[j][k]) ){
			    
				// System.out.println((i+1) +" "+ (j+1));
				if(k==3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "DP":
			    if(includeDP(RelatedRule.rField[i][k-2 + SPadjust],RelatedRule.rField[i][k + SPadjust],RelatedRule.rField[j][k-2 + SPadjust],RelatedRule.rField[j][k + SPadjust]) ){
				
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "PROT":
			    if( includePROT(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],RelatedRule.rField[j][k-2 + SPadjust + DPadjust]) ){
				
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "FLAG":
			    if( includeFLAG(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],RelatedRule.rField[j][k-2 + SPadjust + DPadjust]) ){
			    
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
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
	    for(int i=RelatedRule.rSize-1; 0<=i; i--){
		outside: for(int j=i-1; 0<=j; j--){
		    for(int k=8; k >= 3; k--){
			if(k==8 && RelatedRule.rField[i][0].equals(RelatedRule.rField[j][0]))
			    continue outside;			
			switch(args[k]){
			case "SA":
			    if( includeSA(RelatedRule.rField[i][1],RelatedRule.rField[j][1]) ){
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;			
			    break;
			case "DA":
			    if( includeDA(RelatedRule.rField[i][k-2],RelatedRule.rField[j][k-2]) ){
			    
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "SP":
			    if( includeSP(RelatedRule.rField[i][k-2],RelatedRule.rField[i][k],RelatedRule.rField[j][k-2],RelatedRule.rField[j][k]) ){
			    
				// System.out.println((i+1) +" "+ (j+1));
				if(k==3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "DP":
			    if(includeDP(RelatedRule.rField[i][k-2 + SPadjust],RelatedRule.rField[i][k + SPadjust],RelatedRule.rField[j][k-2 + SPadjust],RelatedRule.rField[j][k + SPadjust]) ){
				
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "PROT":
			    if( includePROT(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],RelatedRule.rField[j][k-2 + SPadjust + DPadjust]) ){
				
				// System.out.println((i+1) +" "+ (j+1));
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
				}
			    }
			    else
				continue outside;
			    break;
			case "FLAG":
			    // if(i==332 && j == 331)
				//	System.out.println( includeFLAG(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],RelatedRule.rField[j][k-2 + SPadjust + DPadjust])+" "+RelatedRule.rField[i][k-2 + SPadjust + DPadjust]+" "+RelatedRule.rField[j][k-2 + SPadjust + DPadjust] );
			    if( includeFLAG(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],RelatedRule.rField[j][k-2 + SPadjust + DPadjust]) ){
				if(k == 3){
				    dep[i].add(String.valueOf(j+1));
				    //   System.out.println(j+1);
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
    
    public static int[] makeEvaluation(List<String> header,String[] args){

	int SPadjust = Arrays.asList(args).contains("SP") ? 2 : 0;
	int DPadjust = Arrays.asList(args).contains("DP") ? 2 : 0;
	int hSize = header.size();	
	int[] eval = new int[RelatedRule.rSize];//評価パケット数を格納する配列
	String[] hField = header.get(0).split("\\s+|\\t+");
	//	System.out.println(hField.length);
	switch(args.length){
	    
	case 4://　評価型＋フィールド数１　のルール
	    for(int j=0; j < hSize; j++){
		hField = header.get(j).split("\\s+|\\t+");
		outside: for(int i=0; i < RelatedRule.rSize; i++){
		    switch(args[3]){

		    case "SA":
			if( isMatchSA(RelatedRule.rField[i][1],hField[0]) ) {			   
			    eval[i]++;
			    break outside;
			}
			break;
		    case "DA":
			if( isMatchDA(RelatedRule.rField[i][1],hField[0]) )    {
			    eval[i]++;
			    break outside;
			}
			break;
		    case "SP":
			if( isMatchSP(RelatedRule.rField[i][1],RelatedRule.rField[i][3],hField[0]) ){
			    eval[i]++;
			    break outside;
			}
			break;
		    case "DP":
			if( isMatchDP(RelatedRule.rField[i][1],RelatedRule.rField[i][3],hField[0]) ){			    
			    eval[i]++;
			    break outside;
			}
			break;
		    case "PROT":
			if( isMatchPROT(RelatedRule.rField[i][1],hField[0]) ){
			    eval[i]++;
			    break outside;
			}
			break;		    
		    }
		}
	    }
	    break;
	    
	case 5://　評価型＋フィールド数２　のルール
	    for(int j=0; j < hSize; j++){
		hField = header.get(j).split("\\s+|\\t+");
		outside: for(int i=0; i < RelatedRule.rSize; i++){
		    for(int k=4; k >= 3; k--){
			switch(args[k]){
			case "SA":
			    if( isMatchSA(RelatedRule.rField[i][1],hField[0]) ) {
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "DA":
			    if( isMatchDA(RelatedRule.rField[i][k-2],hField[k-3]) ){				
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "SP":
			    if( isMatchSP(RelatedRule.rField[i][k-2],RelatedRule.rField[i][k],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "DP":
			    if( isMatchDP(RelatedRule.rField[i][k-2 + SPadjust],RelatedRule.rField[i][k + SPadjust],hField[k-3]) ){
				if(k == 3){				    
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "PROT":
			    if( isMatchPROT(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "FLAG":
			    //System.out.println( RelatedRule.rField[i][0] + " " +  RelatedRule.rField[i][1] + " " + RelatedRule.rField[i][2] + " " + RelatedRule.rField[i][k-2 + SPadjust + DPadjust] + " " + hField[k-3]);
			    if( isMatchFLAG(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
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
		hField = header.get(j).split("\\s+|\\t+");	    
		outside: for(int i=0; i < RelatedRule.rSize; i++){
		    for(int k=5; k >= 3; k--){
			switch(args[k]){
			case "SA":
			    if( isMatchSA(RelatedRule.rField[i][1],hField[0]) ) {
				if(k == 3 ){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "DA":
			    if( isMatchDA(RelatedRule.rField[i][k-2],hField[k-3]) )    {
				if(k == 3 ){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "SP":
			    if( isMatchSP(RelatedRule.rField[i][k-2],RelatedRule.rField[i][k],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "DP":
			    if( isMatchDP(RelatedRule.rField[i][k-2 + SPadjust],RelatedRule.rField[i][k + SPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "PROT":
			    if( isMatchPROT(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "FLAG":			    
			    if( isMatchFLAG(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
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
		hField = header.get(j).split("\\s+|\\t+");
		outside: for(int i=0; i < RelatedRule.rSize; i++){		
		    for(int k=6; k >= 3; k--){
			switch(args[k]){
			case "SA":
			    if( isMatchSA(RelatedRule.rField[i][k-2],hField[k-3]) ) {
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "DA":
			    if( isMatchDA(RelatedRule.rField[i][k-2],hField[k-3]) )    {
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "SP":
			    if( isMatchSP(RelatedRule.rField[i][k-2],RelatedRule.rField[i][k],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "DP":
			    if( isMatchDP(RelatedRule.rField[i][k-2 + SPadjust],RelatedRule.rField[i][k + SPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "PROT":
			    if( isMatchPROT(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "FLAG":			    
			    if( isMatchFLAG(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
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
		hField = header.get(j).split("\\s+|\\t+");	    
		outside: for(int i=0; i < RelatedRule.rSize; i++){		
		    for(int k=7; k >= 3; k--){
			switch(args[k]){
			case "SA":
			    if( isMatchSA(RelatedRule.rField[i][k-2],hField[k-3]) ) {
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "DA":
			    if( isMatchDA(RelatedRule.rField[i][k-2],hField[k-3]) )    {
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "SP":
			    if( isMatchSP(RelatedRule.rField[i][k-2],RelatedRule.rField[i][k],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "DP":
			    if( isMatchDP(RelatedRule.rField[i][k-2 + SPadjust],RelatedRule.rField[i][k + SPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "PROT":
			    if( isMatchPROT(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "FLAG":			    
			    if( isMatchFLAG(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
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
		hField = header.get(j).split("\\s+|\\t+");
		outside: for(int i=0; i < RelatedRule.rSize; i++){
		    for(int k=8; k >= 3; k--){
			switch(args[k]){
			case "SA":
			    if( isMatchSA(RelatedRule.rField[i][k-2],hField[k-3]) ) {
				if(k == 3){
				    //  if(i==809)
				    //	System.out.println(hField[0] + " " +hField[1] + " " +hField[2] + " " +hField[3] + " " +hField[4] + " " +hField[5]);
				    
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "DA":
			    if( isMatchDA(RelatedRule.rField[i][k-2],hField[k-3]) )    {
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "SP":
			    if( isMatchSP(RelatedRule.rField[i][k-2],RelatedRule.rField[i][k],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "DP":
			    if( isMatchDP(RelatedRule.rField[i][k-2 + SPadjust],RelatedRule.rField[i][k + SPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "PROT":
			    if( isMatchPROT(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],hField[k-3]) ){
				if(k == 3){
				    eval[i]++;
				    break outside;
				}
			    }
			    else
				continue outside;
			    break;
			case "FLAG":
			    //System.out.println(RelatedRule.rField[i][k-7] + " " + RelatedRule.rField[i][k-6]+" "+RelatedRule.rField[i][k-5]+" "+RelatedRule.rField[i][k-4 + SPadjust]+" "+RelatedRule.rField[i][k-3 + SPadjust + DPadjust]+" "+RelatedRule.rField[i][k-2 + SPadjust + SPadjust]);
			    if( isMatchFLAG(RelatedRule.rField[i][k-2 + SPadjust + DPadjust],hField[k-3]) ){
				if(k == 3 ){
				    eval[i]++;
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
	    else if( (header.equals("0") && rule.equals("0x0000/0xff00") ) || (header.equals("4294967295") && ( rule.equals("0x0400/0x0400") || rule.equals("0x0100/0x0100") ) ) /*|| (header.equals("33559040") && rule.equals("0x0400/0x0400") ) || (header.equals("67109888") && rule.equals("0x0200/0x1200") )*/ )//fw_seed
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
