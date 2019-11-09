package pieces;

import util.Move;

public class Bishop extends Piece {
    public Bishop(Color color) {
        super(color);
        this.type = Type.BISHOP;
    }

    @Override
    public boolean validateMove(Move move) {
        // [TODO] fill this one...
        return false;
    }
}
