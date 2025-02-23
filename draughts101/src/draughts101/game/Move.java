package draughts101.game;

import java.util.ArrayList;
import java.util.Stack;

final public class Move extends Stack<Integer> {

    public Move(int to) {
        add(to);
    }
    
    public Move(ArrayList<Integer> captured, int to) {
        addAll(captured);
        add(to);
    }
    
}
