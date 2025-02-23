package draughts101.ai;

import static draughts101.ai.MinMax.PROMOTION;
import static draughts101.ai.MinMax.ROW;
import static draughts101.board.SquareBoard.SIZE;

public enum Diagonal {
    MIN_DIAGONAL(-ROW) {
        @Override
        public boolean hasNext(int index) {
            return index % SIZE != MIN_X && index >= MIN_Y;
        }

        @Override
        public long getLine(int index, long occupied, long from) {
            long mask = DIAGONAL[PLUS_X - index % ROW + index / ROW % 2 + index / SIZE];

            return mask & (occupied ^ Long.reverse(Long.reverse(mask & occupied) - Long.reverse(from)));
        }
    },
    MIN_ANTI_DIAGONAL(-ROW + 1) {
        @Override
        public boolean hasNext(int index) {
            return index % SIZE != PLUS_X && index >= MIN_Y;
        }

        @Override
        public long getLine(int index, long occupied, long from) {
            long mask = ANTI_DIAGONAL[index % ROW + index / SIZE];

            return mask & (occupied ^ Long.reverse(Long.reverse(mask & occupied) - Long.reverse(from)));
        }
    },
    PLUS_ANTI_DIAGONAL(ROW) {
        @Override
        public boolean hasNext(int index) {
            return index % SIZE != MIN_X && index < PLUS_Y;
        }

        @Override
        public long getLine(int index, long occupied, long from) {
            long mask = ANTI_DIAGONAL[index % ROW + index / SIZE];

            return mask & (occupied ^ ((mask & occupied) - from));
        }
    },
    PLUS_DIAGONAL(ROW + 1) {
        @Override
        public boolean hasNext(int index) {
            return index % SIZE != PLUS_X && index < PLUS_Y;
        }

        @Override
        public long getLine(int index, long occupied, long from) {
            long mask = DIAGONAL[PLUS_X - index % ROW + index / ROW % 2 + index / SIZE];

            return mask & (occupied ^ ((mask & occupied) - from));
        }
    };

    final private int step;

    Diagonal(int step) {
        this.step = step;
    }

    final private static int MIN_X = ROW;
    final private static int MIN_Y = ROW;
    final private static int PLUS_X = ROW - 1;
    final private static int PLUS_Y = ROW * PROMOTION;

    final private static long[] DIAGONAL = new long[SIZE];
    final private static long[] ANTI_DIAGONAL = new long[SIZE - 1];
    
    public abstract boolean hasNext(int index);

    public long getNext(int index) {
        return 1l << index + step - index / ROW % 2;
    }

    public abstract long getLine(int index, long occupied, long from);
    
    static {
        for (int i = 0; i < DIAGONAL.length; i++) {
            DIAGONAL[i] = 0l;
            
            for (int bit = PLUS_X - Math.min(i, PLUS_X) + Math.max(0, Math.min(i - PLUS_X, 1)) * ROW + Math.max(0, i - ROW) * SIZE, j = 0; j < 1 + (Math.min(i, PLUS_X) - Math.max(0, i - ROW)) * 2; bit += ROW + 1 - bit / ROW % 2, j++) {
                DIAGONAL[i] |= 1l << bit;
            }
        }

        for (int i = 0; i < ANTI_DIAGONAL.length; i++) {
            ANTI_DIAGONAL[i] = 0l;
            
            for (int bit = Math.min(i, PLUS_X) + Math.max(0, i - PLUS_X) * SIZE, j = 0; j < 2 + (Math.min(i, PLUS_X) - Math.max(0, i - PLUS_X)) * 2; bit += ROW - bit / ROW % 2, j++) {
                ANTI_DIAGONAL[i] |= 1l << bit;
            }
        }
    }
    
}
