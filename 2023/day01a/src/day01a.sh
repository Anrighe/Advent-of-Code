#!/bin/bash

input="input.txt"
declare -i sum=0

re='^[0-9]$'

while IFS= read -r line || [ -n "$line" ]; do

    value=''
    for (( i=0; i<${#line}; i++)); do

        # ${line:$i:1} expands to the substring starting at position $i of length 1
        if [[ ${line:$i:1} =~ $re ]]; then 
            value="$value${line:$i:1}"
        fi

    done

    value="${value:0:1}${value: -1}" # Keeps only the first and the last digit of value
    sum=$(($sum + $value))

done < "$input"
echo "The sum of all of the calibration values is: $sum"



