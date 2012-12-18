package awesome.tony.fastfind;

/**
 * Node representing the essential element of the modified Trie used
 * for Aho Corasick data structure
 * @author
 */
public class Node {

    private final Node[] neighbors;
    private Node failureNode;
    private SearchTerm term;

    Node(final SearchTerm term) {
        this.neighbors = new Node[256];
        this.term = term;
    }

    Node() {
        neighbors = new Node[256];
        term = null;
    }

    void setFailureNode(final Node fail) {
        this.failureNode = fail;
    }

    /**
     * @return the node from which this node is related for failure cases 
     * (represents the longest valid subsequence from which this search term stems
     */
    public Node getFailureNode() {
        return failureNode;
    }

    /**
     * @return true if this node indicates a successfully matched search term; false otherwise
     */
    public boolean isMatchingNode() {
        return term != null;
    }

    void setSearchTerm(final SearchTerm term) {
        this.term = term;
    }
    
    /**
     * @return the term for which this node is relevant.
     */
    public SearchTerm getSearchTerm() {
        return term;
    }
    
    /**
     * @param index
     * @return the neighbor node for the given index
     * @throws IndexOutOfBoundsException
     */
    public Node getNeighbor(final int index){
        return neighbors[index]; 
    }
    
    void setNeighbor(final Node neighbor, final int index){
        neighbors[index] = neighbor;
    }
}
