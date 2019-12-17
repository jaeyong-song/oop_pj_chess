package util;

import board.Board;
import board.Pair;
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

    // TO invert current MoveColor
    public static void invertCurrentMoveColor() {
        if(currentMoveColor.equals(Piece.Color.WHITE)) {
            currentMoveColor = Piece.Color.BLACK;
        } else {
            currentMoveColor = Piece.Color.WHITE;
        }
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
        //[TODO] whiteKingFile / whiteKingRank / blackKingFile / blackKingRank should be updated!!!!
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

        List<Pair> checkMakers = getAllCheckMakers();
        for(Pair p: checkMakers) {
            Piece maker = Board.getSquare(p.getFile(), p.getRank()).getCurrentPiece();
            if(move.getPiece().getColor() != maker.getColor()) {
                return true;
            }
        }
        Piece.Color mkrClr = Board.getSquare(checkMakers.get(0).getFile(), checkMakers.get(0).getRank()).getCurrentPiece().getColor();

        // 1. check if movement of king can overcome check
        // there's 8 ways for king's move
        char kingFile = PieceSet.getOpponentKingFile(mkrClr);
        int kingRank = PieceSet.getOpponentKingRank(mkrClr);
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
        Piece.Color opponentColor = mkrClr == Piece.Color.BLACK ? Piece.Color.WHITE : Piece.Color.BLACK;
        if(checkMakers.size() == 1) {
            Pair checkMaker = checkMakers.get(0);
            Square checkMakerSqr = Board.getSquare(checkMaker.getFile(), checkMaker.getRank());
            Piece piece = checkMakerSqr.getCurrentPiece();
            Pair opponentKing = PieceSet.getOpponentKingPosition(piece.getColor());
            List<Pair> pathPairs = getPathPairs(Board.getSquare(checkMaker.getFile(), checkMaker.getRank()).getCurrentPiece(),
                                                checkMaker.getFile(), checkMaker.getRank(), opponentKing.getFile(), opponentKing.getRank());
            for(int i = 1; i <= 8; i++) {
                for(char j = 'a'; j <= 'h'; j++) {
                    Square sqr = Board.getSquare(j, i);
                    Piece tmpP = sqr.getCurrentPiece();
                    if(tmpP != null) {
                        // 1) check if maker can be captured
                        if(validateMove(new Move(tmpP, j, i, checkMaker.getFile(), checkMaker.getRank()))) {
                            return false;
                        }
                        // 2) can move on the way of path
                        for(Pair p: pathPairs) {
                            if(validateMove(new Move(tmpP, j, i, p.getFile(), p.getRank()))) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        // 3. if getAllcheckMakers.size() >= 2
        if(checkMakers.size() >= 2) {
            // 1) check if capturing one of the checkMakers can make getAllCheckMakers().size == 0
            for(int chkMakerNum = 0; chkMakerNum < checkMakers.size(); chkMakerNum++) {
                for(int i = 0; i <= 8; i++) {
                    for(char j = 'a'; j <= 'h'; j++) {
                        Square sqr = Board.getSquare(j, i);
                        Piece tmpP = sqr.getCurrentPiece();
                        if(tmpP != null) {
                            Move mv = new Move(tmpP, j, i, checkMakers.get(chkMakerNum).getFile(), checkMakers.get(chkMakerNum).getRank());
                            if(validateMove(mv)) {
                                Board.executeMove(mv);
                                if(getAllCheckMakers().size() == 0) {
                                    Board.undoMove(mv);
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            // 2) get intersection Squares in the check paths of the checkMakers and check if one movement of some piece
            //    can block that intersection Squares
            Pair checkMaker = checkMakers.get(0);
            Square checkMakerSqr = Board.getSquare(checkMaker.getFile(), checkMaker.getRank());
            Piece piece = checkMakerSqr.getCurrentPiece();
            Pair opponentKing = PieceSet.getOpponentKingPosition(piece.getColor());
            List<Pair> intersectList = getPathPairs(Board.getSquare(checkMaker.getFile(), checkMaker.getRank()).getCurrentPiece(),
                    checkMaker.getFile(), checkMaker.getRank(), opponentKing.getFile(), opponentKing.getRank());
            for(int chkMakerNum = 1; chkMakerNum < checkMakers.size(); chkMakerNum++) {
                checkMaker = checkMakers.get(chkMakerNum);
                List<Pair> pathPairs = getPathPairs(Board.getSquare(checkMaker.getFile(), checkMaker.getRank()).getCurrentPiece(),
                        checkMaker.getFile(), checkMaker.getRank(), opponentKing.getFile(), opponentKing.getRank());
                intersectList = getIntersectPairs(intersectList, pathPairs);
                if(intersectList.size() == 0) {
                    return false;
                } else {
                    for(int pairN = 0; pairN < intersectList.size(); pairN++) {
                        for(int i = 0; i <= 8; i++) {
                            for(char j = 'a'; j <= 'h'; j++) {
                                Square sqr = Board.getSquare(j, i);
                                Piece tmpP = sqr.getCurrentPiece();
                                if(tmpP != null) {
                                    Pair p = intersectList.get(pairN);
                                    Move mv = new Move(tmpP, j, i, p.getFile(), p.getRank());
                                    if(validateMove(mv)) {
                                        Board.executeMove(mv);
                                        if(getAllCheckMakers().size() == 0) {
                                            Board.undoMove(mv);
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. all other cases
        return true;
    }

    // [FIXME] return pairs on my path
    private static List<Pair> getPathPairs(Piece p, char originFile, int originRank, char destFile, int destRank) {
        List<Pair> list = new LinkedList<Pair>();
        // Knight go over other pieces
        if(p.getType().equals(Piece.Type.KNIGHT)) {
            return list;
        }
        int fileAdvance = Integer.signum(destFile - originFile);
        int rankAdvance = Integer.signum(destRank - originRank);
        System.out.println(fileAdvance+rankAdvance);
        for(int file = originFile + fileAdvance, rank = originRank + rankAdvance;
            file != destFile || rank != destRank; file += fileAdvance, rank += rankAdvance) {
            list.add(new Pair((char)file, rank));
        }
        return list;
    }

    // [FIXME] intersection pairs of PairList
    private static List<Pair> getIntersectPairs(List<Pair> lst1, List<Pair> lst2) {
        List<Pair> list = new LinkedList<Pair>();
        for(Pair p1: lst1) {
            for(Pair p2: lst2) {
                if(p1.equals(p2)) {
                    list.add(p1);
                }
            }
        }
        return list;
    }

    private static List<Pair> getAllCheckMakers() {
        // [FIXME] make private method used for isCheckMate(Move move)
        // find all checkMakers by iteration of all pieces on the board
        // move: origin -> dest
        // check: dest -> King is valid?? use validateMove method
        List<Pair> checkMakers = new LinkedList<Pair>();
        for(int i = 1; i <= 8; i++) {
            for(char j = 'a'; j <= 'h'; j++) {
                Square sqr = Board.getSquare(j, i);
                Piece p = sqr.getCurrentPiece();
                if(p != null) {
                    if(validateMove(new Move(p, j, i, PieceSet.getOpponentKingFile(p.getColor()),
                            PieceSet.getOpponentKingRank(p.getColor())), true)) {
                        checkMakers.add(new Pair(j, i));
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
