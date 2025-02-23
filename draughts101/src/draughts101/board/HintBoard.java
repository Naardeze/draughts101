package draughts101.board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Set;

final public class HintBoard extends AbstractBoard {
    final public static int NONE = -1;
    
    private int selected;
    private Set<Integer> keySet;
    
    public HintBoard(Rectangle[] square) {
        super(square);
        
        setOpaque(false);
        setForeground(Color.orange);
        setVisible(false);
    }
    
    public int getSelected() {
        return selected;
    }
    
    public void setSelected(int selected) {
        this.selected = selected;
    }
    
    public void setKeySet(Set<Integer> keySet) {
        this.keySet = keySet;
        selected = NONE;
        
        setVisible(true);
    }
    
    @Override
    public void paint(Graphics g) {
        if (selected != NONE) {
            paintSquare(g, square[selected]);        
        } else if (draughts101.Draughts101.HINT.isContentAreaFilled()) {
            keySet.forEach(index -> paintSquare(g, square[index]));
        }
    }
    
}
