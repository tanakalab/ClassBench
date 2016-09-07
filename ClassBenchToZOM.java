import java.io.*;
import java.util.*;


public class ClassBenchToZOM {//ClassBench形式のルールリストを0,1,*のルールリストに変える
    public static void main(String[] args){
	
	if (args.length != 2) {
	    System.out.println("Arguments Error!\nUsage: $ java ClassBenchToZOM <rulelist> <outputfile>");
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
	    int[] sport = new int[2];
	    int[] dport = new int[2];
	    String promask;//プロトコルとマスク
	    String flagmask;//フラグとマスク
	    
	    while((rule = br.readLine()) != null){
		List<String> slist = new ArrayList<String>();
		List<String> dlist = new ArrayList<String>();
		String[] result = rule.split("\\s+|\\t+");
		StringBuilder sb = new StringBuilder(result[0]);
		sb.deleteCharAt(0);
		result[0]=sb.toString();

		switch(result.length){
		case 1:
		    SA = CIDRToZOM(result[0]);//0,1,*の送信元アドレス		    

		    origin_list.add( SA );

		    break;
		    
		case 2:
		    SA = CIDRToZOM(result[0]);//0,1,*の送信元アドレス
		    DA = CIDRToZOM(result[1]);//0,1,*の送信先アドレス
		   
		    origin_list.add( SA + " " + DA );
		       		    
		    break;
			
		case 5:
		    SA = CIDRToZOM(result[0]);//0,1,*の送信元アドレス
		    DA = CIDRToZOM(result[1]);//0,1,*の送信先アドレス
		    sport[0] = Integer.parseInt(result[2]);
		    sport[1] = Integer.parseInt(result[4]);
		    slist = RangeToZOM.rangeTozom(16,0,65535,sport[0],sport[1]);//送信元ポートレンジ（0,1,*のリスト）

		    for(String sp : slist){		    
			origin_list.add( SA + " " + DA + " " + sp );
		    }
		    
		    break;
		    
		case 8:		    
		    SA = CIDRToZOM(result[0]);//0,1,*の送信元アドレス
		    DA = CIDRToZOM(result[1]);//0,1,*の送信先アドレス
		    sport[0] = Integer.parseInt(result[2]);
		    sport[1] = Integer.parseInt(result[4]);
		    dport[0] = Integer.parseInt(result[5]);
		    dport[1] = Integer.parseInt(result[7]);
		    slist = RangeToZOM.rangeTozom(16,0,65535,sport[0],sport[1]);//送信元ポートレンジ（0,1,*のリスト）
		    dlist = RangeToZOM.rangeTozom(16,0,65535,dport[0],dport[1]);//送信先ポートレンジ（0,1,*のリスト）
		    
		    for(String sp : slist){
			for(String dp : dlist){
			    origin_list.add( SA + " " + DA + " " + sp + " " + dp );
			}
		    }
		    break;
		    
		case 9:
		    SA = CIDRToZOM(result[0]);//0,1,*の送信元アドレス
		    DA = CIDRToZOM(result[1]);//0,1,*の送信先アドレス
		    sport[0] = Integer.parseInt(result[2]);
		    sport[1] = Integer.parseInt(result[4]);
		    dport[0] = Integer.parseInt(result[5]);
		    dport[1] = Integer.parseInt(result[7]);
		    slist = RangeToZOM.rangeTozom(16,0,65535,sport[0],sport[1]);//送信元ポートレンジ（0,1,*のリスト）
		    dlist = RangeToZOM.rangeTozom(16,0,65535,dport[0],dport[1]);//送信先ポートレンジ（0,1,*のリスト）
		    promask = prmsTozom(result[8]);//プロトコルとマスク
		    
		    for(String sp : slist){
			for(String dp : dlist){
			    origin_list.add( SA + " " + DA + " " + sp + " " + dp +  " " + promask );
			}
		    }
		    break;
		    
		case 10:
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

	    
	    for(String ZOM : origin_list){//0,1,*のリストの表示
		//System.out.println(ZOM);
		bw.write(ZOM);
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
