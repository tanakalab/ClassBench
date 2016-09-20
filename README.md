# ClassBench ルール変換プログラム

ClassBench 

http://www.arl.wustl.edu/classbench/ 

で生成したルールを各提案手法の形式のルールリストとヘッダリストへ変換するプログラム群  
ClassBenchから下記の1~7の形式の中の一つを生成することができる．    

1. ClassBenchで生成されたルールセットとパケットを出力する．     
例）  
ルール   : @131.10.42.40/32　　95.184.130.35/32　　0 : 65535　　1724 : 1724　　0x06/0xFF　　0x1000/0x1000       
パケット : 2198481430　　1709293566　　65535　　1712　　6　　4294967295      
　
  
2. ClassBenchで生成されたルールセットとパケットをフィールドごとにポートはレンジルールでその他は0,1,＊形式で生成する．  
例）  
ルール   :<p>0110010011011111011111**********　10100101011111101101000100100011　0-65535　61900-61909　00000110</p>  
パケット :  
01100100110111110111111101100001　10111010111100110000001111111111　1111111111111111　0000000000000000　00000110    
  
  
3. ClassBenchで生成されたルールセットとパケットをフィールドごとに0,1,＊形式で生成する．       
例）  
ルール   :  
<p>0010010101010010011111000011011*　10011111110001111111001111110001　****************　0001010111111111　00000110</p>     
パケット :  
00100101010100100111110000100001　10011111110001111101111000001001　0000000000000000　0000010111110101　00000110    
  
  
 
4. ClassBenchで生成されたルールリストとパケットを0,1,＊形式で生成する．  
　例）  
ルール   :   
<p>100101111100000010010000101010**01110101111011110011101001010000****************00010011100011**00000110</p>
パケット :   
00100101010100100111110000100001100111111100011111011110000010010000000000000000000001011111010100000110  
  
  
5. ClassBenchで生成されたルールセットとパケットをフィールドごとに0,1,＊形式で評価パケット数と評価型を付与して生成する．    　
　(ルールの末尾のフィールドは評価パケット数)    
　例）  
<p>ルール   : Accept　00000010100001010000011101111***　10101110001100111010101*********　****************　2</p>    
パケット : 00100101010100100111110000100001　10011111110001111101111000001001　0000000000000000     
  
  
6. ClassBenchで生成されたルールセットとパケットを0,1,＊形式で評価パケット数と評価型を付与して生成する．   
 (ルールの末尾のフィールドは評価パケット数)    
　例）  
<p>ルール   : Deny　101000010110101011110111********00100110011011000111111101010011****************　2</p>      
パケット : 00100101010100100111110000100001100111111100011111011110000010010000000000000000    
  
  
7. ClassBenchで生成されたルールセットとパケットを用いた隣接リスト形式で生成する．    
例）  
1    
0   
0   
3　4   　　


##Usage   

git clone git@github.com:tanakalab/ClassBench.gitを実行することで,カレントディレクトリにClassbenchというディレクトリが作成される.  
作成されたディレクトリに移動しcompile.shを実行することで,db_generatorとtrace_generatorを使う準備が整う.     
そして後は自分の生成したいルールセットに合わせて下記の1~7のどれかのスクリプトを実行する．  
下記の1~7は上記で示した1~7に対応している．

パラメーターファイル  
(acl1_seed　acl3_seed　acl5_seed　fw2_seed　fw4_seed　ipc1_seed　acl2_seed　acl4_seed　fw1_seed　fw3_seed　fw5_seed　ipc2_seed)  

コマンド  

$ git clone git@github.com:tanakalab/ClassBench.git  
$ cd ClassBench    
$ sh compile.sh   
  
  
 1. classbench.shの引数としてルール数,フィールド数(1 ≦ n ≦ 6),パラメーターファイル、パケット数,ルールファイル名,パケットファイル名を入力し実行することで生成される．  
 例)  
$ sh classbench.sh 100 6 acl1_seed 100 Rule Header
  
  
 2. ZOMRangeFieldList.shの引数としてルール数,フィールド数(1 ≦ n ≦ 6),パラメーターファイル、パケット数,ルールファイル名,パケットファイル名を入力し実行することで生成される．  
 例)  
$ sh ZOMRangeFieldList.sh 100 5 acl1_seed 100 Rule Header
  
  
 3. ZOMFieldList.shの引数としてルール数,フィールド数(1 ≦ n ≦ 6),パラメーターファイル、パケット数,ルールファイル名,パケットファイル名を入力し実行することで生成される．   
 例)  
$ sh ZOMFieldList.sh 100 5 acl1_seed 100 Rule Header
  
  
 4. ZOMList.shの引数としてルール数,フィールド数(1 ≦ n ≦ 6),パラメーターファイル、パケット数,ルールファイル名,パケットファイル名を入力し実行することで生成される．  
 例)  
$ sh ZOMList.sh 100 5 acl1_seed 100 Rule Header
  
  
 5. EvalZOMFieldList.shの引数としてルール数,フィールド数(1 ≦ n ≦ 6),パラメーターファイル、パケット数,ルールファイル名,パケットファイル名,評価型(Accept)の割合を入力し実行することで生成される．  
 例)  
$ sh EvalZOMFieldList.sh 100 3 acl1_seed 100 Rule Header 0.5
  
  
 6. EvalZOMList.shの引数としてルール数,フィールド数(1 ≦ n ≦ 6),パラメーターファイル、パケット数,ルールファイル名,パケットファイル名,評価型(Accept)の割合を入力し実行することで生成される．  
 例)  
$ sh EvalZOMList.sh 100 3 acl1_seed 100 Rule Header 0.5
  
  
 7. AdjacencyList.shの引数としてルール数,フィールド数(1 ≦ n ≦ 6),パラメーターファイル、パケット数,ルールファイル名,評価型(Accept)の割合を入力することで生成される．  
 例）  
$ sh AdjacencyList.sh 100 6 acl1_seed 100 Rule 0.5
 
