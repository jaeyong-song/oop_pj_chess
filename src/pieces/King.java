package pieces;

import util.Move;

public class King extends Piece {
    public King(Color color) {
        super(color);
        this.type = Type.KING;
    }

    @Override
    public boolean validateMove(Move move) {
        // [FIXME] fill this one...
        /* king moves one level...
        * && can move if there's no piece on the way
        * && can move if there's a piece that can be captured by King */
        if(move.getCapturedPiece() == null // there's no piece to capture
                || (move.getCapturedPiece() != null // there's a piece to capture (different color)
                    && !move.getPiece().isSameColor(move.getCapturedPiece()))) {
            // can move one level
            if(move.getFileAbsDiff() <= 1 && move.getRankAbsDiff() <= 1) {
                return true;
            }
        }
        return false;
    }
}
