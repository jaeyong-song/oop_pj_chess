package pieces;

import util.Move;

public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color);
        this.type = Type.PAWN;
    }

    @Override
    public boolean validateMove(Move move) {
        // [FIXME] fill this one...
        /* Pawns have many cases for move
        * 1. first move: moves one/two ranks, cannot capture
        * 2. move forward: cannot capture
        * 3. move diagonal if and only if it captures
        * BE CAREFUL: the order should be 3 -> 1 -> 2
        * because 'move diagonal' has upper priority ..*/

        // color for if cases
        Color clr = move.getPiece().getColor();

        // 3. move diagonal if and only if it captures
        if(clr.equals(Color.WHITE)) {
            if(move.getFileAbsDiff()*move.getRankDiff() == 1 // move diagonally forward
                    && move.getCapturedPiece() != null // should capture
                    && !move.getPiece().isSameColor(move.getCapturedPiece())) { // diff color
                return true;
            }
        } else if(clr.equals(Color.BLACK)) {
            if(move.getFileAbsDiff()*move.getRankDiff() == -1 // move diagonally forward
                    && move.getCapturedPiece() != null // should capture
                    && !move.getPiece().isSameColor(move.getCapturedPiece())) { // diff color
                return true;
            }
        }

        // 1. first move
        if(clr.equals(Color.WHITE)) {
            if(move.getOriginRank() == 2 // at starting point
                    && move.getFileAbsDiff() == 0 // along file
                    && move.getRankDiff() >= 1 // move one rank
                    && move.getRankDiff() <= 2 // or two ranks
                    && move.getCapturedPiece() == null) { // no capturing
                return true;
            }
        } else if(clr.equals(Color.BLACK)) {
            if(move.getOriginRank() == 7 // at starting point
                    && move.getFileAbsDiff() == 0 // along file
                    && move.getRankDiff() >= -2 // move two ranks
                    && move.getRankDiff() <= -1 // or one rank
                    && move.getCapturedPiece() == null) { // no capturing
                return true;
            }
        }

        // 2. move forward: cannot capture
        if(clr.equals(Color.WHITE)) {
            if(move.getFileAbsDiff() == 0 // along file
                    && move.getRankDiff() == 1 // move forward 1 rank
                    && move.getCapturedPiece() == null) { // no capturing
                return true;
            }
        } else if(clr.equals(Color.BLACK))  {
            if(move.getFileAbsDiff() == 0 // along file
                    && move.getRankDiff() == -1 // move forward -1 rank
                    && move.getCapturedPiece() == null) { // no capturing
                return true;
            }
        }

        // others
        return false;
    }
}
