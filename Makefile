ALL_SRC = src/capps/gp/gptrees/*.java src/capps/gp/gpcreatures/*.java
TEST_SRC = test/capps/gp/gptrees/*.java
OPT1 = -g -classpath src:lib/guice-3.0/guice-3.0.jar -sourcepath src -d ./bin
OPT2_END = -C bin

all: $(ALL_SRC) $(TEST_SRC)
	javac -g -sourcepath src -d ./bin $(ALL_SRC) $(TEST_SRC)

clean: 
	rm -f -r bin/*
