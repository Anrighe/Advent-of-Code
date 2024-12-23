import java.util.Objects;

/**
 * Represents an antinode on the map (could also be considerated as a set of coordinates).
 */
public class Antinode {
    private int row;
    private int column;

    /**
     * Constructs an antinode at the specified row and column.
     * @param row Row of the antinode
     * @param column Column of the antinode
     */
    public Antinode(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", row, column);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Antinode antinode = (Antinode) obj;
        return row == antinode.row && column == antinode.column;
    }
}
