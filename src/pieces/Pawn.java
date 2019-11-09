package pieces;

import util.Move;

public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color);
        this.type = Type.PAWN;
    }

    @Override
    public boolean validateMove(Move move) {
        // [TODO] fill this one...
        return false;
    }
}
