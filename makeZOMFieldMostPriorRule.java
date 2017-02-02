import java.io.*;
import java.util.*;

public class makeZOMFieldMostPriorRule {//0,1,*で表されるルール形式の各ヘッダの最優先ルールを見つける
    public static void main(String[] args){
	if(args.length != 3){
	    System.out.println("Arguments Error!\nUsage: $ java AddEvaluationZOM  <rulelist> <headerlist> <outputfile>");
	    System.exit(1);
	}	
	try{
	    List<String> rule = new ArrayList<String>();
	    List<String> header = new ArrayList<String>();
	    HashMap<String,Integer> map = new HashMap<String,Integer>(); 
	    
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
	    int[] mostPriorRuleNum = new int[hSize];
	    for(int i = 0; i < mostPriorRuleNum.length; i++)
		mostPriorRuleNum[i] = -1;

	    if( rule.get(0).contains(" ") || rule.get(0).contains("\t") )
		mostPriorRuleNum = makeMostPriorRule(rule,header);

	    
	    else {
		for(int j=0; j < hSize; j++){
		     if( map.containsKey(header.get(j)) ){		  
			mostPriorRuleNum[j] = map.get(header.get(j));
			continue;
		     }
		    for(int i=0; i < rSize; i++){
			if( isMatch(rule.get(i),header.get(j),rule.get(0).length()) ){    
			    map.put(header.get(j),i);
			    mostPriorRuleNum[j] = i;
			    break;
			}
		    }
		}	       		
	    }


	    
	    for(int i = 0; i < hSize; i++){   //結果の表示
		bw.write("Header["+ (i+1) +"] --> "+ header.get(i) + " ===> " + String.valueOf(mostPriorRuleNum[i]+1) );
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



    public static int[] makeMostPriorRule(List<String> fieldRule,List<String> fieldHeader){
	
	int rSize = fieldRule.size();	
	int hSize = fieldHeader.size();
	int[] mostPriorRuleNum = new int[hSize];	
	for(int i = 0; i < mostPriorRuleNum.length; i++)
	    mostPriorRuleNum[i] = -1;
	HashMap<String,Integer> map = new HashMap<String,Integer>(); 
	
	List<String> rule = new ArrayList<String>();
	List<String> header = new ArrayList<String>();

	String[] field;		
	
	for(int i = 0; i < rSize; i++){	    	    
	    StringBuilder sb = new StringBuilder();
	    field = fieldRule.get(i).split(" ");
	    
	    for(int j = 0; j < field.length; j++){
		sb.append( field[j] );
	    }
	    rule.add(sb.toString());
	}
	
	for(int i = 0; i < hSize; i++){	    	    
	    StringBuilder sb = new StringBuilder();
	    field = fieldHeader.get(i).split(" ");
		    
	    for(int j = 0; j < field.length; j++){
		sb.append( field[j] );
	    }
	    header.add(sb.toString());
	}
	
	    for(int j=0; j < hSize; j++){
		if( map.containsKey(header.get(j)) ){		  
		    mostPriorRuleNum[j] = map.get(fieldHeader.get(j));
		    continue;
		}
		for(int i=0; i < rSize; i++){
		    if( isMatch(rule.get(i),header.get(j),rule.get(0).length()) ){    
			map.put(fieldHeader.get(j),i);
			mostPriorRuleNum[j] = i;
			break;
		    }
		}
	    }
	
	return mostPriorRuleNum;
    }

    
    public static boolean isMatch(String rule,String header,int size){
	    
	for(int i=0; i < size; i++){
	    if( rule.charAt(i) != '*' && rule.charAt(i) != header.charAt(i) )
		return false;
	}
	return true;
    }
    
}
