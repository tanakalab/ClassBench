# ClassBench ルール変換プログラム

ClassBench 

http://www.arl.wustl.edu/classbench/ 

で生成したルールを各提案手法の形式のルールへ変換するプログラム群

##目的


* ClassBenchで生成されたルールセットとパケットを生成する．　　
例）ルール   : @131.10.42.40/32	 95.184.130.35/32	 0 : 65535	 1724 : 1724	 0x06/0xFF	 0x1000/0x1000　　　　
　　パケット : 2198481430 	1709293566	 65535	 1712	 6 	4294967295    
　

* ClassBenchで生成されたルールセットとパケットをフィールドごとに0,1,＊形式で生成する．　 　    
例）ルール   : 00100101010100100111110000110110  10011111110001111111001111110001  **************** 0001010111111111  00000110  ***1************　
　　パケット :     
　　

* ClassBenchで生成されたルールセットとパケットをフィールドごとに0,1,＊形式で生成する．　　
 　さらに,0,1を＊に,＊を０,１に確率Pで変換する.
　例）  
   生成されるルールのアドレスは103.155.166.80/27のようにCIDR表記で表される.  
   103.155.166.80/27はプレフィックス長が27なのでサブネットマスクは11111111111111111111111111100000になる.  
   よって  
     01100111100110111010011001010000  
     11111111111111111111111111100000  
     01100111100110111010011001010000  
   より,
   0,1,* で表すと  
      011001111001101110100110010*****
   となる.ここで*は don't care を表す.  
   さらに,任意のマスクを含むルールにしたいので,0,1に0,1をマスクに変換する.
   つまり011001111001101110100110010*****を  
   01＊00111＊0011＊0111＊0＊10＊011001＊010＊00のように加工する.  
* ClassBenchで生成されたルールセットとパケットを0,1,＊形式で生成する．  　
* ClassBenchで生成されたルールセットとパケットをフィールドごとに0,1,＊形式で評価パケット数と評価型を付与して生成する．　　
* ClassBenchで生成されたルールセットとパケットを0,1,＊形式で評価パケット数と評価型を付与して生成する．　
* ClassBenchで生成されたルールセットとパケットを用いた隣接リスト形式で生成する．

##Usage   
コマンド＄git clone git@github.com:tanakalab/ClassBench.gitを実行することで,カレントディレクトリにClassbenchというディレクトリが作成される.   
作成されたClassbenchというディレクトリに移動する．    
compile.shを実行することで,db_generatorとtrace_generatorを使う準備が整う.      
addlessRuleAndPacketGenerator.shのパラメーターを好きなように変えて走らせる.      
０,１,＊で構成されたルールリストと2進表記のパケットが生成される.   

$ git clone git@github.com:tanakalab/ClassBench.git  
$ cd ClassBench    
$ ./compile.sh   
$ ./addlessRuleAndPacketGenerator.sh  (input parameter file) (number of filters) (smoothness(0~64)) (address scope(-1~1)) (application scope(-1~1)) (probability(0~1)) (output Rule_filename) (Pareto parameter a) (Pareto parameter b) (scale) (output Packet_filename)  

##example:   
$./addlessRuleAndPacketGnerator.sh acl1_seed 10000 2 0.5 -0.1 Rule 1 0.1 10 Packet  
