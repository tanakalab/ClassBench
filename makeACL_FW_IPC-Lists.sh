#! /bin/bash -
# if an input number is less than 2 then program does not terminate ...
# I need to that anyone fix this bug.

# m is a number of generated rulelists
m=$3
# m is a number of rules in a rulelist
n=$1
echo $((m+=1))
# if [ $# == 3 ]
if [ $# = 3 ]
then
    sh ZOMRangeFieldList.sh $n 6 acl2_seed $2 acl_${n}_1 header_acl
    i=2
    while [ $i != $m ]
    do
	sh ZOMRangeFieldList.sh $n 6 acl2_seed 100 acl_${n}_${i} tmp
	echo $((i+=1))
	echo -e '\n'
    done

    sh ZOMRangeFieldList.sh $n 6 fw2_seed $2 fw_${n}_1 header_fw
    i=2
    while [ $i != $m ]
    do
	sh ZOMRangeFieldList.sh $n 6 fw2_seed 100 fw_${n}_${i} tmp
	echo $((i+=1))
	echo -e '\n'
    done

    sh ZOMRangeFieldList.sh $n 6 ipc2_seed $2 ipc_${n}_1 header_ipc
    i=2
    while [ $i != $m ]
    do
	sh ZOMRangeFieldList.sh $n 6 ipc2_seed 100 ipc_${n}_${i} tmp
	echo $((i+=1))
	echo -e '\n'
    done
fi
