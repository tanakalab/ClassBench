import java.io.*;
import java.util.*;


public class ClassBenchToZOM {//ClassBench形式のルールリストを0,1,*のルールリストに変える
    public static void main(String[] args){
	
	if (args.length < 3 || args.length > 8 ) {
	    System.out.println("Arguments Error!\nUsage: $ java ClassBenchToZOM <rulelist> <outputfile> [fields] ");
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
	    List<String> ZOM = new ArrayList<String>();
	    List<String> x = new ArrayList<String>();
	    List<String> superZOM = new ArrayList<String>();
	    int SPadjust = Arrays.asList(args).contains("SP") ? 2 : 0;
	    int DPadjust = Arrays.asList(args).contains("DP") ? 2 : 0;
	    int[] sport = new int[2];
	    int[] dport = new int[2];
	    			    
	    while((rule = br.readLine()) != null){
		
		String[] result = rule.split("\\s+|\\t+");
		if( Arrays.asList(args).contains("SA") ){
		    StringBuilder sb = new StringBuilder(result[0]);
		    sb.deleteCharAt(0);
		    result[0]=sb.toString();
		}
		switch(args.length){
		case 3:
		    ZOM.clear();
		    switch(args[2]){
		    case "SA":
			ZOM.add( CIDRToZOM(result[0]) );//0,1,*の送信元アドレス		    
			break;
		    case "DA":
			ZOM.add( CIDRToZOM(result[0]) );//0,1,*の送信先アドレス
			break;
		    case "SP":
			sport[0] = Integer.parseInt(result[0]);
			sport[1] = Integer.parseInt(result[2]);
			ZOM = RangeToZOM.rangeTozom(16,0,65535,sport[0],sport[1]);//送信元ポートレンジ（0,1,*のリスト）
			break;
		    case "DP":
			dport[0] = Integer.parseInt(result[0]);
			dport[1] = Integer.parseInt(result[2]);
			ZOM = RangeToZOM.rangeTozom(16,0,65535,dport[0],dport[1]);//送信先ポートレンジ（0,1,*のリスト)
			break;
		    case "PROT":
			ZOM.add( prmsTozom(result[0]) );//プロトコルとマスク
			break;
		    }
		    for(String list : ZOM)
			origin_list.add( list );
		    break;		    
		    
		case 4:
		    for(int i = 2; i < 4 ;i++){
			superZOM.clear();
			x.clear();
			switch(args[i]){
			case "SA":
			    x.add( CIDRToZOM(result[i-2]) );//0,1,*の送信元アドレス
			    break;
			case "DA":
			    x.add( CIDRToZOM(result[i-2]) );//0,1,*の送信先アドレス
			    break;
			case "SP":
			    sport[0] = Integer.parseInt(result[i-2]);
			    sport[1] = Integer.parseInt(result[i]);
			    x = RangeToZOM.rangeTozom(16,0,65535,sport[0],sport[1]);//送信元ポートレンジ（0,1,*のリスト）
			    break;
			case "DP":
			    dport[0] = Integer.parseInt(result[i-2 + SPadjust]);
			    dport[1] = Integer.parseInt(result[i + SPadjust]);
			    x = RangeToZOM.rangeTozom(16,0,65535,dport[0],dport[1]);//送信先ポートレンジ（0,1,*のリスト)
			    break;
			case "PROT":
			    x.add( prmsTozom(result[i-2 + SPadjust + DPadjust]) );//プロトコルとマスク
			    break;
			case "FLAG":
			    x.add( fgmsTozom(result[i-2 + SPadjust + DPadjust]) );//フラグとマスク
			    break;
			}
			if(i == 2)
			    ZOM.addAll( x );
			else{
			    for(String str1 : ZOM){
				for(String str2 : x){
				    superZOM.add( str1 + " " + str2 );
				}
			    }
			    ZOM.clear();
			    if(i!=3)
				ZOM.addAll( superZOM );
			}	
		    }
		    for(String list : superZOM)
			origin_list.add( list );
		    break;
		    
		case 5:
		    for(int i = 2; i < 5 ;i++){
			superZOM.clear();			
			x.clear();
			switch(args[i]){
			case "SA":
			    x.add( CIDRToZOM(result[i-2]) );//0,1,*の送信元アドレス
			    break;
			case "DA":
			    x.add( CIDRToZOM(result[i-2]) );//0,1,*の送信先アドレス
			    break;
			case "SP":
			    sport[0] = Integer.parseInt(result[i-2]);
			    sport[1] = Integer.parseInt(result[i]);
			    x = RangeToZOM.rangeTozom(16,0,65535,sport[0],sport[1]);//送信元ポートレンジ（0,1,*のリスト）
			    break;
			case "DP":
			    dport[0] = Integer.parseInt(result[i-2 + SPadjust]);
			    dport[1] = Integer.parseInt(result[i + SPadjust]);
			    x = RangeToZOM.rangeTozom(16,0,65535,dport[0],dport[1]);//送信先ポートレンジ（0,1,*のリスト)
			    break;
			case "PROT":
			    x.add( prmsTozom(result[i-2 + SPadjust + DPadjust]) );//プロトコルとマスク
			    break;
			case "FLAG":
			    x.add( fgmsTozom(result[i-2 + SPadjust + DPadjust]) );//フラグとマスク
			    break;
			}
			if(i == 2)
			    ZOM.addAll( x );
			else{
			    for(String str1 : ZOM){
				for(String str2 : x){
				    superZOM.add( str1 + " " + str2 );
				}
			    }
			    ZOM.clear();
			    if(i!=4)
				ZOM.addAll( superZOM );
			}
		    }
		    for(String list : superZOM)
			origin_list.add( list );
		    break;
		    
		case 6:		    
		    for(int i = 2; i < 6 ;i++){
			superZOM.clear();
			x.clear();
			switch(args[i]){
			case "SA":
			    x.add( CIDRToZOM(result[i-2]) );//0,1,*の送信元アドレス
			    break;
			case "DA":
			    x.add( CIDRToZOM(result[i-2]) );//0,1,*の送信先アドレス
			    break;
			case "SP":
			    sport[0] = Integer.parseInt(result[i-2]);
			    sport[1] = Integer.parseInt(result[i]);
			    x = RangeToZOM.rangeTozom(16,0,65535,sport[0],sport[1]);//送信元ポートレンジ（0,1,*のリスト）
			    break;
			case "DP":
			    dport[0] = Integer.parseInt(result[i-2 + SPadjust]);
			    dport[1] = Integer.parseInt(result[i + SPadjust]);
			    x = RangeToZOM.rangeTozom(16,0,65535,dport[0],dport[1]);//送信先ポートレンジ（0,1,*のリスト)
			    break;
			case "PROT":
			    x.add( prmsTozom(result[i-2 + SPadjust + DPadjust]) );//プロトコルとマスク
			    break;
			case "FLAG":
			    x.add( fgmsTozom(result[i-2 + SPadjust + DPadjust]) );//フラグとマスク
			    break;
			}
			if(i == 2)
			    ZOM.addAll( x );
			else{
			    for(String str1 : ZOM){
				for(String str2 : x){
				    superZOM.add( str1 + " " + str2 );
				}
			    }
			    ZOM.clear();
			    if(i!=5)
				ZOM.addAll( superZOM );
			}
		    }
		    for(String list : superZOM)
			origin_list.add( list );
		    break;
		    
		case 7:
		    for(int i = 2; i < 7 ;i++){
		    	superZOM.clear();
		    	x.clear();
		    	//	ZOM.clear();
		    	switch(args[i]){
		    	case "SA":
		    	    x.add( CIDRToZOM(result[i-2]) );//0,1,*の送信元アドレス
		    	    break;
		    	case "DA":
		    	    x.add( CIDRToZOM(result[i-2]) );//0,1,*の送信先アドレス
		    	    break;
		    	case "SP":
		    	    sport[0] = Integer.parseInt(result[i-2]);
		    	    sport[1] = Integer.parseInt(result[i]);
		    	    x = RangeToZOM.rangeTozom(16,0,65535,sport[0],sport[1]);//送信元ポートレンジ（0,1,*のリスト）
		    	    break;
		    	case "DP":
		    	    dport[0] = Integer.parseInt(result[i-2 + SPadjust]);
		    	    dport[1] = Integer.parseInt(result[i + SPadjust]);
		    	    x = RangeToZOM.rangeTozom(16,0,65535,dport[0],dport[1]);//送信先ポートレンジ（0,1,*のリスト)
		    	    break;
		    	case "PROT":
		    	    x.add( prmsTozom(result[i-2 + SPadjust + DPadjust]) );//プロトコルとマスク
		    	    break;
		    	case "FLAG":
		    	    x.add( fgmsTozom(result[i-2 + SPadjust + DPadjust]) );//フラグとマスク
		    	    break;
		    	}
		    	if(i == 2)
		    	    ZOM.addAll( x );
			
		    	else{								
		    	    for(String str1 : ZOM){
		    		for(String str2 : x){
		    		    superZOM.add( str1 + " " + str2 );
		    		}
		    	    }
		    	    ZOM.clear();
		    	    if(i!=6)
		    		ZOM.addAll( superZOM );
		    	}
		    }
		    for(String list : superZOM){
		    	origin_list.add( list );
		    }
		    break;
		    
		    
		case 8:
		    String SA,DA,promask,flagmask;
		    List<String> slist = new ArrayList<String>();
		    List<String> dlist = new ArrayList<String>();
		    
		    SA = CIDRToZOM(result[0]);//0,1,*の送信元アドレス
		    DA = CIDRToZOM(result[1]);//0,1,*の送信先アドレス
		    sport[0] = Integer.parseInt(result[2]);
		    sport[1] = Integer.parseInt(result[4]);
		    dport[0] = Integer.parseInt(result[5]);
		    dport[1] = Integer.parseInt(result[7]);
		    slist = RangeToZOM.rangeTozom(16,0,65535,sport[0],sport[1]);//送信元ポートレンジ（0,1,*のリスト）
		    dlist = RangeToZOM.rangeTozom(16,0,65535,dport[0],dport[1]);//送信先ポートレンジ（0,1,*のリスト）
		    promask = prmsTozom(result[8]);//プロトコルとマスク
		    flagmask = fgmsTozom(result[9]);//フラグとマスク
		    
		    for(String sp : slist){
			for(String dp : dlist){
			    origin_list.add( SA + " " + DA + " " + sp + " " + dp +  " " + promask + " " + flagmask );
			}
		    }
		    break;
		    
		}
	    }

	    for(String str : origin_list){//0,1,*のリストの表示
		bw.write(str);
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
	//	return (tenTotwo(Integer.decode(pm[0]),8) + tenTotwo(Integer.decode(pm[1]),8));
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
	//	return (tenTotwo(Integer.decode(fm[0]),16) + tenTotwo(Integer.decode(fm[1]),16));
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
