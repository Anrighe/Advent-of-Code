import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Represents an abstract syntax tree (AST) for evaluating equations.
 * Nodes represent numbers, and edges represent addition or multiplication operations.
 */
public class AbstractSyntaxTree {

    private Node root;

    /**
     * Constructs an AST with a single root node.
     * @param nodeValue Initial value for the root node.
     */
    public AbstractSyntaxTree(long nodeValue) {
        root = new Node(nodeValue);
    }

    /**
     * Inserts a new value as children of the given node with addition and multiplication edges.
     * @param node The node to which new children are added.
     * @param newNodeValue The value of the new node.
     */
    public void insert(Node node, long newNodeValue) {
        node.setNodeToAdd(new Node(newNodeValue));
        node.setNodeToMultiply(new Node(newNodeValue));
    }

    /**
     * Performs a depth-first search to collect all nodes in the tree.
     * @param node The starting node for the traversal.
     * @return A list of all nodes in the tree.
     */    
    public List<Node> dfsVisitGetAllNodes(Node node) {
        List<Node> nodes = new ArrayList<>();  

        if (node == null)
            return nodes;
 
        nodes.add(node);

        if (node.getNodeToAdd() != null)
            nodes.addAll(dfsVisitGetAllNodes(node.getNodeToAdd()));

        if (node.getNodeToAdd() != null)
            nodes.addAll(dfsVisitGetAllNodes(node.getNodeToMultiply()));
            
        return nodes;
    }

    /**
     * Recursively calculates all possible results for equations formed by the tree.
     * @param calculatedValue The current value being evaluated.
     * @param node The current node in the traversal.
     * @return A list of all possible results.
     */    
    public List<Long> getAllPossibleEquationsResults(long calculatedValue, Node node) {
        List<Long> result = new ArrayList<>();

        if (node == null) {
            result.add(calculatedValue);
            return result;
        }

        result.addAll(getAllPossibleEquationsResults(calculatedValue + node.getValue(), node.getNodeToAdd()));
        result.addAll(getAllPossibleEquationsResults(calculatedValue * node.getValue(), node.getNodeToMultiply()));

        return result;
    }

    /**
     * Retrieves all leaf nodes in the tree.
     * @return A list of leaf nodes.
     */    
    public List<Node> getAllLeafs() {
        List<Node> leafs = new ArrayList<>();
        List<Node> allNodes = dfsVisitGetAllNodes(root);
        leafs = allNodes.stream()
                        .filter(n -> n.getNodeToAdd() == null && n.getNodeToMultiply() == null)
                        .collect(Collectors.toList());
        return leafs;
    }
 
    public Node getRoot() { return root; }
    public void setRoot(Node newRoot) { root = newRoot; }
}