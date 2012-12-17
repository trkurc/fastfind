package awesome.tony.fastfind;

import java.util.Arrays;

/**
 *
 * @author
 */
public class Node {

    private final Node[] neighbors;
    private Node failureNode;
    private byte[] match;

    Node(byte[] match) {
        this.neighbors = new Node[256];
        this.match = Arrays.copyOf(match, match.length);
    }

    Node() {
        neighbors = new Node[256];
        match = null;
    }

    void setFailureNode(final Node fail) {
        this.failureNode = fail;
    }

    public Node getFailureNode() {
        return failureNode;
    }

    public boolean hasMatch() {
        return match != null;
    }

    void setMatchValue(final byte[] match) {
        this.match = Arrays.copyOf(match, match.length);
    }
    
    public byte[] getMatch() {
        if(match == null){
            return null;
        }
        return Arrays.copyOf(match, match.length);
    }
    
    public Node getNeighbor(final int index){
        return neighbors[index]; 
    }
    
    void setNeighbor(final Node neighbor, final int index){
        neighbors[index] = neighbor;
    }
}
