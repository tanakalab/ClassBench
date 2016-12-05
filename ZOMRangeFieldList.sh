ruleNum=`expr $# - 4`
seed_file=`expr $# - 3`
headNum=`expr $# - 2`
ruleName=`expr $# - 1`
headerName=$#
defaultRule=""

cd db_generator
eval ./db_generator -bc ../parameter_files/'$'$seed_file '$'$ruleNum 2 0.5 -0.1 MyFilters

cd ../trace_generator
eval ./trace_generator 1 0.1 '$'$headNum ../db_generator/MyFilters
cd ..

if [ $1 == "SA" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $1}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $1}' > c
    defaultRule=${defaultRule}\"s/\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*
fi

if [ $1 == "DA" ]  ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $2}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $2}' > c
    defaultRule=${defaultRule}\"s\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*
elif [ $2 == "DA" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $2}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $2}' > a
    paste c a > b
    mv b c
    defaultRule=${defaultRule}\ \\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*
fi

if [ $1 == "SP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $3}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $3}' > c
        defaultRule=${defaultRule}\"s/0-65535
elif [  $2 == "SP" ] || [ $3 == "SP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $3}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $3}' > a
    paste c a > b
    mv b c
    defaultRule=${defaultRule}\ 0-65535
fi

if [ $1 == "DP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $4}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $4}' > c
    defaultRule=${defaultRule}\"s0-65535
elif [ $2 == "DP" ] || [ $3 == "DP" ] || [ $4 == "DP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $4}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $4}' > a
    paste c a > b
    mv b c
    defaultRule=${defaultRule}\ 0-65535
fi

if [ $1 == "PROT" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $5}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $5}' > c
    defaultRule=${defaultRule}\"s\\*\\*\\*\\*\\*\\*\\*\\*
    if [ $2 == "FLAG" ] ; then
	cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $6}' > y
	paste x y > z
	mv z x
	cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $6}' > a
	paste c a > b
	mv b c
	defaultRule=${defaultRule}\ \\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*
    fi 
elif [ $2 == "PROT" ] || [ $3 == "PROT" ] || [ $4 == "PROT" ] || [ $5 == "PROT" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $5}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $5}' > a
    paste c a > b
    mv b c
    defaultRule=${defaultRule}\ \\*\\*\\*\\*\\*\\*\\*\\*
    if [ $3 == "FLAG" ] || [ $4 == "FLAG" ] || [ $5 == "FLAG" ] || [ $6 == "FLAG" ] ; then
	cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $6}' > y
	paste x y > z
	mv z x
	cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $6}' > a
	paste c a > b
	mv b c
	defaultRule=${defaultRule}\ \\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*
    fi    
elif [ $1 == "FLAG" ] || [ $2 == "FLAG" ] || [ $3 == "FLAG" ] || [ $4 == "FLAG" ] || [ $5 == "FLAG" ] ; then
    echo "error : FLAG needs PROT argument."
    exit
fi

case "$#" in
    "6") eval java ClassBenchToRange x d $1
	 ;;
    "7") eval java ClassBenchToRange x d $1 $2
	 ;;
    "8") eval java ClassBenchToRange x d $1 $2 $3
	 ;;
    "9") eval java ClassBenchToRange x d $1 $2 $3 $4
	 ;;
    "10") eval java ClassBenchToRange x d $1 $2 $3 $4 $5
	  ;;
    "11") eval java ClassBenchToRange x d $1 $2 $3 $4 $5 $6
          ;;
esac

case "$#" in
    "6") eval java ZOHeaderFromClassbench c '$'{$headerName} $1
	 ;;
    "7") eval java ZOHeaderFromClassbench c '$'{$headerName} $1 $2
	 ;;
    "8") eval java ZOHeaderFromClassbench c '$'{$headerName} $1 $2 $3
	 ;;
    "9") eval java ZOHeaderFromClassbench c '$'{$headerName} $1 $2 $3 $4
	 ;;
    "10") eval java ZOHeaderFromClassbench c '$'{$headerName} $1 $2 $3 $4 $5
	  ;;
    "11") eval java ZOHeaderFromClassbench c '$'{$headerName} $1 $2 $3 $4 $5 $6
	  ;;
esac

#echo "$defaultRule//g\" < c > x"
eval sed -e $defaultRule//g\" < d > x
eval mv x '$'{$ruleName}

#rm x
rm c
rm d
if [ ! $# == 6 ] ; then
    rm a
    rm y
fi
