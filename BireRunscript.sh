#!/bin/bash

echo "########## LOGGING FROM `date` ##########"


echo "pwd = `pwd`"

java=`which java`

echo "Java = $java"


for i in {0..14}
	do
 		echo "Setting : $i" 
		echo "########## LOGGING FROM `date` ##########"

		java -Xmx32g -jar NERFGUN.jar -s $i
	
	done
