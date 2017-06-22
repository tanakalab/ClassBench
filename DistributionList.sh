###############################################################################################

# input form is
# sh DistributionList.sh [fields that you wanna make] RuleNumber seed_file packetNumber(scale factor) RuleFileName HeaderFileName AdjacencyListFileName EvaluationProbability

# input example ::
# $ sh DistributionList.sh SA DA SP DP PROT FLAG 500 ipc1_seed 1 Rule Header Adjacency probability

################################################################################################

# if [ $1 = "-prior" ]; then
#     if [ $2 == "SA" ] || [ $2 == "DA" ] || [ $2 == "SP" ] || [ $2 == "DP" ] || [ $2 == "PROT" ] || [ $2 == "FLAG" ]; then
# 	echo "error : Specify a MostPriorRule file."
# 	exit
#     fi
#     adjust=2
#else
    adjust=0
#fi

#MostPriorRuleFile=$2

eval first='$'{`expr 1 + $adjust`}
eval second='$'{`expr 2 + $adjust`}
eval third='$'{`expr 3 + $adjust`}
eval fourth='$'{`expr 4 + $adjust`}
eval fifth='$'{`expr 5 + $adjust`}
eval sixth='$'{`expr 6 + $adjust`}

eval ruleNum='$'{`expr $# - 6`}
eval seed_file='$'{`expr $# - 5`}
eval headerNum='$'{`expr $# - 4`}
eval ruleName='$'{`expr $# - 3`}
eval headerName='$'{`expr $# - 2`}
eval adjacencyListName='$'{`expr $# - 1`}
eval probability='$'{$#}
defaultRule=""


cd db_generator
./db_generator -bc ../parameter_files/$seed_file $ruleNum 2 0.5 -0.1 MyFilters

cd ../trace_generator
./trace_generator 1 0.1 $headerNum ../db_generator/MyFilters
cd ..

if [ $first == "SA" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $1}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $1}' > c
    defaultRule="${defaultRule}@0.0.0.0\/0\t*"
    #defaultRule="${defaultRule}\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
fi

if [ $first == "DA" ]  ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $2}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $2}' > c
    defaultRule="${defaultRule}0\.0\.0\.0\/0\t*"
    #defaultRule="${defaultRule}\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
elif [ $second == "DA" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $2}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $2}' > a
    paste c a > b
    mv b c
    defaultRule="${defaultRule}0.0.0.0\/0\t*"
    #defaultRule="${defaultRule}\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
fi

if [ $first == "SP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $3}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $3}' > c
    defaultRule="${defaultRule}0 : 65535\t*"
    #defaultRule="${defaultRule}\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
elif [  $second == "SP" ] || [ $third == "SP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $3}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $3}' > a
    paste c a > b
    mv b c
    defaultRule="${defaultRule}0 : 65535\t*"
    #defaultRule="${defaultRule}\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
fi

if [ $first == "DP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $4}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $4}' > c
    defaultRule="${defaultRule}0 : 65535\t*"
    #defaultRule="${defaultRule}\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
elif [ $second == "DP" ] || [ $third == "DP" ] || [ $fourth == "DP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $4}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $4}' > a
    paste c a > b
    mv b c
    defaultRule="${defaultRule}0 : 65535\t*"
    #defaultRule="${defaultRule}\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
fi

if [ $first == "PROT" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $5}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $5}' > c
    defaultRule="${defaultRule}0x00\/0x00\t*"
    #defaultRule="${defaultRule}\*\*\*\*\*\*\*\*"
    if [ $second == "FLAG" ] ; then
	cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $6}' > y
	paste x y > z
	mv z x
	cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $6}' > a
	paste c a > b
	mv b c
	defaultRule="${defaultRule}0x0000\/0x0000\t*"
	#defaultRule="${defaultRule}\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
    fi 
elif [ $second == "PROT" ] || [ $third == "PROT" ] || [ $fourth == "PROT" ] || [ $fifth == "PROT" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $5}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $5}' > a
    paste c a > b
    mv b c
    defaultRule="${defaultRule}0x00\/0x00\t*"
    #defaultRule="${defaultRule}\*\*\*\*\*\*\*\*"
    if [ $third == "FLAG" ] || [ $fourth == "FLAG" ] || [ $fifth == "FLAG" ] || [ $sixth == "FLAG" ] ; then
	cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $6}' > y
	paste x y > z
	mv z x
	cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $6}' > a
	paste c a > b
	mv b c
	defaultRule="${defaultRule}0x0000\/0x0000\t*"
	#defaultRule="${defaultRule}\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
    fi    
elif [ $first == "FLAG" ] || [ $second == "FLAG" ] || [ $third == "FLAG" ] || [ $fourth == "FLAG" ] || [ $fifth == "FLAG" ] ; then
    echo "error : FLAG needs PROT argument."
    exit
fi

sed -e "/$defaultRule/d" < x > y #デフォルトルール消去

#cp x original_RuleList
mv y x

case `expr $# - $adjust` in
    8) java NumberedZOM x $ruleName $first
	 ;;
    9) java NumberedZOM x $ruleName $first $second
	 ;;
    10) java NumberedZOM x $ruleName $first $second $third
	 ;;
    11) java NumberedZOM x $ruleName $first $second $third $fourth
	 ;;
    12) java NumberedZOM x $ruleName $first $second $third $fourth $fifth
	  ;;
    13) java NumberedZOM x $ruleName $first $second $third $fourth $fifth $sixth
          ;;
esac

case `expr $# - $adjust` in
    8) java ZOHeaderAndDistribution c $headerName $first
	 ;;
    9) java ZOHeaderAndDistribution c $headerName $first $second
	 ;;
    10) java ZOHeaderAndDistribution c $headerName $first $second $third
	 ;;
    11) java ZOHeaderAndDistribution c $headerName $first $second $third $fourth
	 ;;
    12) java ZOHeaderAndDistribution c $headerName $first $second $third $fourth $fifth
	  ;;
    13) java ZOHeaderAndDistribution c $headerName $first $second $third $fourth $fifth $sixth
	  ;;
esac

eval  java AddEtype x w $probability
case `expr $# - $adjust` in
    8) java GeneralAdjacencyList w c $adjacencyListName $first
	 ;;
    9) java GeneralAdjacencyList w c $adjacencyListName $first $second
	 ;;
    10) java GeneralAdjacencyList w c $adjacencyListName $first $second $third
	 ;;
    11) java GeneralAdjacencyList w c $adjacencyListName $first $second $third $fourth
	 ;;
    12) java GeneralAdjacencyList w c $adjacencyListName $first $second $third $fourth $fifth
	  ;;
    13) java GeneralAdjacencyList w c $adjacencyListName $first $second $third $fourth $fifth $sixth
	  ;;
esac

# if [ $adjust == 2 ];then
# java makeZOMFieldMostPriorRule zomf zof $MostPriorRuleFile
# fi

#mv zomf $ruleName
#mv zof $headerName

rm x
rm c
#rm w
if [ ! `expr $# - $adjust` == 8 ] ; then
    rm a
fi
