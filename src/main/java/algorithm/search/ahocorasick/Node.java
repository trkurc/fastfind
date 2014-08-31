package algorithm.search.ahocorasick;

/**
 * Node representing the essential element of the modified Trie used for Aho
 * Corasick data structure
 *
 * @author
 */
public interface Node {

    void setFailureNode(final Node fail);

    /**
     * @return the node from which this node is related for failure cases
     * (represents the longest valid subsequence from which this search term
     * stems
     */
    public Node getFailureNode();

    /**
     * @return true if this node indicates a successfully matched search term;
     * false otherwise
     */
    public boolean isMatchingNode();

    public boolean hasMatchOnFailPath();

    public void setMatchOnFailPath();

    public void setSearchTerm(final SearchTerm term);

    /**
     * @return the term for which this node is relevant.
     */
    public SearchTerm getSearchTerm();

    /**
     * @param index
     * @return the neighbor node for the given index
     * @throws IndexOutOfBoundsException
     */
    public Node getNeighbor(final int index);

    public void setNeighbor(final Node neighbor, final int index);
}
