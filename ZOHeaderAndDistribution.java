import java.io.*;
import java.util.*;

public class ZOHeaderAndDistribution {
    public static void main(String[] args){

	if (args.length < 3 || args.length > 8 ) {
	    System.out.println("Arguments Error!\nUsage: $ java ZOHeaderFromClassBench <headerlist> <outputfile> [fields] ");
	    System.exit(1);
	}
	for(int i = 2; i < args.length; i++){
	    if( !(args[i].equals("SA") ||  args[i].equals("DA") ||  args[i].equals("SP") ||  args[i].equals("DP") ||  args[i].equals("PROT") ||  args[i].equals("FLAG") ) ){
		System.out.println("Arguments Error! : " + args[i]  + "\nSelect field arguments in SA,DA,SP,DP,PROT and FLAG");
		System.exit(1);
	    }
	}

	try{
	    ArrayList<String> origin_list = new ArrayList<String>();
	    File input = new File(args[0]);
	    BufferedReader br = new BufferedReader(new FileReader(input));//入力ファイル
	    File output = new File(args[1]);
	    BufferedWriter bw = new BufferedWriter(new FileWriter(output));//出力ファイル
	    String rule;
	    String ZO = "",x = "";

	    // while((rule = br.readLine()) != null)
	    // 	origin_list.add(rule);
	    
	     while((rule = br.readLine()) != null){
	    	 String[] result = rule.split("\\s+|\\t+");
		 
	    	 switch(args.length){
	    	 case 3:
	    	     switch(args[2]){
	    	     case "SA":
	    		 ZO = tenTotwo(Long.parseLong(result[0]),32);		     
	    		 break;
	    	     case "DA":
	    		 ZO = tenTotwo(Long.parseLong(result[0]),32);
	    		 break;
	    	     case "SP":
	    		 ZO = tenTotwo(Long.parseLong(result[0]),16);
	    		 break;
	    	     case "DP":
	    		 ZO = tenTotwo(Long.parseLong(result[0]),16);
	    		 break;
	    	     case "PROT":
	    		 ZO = tenTotwo(Long.parseLong(result[0]),8);
	    		 break;
	    	     }
	    	     origin_list.add( ZO );
	    	     break;
		     
	    	 case 4:
	    	     for(int i = 2; i < 4 ;i++){
	    		switch(args[i]){
	    		case "SA":			    
	    		    x = tenTotwo(Long.parseLong(result[i-2]),32);
	    		    break;
	    		case "DA":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),32);
	    		    break;
	    		case "SP":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),16);
	    		    break;
	    		case "DP":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),16);
	    		    break;
	    		case "PROT":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),8);
	    		    break;
	    		case "FLAG":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),32);
	    		    x = x.substring(0,16);
	    		    break;
	    		}
	    		if(i == 2)
	    		    ZO = x;
	    		else
	    		    ZO += x;
	    	     }
	    	     origin_list.add( ZO );
	    	     break;
		     
	    	 case 5:
	    	     for(int i = 2; i < 5 ;i++){
	    		switch(args[i]){
	    		case "SA":			    
	    		    x = tenTotwo(Long.parseLong(result[i-2]),32);
	    		    break;
	    		case "DA":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),32);
	    		    break;
	    		case "SP":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),16);
	    		    break;
	    		case "DP":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),16);
	    		    break;
	    		case "PROT":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),8);
	    		    break;
	    		case "FLAG":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),32);
	    		    x = x.substring(0,16);
	    		    break;
	    		}
	    		if(i == 2)
	    		    ZO = x;
	    		else
	    		    ZO += x;
	    	     }
	    	     origin_list.add( ZO );
	    	     break;
		     
	    	 case 6:
	    	     for(int i = 2; i < 6 ;i++){
	    		switch(args[i]){
	    		case "SA":			    
	    		    x = tenTotwo(Long.parseLong(result[i-2]),32);
	    		    break;
	    		case "DA":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),32);
	    		    break;
	    		case "SP":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),16);
	    		    break;
	    		case "DP":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),16);
	    		    break;
	    		case "PROT":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),8);
	    		    break;
	    		case "FLAG":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),32);
	    		    x = x.substring(0,16);
	    		    break;
	    		}
	    		if(i == 2)
	    		    ZO = x;
	    		else
	    		    ZO += x;
	    	     }
	    	     origin_list.add( ZO );
	    	     break;
		     
	    	 case 7:
	    	     for(int i = 2; i < 7 ;i++){
	    		switch(args[i]){
	    		case "SA":			    
	    		    x = tenTotwo(Long.parseLong(result[i-2]),32);
	    		    break;
	    		case "DA":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),32);
	    		    break;
	    		case "SP":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),16);
	    		    break;
	    		case "DP":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),16);
	    		    break;
	    		case "PROT":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),8);
	    		    break;
	    		case "FLAG":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),32);
	    		    x = x.substring(0,16);
	    		    break;
	    		}
	    		if(i == 2)
	    		    ZO = x;
	    		else
	    		    ZO += x;
	    	     }
	    	     origin_list.add( ZO );
	    	     break;
		     
	    	 case 8:
	    	     for(int i = 2; i < 8 ;i++){
	    		switch(args[i]){
	    		case "SA":			    
	    		    x = tenTotwo(Long.parseLong(result[i-2]),32);
	    		    break;
	    		case "DA":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),32);
	    		    break;
	    		case "SP":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),16);
	    		    break;
	    		case "DP":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),16);
	    		    break;
	    		case "PROT":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),8);
	    		    break;
	    		case "FLAG":
	    		    x = tenTotwo(Long.parseLong(result[i-2]),32);
	    		    x = x.substring(0,16);
	    		    break;
	    		}
	    		if(i == 2)
	    		    ZO = x;
	    		else
	    		    ZO += x;
	    	     }
	    	     origin_list.add( ZO );
	    	     break;
	    	 }
	     }

	     origin_list = makeDistribution(origin_list);
	     
	     for(String variable : origin_list){//0,1のリストの表示
		 //System.out.println(ZO);
		 bw.write( variable );
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

    public static ArrayList<String> makeDistribution(ArrayList<String> origin_list){

	ArrayList<String> dlist = new ArrayList<String>();
	String str;
	int count = 1; 
	
	for(int i = 0; i < origin_list.size(); i++){
	    str = origin_list.get(i);
	    for(int j = i+1; j < origin_list.size(); j++){
		if( str.equals(origin_list.get(j)) ){
		    count++;
		    origin_list.remove(j);
		    j--;
		}
	    }
	    dlist.add(origin_list.get(i) + " " + count);
	    count = 1;
	}

	return dlist;
    }
    
    public static String tenTotwo(long num, int numwidth){//10進表記を2進表記に変換する
	int[] two;
	int i;
	String returnBits = "";
	two = new int[65535];

	for(i=0;i<(numwidth-1);i++) {
	    two[i] = (int)(num%2);
	    num = num >> 1;
	}
	two[i] = (int)num;
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
