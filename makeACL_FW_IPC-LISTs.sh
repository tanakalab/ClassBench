#! /bin/bash -
#
# Example
# $ sh makeACL_FW_IPC-LISTS.sh 2000 100000 10 2000 10000
#
# if an input number is less than 2 then program does not terminate ...
# I need to that anyone fix this bug.

# m is a number of generated rulelists
m=$3
# n is a number of rules in a rulelist
n=$1
# [a,b] are range of a number of generated rules
a=$4
b=$5
echo $((b+=2000))
if [ $# == 5 ]
then
    i=$a
    while [ $i != $b ]
    do
	sh makeACL_FW_IPC-Lists.sh $i $2 $m
	echo $((i+=2000))
	echo -e '\n'
    done
fi
