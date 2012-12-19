package awesome.tony.fastfind;

public interface NodeFactory {
	public Node getNode();

	Node getNode(SearchTerm term);
}
