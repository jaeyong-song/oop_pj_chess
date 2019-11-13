package util;

import board.Board;
import board.Square;
import pieces.Piece;
import pieces.PieceSet;

import java.util.LinkedList;
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
        // [FIXME]-check
        /* check if after this move
        * this piece can capture King or not */
        // +a) there can be cases that movement of piece can trigger other piece to make check
        // 1. this move makes other piece to make check
        // 2. this move makes this piece to make check

        return getAllCheckMakers().size() >= 1 ? true : false;
    }

    public static boolean isCheckMate(Move move) {
        // TODO-check
        /* firstly, this move must be check(but already checked in usage)
        * check if check can be overcome by one move of a piece
        * 1. find all pieces that make check (getAllCheckMakers();)
        * 2. find if movement of one piece can make getAllCheckMakers().size() == 0
        *   => == 0 : return false(no checkmate) / != 0: return true(checkmate) */

        /* !!!Detail Algorithm!!!
        * 1. check if movement of king can overcome check
        * 2. if getAllcheckMakers().size() == 1
        *   => 1) check if maker can be captured, 2) can move on the way of maker's check path
        * 3. if getAllcheckMakers().size() >= 2
        *   => 1) check if capturing one of the checkMakers can make getAllcheckMakers().size == 0
        *   => 2) get intersection Squares in the check paths of the checkMakers and check if
        *         one movement of some piece can block that intersection Squares
        * 4. all other cases => checkMate*/

        List<Square> checkMakers = getAllCheckMakers();
        Piece.Color mkrClr = checkMakers.get(0).getCurrentPiece().getColor();
        char kingFile = PieceSet.getOpponentKingFile(mkrClr);
        int kingRank = PieceSet.getOpponentKingRank(mkrClr);

        // 1. check if movement of king can overcome check
        // there's 8 ways for king's move
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                // no movement or out of range
                if(i == 0 && j == 0 || kingFile+i < 'a' || kingFile+i > 'h'
                        || kingRank+j < 1 || kingRank+j > 8) {
                    continue;
                }
                Move mv = new Move(kingFile,kingRank, (char)(kingFile+i), kingRank+j);
                if(validateMove(mv)) {
                    Board.executeMove(mv);
                    if(getAllCheckMakers().size() == 0) {
                        Board.undoMove(mv);
                        return false;
                    }
                    Board.undoMove(mv);
                }
            }
        }

        // 2. if getAllcheckMakers().size() == 1
        if(checkMakers.size() == 1) {

        }

        // 3. if getAllcheckMakers.size() >= 2
        if(checkMakers.size() >= 2) {

        }

        // 4. all other cases
        return true;
    }

    private static List<Square> getAllCheckMakers() {
        // [FIXME] make private method used for isCheckMate(Move move)
        // find all checkMakers by iteration of all pieces on the board
        // move: origin -> dest
        // check: dest -> King is valid?? use validateMove method
        List<Square> checkMakers = new LinkedList<Square>();
        for(int i = 1; i <= 8; i++) {
            for(char j = 'a'; j < 'h'; j++) {
                Square sqr = Board.getSquare(j, i);
                Piece p = sqr.getCurrentPiece();
                if(p != null) {
                    if(validateMove(new Move(p, j, i, PieceSet.getOpponentKingFile(p.getColor()),
                            PieceSet.getOpponentKingRank(p.getColor())), true)) {
                        checkMakers.add(sqr);
                    }
                }
            }
        }
        return checkMakers;
    }

    /* origin-destination position was checked by validateMove
    * so, by validateClearPath Method we should check if it is clear path to go dest*/
    private static boolean validateClearPath(Move move) {
        // [FIXME]-movement
        //[FIXED] I found a case that Queen can go over pawn...

        /* There's a lot of cases for path
        * 1. one level move: already checked -> return true
        * 2. Knight move: go over for any kind of pieces -> return true
        * 3. diagonal: no other pieces except dest -> return true else false
        * 4. along file/rank: no other pieces except dest -> return true else false */

        // 1. one level move
        if(move.getFileAbsDiff() == 1 || move.getRankAbsDiff() == 1) {
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
