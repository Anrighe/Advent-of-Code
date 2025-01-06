import java.util.List;

/** Represents a stone in the simulation, with a value that can transform or split based on specific rules */
public class Stone {
    private long value;

    public Stone(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value + "";
    }

    /**
     * Splits the stone into two stones if its value has an even number of digits.
     * 
     * The left half of the digits forms the first stone, and the right half 
     * forms the second stone. Throws an {@link AssertionError} if the value 
     * has an odd number of digits.
     * 
     * @return A list containing two new {@link Stone} objects.
     * @throws AssertionError If the stone's value has an odd number of digits.
     */
    public List<Stone> split() throws AssertionError{
        String valueString = String.valueOf(value);
        assert valueString.length() %2 == 0;

        Stone firstStone = new Stone(Long.valueOf(valueString.substring(0, (valueString.length()/2))));
        Stone secondStone = new Stone (Long.valueOf(valueString.substring(valueString.length()/2, valueString.length())));

        return List.of(firstStone, secondStone);
    }

}
