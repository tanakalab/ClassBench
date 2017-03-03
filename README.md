# ClassBench ルール変換プログラム

ClassBench 

http://www.arl.wustl.edu/classbench/ 

で生成したルールを各提案手法の形式のルールリストとヘッダリストへ変換するプログラム群  
ClassBenchから下記の1~7の形式の中の一つを生成することができる．    

##Usage   

git clone git@github.com:tanakalab/ClassBench.gitを実行することで,カレントディレクトリにClassbenchというディレクトリが作成される.  
作成されたディレクトリに移動しdownloads.shを実行することで,db_generatorとtrace_generatorを使う準備が整う.     
compile.shによってそれぞれのプログラムをコンパイルをする．  
そして後は自分の生成したいルールセットに合わせて下記の1~7のどれかのスクリプトを実行する．  

コマンド  

$ git clone git@github.com:tanakalab/ClassBench.git  
$ cd ClassBench    
$ sh downloads.sh   
$ sh compile.sh  
    
    
    
1. ClassBenchの形式で生成する．        
例）    
ルール   : @131.10.42.40/32　95.184.130.35/32　0 : 65535　1724 : 1724　0x06/0xFF　0x1000/0x1000     
  　    
classbench.shの引数として生成したいフィールド,ルール数,パラメーターファイル,パケット数(１以上の整数),ルールファイル名,パケットファイル名を入力し実行することで生成される．    
 例)     
$ sh classbench.sh SA DA SP DP PROT FLAG 300 acl5_seed 2 Rule Header       　 
        
      
    
      
       
        
          
2. ClassBenchで生成されたルールセットに評価型と評価パケット数を付与した形式で生成する．     
例）  
ルール   : Accept　@131.10.42.40/32　95.184.130.35/32　0 : 65535　1724 : 1724　0x06/0xFF　0x1000/0x1000　4   
　  
AddEPNToClassBench.shの引数として生成したいフィールド,ルール数,パラメーターファイル,パケット数(１以上の整数),ルールファイル名,評価型(Accept)の割合を入力し実行することで生成される．  
 例)  
$ sh AddEPNToClassBench.sh SA DA SP DP PROT FLAG 300 acl5_seed 2 Rule 0.5    　
   　　
   　　　
   　　
   　　
    　　
　　　　    　　
3. ClassBenchで生成されたルールセットとパケットをフィールドごとにポートはレンジルールでその他は0,1,＊形式で生成する．  
例)                               
<p>ルール   : 0110010011011111011111**********　0-65535　61900-61909　00000110</p>  
パケット : 01100100110111110111111101100001　1111111111111111　1111000111001100　61900　00000110    
    
 ZOMRangeFieldList.shの引数として生成したいフィールド,ルール数,パラメーターファイル,パケット数(１以上の整数),ルールファイル名,パケットファイル名を入力し実行することで生成される．(-priorオプションとファイルを指定することにより，各パケットの最優先ルールのリストを指定されたファイルに出力する事ができる．)  
 例)  
  $ sh ZOMRangeFieldList.sh SA DA SP DP PROT FLAG 400 fw1_seed 1 Rule Header     　　             
  ($ sh ZOMRangeFieldList.sh -prior MostPriorRuleList SA DA SP DP PROT FLAG 400 fw1_seed 1 Rule Header)     
              
    　　　  　　
    　
     
     
4. ClassBenchで生成されたルールセットとパケットをフィールドごとに0,1,＊形式で生成する．       
例)                          
<p>ルール   : 0010010101010010011111000011011*　10011111110001111111001111110001　****************</p>     
パケット : 00100101010100100111110000100001　10011111110001111101111000001001　0000000000000000    
    
 ZOMFieldList.shの引数として生成したいフィールド,ルール数,パラメーターファイル,パケット数(１以上の整数),ルールファイル名,パケットファイル名を入力し実行することで生成される．(-priorオプションとファイルを指定することにより，各パケットの最優先ルールのリストを指定されたファイルに出力する事ができる．)        
 例)  
  $ sh ZOMFieldList.sh SA DA SP 1000 acl4_seed 1 Rule Header     　　            
  ($ sh ZOMFieldList.sh -prior MostPriorRuleList SA DA SP 1000 acl4_seed 1 Rule Header)         
       
       
       
       
