/** Represents a coordinate in a 2D matrix with row and column indices. */
public class Coordinate {
    private int row;
    private int col;

    /**
     * Constructs a Coordinate with specified row and column indices.
     *
     * @param row Row index.
     * @param col Column index.
     */
    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public String toString() {
        String ret = String.format("[%s, %s]", row, col);
        return ret;
    }

    /**
     * Checks equality between this coordinate and another object.
     *
     * @param obj The object to compare.
     * @return True if the object is a Coordinate with the same row and column indices; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Coordinate))
            return false;

        Coordinate coordinate = (Coordinate) obj;
        return coordinate.getRow() == row && coordinate.getCol() == col;
    }
}