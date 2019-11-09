package pieces;

import util.Move;

public class Queen extends Piece {
    public Queen(Color color) {
        super(color);
        this.type = Type.QUEEN;
    }

    @Override
    public boolean validateMove(Move move) {
        // [FIXME] fill this one...
        /* Queen moves diagonally or one sided...
         * && can move if there's no piece on the way
         * && can move if there's a piece that can be captured */
        if(move.getCapturedPiece() == null // there's no piece to capture
                || (move.getCapturedPiece() != null // there's piece of diff color
                    && !move.getPiece().isSameColor(move.getCapturedPiece()))) {
            // diagonal move
            if(move.getFileAbsDiff() == move.getRankAbsDiff()) {
                return true;
            }
            // move along file
            if(move.getRankAbsDiff() != 0 && move.getFileAbsDiff() == 0) {
                return true;
            }
            // move along rank
            if(move.getRankAbsDiff() == 0 && move.getFileAbsDiff() != 0) {
                return true;
            }
        }
        return false;
    }
}
