import java.io.*;
import java.util.*;

public class makeZOMRangeMostPriorRule {//ポートのみレンジルールであるルール形式の各ヘッダの最優先ルールを見つける
    public static void main(String[] args){
	if(args.length != 3){
	    System.out.println("Arguments Error!\nUsage: $ java makeZOMRangeMostPriorRule <rulelist> <headerlist> <outputfile>");
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

	    else if(rule.get(0).contains("-")){   
		for(int j=0; j < hSize; j++){
		    if( map.containsKey(header.get(j)) ){		  
			mostPriorRuleNum[j] = map.get(header.get(j));
			continue;
		    }
		    for(int i=0; i < rSize; i++){
			if( isMatchRange(rule.get(i),header.get(j),1) ){    
			    map.put(header.get(j),i);
			    mostPriorRuleNum[j] = i;
			    break;
			}
		    }
		}	
	    }
	    
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
		bw.write("Header["+ (i+1) +"] --> "+ header.get(i).replaceAll(" ","") + " ===> " + String.valueOf(mostPriorRuleNum[i]+1) );
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
	List<String> ruleRange = new ArrayList<String>();

	List<String> header = new ArrayList<String>();
	List<String> headerRange = new ArrayList<String>();

	String[] field = fieldRule.get(0).split(" ");	
	int index=-1,containTwoRange = 0;
	
	for(int i = 0 ; i < field.length; i++){
	    if(field[i].contains("-") ){
		index = i;
		containTwoRange++;
	    }
	}
	
	if(containTwoRange == 2)
	    index--;
		
	for(int i = 0; i < rSize; i++){	    	    
	    StringBuilder sb = new StringBuilder();
	    field = fieldRule.get(i).split(" ");

	    for(int j = 0; j < field.length; j++){
		if(index == j && containTwoRange == 2){
		    ruleRange.add(field[j] + " " + field[j+1]);
		    j++;
		    continue;
		}
		else if(index == j){
		    ruleRange.add(field[j]);
		    continue;
		}
		else
		    sb.append( field[j] );
	    }
	    rule.add(sb.toString());
	}
	
	for(int i = 0; i < hSize; i++){	    	    
	    StringBuilder sb = new StringBuilder();
	    field = fieldHeader.get(i).split(" ");
	    
	    for(int j = 0; j < field.length; j++){
		if(index == j && containTwoRange == 2){
		    headerRange.add(field[j] + " " + field[j+1]);
		    j++;
		    continue;
		}
		else if(index == j){
		    headerRange.add(field[j]);
		    continue;
		}
		else
		    sb.append( field[j] );
	    }
	    header.add(sb.toString());
	}
	
	if(containTwoRange == 0){
	    for(int j=0; j < hSize; j++){
		if( map.containsKey(fieldHeader.get(j)) ){		  
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
	}
	else{
	    for(int j=0; j < hSize; j++){
		if( map.containsKey(fieldHeader.get(j)) ){		  
		    mostPriorRuleNum[j] = map.get(fieldHeader.get(j));
		    continue;
		}
		for(int i=0; i < rSize; i++){
		    if(isMatchRange(ruleRange.get(i),headerRange.get(j),containTwoRange) && isMatch(rule.get(i),header.get(j),rule.get(0).length()) ){    
			map.put(fieldHeader.get(j),i);
			mostPriorRuleNum[j] = i;
			break;
		    }
		}
	    }
	}
	return mostPriorRuleNum;
    }

    static boolean  isMatchRange(String rule,String header,int containTwoRange){
	
	if(containTwoRange == 2){
	    String[] twoRange,twoHeader,Range0,Range1;
	    int Header_0,Header_1;
	    
	    twoRange = rule.split(" ");
	    twoHeader = header.split(" ");
	    Header_0 = twoToten(twoHeader[0]);
	    Header_1 = twoToten(twoHeader[1]);
	    Range0 = twoRange[0].split("-");
	    Range1 = twoRange[1].split("-");

	    if(Integer.parseInt(Range0[0]) <= Header_0 && Header_0 <= Integer.parseInt(Range0[1]) && Integer.parseInt(Range1[0]) <= Header_1 && Header_1 <= Integer.parseInt(Range1[1]) )
		return true;
	}
	
	else if(containTwoRange == 1){
	    String[] Range;
	    int tenHeader;
	    
	    Range = rule.split("-");
	    tenHeader = twoToten(header);
	    
	    if(Integer.parseInt(Range[0]) <= tenHeader && tenHeader <= Integer.parseInt(Range[1]) )
		return true;
	}
	return false;
    }
    
	static boolean isMatch(String rule,String header,int size){
	    
	for(int i=0; i < size; i++){
	    if( rule.charAt(i) != '*' && rule.charAt(i) != header.charAt(i) )
		return false;
	}
	return true;
    }

    static int twoToten(String RangeField){
	
	int portValue = 0;

	for(int i = 0; i < 16; i++){
	    if(RangeField.charAt(i) == '1')
		portValue += Math.pow(2,(15-i));
	}
	return portValue;
    }
}

