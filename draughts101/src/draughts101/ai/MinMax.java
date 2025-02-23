package draughts101.ai;

import static draughts101.board.PositionBoard.EMPTY;
import static draughts101.board.SquareBoard.SIZE;
import static draughts101.game.Game.KING;
import static draughts101.game.Game.PAWN;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

final public class MinMax extends HashMap<String, Integer> {
    final public static int ROW = SIZE / 2;
    final public static int PROMOTION = SIZE - 1;
    
    private static long middle = 0l;

    final private Node node;
    final private int color;
    
    public MinMax(Node node, int color) {
        this.node = node;
        this.color = color;
    }
    
    public int getValue(char[] position, long hasTurn, long noTurn, MinMax minMax, int[] alfaBeta, int depth) {
        HashMap<Integer, ArrayList<Long>> moves = new HashMap();
        int captureCount = 0;
        
        for (long occupied = hasTurn | noTurn, empty = ~occupied, captureMiddle = noTurn & middle, player = hasTurn; player != 0l; player ^= Long.lowestOneBit(player)) {
            int index = Long.numberOfTrailingZeros(player);            
            char piece = position[index];

            ArrayList<Long> pieceMoves = new ArrayList();
            
            for (Diagonal[] horizontal : new Diagonal[][] {{Diagonal.MIN_DIAGONAL, Diagonal.PLUS_ANTI_DIAGONAL}, {Diagonal.MIN_ANTI_DIAGONAL, Diagonal.PLUS_DIAGONAL}}) {
                for (Diagonal vertical : horizontal) {
                    if (vertical.hasNext(index)) {
                        long move = vertical.getNext(index);
                        
                        if (piece == KING[color] && (move & empty & middle) == move) {
                            move = vertical.getLine(index, occupied, move);
                        }
                        
                        long capture = move & captureMiddle;
                        
                        if (capture != 0l) {
                            long next = vertical.getNext(Long.numberOfTrailingZeros(capture));
                            
                            if ((next & empty) == next) {
                                if (piece == KING[color] && (next & middle) == next) {
                                    next = vertical.getLine(index, occupied, next) & empty;
                                }
                                
                                ArrayList<Long> captureMoves = new ArrayList(Arrays.asList(new Long[] {capture | next}));

                                occupied ^= 1l << index;
                                empty = ~occupied;

                                do {
                                    move = captureMoves.remove(0);

                                    long captured = move & captureMiddle;
                                    
                                    if (Long.bitCount(captured) >= captureCount) {
                                        if (Long.bitCount(captured) > captureCount) {
                                            pieceMoves.clear();
                                            moves.clear();

                                            captureCount++;
                                        }
                                        
                                        pieceMoves.add(move);
                                    }
                                    
                                    if (captured != captureMiddle) {
                                        for (long destinations = move & empty; destinations != 0l; destinations ^= Long.lowestOneBit(destinations)) {
                                            int step = Long.numberOfTrailingZeros(destinations);
                                            
                                            for (Diagonal diagonal : Diagonal.values()) {
                                                if (diagonal.hasNext(step)) {
                                                    next = diagonal.getNext(step);
                                                    
                                                    if (piece == KING[color] && (next & empty & middle) == next) {
                                                        next = diagonal.getLine(step, occupied, next);
                                                    }
                                                    
                                                    if ((next & move) == 0l) {
                                                        capture = next & captureMiddle;
                                                        
                                                        if (capture != 0l) {
                                                            next = diagonal.getNext(Long.numberOfTrailingZeros(capture));
                                                            
                                                            if ((next & empty) == next) {
                                                                if (piece == KING[color] && (next & middle) == next) {
                                                                    next = diagonal.getLine(step, occupied, next) & empty;
                                                                }
                                                                
                                                                captureMoves.add(captured | capture | next);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } while (!captureMoves.isEmpty());
                                
                                occupied ^= 1l << index;
                                empty = ~occupied;
                            }
                        }
                        
                        if (captureCount == 0 && (piece == KING[color] || vertical == horizontal[color])) {
                            move &= empty;
                            
                            if (move != 0l) {
                                pieceMoves.add(move);
                            }
                        }
                    }
                }
                
                if (!pieceMoves.isEmpty()) {
                    moves.put(index, pieceMoves);
                }
            }
        }
        
        if (moves.isEmpty()) {
            return alfaBeta[node.ordinal()];
        } else if (depth > 0) {
            depth--;
        } else if (captureCount == 0) {
            return node.getValue(Long.bitCount(hasTurn) - Long.bitCount(noTurn));
        }

        pruning : for (int from : moves.keySet()) {
            char[] board = position.clone();
            char piece = board[from];

            board[from] = EMPTY;

            for (long move : moves.get(from)) {
                long capture = move & noTurn;

                ArrayList<Integer> captures = new ArrayList();

                for (long bitBoard = capture; bitBoard != 0l; bitBoard ^= Long.lowestOneBit(bitBoard)) {
                    captures.add(Long.numberOfTrailingZeros(bitBoard));
                }

                for (long destinations = move ^ capture; destinations != 0l; destinations ^= Long.lowestOneBit(destinations)) {
                    int to = Long.numberOfTrailingZeros(destinations);

                    String key = String.valueOf(getBoard(color, board.clone(), piece, captures, to));

                    if (!containsKey(key)) {
                        put(key, minMax.getValue(key.toCharArray(), noTurn ^ capture, hasTurn ^ (1l << from | 1l << to), this, alfaBeta.clone(), depth));
                    }

                    int value = get(key);

                    if (node.isAlfaBeta(alfaBeta, value)) {
                        alfaBeta[node.ordinal()] = value;

                        if (alfaBeta[Node.MAX.ordinal()] >= alfaBeta[Node.MIN.ordinal()]) {
                            break pruning;
                        }
                    }
                }
            }
        }

        return alfaBeta[node.ordinal()];
    }

    public static char[] getBoard(int color, char[] board, char piece, AbstractList<Integer> captures, int to) {
        if (piece == PAWN[color] && to / ROW == color * PROMOTION) {
            piece = KING[color];
        }
        
        board[to] = piece;

        captures.forEach(index -> board[index] = EMPTY);

        return board;
    }
    
    static {
        for (int i = ROW; i < ROW * PROMOTION; i++) {
            if (i % SIZE != ROW - 1 && i % SIZE != ROW) {
                middle |= 1l << i;
            }
        }
    }
    
}
