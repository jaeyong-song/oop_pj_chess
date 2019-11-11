package util;

import board.Board;
import pieces.Piece;
import pieces.PieceSet;

import java.util.List;

public class MoveValidator {

    private static MoveValidator ourInstance = new MoveValidator();

    public static MoveValidator getInstance() {
        return ourInstance;
    }

    private MoveValidator() {
        currentMoveColor = Piece.Color.WHITE;
    }

    private static Piece.Color currentMoveColor;

    public static boolean validateMove(Move move) {
        return validateMove(move, false);
    }

    public static boolean validateMove(Move move, boolean ignoreColorCheck) {
        // check for out of bounds
        if (move.getDestinationFile() < 'a' || move.getDestinationFile() > 'h'
                || move.getDestinationRank() < 1 || move.getDestinationRank() > 8) {
            return false;
        }

        // check for valid origin
        if (move.getPiece() == null) {
            return false;
        }

        // check for valid color
        if (!move.getPiece().getColor().equals(currentMoveColor) && !ignoreColorCheck) {
            return false;
        }

        // check for valid destination
        if (move.getCapturedPiece() != null) {
            if (move.getPiece().getColor().equals(move.getCapturedPiece().getColor())) {
                return false;
            }
        }

        // check for piece rule
        if (!move.getPiece().validateMove(move)) {
            return false;
        }

        // check for clear path
        if (!validateClearPath(move)) {
            return false;
        }

        currentMoveColor = currentMoveColor.equals(Piece.Color.WHITE) ? Piece.Color.BLACK : Piece.Color.WHITE;
        return true;
    }

    public static boolean isCheckMove(Move move) {
        // [TODO]-check
        /* check if after this move
        * this piece can capture King or not */
        // move: origin -> dest
        // check: dest -> King is valid?? use validateMove method
        // +a) [FIXME] there can be cases that movement of piece can trigger other piece to make check
        int newOriginFile = move.getDestinationFile();
        int newOriginRank = move.getDestinationRank();
        return validateMove(new Move(move.getPiece(), (char)newOriginFile, newOriginRank,
                PieceSet.getOpponentKingFile(move.getPiece().getColor()), PieceSet.getOpponentKingRank(move.getPiece().getColor())), true);
    }

    public static boolean isCheckMate(Move move) {
        // TODO-check
        /* firstly, this move must be check(but already checked in usage)
        * execute temporary movement and check if check can be overcome by one move of a piece
        * 1. find all pieces that make check (called frequently => private Method)
        * 2. find if movement of one piece can make getAllCheckMaker().size() == 0
        *   => == 0 : return false(no checkmate) / != 0: return true(checkmate);
        *   => HOW: one movement on the ways of check pieces or
        *           capturing check pieces can make getAllCheckMaker().size() == 0*/

        return false;
    }

    private static List<Piece> getAllCheckMaker() {
        // [TODO] make private method used for isCheckMate(Move move)
        return null;
    }

    /* origin-destination position was checked by validateMove
    * so, by validateClearPath Method we should check if it is clear path to go dest*/
    private static boolean validateClearPath(Move move) {
        // [FIXME]-movement

        /* There's a lot of cases for path
        * 1. one level move: already checked -> return true
        * 2. Knight move: go over for any kind of pieces -> return true
        * 3. diagonal: no other pieces except dest -> return true else false
        * 4. along file/rank: no other pieces except dest -> return true else false */

        // 1. one level move
        if(move.getFileAbsDiff()*move.getRankAbsDiff() <= 1) {
            return true;
        }

        // 2. Knight move
        if(move.getFileAbsDiff()*move.getRankAbsDiff() == 2) {
            return true;
        }

        // 3. diagonal
        if(move.getFileAbsDiff() == move.getRankAbsDiff()) { // diagonal move
            // direction finding
            int fileToward = move.getFileDiff() > 0 ? 1 : -1;
            int rankToward = move.getRankDiff() > 0 ? 1 : -1;

            int destFile = move.getDestinationFile();
            int destRank = move.getDestinationRank();

            int i = move.getOriginFile() + fileToward;
            int j = move.getOriginRank() + rankToward;

            // while get to the destination
            while(i != destFile && j != destRank) {
                // if there's some pieces on the way of path -> return false
                if(Board.getSquare((char)i, j).getCurrentPiece() != null) {
                    return false;
                }
                i += fileToward; j += rankToward;
            }
            // there's no piece on the way of path -> return true
            return true;
        }

        // 4. along file/rank
        if(move.getFileAbsDiff() == 0 && move.getRankAbsDiff() != 0) { // file
            int rankToward = move.getRankDiff() > 0 ? 1 : -1;
            int originRank = move.getOriginRank();
            int destRank = move.getDestinationRank();
            for(int i = originRank + rankToward; i != destRank; i += rankToward) {
                if(Board.getSquare(move.getOriginFile(), i).getCurrentPiece() != null) {
                    return false; // there's some pieces on the way -> false
                }
            }
            return true;
        }
        else if(move.getRankAbsDiff() != 0 && move.getRankAbsDiff() == 0) { // rank
            int fileToward = move.getFileDiff() > 0 ? 1: -1;
            int originFile = move.getOriginFile();
            int destFile = move.getDestinationFile();
            for(int i = originFile + fileToward; i != destFile; i += fileToward) {
                if(Board.getSquare((char)i, move.getOriginRank()).getCurrentPiece() != null) {
                    return false; // there's some pieces on the way -> false
                }
            }
            return true;
        }

        // all other cases
        return false;
    }

}
