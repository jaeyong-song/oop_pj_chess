package util;

import pieces.Pawn;
import pieces.Piece;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MoveLogger {

    /**
     * Wrapper class for a pair of moves.
     */
    private static class MoveRound {
        private Move whiteMove;
        private Move blackMove;

        public MoveRound(Move whiteMove, Move blackMove) {
            this.whiteMove = whiteMove;
            this.blackMove = blackMove;
        }

    }

    private static List<MoveRound> moveHistory;
    private static List<Move> moveRoundBuffer;

    //[FIXME]
    public static List<Move> lastmove;
    private static int i;
    public static ArrayList<Piece> capturedPieces = new ArrayList<>();



    private static MoveLogger moveLoggerInstance = new MoveLogger();

    public static MoveLogger getInstance() {
        return moveLoggerInstance;
    }

    private MoveLogger() {
        initialize();
    }

    /**
     * Add a executeMove to the history
     * @param move
     */
    public static void addMove(Move move) {
        moveRoundBuffer.add(move);
        saveLastMove(move);
        counti(move);
        savecapturedPieces(move);
        if (moveRoundBuffer.size() == 2) {
            //[FIXME]

            moveHistory.add(new MoveRound(moveRoundBuffer.get(0), moveRoundBuffer.get(1)));
            moveRoundBuffer.clear();

        }

    }

    //[FIXME] save
    public static void saveLastMove(Move move){

        lastmove.add(move);
    }


//[FIXME] save
    public static void lastmoveout(){
        try {
            ObjectOutputStream objout =
                    new ObjectOutputStream(new FileOutputStream("objstrings.dat"));
            objout.writeObject(lastmove);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
//[FIXME] load
    public static ArrayList<Move> lastmovein(){
        try {
            ObjectInputStream objIn =
                    new ObjectInputStream(new FileInputStream("objstrings.dat"));
            ArrayList<Move> lastmovefromf = (ArrayList<Move>)objIn.readObject();
            return lastmovefromf;
        }
        catch (ClassNotFoundException|IOException a) {
            a.printStackTrace();
            return new ArrayList<>();
        }
    }

    //[FIXME] 50 Draw
    /*기물의 포획이 없고, 폰의 움직임이 없는 상태로 50수가 되면 Draw*/
    private static void counti(Move move) {
        if (move.getPiece().getType()== Piece.Type.PAWN | move.getCapturedPiece()!=null) {
            i = 0;
        }
        else {
            i++;
        }
        if (i==50) {
            Core.getGameModel().gameFrame.show50DrawDialog();

        }

    }

    //[FIXME] 잡힌 Pieces들 저장
    private static void savecapturedPieces(Move move) {
        if (move.getCapturedPiece()!=null){
            capturedPieces.add(move.getCapturedPiece());
            //System.out.println(capturedPieces);
            //System.out.println(countPiece(capturedPieces, Piece.Type.PAWN));
        }

        if (countPiece(capturedPieces, Piece.Type.PAWN) == 16
            && countPiece(capturedPieces, Piece.Type.ROOK) ==4
            && countPiece(capturedPieces, Piece.Type.QUEEN) ==2) {
            Core.getGameModel().gameFrame.showinsufficentpiecesDialog();
        }


    }

    public static int countPiece(ArrayList<Piece> list, Piece.Type k) {
        int cnt = 0;
        for(int i = 0; i < list.size() ; i++) {
            if (list.get(i).getType() == k) {
                cnt++;
            }
        }
        return cnt;
    }






    private void initialize() {
        moveHistory = new ArrayList<MoveRound>();
        moveRoundBuffer = new ArrayList<Move>();

        //[FIXME]
        lastmove = new ArrayList<Move>();


    }
}
