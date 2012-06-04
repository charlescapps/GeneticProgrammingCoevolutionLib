#Example config file for Genetic Programming Java library
#Used for a non-spatial, tournament-based evolution

SEED NULL			#NULL indicates to get a new seed with System.currentTimeMillis
POP_SIZE 32
TOURNY_SIZE 8       #Used for non-spatial tournament populations
PROB_CROSS 0.9 		#probability that winner of tournament crosses over.
PROB_MUTATE 0.1 	#probability of mutation
NUM_GENS 16 		#terminate after NUM_GENS generations
GP_DEPTH 5 			#starting depth for GP trees

POP_CLASS capps.gp.gppopulations.NonSpatialGamePop
CREATURE_CLASS capps.gp.gpcreatures.MinichessCreature

OUTPUT_FINAL_GEN true
OUTPUT_SHORT true
OUTPUT_LONG false

OUTPUT_DIR results/nonspatial_game
HEADER_FILE header_s.dat
SHORT_INFO_FILE spatial_short_info_s.dat
FINAL_GEN_FILE final_gen_info_s.dat
