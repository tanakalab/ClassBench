import java.io.*;
import java.util.*;

public class ZOHeaderFromClassbench {
    public static void main(String[] args){

	if (args.length != 2) {
	    System.out.println("Arguments Error!\nUsage: $ java ZOMHeaderFromClassbench <headerlist> <outputfile>");
	    System.exit(1);
	}
	
	try{
	    List<String> origin_list = new ArrayList<String>();
	    File input = new File(args[0]);
	    BufferedReader br = new BufferedReader(new FileReader(input));//入力ファイル
	    File output = new File(args[1]);
	    BufferedWriter bw = new BufferedWriter(new FileWriter(output));//出力ファイル
	    String rule;
	    String SA;//0,1,*の送信元アドレス
	    String DA;//0,1,*の送信先アドレス
	    String sport;
	    String dport;
	    String promask;//プロトコルとマスク
	    String flagmask;//フラグとマスク
	    
	     while((rule = br.readLine()) != null){
		 String[] result = rule.split("\\s+|\\t+");
		 
		 // StringBuilder sb = new StringBuilder(result[0]);
		 // sb.deleteCharAt(0);
		 // result[0]=sb.toString();

		 switch(result.length){
		 case 1:
		     SA = tenTotwo(Long.parseLong(result[0]),32);		     
		     origin_list.add( SA );
		     break;
		 case 2:
		     SA = tenTotwo(Long.parseLong(result[0]),32);
		     DA = tenTotwo(Long.parseLong(result[1]),32);
		     origin_list.add( SA + " " + DA);
		     break;
		 case 3:
		     SA = tenTotwo(Long.parseLong(result[0]),32);
		     DA = tenTotwo(Long.parseLong(result[1]),32);
		     sport = tenTotwo(Long.parseLong(result[2]),16);
		     origin_list.add( SA + " " + DA + " " + sport);
		     break;
		 case 4:
		     SA = tenTotwo(Long.parseLong(result[0]),32);
		     DA = tenTotwo(Long.parseLong(result[1]),32);
		     sport = tenTotwo(Long.parseLong(result[2]),16);
		     dport = tenTotwo(Long.parseLong(result[3]),16);
		     origin_list.add( SA + " " + DA + " " + sport + " " + dport);
		     break;
		 case 5:
		     SA = tenTotwo(Long.parseLong(result[0]),32);
		     DA = tenTotwo(Long.parseLong(result[1]),32);
		     sport = tenTotwo(Long.parseLong(result[2]),16);
		     dport = tenTotwo(Long.parseLong(result[3]),16);
		     promask = tenTotwo(Long.parseLong(result[4]),8);
		     origin_list.add( SA + " " + DA + " " + sport + " " + dport + " " + promask);
		     break;
		 case 6:
		     SA = tenTotwo(Long.parseLong(result[0]),32);
		     DA = tenTotwo(Long.parseLong(result[1]),32);
		     sport = tenTotwo(Long.parseLong(result[2]),16);
		     dport = tenTotwo(Long.parseLong(result[3]),16);
		     promask = tenTotwo(Long.parseLong(result[4]),8);
		     flagmask = tenTotwo(Long.parseLong(result[5]),32);
		     flagmask = flagmask.substring(0,16);
		     origin_list.add( SA + " " + DA + " " + sport + " " + dport + " " + promask + " " + flagmask);
		     break;
		 }
	     }
	     
	     for(String ZO : origin_list){//0,1のリストの表示
		 //System.out.println(ZOM);
		 bw.write(ZO);
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
