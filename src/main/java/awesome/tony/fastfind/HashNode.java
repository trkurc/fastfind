package awesome.tony.fastfind;

import java.util.HashMap;

/**
 * Node representing the essential element of the modified Trie used
 * for Aho Corasick data structure
 * @author
 */
public class HashNode implements Node {

    //private final Node[] neighbors;
	private final HashMap<Integer, Node> neighbors2;
    private Node failureNode;
    private SearchTerm term;
    private boolean matchOnFailPath = false;
    
    HashNode(final SearchTerm term) {
       // this.neighbors = new Node[256];
        this.neighbors2 = new HashMap<Integer, Node>();

        this.term = term;
    }

    HashNode() {
        //neighbors = new Node[256];
        this.neighbors2 = new HashMap<Integer, Node>();
        term = null;
    }

    public void setFailureNode(final Node fail) {
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

    public boolean hasMatchOnFailPath(){
    	return this.matchOnFailPath;
    }
    
    public void setMatchOnFailPath(){
    	 this.matchOnFailPath = true;
    }
    
    public void setSearchTerm(final SearchTerm term) {
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
       // return neighbors[index];
    	return neighbors2.get(index);
    }
    
    public void setNeighbor(final Node neighbor, final int index){
       // neighbors[index] = neighbor;
    	neighbors2.put(index, neighbor);
    }
}
