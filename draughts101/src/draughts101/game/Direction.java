package draughts101.game;

import static draughts101.board.SquareBoard.SIZE;
import static draughts101.board.SquareBoard.x;
import static draughts101.board.SquareBoard.y;
import java.awt.Point;

final public class Direction extends Point {
    final public static Direction MIN_X_MIN_Y = new Direction(-1, -1);
    final public static Direction MIN_X_PLUS_Y = new Direction(-1, 1);
    final public static Direction PLUS_X_MIN_Y = new Direction(1, -1);
    final public static Direction PLUS_X_PLUS_Y = new Direction(1, 1);
    
    public Direction(int x, int y) {
        super(x, y);
    }
    
    public boolean hasNext(int index) {
        int x = x(index) + this.x;
        int y = y(index) + this.y;
        
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }
    
    public int getNext(int index) {
        return (x(index) + x) / 2 + (y(index) + y) * (SIZE / 2);
    }
    
}
