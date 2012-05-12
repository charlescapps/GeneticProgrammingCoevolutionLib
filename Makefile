JAVA_FILES=javafiles.txt  #with javac you can specify a file

GP_SRC = src/capps/gp/gpcreatures/*.java \
src/capps/gp/gppopulations/*.java \
src/capps/gp/gpcreatures/*.java \
src/capps/gp/gptrees/*.java \
src/capps/gp/gpexceptions/*.java \
src/capps/gp/gpglobal/*.java \
src/capps/gp/gpdrivers/*.java \
impl/capps/gp/gpcreatures/*.java \

MINICHESS_SRC = impl/minichess/*.java \
				impl/minichess/ai/*.java \
				impl/minichess/ai/threads/*.java \
				impl/minichess/play/*.java \
				impl/minichess/boards/*.java \
				impl/minichess/config/*.java \
				impl/minichess/exceptions/*.java \
				impl/minichess/heuristic/*.java \
				impl/minichess/executables/*.java

TEST_SRC = test/capps/gp/gptrees/*.java

general_evolver.jar: gp_classes
	jar vcfe general_evolver.jar capps.gp.gpdrivers.GeneralEvolver -C bin \
		capps

evolve_funcs.jar: gp_classes
	jar vcfe evolve_funcs.jar capps.gp.gpdrivers.EvolveFuncApproxMain -C bin \
		capps

play_minichess.jar: minichess_classes
	jar vcfe play_minichess.jar minichess.executables.PlayMinichessFromConfig \
	-C bin minichess

gp_classes: $(GP_SRC) $(TEST_SRC)
	javac -g -sourcepath src:test:impl -d ./bin $(ALL_SRC) $(TEST_SRC)
	
minichess_classes: $(MINICHESS_SRC)
	javac -g -sourcepath impl -d ./bin $(MINICHESS_SRC)

clean: 
	rm -f -r bin/*
