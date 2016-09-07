#echo "input rule number"
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
#echo "input rule name"
#read RULENAME 
case "$2" in 
    "1") cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $1}' > $5
	 ;;
    "2") cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $1,$2}' > $5
	 ;;
    "3") cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $1,$2,$3}' > $5
         ;;
    "4") cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $1,$2,$3,$4}' > $5
	 ;;
    "5") cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $1,$2,$3,$4,$5}' > $5
	 ;;
    "6") cat db_generator/MyFilters | awk -F'\t' 'BEGIN{OFS="\t"} {print $1,$2,$3,$4,$5,$6}' > $5
	 ;;
esac
# echo "input header name"
# read HEADERNAME
case "$2" in 
    "1") cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $1}' > $6
	 ;;
    "2") cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $1,$2}' > $6
	 ;;
    "3") cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $1,$2,$3}' > $6
         ;;
    "4") cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $1,$2,$3,$4}' > $6
	 ;;
    "5") cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $1,$2,$3,$4,$5}' > $6
	 ;;
    "6") cat db_generator/MyFilters_trace | awk 'BEGIN{OFS="\t"} {print $1,$2,$3,$4,$5,$6}' > $6
	 ;;
esac
