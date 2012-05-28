#Example config file for Genetic Programming Java library
#Used for a spatial, tournament-based evolution

SEED NULL			#NULL indicates to get a new seed with System.currentTimeMillis
POP_SIZE 16
TOURNY_SIZE 4       #Used for non-spatial tournament populations
PROB_CROSS 0.9 		#probability that winner of tournament crosses over.
PROB_MUTATE 0.1 	#probability of mutation
NUM_GENS 5 		#terminate after NUM_GENS generations
GP_DEPTH 3 			#starting depth for GP trees

POP_CLASS capps.gp.gppopulations.NonSpatialTournamentPop
CREATURE_CLASS capps.gp.gpcreatures.MinichessBeatRandCreature

OUTPUT_FINAL_GEN true
OUTPUT_SHORT true
OUTPUT_LONG false

OUTPUT_DIR results/minichess_results
HEADER_FILE header.dat
SHORT_INFO_FILE spatial_short_info.dat
FINAL_GEN_FILE final_gen_info.dat
