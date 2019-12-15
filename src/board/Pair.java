package board;

public class Pair {
    private char file;
    private int rank;
    public Pair(char file, int rank) {
        this.file = file;
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public char getFile() {
        return file;
    }

    public void setFile(char file) {
        this.file = file;
    }

    public boolean equals(Object other) {
        if(this.getClass() == other.getClass()) {
            Pair otherP = (Pair) other;
            if(this.file == otherP.getFile()
                    && this.rank == otherP.getRank()) {
                return true;
            }
        }
        return false;
    }
}
