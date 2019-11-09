package pieces;

import util.Move;

public class Knight extends Piece {
    public Knight(Color color) {
        super(color);
        this.type = Type.KNIGHT;
    }

    @Override
    public boolean validateMove(Move move) {
        // [FIXME] fill this one...
        /* Knight moves 2 levels for one side and diagonally one level...
         * && can move if there's no piece on the way
         * && can move if there's a piece that can be captured */
        if(move.getCapturedPiece() == null // no piece on the way
                || (move.getCapturedPiece() != null // there's a piece that can be captured (different color)
                && !move.getPiece().isSameColor(move.getCapturedPiece()))) {
            // Knight moves 2 levels for one side and diagonally one level...
            // it means that fileDiff*rankDiff == 2
            if(move.getFileAbsDiff()*move.getRankAbsDiff() == 2) {
                return true;
            }
        }
        return false;
    }
}
