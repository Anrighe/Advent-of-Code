/** Represents a node in the abstract syntax tree, with addition and multiplication children. */
public class Node {

    private long value;
    private Node nodeToAdd;
    private Node nodeToMultiply;
    private Node nodeToConcatenate;

    /**
     * Constructs a node with a given value and no children.
     * @param nodeValue The value of the node.
     */    
    public Node(long nodeValue) {
        this.value = nodeValue;
        nodeToAdd = null;
        nodeToMultiply = null;
        nodeToConcatenate = null;
    }

    /**
     * Constructs a node with a given value and specified children.
     * @param nodeValue The value of the node.
     * @param nodeToAdd Child node representing addition.
     * @param nodeToMultiply Child node representing multiplication.
     * @param nodeToConcatenateToSet Child node representing concatenation.
     */    
    public Node(long nodeValue, Node nodeToAddToSet, Node nodeToMultiplyToSet, Node nodeToConcatenateToSet) {
        this.value = nodeValue;
        nodeToAdd = nodeToAddToSet;
        nodeToMultiply = nodeToMultiplyToSet;
    }

    public long getValue() { return value; }
    public Node getNodeToAdd() { return nodeToAdd; }
    public Node getNodeToMultiply() { return nodeToMultiply; }
    public Node getNodeToConcatenate() { return nodeToConcatenate; }


    public void setValue(int valueToSet) { value = valueToSet; }
    public void setNodeToAdd(Node nodeToSet) { nodeToAdd = nodeToSet; }
    public void setNodeToMultiply(Node nodeToSet) { nodeToMultiply = nodeToSet; }
    public void setNodeToConcatenate(Node nodeToSet) { nodeToConcatenate = nodeToSet; }

    public String toString() {
        return String.format("(%s): [+%s][*%s][||%s]", value, nodeToAdd, nodeToMultiply, nodeToConcatenate);
    }
}
