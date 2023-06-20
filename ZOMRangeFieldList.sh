# if [ $1 == "-prior" ]; then
if [ $1 = "-prior" ]; then
    # if [ $2 == "SA" ] || [ $2 == "DA" ] || [ $2 == "SP" ] || [ $2 == "DP" ] || [ $2 == "PROT" ] || [ $2 == "FLAG" ]; then
    if [ $2 = "SA" ] || [ $2 = "DA" ] || [ $2 = "SP" ] || [ $2 = "DP" ] || [ $2 = "PROT" ] || [ $2 = "FLAG" ]; then
	echo "error : Specify a MostPriorRule file."
	exit
    fi
    adjust=2
else
    adjust=0
fi

MostPriorRuleFile=$2

eval first='$'{`expr 1 + $adjust`}
eval second='$'{`expr 2 + $adjust`}
eval third='$'{`expr 3 + $adjust`}
eval fourth='$'{`expr 4 + $adjust`}
eval fifth='$'{`expr 5 + $adjust`}
eval sixth='$'{`expr 6 + $adjust`}

eval ruleNum='$'{`expr $# - 4`}
eval seed_file='$'{`expr $# - 3`}
eval headerNum='$'{`expr $# - 2`}
eval ruleName='$'{`expr $# - 1`}
eval headerName='$'{$#}
defaultRule=""

cd db_generator
./db_generator -bc ../parameter_files/$seed_file $ruleNum 2 0.5 -0.1 MyFilters

cd ../trace_generator
./trace_generator 1 0.1 $headerNum ../db_generator/MyFilters
cd ..

# if [ $first == "SA" ] ; then
if [ $first = "SA" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $1}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $1}' > c
    defaultRule="${defaultRule}s/\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
fi

# if [ $first == "DA" ]  ; then
if [ $first = "DA" ]  ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $2}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $2}' > c
    defaultRule="${defaultRule}s/\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
# elif [ $second == "DA" ] ; then
elif [ $second = "DA" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $2}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $2}' > a
    paste c a > b
    mv b c
    defaultRule="${defaultRule} \*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
fi

# if [ $first == "SP" ] ; then
if [ $first = "SP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $3}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $3}' > c
    defaultRule="${defaultRule}s/0-65535"
# elif [  $second == "SP" ] || [ $third == "SP" ] ; then
elif [  $second = "SP" ] || [ $third = "SP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $3}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $3}' > a
    paste c a > b
    mv b c
    defaultRule="${defaultRule} 0-65535"
fi

# if [ $first == "DP" ] ; then
if [ $first = "DP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $4}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $4}' > c
    defaultRule="${defaultRule}s/0-65535"
# elif [ $second == "DP" ] || [ $third == "DP" ] || [ $fourth == "DP" ] ; then
elif [ $second = "DP" ] || [ $third = "DP" ] || [ $fourth = "DP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $4}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $4}' > a
    paste c a > b
    mv b c
    defaultRule="${defaultRule} 0-65535"
fi

# if [ $first == "PROT" ] ; then
if [ $first = "PROT" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $5}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $5}' > c
    defaultRule="${defaultRule}s/\*\*\*\*\*\*\*\*"
    # if [ $second == "FLAG" ] ; then
    if [ $second = "FLAG" ] ; then
	cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $6}' > y
	paste x y > z
	mv z x
	cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $6}' > a
	paste c a > b
	mv b c
	defaultRule="${defaultRule} \*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
    fi 
# elif [ $second == "PROT" ] || [ $third == "PROT" ] || [ $fourth == "PROT" ] || [ $fifth == "PROT" ] ; then
elif [ $second = "PROT" ] || [ $third = "PROT" ] || [ $fourth = "PROT" ] || [ $fifth = "PROT" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $5}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $5}' > a
    paste c a > b
    mv b c
    defaultRule="${defaultRule} \*\*\*\*\*\*\*\*"
    # if [ $third == "FLAG" ] || [ $fourth == "FLAG" ] || [ $fifth == "FLAG" ] || [ $sixth == "FLAG" ] ; then
    if [ $third = "FLAG" ] || [ $fourth = "FLAG" ] || [ $fifth = "FLAG" ] || [ $sixth = "FLAG" ] ; then
	cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $6}' > y
	paste x y > z
	mv z x
	cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $6}' > a
	paste c a > b
	mv b c
	defaultRule="${defaultRule} \*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
    fi    
# elif [ $first == "FLAG" ] || [ $second == "FLAG" ] || [ $third == "FLAG" ] || [ $fourth == "FLAG" ] || [ $fifth == "FLAG" ] ; then
elif [ $first = "FLAG" ] || [ $second = "FLAG" ] || [ $third = "FLAG" ] || [ $fourth = "FLAG" ] || [ $fifth = "FLAG" ] ; then
    echo "error : FLAG needs PROT argument."
    exit
fi

case `expr $# - $adjust` in
    6) java ClassBenchToRange x d $first
	 ;;
    7) java ClassBenchToRange x d $first $second
	 ;;
    8) java ClassBenchToRange x d $first $second $third
	 ;;
    9) java ClassBenchToRange x d $first $second $third $fourth
	 ;;
    10) java ClassBenchToRange x d $first $second $third $fourth $fifth
	  ;;
    11) java ClassBenchToRange x d $first $second $third $fourth $fifth $sixth
          ;;
esac

case `expr $# - $adjust` in
    6) java ZOHeaderFromClassBench c $headerName $first
	 ;;
    7) java ZOHeaderFromClassBench c $headerName $first $second
	 ;;
    8) java ZOHeaderFromClassBench c $headerName $first $second $third
	 ;;
    9) java ZOHeaderFromClassBench c $headerName $first $second $third $fourth
	 ;;
    10) java ZOHeaderFromClassBench c $headerName $first $second $third $fourth $fifth
	  ;;
    11) java ZOHeaderFromClassBench c $headerName $first $second $third $fourth $fifth $sixth
	  ;;
esac

#echo " \"$defaultRule//g\" "
sed -e "$defaultRule//g" < d > c #デフォルトルール消去
sed '/^$/d' c > x #空行消去
#awk '!Overlap[$0]++' x > d #重複消去

# if [ $adjust == 2 ];then
if [ $adjust = 2 ];then
java makeZOMRangeMostPriorRule x $headerName $MostPriorRuleFile
fi

mv x $ruleName

rm c
rm d
# if [ ! `expr $# - $adjust` == 6 ] ; then
if [ ! `expr $# - $adjust` = 6 ] ; then
    rm a
    rm y
fi
