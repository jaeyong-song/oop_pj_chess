package util;

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

    //[FIXME] 각 piece에 대해 마지막 move만 가지고 있는 list
    public static List<Move> lastmove;



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
        if (moveRoundBuffer.size() == 2) {
            //[FIXME]

            moveHistory.add(new MoveRound(moveRoundBuffer.get(0), moveRoundBuffer.get(1)));
            moveRoundBuffer.clear();

        }

    }

    //[FIXME] save
    public static void saveLastMove(Move move){
        for(int i=0 ; i <lastmove.size(); i++) {
            if(move.getPiece() == lastmove.get(i).getPiece()) {
                //나중에 execute move를 해주는 걸 고려해서 Destination만 update해줬습니다.
                Move kkmove = new Move(lastmove.get(i).getPiece(), lastmove.get(i).getCapturedPiece(),
                        lastmove.get(i).getOriginFile(), lastmove.get(i).getOriginRank(),
                        move.getDestinationFile(), move.getDestinationRank());
                lastmove.set(i, kkmove);
                break;
            }
        }
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






    private void initialize() {
        moveHistory = new ArrayList<MoveRound>();
        moveRoundBuffer = new ArrayList<Move>();

        //[FIXME]
        lastmove = new ArrayList<Move>();


    }
}
