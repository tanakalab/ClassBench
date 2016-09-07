
# echo "input rule number"
# read RULENUM
# echo "input field number (1 <= n <= 6)"
# read FIELD
# echo "input a file in parameter_files"
# read PARAMETER
cd db_generator
./db_generator -bc ../parameter_files/$3 $1 2 0.5 -0.1 MyFilters
# echo "input header number"
# read HEADERNUM
cd ../trace_generator
./trace_generator 1 0.1 `expr $4 / $1` ../db_generator/MyFilters
cd ..
# echo "input rule name"
# read RULENAME 
case "$2" in 
    "1") cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $1}' > x
	 ;;
    "2") cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $1,$2}' > x
	 ;;
    "3") cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $1,$2,$3}' > x
         ;;
    "4") cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $1,$2,$3,$4}' > x
	 ;;
    "5") cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $1,$2,$3,$4,$5}' > x
	 ;;
    "6") cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $1,$2,$3,$4,$5,$6}' > x
	 ;;
esac

case "$2" in 
    "1") cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $1}' > y
	 ;;
    "2") cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $1,$2}' > y
	 ;;
    "3") cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $1,$2,$3}' > y
         ;;
    "4") cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $1,$2,$3,$4}' > y
	 ;;
    "5") cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $1,$2,$3,$4,$5}' > y
	 ;;
    "6") cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $1,$2,$3,$4,$5,$6}' > y
	 ;;
esac

# echo "input probability of evaluation type (0 <= p <=1)"
# read PROBABILITY
java AddEtype x a $6
java ClassBenchToAdjacencyList a y $2  $5

rm x
rm y
rm a
