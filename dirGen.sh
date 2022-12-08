#!/bin/bash

for i in {1..25}
do
	if [ $i -lt 10 ]
	then
		mkdir -p "day0"$i"a/src"
		touch "day0"$i"a/input.txt"
	else
		mkdir -p "day"$i"a/src"
		touch "day"$i"a/input.txt"
	fi
done

