import java.io.*;
import java.util.*;


public class ClassBenchToRange {//ClassBench形式のルールリストをポートはレンジで他は0,1,*のルールリストに変える
    public static void main(String[] args){
	
	if (args.length < 3 || args.length > 8 ) {
	    System.out.println("Arguments Error!\nUsage: $ java ClassBenchToRange <rulelist> <outputfile> [fields] ");
	    System.exit(1);
	}
	for(int i = 2; i < args.length; i++){
	    if( !(args[i].equals("SA") ||  args[i].equals("DA") ||  args[i].equals("SP") ||  args[i].equals("DP") ||  args[i].equals("PROT") ||  args[i].equals("FLAG") ) ){
		System.out.println("Arguments Error! : " + args[i]  + "\nSelect field arguments in SA,DA,SP,DP,PROT and FLAG");
		System.exit(1);
	    }
	}
	
	try{
	    List<String> origin_list = new ArrayList<String>();
	    File input = new File(args[0]);
	    BufferedReader br = new BufferedReader(new FileReader(input));//入力ファイル
	    File output = new File(args[1]);
	    BufferedWriter bw = new BufferedWriter(new FileWriter(output));//出力ファイル
	    String rule;
	    String ZOM = "",x = "";
	    int SPadjust = Arrays.asList(args).contains("SP") ? 2 : 0;
	    int DPadjust = Arrays.asList(args).contains("DP") ? 2 : 0;
	    
	    while((rule = br.readLine()) != null){
	     
		String[] result = rule.split("\\s+|\\t+");
		if( Arrays.asList(args).contains("SA") ){
		    StringBuilder sb = new StringBuilder(result[0]);
		    sb.deleteCharAt(0);
		    result[0]=sb.toString();
		}
		switch(args.length){
		    
		case 3:
		    switch(args[2]){
		    case "SA":
			ZOM = CIDRToZOM(result[0]);//0,1,*の送信元アドレス		    
			break;
		    case "DA":
			ZOM = CIDRToZOM(result[0]);//0,1,*の送信先アドレス
			break;
		    case "SP":
			ZOM = result[0] + "-" + result[2];//送信元ポートレンジ（0,1,*のリスト）
			break;
		    case "DP":
			ZOM = result[0] + "-" + result[2];//送信先ポートレンジ（0,1,*のリスト）
			break;
		    case "PROT":
			ZOM = prmsTozom(result[0]);//プロトコルとマスク
			break;
		    }
		    origin_list.add( ZOM );
		    break;
		    
		case 4:
		    for(int i = 2; i < 4 ;i++){
			switch(args[i]){
			case "SA":
			    x = CIDRToZOM(result[i-2]);//0,1,*の送信元アドレス
			    break;
			case "DA":
			    x = CIDRToZOM(result[i-2]);//0,1,*の送信先アドレス
			    break;
			case "SP":
			    x = result[i-2] + "-" + result[i];//送信元ポートレンジ（0,1,*のリスト）
			    break;
			case "DP":
			    x = result[i-2 + SPadjust] + "-" + result[i + SPadjust];//送信先ポートレンジ（0,1,*のリスト）
			    break;
			case "PROT":
			    x = prmsTozom(result[i-2 + SPadjust + DPadjust]);//プロトコルとマスク
			    break;
			case "FLAG":
			    x = fgmsTozom(result[i-2 + SPadjust + DPadjust]);//フラグとマスク
			    break;
			}
			if(i == 2)
			    ZOM = x;
			else
			    ZOM += " " + x;
		    }
		    origin_list.add( ZOM );		       		    
		    break;
			
		case 5:
		    for(int i = 2; i < 5 ;i++){
			switch(args[i]){
			case "SA":
			    x = CIDRToZOM(result[i-2]);//0,1,*の送信元アドレス
			    break;
			case "DA":
			    x = CIDRToZOM(result[i-2]);//0,1,*の送信先アドレス
			    break;
			case "SP":
			    x = result[i-2] + "-" + result[i];//送信元ポートレンジ（0,1,*のリスト）
			    break;
			case "DP":
			    x = result[i-2 + SPadjust] + "-" + result[i + SPadjust];//送信先ポートレンジ（0,1,*のリスト）
			    break;
			case "PROT":
			    x = prmsTozom(result[i-2 + SPadjust + DPadjust]);//プロトコルとマスク
			    break;
			case "FLAG":
			    x = fgmsTozom(result[i-2 + SPadjust + DPadjust]);//フラグとマスク
			    break;
			}
			if(i == 2)
			    ZOM = x;
			else
			    ZOM += " " + x;
		    }
		    origin_list.add( ZOM );		    
		    break;
		    
		case 6:		    
		    for(int i = 2; i < 6 ;i++){
			switch(args[i]){
			case "SA":
			    x = CIDRToZOM(result[i-2]);//0,1,*の送信元アドレス
			    break;
			case "DA":
			    x = CIDRToZOM(result[i-2]);//0,1,*の送信先アドレス
			    break;
			case "SP":
			    x = result[i-2] + "-" + result[i];//送信元ポートレンジ（0,1,*のリスト）
			    break;
			case "DP":
			    x = result[i-2 + SPadjust] + "-" + result[i + SPadjust];//送信先ポートレンジ（0,1,*のリスト）
			    break;
			case "PROT":
			    x = prmsTozom(result[i-2 + SPadjust + DPadjust]);//プロトコルとマスク
			    break;
			case "FLAG":
			    x = fgmsTozom(result[i-2 + SPadjust + DPadjust]);//フラグとマスク
			    break;
			}
			if(i == 2)
			    ZOM = x;
			else
			    ZOM += " " + x;
		    }
		    origin_list.add( ZOM );		    
		    break;
		    
		case 7:
		    for(int i = 2; i < 7 ;i++){
			switch(args[i]){
			case "SA":
			    x = CIDRToZOM(result[i-2]);//0,1,*の送信元アドレス
			    break;
			case "DA":
			    x = CIDRToZOM(result[i-2]);//0,1,*の送信先アドレス
			    break;
			case "SP":
			    x = result[i-2] + "-" + result[i];//送信元ポートレンジ（0,1,*のリスト）
			    break;
			case "DP":
			    x = result[i-2 + SPadjust] + "-" + result[i + SPadjust];//送信先ポートレンジ（0,1,*のリスト）
			    break;
			case "PROT":
			    x = prmsTozom(result[i-2 + SPadjust + DPadjust]);//プロトコルとマスク
			    break;
			case "FLAG":
			    x = fgmsTozom(result[i-2 + SPadjust + DPadjust]);//フラグとマスク
			    break;
			}
			if(i == 2)
			    ZOM = x;
			else
			    ZOM += " " + x;
		    }
		    origin_list.add( ZOM );		    
		    break;
		    
		case 8:
		    String SA,DA,sp,dp,promask,flagmask;
		    
		    SA = CIDRToZOM(result[0]);//0,1,*の送信元アドレス
		    DA = CIDRToZOM(result[1]);//0,1,*の送信先アドレス
		    sp = result[2] + "-" + result[4];//送信元ポートレンジ
		    dp = result[5] + "-" + result[7];//送信先ポートレンジ
		    promask = prmsTozom(result[8]);//プロトコルとマスク
		    flagmask = fgmsTozom(result[9]);//フラグとマスク
		    
		    origin_list.add( SA + " " + DA + " " + sp + " " + dp +  " " + promask + " " + flagmask );
		    
		    break;		    
		    
		}
	    }	    
	    for(String variable : origin_list){//0,1,*のリストの表示
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

    public static String prmsTozom(String promask){
	
	String[] pm = promask.split("/");
	if(pm[1].equals("0xFF"))
	    return tenTotwo(Integer.decode(pm[0]),8);
	else if(pm[1].equals("0x00"))
	    return "********";
	else{
	    System.out.println("Protocol/Mask is not ClassBench Form");
	    System.exit(1);
	}
	return null;
    }
    public static String fgmsTozom(String flagmask){

	String[] fm = flagmask.split("/");
	StringBuilder ZOM = new StringBuilder(tenTotwo(Integer.decode(fm[0]),16));
	StringBuilder MASK = new StringBuilder(tenTotwo(Integer.decode(fm[1]),16));
	for(int i = 0; i < 16; i++){
	    if(MASK.charAt(i)=='0')
		ZOM.setCharAt(i,'*');
	}
	return ZOM.toString();
    }


    public static String tenTotwo(int num, int numwidth){//10進表記を2進表記に変換する
	int[] two;
	int i;
	String returnBits = "";
	two = new int[65535];

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

    public static String CIDRToZOM(String CIDR)
    {
	String[] ZO = CIDR.split("\\.|/") ;
	int i; 

	for(i=0;i<4;i++){
	    ZO[i]=tenTotwo(Integer.parseInt(ZO[i]),8);   
	}
	StringBuilder ZOM = new StringBuilder(ZO[0] + ZO[1] + ZO[2] + ZO[3]);
	int plefix=Integer.parseInt(ZO[4]);

	while(plefix < 32){
	    ZOM.setCharAt(plefix,'*');
	    plefix++;
	}
	return ZOM.toString();
    }
}
