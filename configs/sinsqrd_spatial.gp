#Example config file for Genetic Programming Java library
#Used for a spatial, tournament-based evolution

SEED NULL			#NULL indicates to get a new seed with System.currentTimeMillis
POP_SIZE 1000
POP_SIZE_SPATIAL 40 #Side length N for an NxN grid spatial population
TOURNY_SIZE 9 		#Used for non-spatial tournament populations
PROB_CROSS 0.9 		#probability that winner of tournament crosses over.
NUM_GENS 500 		#terminate after 50 generations
MAX_FITNESS 100 	#terminate if some creature has fitness > MAX_FITNESS
GP_DEPTH 4 			#starting depth for GP trees

POP_CLASS capps.gp.gppopulations.SpatialTournamentPop
CREATURE_CLASS capps.gp.gpcreatures.SinSqCreature

OUTPUT_FINAL_GEN true
OUTPUT_SHORT false
OUTPUT_LONG false

OUTPUT_DIR results/function_approx/spatial
HEADER_FILE header_s.dat
SHORT_INFO_FILE spatial_short_info_s.dat
FINAL_GEN_FILE final_gen_info_s.dat
