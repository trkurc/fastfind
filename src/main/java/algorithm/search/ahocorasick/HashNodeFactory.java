package algorithm.search.ahocorasick;

public class HashNodeFactory implements NodeFactory {

    @Override
    public Node getNode() {
        return new HashNode();
    }

    @Override
    public Node getNode(SearchTerm term) {
        return new HashNode(term);
    }
}