5. ClassBenchで生成されたルールリストとパケットを0,1,＊形式で生成する．  
例）                     
<p>ルール   : 01110101111011110011101001010000****************00010011100011**00000110</p>
パケット : 100111111100011111011110000010010000000000000000000001011111010100000110  
     
 ZOMList.shの引数として生成したいフィールド,ルール数,パラメーターファイル,パケット数(１以上の整数),ルールファイル名,パケットファイル名を入力し実行することで生成される．(-priorオプションとファイルを指定することにより，各パケットの最優先ルールのリストを指定されたファイルに出力する事ができる．)      
 例)   
  $ sh ZOMList.sh DA SP DP PROT 500 ipc1_seed 1 Rule Header                  
  ($ sh ZOMList.sh -prior MostPriorRuleList DA SP DP PROT 500 ipc1_seed 1 Rule Header)     　　　  　　　
           
      
             
             
               
6. ClassBenchで生成されたルールセットとパケットをフィールドごとに0,1,＊形式で評価パケット数と評価型を付与して生成する．    　
　(ルールの末尾のフィールドは評価パケット数)    
例)                                       
<p>ルール   : Accept　00000010100001010000011101111***　10101110001100111010101*********　****************　2</p>    
パケット : 00100101010100100111110000100001　10011111110001111101111000001001　0000000000000000     
  
 EvalZOMFieldList.shの引数として生成したいフィールド,ルール数,パラメーターファイル,パケット数(１以上の整数),ルールファイル名,パケットファイル名,評価型(Accept)の割合を入力し実行することで生成される．  
 例)  
$ sh EvalZOMFieldList.sh SA DA SP 1000 fw3_seed 2 Rule Header 0.7      　　　　　　
　　　　　　　　
    　　　　　　
     　　　　　　　
    　　　　　　　　
7. ClassBenchで生成されたルールセットとパケットを0,1,＊形式で評価パケット数と評価型を付与して生成する．   
 (ルールの末尾のフィールドは評価パケット数)    
例）                   
<p>ルール   : Deny　101000010110101011110111********00100110011011000111111101010011****************　2</p>      
パケット : 00100101010100100111110000100001100111111100011111011110000010010000000000000000    
  
 EvalZOMList.shの引数としてルール数,フィールド数(1 ≦ n ≦ 6),パラメーターファイル,パケット数(１以上の整数),ルールファイル名,パケットファイル名,評価型(Accept)の割合を入力し実行することで生成される．  
 例)                     
$ sh EvalZOMList.sh SA DA DP 100 fw5_seed 1 Rule Header 0.3     　　　　　　
　　　　　　　
    　　　　　　　
    　　　　　　　
    　　　　　　　　
8. ClassBenchで生成されたルールセットとパケットを用いた隣接リスト形式で生成する．    
例）  
1    
0   
0   
3　4   　　

 AdjacencyList.shの引数として生成したいフィールド,ルール数,パラメーターファイル、パケット数(1以上の整数),ルールファイル名,評価型(Accept)の割合を入力することで生成される．ファイル名の先頭にNET(Non Evaluation Type)と書いてある物は評価型が付与されないルールを扱う．なのでNETと書いてある場合は評価型の割合は引数として渡さない．  
 例）  
$ sh AdjacencyList.sh SA DA SP DP PROT FLAG 100 acl1_seed 1 Rule 0.5   
  
 例）  
$ sh NETAdjacencyList.sh SA DA SP DP PROT FLAG 100 acl1_seed 1 Rule
  
※パラメーターファイル  
(acl1_seed　acl2_seed　acl3_seed　acl4_seed　acl5_seed　fw1_seed　fw2_seed　fw3_seed　fw4_seed　fw5_seed　ipc1_seed　ipc2_seed)  

※パケット数はルール数の何倍のパケットが欲しいのかを指定する．  

※フィールド  
(SA(source address)　DA(destination address)　SP(source port)　DP(destination port)　PROT(protocol)　FLAG(flags))  
※フィールドを入力するときはこの順番からいらないフィールドを削った状態の物を引数とする．
