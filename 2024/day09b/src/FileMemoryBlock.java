/** Represents a memory block identified by a file block ID. */
public class FileMemoryBlock implements MemoryBlock {
    
    private long fileBlockId;
    private int size;

    /**
     * Constructs a memory block with a specific Id.
     * @param fileBlockId File ID for the block.
     */    
    public FileMemoryBlock(long fileBlockId, int size) {
        this.fileBlockId = fileBlockId;
        this.size = size;
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
     * Gets the size of the file block.
     * @return File block size.
     */    
    public int getSize() {
        return size;
    }

    /**
     * Sets the file block ID.
     * @param fileBlockId New file block ID.
     */    
    public void setFileBlockId(long fileBlockId) {
        this.fileBlockId = fileBlockId;
    }

    /**
     * Sets the size of the file block.
     * @param fileBlockId New file block size.
     */        
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        String ret = "";
        for (int i = 0; i < size; ++i)
            ret += "" + fileBlockId;
        return ret;
    }
}