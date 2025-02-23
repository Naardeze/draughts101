package draughts101.ai;

public enum Node {
    MIN {
        @Override
        public boolean isAlfaBeta(int[] alfaBeta, int value) {
            return value < alfaBeta[ordinal()];
        }

        @Override
        public int getValue(int value) {
            return -value;
        }
    },
    MAX {
        @Override
        public boolean isAlfaBeta(int[] alfaBeta, int value) {
            return value > alfaBeta[ordinal()];
        }

        @Override
        public int getValue(int value) {
            return value;
        }
    };

    public abstract boolean isAlfaBeta(int[] alfaBeta, int value);

    public abstract int getValue(int value);

}
