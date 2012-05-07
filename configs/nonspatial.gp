#Example config file for Genetic Programming Java library
#Used for a non-spatial, tournament-based evolution

SEED NULL 			# NULL indicates to get a new seed with System.currentTimeMillis
POP_SIZE 1000
TOURNY_SIZE 9 		#Used for non-spatial tournament populations
PROB_CROSS 0.9 		#probability that winner of tournament crosses over.
NUM_GENS 500 		#terminate after 50 generations
MAX_FITNESS 100 	#terminate if some creature has fitness > MAX_FITNESS
GP_DEPTH 5 			#starting depth for GP trees

OUTPUT_DIR results/function_approx
