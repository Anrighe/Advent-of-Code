/**
 * Represents an antenna with a specific frequency and location (set of coordinates).
 */
public class Antenna {
    private char type;
    private int row;
    private int column;
    
    /**
     * Constructs an antenna with the specified frequency, row, and column.
     * @param type Frequency type of the antenna
     * @param row Row of the antenna
     * @param column Column of the antenna
     */
    public Antenna(char type, int row, int column) {
        this.type = type;
        this.row = row;
        this.column = column;
    }
    
    public char getType() {
        return type;
    }

    public int getRow() {
        return row;
    }
    
    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return String.format("Antenna: %s, row: %s, column: %s", type, row, column);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Antenna)) {
            return false;
        }

        Antenna antenna = (Antenna) obj;

        return antenna.type == this.type && antenna.row == this.row && antenna.column == this.column;
    }
}
