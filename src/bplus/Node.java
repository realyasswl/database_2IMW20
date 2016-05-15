package bplus;

import java.util.LinkedList;
import java.util.List;

public class Node {
	public Node() {
		children = new LinkedList();
		keyList = new LinkedList<Comparable>();
	}

	// length of keyList should be at most n-1
	private List<Comparable> keyList;
	// length of children should be at most n
	private List children;
	private boolean leaf;
	private boolean root;

	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

	private Node parent;

	public Node getParent() {
		return parent;
	}

	public List<Comparable> getKeyList() {
		return keyList;
	}

	public List getChildren() {
		return children;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setKeyList(List<Comparable> keyList) {
		this.keyList = keyList;
	}

	public void setChildren(List children) {
		this.children = children;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public void setParent(Node parent) {
		if (this.keyList.indexOf(14) >= 0) {
			System.out.println("");
		}
		this.parent = parent;
	}

	public Node getPn() throws UnsupportedOperationException {
		if (leaf) {
			return (Node) children.get(children.size() - 1);
		} else {
			throw new UnsupportedOperationException("This node is not a leaf node.");
		}
	}

	public void add(Node n, Comparable key, int i) {
		children.add(i + 1, n);
		keyList.add(i, key);
		n.setParent(this);
	}

	public void add(Node n, Comparable key) {
		add(n, key, keyList.size());
	}
}
