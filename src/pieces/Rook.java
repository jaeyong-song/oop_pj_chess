package pieces;

import util.Move;

public class Rook extends Piece {

    public Rook(Color color) {
        super(color);
        this.type = Type.ROOK;
    }

    @Override
    public boolean validateMove(Move move) {
        // executeMove or capture
        if ((move.getCapturedPiece() == null)
                || (move.getCapturedPiece() != null
                    && !move.getPiece().isSameColor(move.getCapturedPiece()))) {
            //[FIXME] I created another codes for the given codes...
            // along file
            if(move.getRankAbsDiff() != 0 && move.getFileAbsDiff() == 0) {
                return true;
            }
            // along rank
            if(move.getRankAbsDiff() == 0 && move.getFileAbsDiff() != 0) {
                return true;
            }

            /* This given codes are not efficient
            * so I commented on these... */
            /*// along file
            if (move.getDestinationFile() == move.getOriginFile()
                    && move.getDestinationRank() != move.getOriginRank()) {
                return true;
            }
            // along rank
            if (move.getDestinationFile() != move.getOriginFile()
                    && move.getDestinationRank() == move.getOriginRank()) {
                return true;
            }*/
        }

        // all other cases
        return false;
    }

}
