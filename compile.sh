#クラスファイルを生成したくない物をコメントアウトしてください．

#2.ClassBenchで生成されたルール形式に評価型と評価パケット数を付与した物を生成する．
javac AddEPNToClassBench.java

#3.ClassBenchで生成されたルールセットとパケットをフィールドごとにポートはレンジルールでその他は0,1,＊形式で生成する．
javac ClassBenchToRange.java
javac ZOHeaderFromClassbench.java

#4.ClassBenchで生成されたルールセットとパケットをフィールドごとに0,1,＊形式で生成する．
#5.ClassBenchで生成されたルールリストとパケットを0,1,＊形式で生成する．
javac ClassBenchToZOM.java
javac ZOHeaderFromClassbench.java

#6.ClassBenchで生成されたルールセットとパケットをフィールドごとに0,1,＊形式で評価パケット数と評価型を付与して生成する.
#7.ClassBenchで生成されたルールセットとパケットを0,1,＊形式で評価パケット数と評価型を付与して生成する．
javac ClassBenchToZOM.java
javac ZOHeaderFromClassbench.java
javac AddEvaluationZOM.java
javac AddEtype.java

#8.ClassBenchで生成されたルールセットとパケットを用いた隣接リスト形式で生成する．
javac AddEtype.java
javac ClassBenchToAdjacencyList.java
javac NETClassBenchToAdjacencyList.java
