package draughts101.board;

import static draughts101.board.SquareBoard.SIZE;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.AbstractList;
import java.util.ArrayList;

final public class PositionBoard extends AbstractBoard {
    final public static char WHITE_PAWN = '\u26c0';
    final public static char WHITE_KING = '\u26c1';
    final public static char BLACK_PAWN = '\u26c2';
    final public static char BLACK_KING = '\u26c3';
   
    final public static char EMPTY = ' ';

    final public static String WBWB = String.valueOf(new char[] {WHITE_PAWN, BLACK_PAWN, WHITE_KING, BLACK_KING});

    final public static Color[] COLOR = {Color.white, Color.black};
    
    final public static double Y = 0.78;
    
    private char[] position;

    private AbstractList<Integer> move = new ArrayList();
    
    public PositionBoard(Rectangle[] square) {
        super(square);
        
        position = new char[square.length];
        
        for(int i = 0; i < position.length / 2 - SIZE / 2; i++) {
            position[i] = BLACK_PAWN;
        }
        
        for(int i = position.length / 2 - SIZE / 2; i < position.length / 2 + SIZE / 2; i++) {
            position[i] = EMPTY;
        }

        for(int i = position.length / 2 + SIZE / 2; i < position.length; i++) {
            position[i] = WHITE_PAWN;
        }

        setOpaque(false);        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setFont(getFont().deriveFont((float) getHeight() / SIZE));
            }
        });
    }
    
    public char getIndex(int index) {
        return position[index];
    }
    
    public void setIndex(int index, char piece) {
        position[index] = piece;
    }
    
    public String getPosition() {
        return String.valueOf(position);
    }
    
    public void setPosition(String position) {
        this.position = position.toCharArray();
    }
    
    public AbstractList<Integer> getMove() {
        return move;
    }
    
    public void setMove(AbstractList<Integer> move) {
        this.move = move;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (int index : move){
            g.setColor(new Color[] {Color.yellow, Color.green}[(move.indexOf(index) + 1) / move.size()]);
            
            paintSquare(g, square[index]);
        }
        
        for (int i = 0; i < position.length; i++) {
            if (position[i] != EMPTY) {
                g.setColor(COLOR[WBWB.indexOf(position[i]) % COLOR.length]);
                g.drawString(String.valueOf(position[i]), square[i].x + (square[i].width - g.getFontMetrics().charWidth(position[i])) / 2, square[i].y + (int) (Y * square[i].height));
            }
        }
    }
}
