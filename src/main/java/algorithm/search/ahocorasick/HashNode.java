package algorithm.search.ahocorasick;

import java.util.HashMap;

/**
 * Node representing the essential element of the modified Trie used for Aho
 * Corasick data structure
 *
 */
public class HashNode implements Node {

    private final HashMap<Integer, Node> neighbors2;
    private Node failureNode;
    private SearchTerm term;
    private boolean matchOnFailPath = false;

    HashNode(final SearchTerm term) {
        this.neighbors2 = new HashMap<>();
        this.term = term;
    }

    HashNode() {
        this.neighbors2 = new HashMap<>();
        term = null;
    }

    @Override
    public void setFailureNode(final Node fail) {
        this.failureNode = fail;
    }

    /**
     * @return the node from which this node is related for failure cases
     * (represents the longest valid subsequence from which this search term
     * stems
     */
    @Override
    public Node getFailureNode() {
        return failureNode;
    }

    /**
     * @return true if this node indicates a successfully matched search term;
     * false otherwise
     */
    @Override
    public boolean isMatchingNode() {
        return term != null;
    }

    @Override
    public boolean hasMatchOnFailPath() {
        return this.matchOnFailPath;
    }

    /**
     *
     */
    @Override
    public void setMatchOnFailPath() {
        this.matchOnFailPath = true;
    }

    @Override
    public void setSearchTerm(final SearchTerm term) {
        this.term = term;
    }

    /**
     * @return the term for which this node is relevant.
     */
    @Override
    public SearchTerm getSearchTerm() {
        return term;
    }

    /**
     * @param index
     * @return the neighbor node for the given index
     * @throws IndexOutOfBoundsException
     */
    @Override
    public Node getNeighbor(final int index) {
        return neighbors2.get(index);
    }

    @Override
    public void setNeighbor(final Node neighbor, final int index) {
        neighbors2.put(index, neighbor);
    }
}
