
# echo "input rule number"
# read RULENUM
# echo "input field number (1 <= n <= 6)"
# read FIELD
# echo "input a file in parameter_files"
# read PARAMETER

#形式　$ sh AdjacencyList.sh SA DA SP DP PROT FLAG 100 acl1_seed 1 Rule 0.5

ruleNum=`expr $# - 4`
seed_file=`expr $# - 3`
headNum=`expr $# - 2`
result=`expr $# - 1`
pro=$#

cd db_generator
eval ./db_generator -bc ../parameter_files/'$'$seed_file '$'$ruleNum 2 0.5 -0.1 MyFilters
#echo "input Header number"
# read HEADERNUM
cd ../trace_generator
eval ./trace_generator 1 0.1 '$'$headNum ../db_generator/MyFilters
cd ..
#echo "input rule name"
# read RULENAME

if [ $1 == "SA" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $1}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $1}' > Header
fi

if [ $1 == "DA" ]  ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $2}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $2}' > Header
elif [ $2 == "DA" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $2}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $2}' > a
    paste Header a > b
    mv b Header
fi

if [ $1 == "SP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $3}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $3}' > Header
elif [  $2 == "SP" ] || [ $3 == "SP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $3}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $3}' > a
    paste Header a > b
    mv b Header
fi

if [ $1 == "DP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $4}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $4}' > Header
elif [ $2 == "DP" ] || [ $3 == "DP" ] || [ $4 == "DP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $4}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $4}' > a
    paste Header a > b
    mv b Header
fi

if [ $1 == "PROT" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $5}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $5}' > Header
    if [ $2 == "FLAG" ] ; then
	cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $6}' > y
	paste x y > z
	mv z x
	cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $6}' > a
	paste Header a > b
	mv b Header
    fi 
elif [ $2 == "PROT" ] || [ $3 == "PROT" ] || [ $4 == "PROT" ] || [ $5 == "PROT" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $5}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $5}' > a
    paste Header a > b
    mv b Header
    if [ $3 == "FLAG" ] || [ $4 == "FLAG" ] || [ $5 == "FLAG" ] || [ $6 == "FLAG" ] ; then
	cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $6}' > y
	paste x y > z
	mv z x
	cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $6}' > a
	paste Header a > b
	mv b Header
    fi    
elif [ $1 == "FLAG" ] || [ $2 == "FLAG" ] || [ $3 == "FLAG" ] || [ $4 == "FLAG" ] || [ $5 == "FLAG" ] ; then
    echo "error : FLAG needs PROT argument."
    exit
fi

#echo "input probability of evaluation type (0 <= p <=1)"
# read PROBABILITY
javac AddEtype.java
eval  java AddEtype x OriginalList '$'{$pro}
javac ClassBenchToAdjacencyList.java
case "$#" in
    "6") eval java ClassBenchToAdjacencyList OriginalList Header '$'{$result} $1
	 ;;
    "7") eval java ClassBenchToAdjacencyList OriginalList Header '$'{$result} $1 $2
	 ;;
    "8") eval java ClassBenchToAdjacencyList OriginalList Header '$'{$result} $1 $2 $3
	 ;;
    "9") eval java ClassBenchToAdjacencyList OriginalList Header '$'{$result} $1 $2 $3 $4
	 ;;
    "10") eval java ClassBenchToAdjacencyList OriginalList Header '$'{$result} $1 $2 $3 $4 $5
	  ;;
    "11") eval java ClassBenchToAdjacencyList OriginalList Header '$'{$result} $1 $2 $3 $4 $5 $6
esac

rm a
rm x
rm y
rm Header
rm OriginalList
