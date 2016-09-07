# ClassBench ルール変換プログラム

ClassBench 

http://www.arl.wustl.edu/classbench/ 

で生成したルールを各提案手法の形式のルールリストとヘッダリストへ変換するプログラム群　　

1. ClassBenchで生成されたルールセットとパケットを出力する．     
例）  
ルール   : @131.10.42.40/32　　95.184.130.35/32　　0 : 65535　　1724 : 1724　　0x06/0xFF　　0x1000/0x1000       
パケット : 2198481430　　1709293566　　65535　　1712　　6　　4294967295      
　

2. ClassBenchで生成されたルールセットとパケットをフィールドごとに0,1,＊形式で生成する．       
例）  
ルール   :   
00100101010100100111110000110110　10011111110001111111001111110001　****************　0001010111111111　00000110     
パケット :   
00100101010100100111110000100001　10011111110001111101111000001001　0000000000000000　0000010111110101　00000110  
　　

3. ClassBenchで生成されたルールリストとパケットを0,1,＊形式で生成する．  
　例）  
ルール   :   
0010010101010010011111000011011010011111110001111111001111110001****************000101011111111100000110  
パケット :   
00100101010100100111110000100001100111111100011111011110000010010000000000000000000001011111010100000110  


4. ClassBenchで生成されたルールセットとパケットをフィールドごとに0,1,＊形式で評価パケット数と評価型を付与して生成する．    　
　(ルールの末尾のフィールドは評価パケット数)    
　例）  
ルール   : Accept　11011011101011000010000101111010　10101000100110010111000111100101　****************　7    
パケット : 00100101010100100111110000100001　10011111110001111101111000001001　0000000000000000     


5. ClassBenchで生成されたルールセットとパケットを0,1,＊形式で評価パケット数と評価型を付与して生成する．   
 (ルールの末尾のフィールドは評価パケット数)    
　例）  
ルール   : Accept　1101101110101100001000010111101010101000100110010111000111100101****************　7      
パケット : 00100101010100100111110000100001100111111100011111011110000010010000000000000000    


6. ClassBenchで生成されたルールセットとパケットを用いた隣接リスト形式で生成する．    
例）  
1    
0   
0   
3　4   　　


##Usage   
コマンド＄git clone git@github.com:tanakalab/ClassBench.gitを実行することで,カレントディレクトリにClassbenchというディレクトリが作成される.  
compile.shを実行することで,db_generatorとtrace_generatorを使う準備が整う.     
$ git clone git@github.com:tanakalab/ClassBench.git  
$ cd ClassBench    
$ sh compile.sh   
 
 1. classbench.shの引数としてルール数,フィールド数(1 ≦ n ≦ 6),パラメーターファイル、パケット数,ルールファイル名,パケットファイル名を入力し実行することで生成される．  
 例)  
$ sh classbench.sh 100 6 acl1_seed 100 Rule Header

 2. ZOMFieldList.shの引数としてルール数,フィールド数(1 ≦ n ≦ 6),パラメーターファイル、パケット数,ルールファイル名,パケットファイル名を入力し実行することで生成される．   
 例)  
$ sh ZOMFieldList.sh 100 5 acl1_seed 100 Rule Header

 3. ZOMList.shの引数としてルール数,フィールド数(1 ≦ n ≦ 6),パラメーターファイル、パケット数,ルールファイル名,パケットファイル名を入力し実行することで生成される．  
 例)  
$ sh ZOMList.sh 100 5 acl1_seed 100 Rule Header

 4. EvalZOMFieldList.shの引数としてルール数,フィールド数(1 ≦ n ≦ 6),パラメーターファイル、パケット数,ルールファイル名,パケットファイル名,評価型(Accept)の割合を入力し実行することで生成される．  
 例)  
$ sh EvalZOMFieldList.sh 100 3 acl1_seed 100 Rule Header 0.5

 5. EvalZOMList.shの引数としてルール数,フィールド数(1 ≦ n ≦ 6),パラメーターファイル、パケット数,ルールファイル名,パケットファイル名,評価型(Accept)の割合を入力し実行することで生成される．  
 例)  
$ sh EvalZOMList.sh 100 3 acl1_seed 100 Rule Header 0.5

 6. AdjacencyList.shの引数としてルール数,フィールド数(1 ≦ n ≦ 6),パラメーターファイル、パケット数,ルールファイル名,評価型(Accept)の割合を入力することで生成される．  
 例）  
$ sh AdjacencyList.sh 100 6 acl1_seed 100 Rule 0.5
 
