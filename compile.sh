#クラスファイルを生成したくない物をコメントアウトしてください．

#2.ClassBenchで生成されたルール形式に評価型と評価パケット数を付与した物を生成する．
javac AddEPNToClassBench.java

#3.ClassBenchで生成されたルールセットとパケットをフィールドごとにポートはレンジルールでその他は0,1,＊形式で生成する．
javac ClassBenchToRange.java
javac ZORangeHeaderFromClassBench.java

#4.ClassBenchで生成されたルールセットとパケットをフィールドごとに0,1,＊形式で生成する．
#5.ClassBenchで生成されたルールリストとパケットを0,1,＊形式で生成する．
#6.ClassBenchで生成されたルールセットとパケットをフィールドごとに0,1,＊形式で評価パケット数と評価型を付与して生成する.
#7.ClassBenchで生成されたルールセットとパケットを0,1,＊形式で評価パケット数と評価型を付与して生成する．
javac ClassBenchToZOM.java
javac ZOHeaderFromClassBench.java
javac AddEvaluationZOM.java
javac AddEtype.java

#8.ClassBenchで生成されたルールセットとパケットを用いた隣接リスト形式で生成する．
#javac AddEtype.java
javac ClassBenchToAdjacencyList.java
javac NETClassBenchToAdjacencyList.java

#9.ClassBenchで生成されたルールセットをルール番号と0,1,＊形式で，さらに隣接リスト形式で生成し，パケットをパケットの個数と0,1,＊形式で生成する．
javac ZOHeaderAndDistribution.java
javac GeneralAdjacencyList.java
javac NumberedZOM.java

## 各ヘッダの最優先ルールファイル生成
javac makeZOMFieldMostPriorRule.java
javac makeZOMRangeMostPriorRule.java
