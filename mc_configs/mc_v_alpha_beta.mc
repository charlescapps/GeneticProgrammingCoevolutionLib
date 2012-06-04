#sample config file for a AlphaBeta v.  minichess creature game

PLAY_INTERFACE minichess.play.PlayMinichess
BLACK_AI minichess.ai.AbNegaMaxID
WHITE_AI minichess.ai.AbNegaMaxID
BLACK_HEURISTIC minichess.heuristic.PointHeuristic
WHITE_HEURISTIC capps.gp.gpcreatures.MinichessCreature

TIME_PER_MOVE 500
ITERATIVE_DEEP_MIN_DEPTH 4
REVERSE_DISPLAY false
