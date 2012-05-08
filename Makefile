JAVA_FILES=javafiles.txt  #with javac you can specify a file

ALL_SRC = src/capps/gp/gpcreatures/*.java \
src/capps/gp/gppopulations/*.java \
src/capps/gp/gpcreatures/*.java \
src/capps/gp/gptrees/*.java \
src/capps/gp/gpexceptions/*.java \
src/capps/gp/gpglobal/*.java \
src/capps/gp/gpdrivers/*.java \
impl/capps/gp/gpcreatures/*.java \
#impl/capps/gp/gpdrivers/*.java

TEST_SRC = test/capps/gp/gptrees/*.java

general_evolver.jar: all
	jar vcfe general_evolver.jar capps.gp.gpdrivers.GeneralEvolver -C bin \
		capps

evolve_funcs.jar: all
	jar vcfe evolve_funcs.jar capps.gp.gpdrivers.EvolveFuncApproxMain -C bin \
		capps

all: $(ALL_SRC) $(TEST_SRC)
	javac -g -sourcepath src:test:impl -d ./bin $(ALL_SRC) $(TEST_SRC)

clean: 
	rm -f -r bin/*
