package awesome.tony.fastfind;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class AhoCorasick {

    private final Node root;
    private Node currentNode;
    private final Map<SearchPhrase, List<Long>> resultMap;
    private long bytesRead;

    public AhoCorasick(final Collection<byte[]> searchPhrases) {
        if (searchPhrases.isEmpty()) {
            throw new IllegalArgumentException();
        }
        root = new Node();
        resultMap = new HashMap<>(searchPhrases.size());
        currentNode = root;
        bytesRead = 0L;
        for (final byte[] phrase : searchPhrases) {
            if (phrase.length == 0) {
                continue;
            }
            addMatchRecursive(phrase, 0, root);
        }
        initialize();
    }

    public void reset() {
        currentNode = root;
        resultMap.clear();
        bytesRead = 0L;
    }
    
    public Map<SearchPhrase, List<Long>> getResults(){
        return new HashMap<>(resultMap);
    }

    private void addMatchRecursive(byte match[], int offset, Node current) {
        int index = ((int) match[offset]) & (0xff);
        boolean atEnd = (offset == (match.length - 1));
        if (current.getNeighbor(index) == null) {
            if (atEnd == true) {
                current.setNeighbor(new Node(match), index);
                return;
            }
            current.setNeighbor(new Node(), index);
        } else if (atEnd == true) {
            current.getNeighbor(index).setMatchValue(match);
            return;
        }
        addMatchRecursive(match, offset + 1, current.getNeighbor(index));
    }

    private void initialize() {
        // perform bfs to build failure links
        final Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        root.setFailureNode(null);
        while (!queue.isEmpty()) {
            final Node current = queue.poll();
            for (int i = 0; i < 256; i++) {
                final Node next = current.getNeighbor(i);
                if (next != null) {
                    // traverse failure to get state
                    Node fail = current.getFailureNode();
                    while ((fail != null) && fail.getNeighbor(i) == null) {
                        fail = fail.getFailureNode();
                    }
                    if (fail != null) {
                        next.setFailureNode(fail.getNeighbor(i));
                    } else {
                        next.setFailureNode(root);
                    }
                    queue.add(next);
                }
            }
        }

    }

    public void evaluate(InputStream stream) throws IOException {
        Node current = currentNode;

        int currentChar;
        while ((currentChar = stream.read()) >= 0) {
            bytesRead++;
            Node next = current.getNeighbor(currentChar);
            if (next == null) {
                next = current.getFailureNode();
                while ((next != null) && next.getNeighbor(currentChar) == null) {
                    next = next.getFailureNode();
                }
                if (next != null) {
                    next = next.getNeighbor(currentChar);
                } else {
                    next = root;
                }
            }
            if (next == null) {
                throw new IllegalStateException("this doesn't make sense to me yet...based on above assignment it seems it could be null yet null seems not legit");
            }
            // Accept condition
            if (next.hasMatch()) {
                addResult(bytesRead, next.getMatch());
            }
            // TODO: Can fix this with state in the node (perhaps the Node class should provide a method to get the failure node chain
            for (Node failNode = next.getFailureNode(); failNode != null; failNode = failNode.getFailureNode()) {
                if (failNode.hasMatch()) {
                    addResult(bytesRead, failNode.getMatch());
                }
            }
            current = next;
        }
        currentNode = current;
    }

    private void addResult(final long index, final byte[] matchingPhrase) {
        final SearchPhrase phrase = new SearchPhrase(matchingPhrase);
        final List<Long> indexes = (resultMap.containsKey(phrase)) ? resultMap.get(phrase) : new ArrayList<Long>(5);
        indexes.add(index);
        resultMap.put(phrase, indexes);
    }
}
