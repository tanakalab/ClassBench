ruleNum=`expr $# - 4`
seed_file=`expr $# - 3`
headNum=`expr $# - 2`
ruleName=`expr $# - 1`
pro=$#
defaultRule=""

cd db_generator
eval ./db_generator -bc ../parameter_files/'$'$seed_file '$'$ruleNum 2 0.5 -0.1 MyFilters

cd ../trace_generator
eval ./trace_generator 1 0.1 '$'$headNum ../db_generator/MyFilters
cd ..

# if [ $1 == "SA" ] ; then
if [ $1 = "SA" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $1}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $1}' > c
    defaultRule="${defaultRule}s;@0.0.0.0/0"
fi

# if [ $1 == "DA" ]  ; then
if [ $1 = "DA" ]  ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $2}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $2}' > c
    defaultRule="${defaultRule}s;0.0.0.0/0"
# elif [ $2 == "DA" ] ; then
elif [ $2 = "DA" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $2}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $2}' > a
    paste c a > b
    mv b c
    defaultRule="${defaultRule}	0.0.0.0/0"
fi

# if [ $1 == "SP" ] ; then
if [ $1 = "SP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $3}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $3}' > c
    defaultRule="${defaultRule}s;0 : 65535"
# elif [  $2 == "SP" ] || [ $3 == "SP" ] ; then
elif [  $2 = "SP" ] || [ $3 = "SP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $3}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $3}' > a
    paste c a > b
    mv b c
    defaultRule="${defaultRule}	0 : 65535"
fi

# if [ $1 == "DP" ] ; then
if [ $1 = "DP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $4}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $4}' > c
    defaultRule="${defaultRule}s;0 : 65535"
# elif [ $2 == "DP" ] || [ $3 == "DP" ] || [ $4 == "DP" ] ; then
elif [ $2 = "DP" ] || [ $3 = "DP" ] || [ $4 = "DP" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $4}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $4}' > a
    paste c a > b
    mv b c
    defaultRule="${defaultRule}	0 : 65535"
fi

# if [ $1 == "PROT" ] ; then
if [ $1 = "PROT" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $5}' > x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $5}' > c
    defaultRule="${defaultRule}s;0x00/0x00"
    # if [ $2 == "FLAG" ] ; then
    if [ $2 = "FLAG" ] ; then
	cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $6}' > y
	paste x y > z
	mv z x
	cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $6}' > a
	paste c a > b
	mv b c
	defaultRule="${defaultRule}	0x0000/0x0000"
    fi 
# elif [ $2 == "PROT" ] || [ $3 == "PROT" ] || [ $4 == "PROT" ] || [ $5 == "PROT" ] ; then
elif [ $2 = "PROT" ] || [ $3 = "PROT" ] || [ $4 = "PROT" ] || [ $5 = "PROT" ] ; then
    cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $5}' > y
    paste x y > z
    mv z x
    cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $5}' > a
    paste c a > b
    mv b c
    defaultRule="${defaultRule}	0x00/0x00"
    # if [ $3 == "FLAG" ] || [ $4 == "FLAG" ] || [ $5 == "FLAG" ] || [ $6 == "FLAG" ] ; then
    if [ $3 = "FLAG" ] || [ $4 = "FLAG" ] || [ $5 = "FLAG" ] || [ $6 = "FLAG" ] ; then
	cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $6}' > y
	paste x y > z
	mv z x
	cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $6}' > a
	paste c a > b
	mv b c
	defaultRule="${defaultRule}	0x0000/0x0000"
    fi    
# elif [ $1 == "FLAG" ] || [ $2 == "FLAG" ] || [ $3 == "FLAG" ] || [ $4 == "FLAG" ] || [ $5 == "FLAG" ] ; then
elif [ $1 = "FLAG" ] || [ $2 = "FLAG" ] || [ $3 = "FLAG" ] || [ $4 = "FLAG" ] || [ $5 = "FLAG" ] ; then
    echo "error : FLAG needs PROT argument."
    exit
fi

#echo " \"$defaultRule++g\" "
sed -e "$defaultRule;;g" < x > b #デフォルトルール消去
sed '/^$/d' b > x #空行消去
#awk '!Overlap[$0]++' x > d #重複消去


eval  java AddEtype x w '$'{$pro}
case "$#" in
    "6") eval java AddEPNToClassBench w c '$'{$ruleName} $1
	 ;;
    "7") eval java AddEPNToClassBench w c '$'{$ruleName} $1 $2
	 ;;
    "8") eval java AddEPNToClassBench w c '$'{$ruleName} $1 $2 $3
	 ;;
    "9") eval java AddEPNToClassBench w c '$'{$ruleName} $1 $2 $3 $4
	 ;;
    "10") eval java AddEPNToClassBench w c '$'{$ruleName} $1 $2 $3 $4 $5
	  ;;
    "11") eval java AddEPNToClassBench w c '$'{$ruleName} $1 $2 $3 $4 $5 $6
	  ;;
esac

rm x
rm c
rm w
# if [ ! $# == 6 ] ; then
if [ ! $# = 6 ] ; then
    rm a
    rm y
fi
