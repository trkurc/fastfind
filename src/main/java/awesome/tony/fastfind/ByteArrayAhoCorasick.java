package awesome.tony.fastfind;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class ByteArrayAhoCorasick {
	private static class Node{
		final private Node neighbors[] = new Node[256];
		// invariant - if matches == false, match is null, TODO: tidy up
		private Node failure = null;
		private byte[] match = null;
		boolean matches = false;

		private Node(byte[] match) {
			this.matches = true;
			this.match = Arrays.copyOf(match, match.length);
		}
		private Node() {
			this.matches = false;
		}
		private void setFail(Node fail) {
			this.failure = fail;
		}
		private Node getFail() {
			return failure;
		}
		private boolean isMatch(){
			return matches;
		}
		private void setMatch(byte[] match) {
			matches = true;
			this.match = Arrays.copyOf(match, match.length);
		}

	}

	public class SearchNugget{
		private SearchNugget(Node node) {
			this.current = node;
		}
		private Node current;
	}

	final Node root = new Node();

	public void addMatch(byte [] match){
		if(match.length < 1) return;
		addMatchRecursive(match, 0, root);
	}
	private void addMatchRecursive(byte match[], int offset, Node current){
		int index = ((int)match[offset])&(0xff);
		boolean atEnd = (offset == (match.length - 1));
		// TODO: a bit untidy
		if(current.neighbors[index] == null){
			if(atEnd == true){
				current.neighbors[index] = new Node(match);
				return;
			}
			current.neighbors[index] = new Node();	
		}
		else if(atEnd == true){
			current.neighbors[index].setMatch(match);
			return;
		}
		addMatchRecursive(match, offset + 1 , current.neighbors[index]);
	}

	public void finalize(){
		// perform bfs to build failure links
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(root);
		root.setFail(null);
		while (!queue.isEmpty()) { 
			Node current = queue.poll();
			for(int i = 0; i < 256; i++) {
				Node next = current.neighbors[i];
				if (next != null) { 
					// traverse failure to get state
					Node fail = current.getFail();
					while((fail != null) && fail.neighbors[i] == null){
						fail = fail.getFail(); 
					} 
					if (fail != null) { 
						next.setFail(fail.neighbors[i]); 
					} else {
						next.setFail(root); 
					} 
					queue.add(next); 
				} 
			} 
		}

	}

	public void evaluate(byte [] bytes){
		evaluate(ByteBuffer.wrap(bytes), null, MatchCallbacks.SYSTEMOUT);
	}

	public SearchNugget startMultiCallSearch(){
		return new SearchNugget(root);
	}

	public void evaluate(byte[] bytes, SearchNugget nugget){
		evaluate(ByteBuffer.wrap(bytes), nugget, MatchCallbacks.SYSTEMOUT);
	}
	
	public void evaluate(byte[] bytes, int offset, int limit, SearchNugget nugget){
		evaluate(bytes, offset, limit, nugget, MatchCallbacks.SYSTEMOUT);
	}
	
	public void evaluate(byte[] bytes, int offset, int limit, SearchNugget nugget, FindCallback f){
		evaluate(ByteBuffer.wrap(bytes, offset, limit), nugget, f);
	}
	
	public static enum MatchCallbacks implements FindCallback{
		SYSTEMOUT;
		@Override
		public void findCallback(int offsetInCurrentBuffer, byte[] termMatch) {
			System.out.println("match at: " + offsetInCurrentBuffer + " ["  + new String(termMatch) + "]");
		}
	};

	void evaluate(ByteBuffer bytes, SearchNugget nugget, FindCallback f) {
		Node current = root;
		if(nugget != null){
			current = nugget.current;
			if(current == null) throw new IllegalArgumentException("bad search nugget");
		}

		while (bytes.position() < bytes.limit()) { 
			int currentChar = bytes.get() & 0xff; 
			Node next = current.neighbors[currentChar]; 
			if (next == null) { 
				next = current.getFail();
				while ((next != null) && next.neighbors[currentChar] == null) { 
					next = next.getFail(); 
				} 
				if (next != null) { 
					next = next.neighbors[currentChar]; 
				} 
				else { 
					next = root; 
				} 
			} 
			// Accept condition
			// TODO: need a better means of passing back an object I don't care about
			if(next.isMatch()){
				f.findCallback(bytes.position(), Arrays.copyOf(next.match, next.match.length));
			}
			// TODO: Can fix this with state in the node (
			for(Node s = next.failure; s != null; s = s.failure){
				if(s.isMatch()){
					f.findCallback(bytes.position(), Arrays.copyOf(s.match, s.match.length));
				}
			}
			current = next; 
		} 
		if(nugget != null){
			nugget.current = current;
		}
	}

}
