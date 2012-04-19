ALL_SRC = src/capps/gp/gptrees/*.java test/capps/gp/gptrees/*.java
OPT1 = -g -classpath src:lib/guice-3.0/guice-3.0.jar -sourcepath src -d ./bin
OPT2_END = -C bin

GP: $(ALL_SRC)
	javac -g -sourcepath src -d ./bin $(ALL_SRC)
