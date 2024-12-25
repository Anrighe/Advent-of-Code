/** Class representing an empty memory block. */
public class EmptyMemoryBlock implements MemoryBlock {

    /** Constructs an empty memory block. */
    public EmptyMemoryBlock() {}

    /**
     * Checks if this is an empty block.
     * @return true, as a {@class EmptyMemoryBlock} is always an empty memory block.
     */
    public boolean isEmptyBlock() {
        return true;
    }

    @Override
    public String toString() {
        return ".";
    }
}