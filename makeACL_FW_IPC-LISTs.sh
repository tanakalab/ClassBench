#! /bin/bash -
#
# Example
# $ sh makeACL_FW_IPC-LISTS.sh 100000 10 2000 10000
#
# if an input number is less than 2 then program does not terminate ...
# I need to that anyone fix this bug.

# m is a number of generated rulelists
m=$2

# [a,b] are range of a number of generated rules
a=$3
b=$4
echo $((b+=2000))
if [ $# == 4 ]
then
    i=$a
    while [ $i != $b ]
    do
	sh makeACL_FW_IPC-Lists.sh $i $1 $m
	echo $((i+=2000))
	echo -e '\n'
    done
fi
