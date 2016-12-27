ruleNum=`expr $# - 5`
seed_file=`expr $# - 4`
headNum=`expr $# - 3`
ruleName=`expr $# - 2`
headerName=`expr $# - 1`
pro=$#
defaultRule=""

cd db_generator
eval ./db_generator -bc ../parameter_files/'$'$seed_file '$'$ruleNum 2 0.5 -0.1 MyFilters

cd ../trace_generator
eval ./trace_generator 1 0.1 '$'$headNum ../db_generator/MyFilters
cd ..

if [ $1 == "SA" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $1}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $1}' > c
    defaultRule="${defaultRule}s/\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
fi

if [ $1 == "DA" ]  ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $2}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $2}' > c
    defaultRule="${defaultRule}s/\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
elif [ $2 == "DA" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $2}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $2}' > a
    paste c a > b
    mv b c
    defaultRule="${defaultRule} \*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
fi

if [ $1 == "SP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $3}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $3}' > c
    defaultRule="${defaultRule}s/\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
elif [  $2 == "SP" ] || [ $3 == "SP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $3}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $3}' > a
    paste c a > b
    mv b c
    defaultRule="${defaultRule} \*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
fi

if [ $1 == "DP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $4}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $4}' > c
    defaultRule="${defaultRule}s/\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
elif [ $2 == "DP" ] || [ $3 == "DP" ] || [ $4 == "DP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $4}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $4}' > a
    paste c a > b
    mv b c
    defaultRule="${defaultRule} \*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
fi

if [ $1 == "PROT" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $5}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $5}' > c
    defaultRule="${defaultRule}s/\*\*\*\*\*\*\*\*"
    if [ $2 == "FLAG" ] ; then
	cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $6}' > y
	paste x y > z
	mv z x
	cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $6}' > a
	paste c a > b
	mv b c
	defaultRule="${defaultRule} \*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
    fi 
elif [ $2 == "PROT" ] || [ $3 == "PROT" ] || [ $4 == "PROT" ] || [ $5 == "PROT" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $5}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $5}' > a
    paste c a > b
    mv b c
    defaultRule="${defaultRule} \*\*\*\*\*\*\*\*"
    if [ $3 == "FLAG" ] || [ $4 == "FLAG" ] || [ $5 == "FLAG" ] || [ $6 == "FLAG" ] ; then
	cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $6}' > y
	paste x y > z
	mv z x
	cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $6}' > a
	paste c a > b
	mv b c
	defaultRule="${defaultRule} \*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*"
    fi    
elif [ $1 == "FLAG" ] || [ $2 == "FLAG" ] || [ $3 == "FLAG" ] || [ $4 == "FLAG" ] || [ $5 == "FLAG" ] ; then
    echo "error : FLAG needs PROT argument."
    exit
fi

case "$#" in
    "7") eval java ClassBenchToZOM x a $1
	 ;;
    "8") eval java ClassBenchToZOM x a $1 $2
	 ;;
    "9") eval java ClassBenchToZOM x a $1 $2 $3
	 ;;
    "10") eval java ClassBenchToZOM x a $1 $2 $3 $4
	 ;;
    "11") eval java ClassBenchToZOM x a $1 $2 $3 $4 $5
	  ;;
    "12") eval java ClassBenchToZOM x a $1 $2 $3 $4 $5 $6
          ;;
esac

case "$#" in
    "7") eval java ZOHeaderFromClassbench c '$'{$headerName} $1
	 ;;
    "8") eval java ZOHeaderFromClassbench c '$'{$headerName} $1 $2
	 ;;
    "9") eval java ZOHeaderFromClassbench c '$'{$headerName} $1 $2 $3
	 ;;
    "10") eval java ZOHeaderFromClassbench c '$'{$headerName} $1 $2 $3 $4
	 ;;
    "11") eval java ZOHeaderFromClassbench c '$'{$headerName} $1 $2 $3 $4 $5
	  ;;
    "12") eval java ZOHeaderFromClassbench c '$'{$headerName} $1 $2 $3 $4 $5 $6
	  ;;
esac

#echo " \"$defaultRule//g\" "
sed -e "$defaultRule//g" < a > c #デフォルトルール消去
sed '/^$/d' c > a #空行消去
#awk '!Overlap[$0]++' x > d #重複消去


eval java AddEvaluationZOM a '$'{$headerName} b

eval java AddEtype b '$'{$ruleName} '$'{$pro}

rm x
rm b
rm c
rm a
if [ ! $# == 7 ] ; then
    rm y
fi
