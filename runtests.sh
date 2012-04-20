#!/bin/bash
java -cp "$CLASSPATH:bin" org.junit.runner.JUnitCore capps.gp.gptrees.GPTreeTest

for f in *.dot 
do
	echo "Converting $f to jpg"
	dot -Tjpg $f  > "$f.jpg"
done
