package minichess.play; 

import minichess.ai.AiInterface;
import minichess.boards.Board;
import minichess.*; 

import minichess.heuristic.PointHeuristic;

public class SimplePlay {
    private static final PointHeuristic h = new PointHeuristic(); 

    /**Returns WHITE if white wins, BLACK if black wins, null for a draw.*/
    public static COLOR playGame(AiInterface whitePlayer, 
            AiInterface blackPlayer) {

        Board b = new Board(); 

        while (!b.isGameOver()) {
            COLOR whoseTurn = b.getWhoseTurn(); 
            Move toPlay = (whoseTurn == COLOR.WHITE ? 
                    whitePlayer.makeMove(b) :
                    blackPlayer.makeMove(b)); 
            b.performMove(toPlay); 
        }

        System.out.println(b); 
        COLOR winner= b.getWinner(); 

        return winner; 
    }

}
