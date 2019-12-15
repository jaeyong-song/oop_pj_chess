package util;

import board.Board;
import pieces.Piece;

import java.io.Serializable;

public class Move implements Serializable {

    private Piece piece;
    private Piece capturedPiece;
    private char originFile;
    private int originRank;
    public char destinationFile;
    public int destinationRank;

    public Move(char originFile, int originRank, char destinationFile, int destinationRank) {
        this.piece = Board.getSquare(originFile, originRank).getCurrentPiece();
        this.capturedPiece = Board.getSquare(destinationFile, destinationRank).getCurrentPiece();
        this.originFile = originFile;
        this.originRank = originRank;
        this.destinationFile = destinationFile;
        this.destinationRank = destinationRank;
    }

    public Move(Piece piece, char originFile, int originRank, char destinationFile, int destinationRank) {
        this.piece = piece;
        this.capturedPiece = Board.getSquare(destinationFile, destinationRank).getCurrentPiece();
        this.originFile = originFile;
        this.originRank = originRank;
        this.destinationFile = destinationFile;
        this.destinationRank = destinationRank;
    }

    public Move(Piece piece, Piece capturedPiece, char originFile, int originRank, char destinationFile, int destinationRank) {
        this.piece = piece;
        this.capturedPiece = capturedPiece;
        this.originFile = originFile;
        this.originRank = originRank;
        this.destinationFile = destinationFile;
        this.destinationRank = destinationRank;
    }

    public Piece getPiece() {
        return piece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public char getOriginFile() {
        return originFile;
    }

    public int getOriginRank() {
        return originRank;
    }

    public char getDestinationFile() {
        return destinationFile;
    }

    //[TODO]
    public void setDestinationFile(Move move){this.destinationFile = move.destinationFile;}

    public void setDestinationRank(Move move){this.destinationRank = move.destinationRank;}

    public int getDestinationRank() {
        return destinationRank;
    }

    // [FIXME] additional function which used frequently
    public int getFileAbsDiff() {
        return Math.abs(getDestinationFile() - getOriginFile());
    }
    public int getRankAbsDiff() {
        return Math.abs(getDestinationRank() - getOriginRank());
    }
    public int getFileDiff() {
        return getDestinationFile() - getOriginFile();
    }
    public int getRankDiff() {
        return getDestinationRank() - getOriginRank();
    }

}
