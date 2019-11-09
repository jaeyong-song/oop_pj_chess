package pieces;

import util.Move;

public class Bishop extends Piece {
    public Bishop(Color color) {
        super(color);
        this.type = Type.BISHOP;
    }

    @Override
    public boolean validateMove(Move move) {
        // [FIXME] fill this one...
        /* Bishop moves diagonally...
         * && can move if there's no piece on the way
         * && can move if there's a piece that can be captured by Bishop */
        if(move.getCapturedPiece() == null // no piece on the way
                || (move.getCapturedPiece() != null // there's a piece that can be captured (different color)
                    && !move.getPiece().isSameColor(move.getCapturedPiece()))) {
            // Bishop moves diagonally (file diff == rank diff)
            if(move.getFileAbsDiff() == move.getRankAbsDiff()) {
                return true;
            }
        }
        return false;
    }
}
