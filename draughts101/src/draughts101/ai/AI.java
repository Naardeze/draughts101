package draughts101.ai;

import static draughts101.board.PositionBoard.EMPTY;
import static draughts101.board.PositionBoard.WBWB;
import draughts101.game.Move;
import java.util.ArrayList;
import java.util.HashMap;

final public class AI {
    final private static int ALFA = Integer.MAX_VALUE;
    final private static int BETA = Integer.MIN_VALUE;
    
    final private int level;
    
    public AI(int level) {
        this.level = level;
    }    
    
    public Move getMove(int ai, char[] position, HashMap<Integer, Move[]> moves) {
        long[] player = {0l, 0l};
        
        for (int index = 0; index < position.length; index++) {
            if (position[index] != EMPTY) {
                player[WBWB.indexOf(position[index]) % player.length] |= 1l << index;
            }
        }
                
        int opponent = 1 - ai;
        int max = BETA;
        
        ArrayList<Move> best = new ArrayList();

        for (int from : moves.keySet()) {
            char[] board = position.clone();
            char piece = board[from];
            
            board[from] = EMPTY;
            
            for (Move move : moves.get(from)) {
                int to = move.pop();

                long capture = 0l;
                
                for (int index : move) {
                    capture |= 1l << index;
                }
                
                int min = new MinMax(Node.MIN, opponent).getValue(MinMax.getBoard(ai, board.clone(), piece, move, to), player[opponent] ^ capture, player[ai] ^ (1l << from | 1l << to), new MinMax(Node.MAX, ai), new int[] {ALFA, BETA}, level);
                
                if (min >= max) {
                    if (min > max) {
                        best.clear();
               
                        max = min;
                    }
                    
                    move.add(0, from);
                    move.add(to);
                    
                    best.add(move);
                }
            }
        }

        return best.get((int) (Math.random() * best.size()));
    }
    
}
