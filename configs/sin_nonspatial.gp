#Example config file for Genetic Programming Java library
#Used for a non-spatial, tournament-based evolution

SEED NULL			#NULL indicates to get a new seed with System.currentTimeMillis
POP_SIZE 1600
TOURNY_SIZE 9 		#Used for non-spatial tournament populations
PROB_CROSS 0.9 		#probability that winner of tournament crosses over.
NUM_GENS 100 		#terminate after 50 generations
MAX_FITNESS 100 	#terminate if some creature has fitness > MAX_FITNESS
GP_DEPTH 4 			#starting depth for GP trees

POP_CLASS capps.gp.gppopulations.NonSpatialTournamentPop
CREATURE_CLASS capps.gp.gpcreatures.SinCreature

OUTPUT_FINAL_GEN true
OUTPUT_SHORT true
OUTPUT_LONG false

OUTPUT_DIR results/function_approx/nonspatial
HEADER_FILE header_ns.dat
SHORT_INFO_FILE short_info_ns.dat
FINAL_GEN_FILE final_gen_info_ns.dat

