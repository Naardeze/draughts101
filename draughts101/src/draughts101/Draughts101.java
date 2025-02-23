package draughts101;

import draughts101.board.PositionBoard;
import draughts101.game.Game;
import static draughts101.game.Game.PAWN;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;

final public class Draughts101 extends JDialog {
    final public static int WHITE = 0;
    final public static int BLACK = 1;
    
    final private static int MIN = 1;
    final private static int MAX = 5;

    final public static JSlider LEVEL = new JSlider(MIN, MAX);
    
    final public static JButton UNDO = new JButton("\u25c0");
    final public static JLabel GAME_OVER = new JLabel("GAME OVER", JLabel.CENTER);
    final public static JButton HINT = new JButton("?");

    private Game game = new Game(WHITE);
    
    private Draughts101() {
        setTitle("Draughts101");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        JMenu aiMenu = new JMenu("AI");
        
        JPanel center = new JPanel();
        JPanel south = new JPanel(new GridLayout(1, 3));
        JPanel parent1 = new JPanel();
        JPanel parent2 = new JPanel();

        for (int color : new int[] {WHITE, BLACK}) {
            JMenuItem pawn = new JMenuItem(String.valueOf(PAWN[color]));        

            pawn.setFont(pawn.getFont().deriveFont((float) 20));
            pawn.setForeground(PositionBoard.COLOR[color]);

            gameMenu.add(pawn).addActionListener(e -> {
                center.remove(game);
                
                game = new Game(color);

                center.add(game);                
                center.revalidate();
            });
        }
        
        LEVEL.setMajorTickSpacing(1);
        LEVEL.setPaintLabels(true);
        
        aiMenu.add(LEVEL);
        
        menuBar.add(gameMenu);
        menuBar.add(aiMenu);
        
        setJMenuBar(menuBar);

        center.add(game);

        UNDO.setFocusable(false);
        UNDO.addActionListener(e -> game.undo());
        
        HINT.setFocusable(false);
        HINT.setContentAreaFilled(false);
        HINT.addActionListener(e -> {
            HINT.setContentAreaFilled(!HINT.isContentAreaFilled());
            
            game.repaint();
        });
        
        parent1.add(UNDO);
        parent2.add(HINT);

        south.add(parent1);
        south.add(GAME_OVER);
        south.add(parent2);
        
        add(center, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
            
    public static void main(String[] args) {
        new Draughts101();
    }
    
}
