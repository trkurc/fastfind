package algorithm.search.ahocorasick;

public interface NodeFactory {

    public Node getNode();

    Node getNode(SearchTerm term);
}
