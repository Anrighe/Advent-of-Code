import java.util.Objects;

/**
 * Represents a two-dimensional coordinate with x and y values.
 * Provides methods to manipulate and compare coordinates, 
 * as well as calculate the midpoint between two coordinates.
 */
public class Coordinate {
    
    private int x;
    private int y;
 
    /**
     * Constructs a {@code Coordinate} with the specified x and y values.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Coordinate (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }

    /**
     * Calculates the midpoint between two coordinates.
     * The midpoint is determined by averaging the x and y coordinates of both points.
     *
     * @param coord1 the first coordinate
     * @param coord2 the second coordinate
     * @return a new {@code Coordinate} representing the midpoint
     */
    public static Coordinate calculateMiddlePoint(Coordinate coord1, Coordinate coord2) {
        int middleX = (coord1.getX() + coord2.getX())/2;
        int middleY = (coord1.getY() + coord2.getY())/2;

        return new Coordinate(middleX, middleY);
    }

    /**
     * Returns a string representation of this {@code Coordinate}.
     * The format is {@code [x, y]}.
     *
     * @return a string representation of this {@code Coordinate}
     */
    @Override
    public String toString() {

        return String.format("[%s, %s]", x, y);
    }

    /**
     * Swaps the x and y coordinates of this {@code Coordinate}.
     */
    public void transposeCoordinates() {
        int tmp = x;
        x = y;
        y = tmp;
    }

    /**
     * Compares this {@code Coordinate} to the specified object.
     * The result is {@code true} if and only if the argument is not {@code null}, 
     * is a {@code Coordinate} object, and has the same x and y values as this {@code Coordinate}.
     *
     * @param obj the object to compare with
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coordinate that = (Coordinate) obj;
        return this.x == that.x && this.y == that.y;
    }  

    /**
     * Returns a hash code value for this {@code Coordinate}.
     * The hash code is computed based on the x and y coordinates.
     *
     * @return a hash code value for this {@code Coordinate}
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}