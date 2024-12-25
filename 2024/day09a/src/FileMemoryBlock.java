/** Represents a memory block identified by a file block ID. */
public class FileMemoryBlock implements MemoryBlock {
    
    private long fileBlockId; 

    /**
     * Constructs a memory block with a specific Id.
     * @param fileBlockId File ID for the block.
     */    
    public FileMemoryBlock(long fileBlockId) {
        this.fileBlockId = fileBlockId;
    }

    /**
     * Checks if this is an empty block.
     * @return false, as a {@class FileMemoryBlock} cannot be an empty memory block.
     */
    public boolean isEmptyBlock() {
        return false;
    }

    /**
     * Gets the file block ID.
     * @return File block ID.
     */
    public long getFileBlockId() {
        return fileBlockId;
    }

    /**
     * Sets the file block ID.
     * @param fileBlockId New file block ID.
     */    
    public void setFileBlockId(long fileBlockId) {
        this.fileBlockId = fileBlockId;
    }

    @Override
    public String toString() {
        return "" + fileBlockId;
    }
}