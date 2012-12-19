package awesome.tony.fastfind;

public class ReferenceArrayNodeFactory implements NodeFactory {

	@Override
	public Node getNode() {
		return new ReferenceArrayNode();
	}

	@Override
	public Node getNode(SearchTerm term) {
		return new ReferenceArrayNode(term);
	}

}
