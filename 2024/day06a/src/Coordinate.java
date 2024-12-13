import java.util.Objects;

/**
 * Represents a two-dimensional coordinate with row and col values.
 * Provides methods to manipulate and compare coordinates, 
 * as well as calculate the midpoint between two coordinates.
 */
public class Coordinate {
    
    private int row;
    private int col;
 
    /**
     * Constructs a {@code Coordinate} with the specified row and col values.
     *
     * @param row
     * @param col
     */
    public Coordinate (int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }

    /**
     * Returns a string representation of this {@code Coordinate}.
     * The format is {@code [row, col]}.
     *
     * @return a string representation of this {@code Coordinate}
     */
    @Override
    public String toString() {

        return String.format("[%s, %s]", row, col);
    }

    /**
     * Compares this {@code Coordinate} to the specified object.
     * The result is {@code true} if and only if the argument is not {@code null}, 
     * is a {@code Coordinate} object, and has the same row and col values as this {@code Coordinate}.
     *
     * @param obj the object to compare with
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coordinate that = (Coordinate) obj;
        return this.row == that.row && this.col == that.col;
    }  

    /**
     * Returns a hash code value for this {@code Coordinate}.
     * The hash code is computed based on the row and col coordinates.
     *
     * @return a hash code value for this {@code Coordinate}
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    public Coordinate add(Coordinate toAdd) {
        return new Coordinate(this.row + toAdd.getRow(), this.col + toAdd.getCol());
    }
}