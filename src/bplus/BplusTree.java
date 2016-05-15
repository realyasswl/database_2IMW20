package bplus;

import java.util.LinkedList;
import java.util.List;

public class BplusTree {
	private BplusTree() {
		super();
	}

	public static BplusTree build(Iterable iter, Command comm) {
		BplusTree tree = new BplusTree();

		for (Object c : iter) {
			Comparable v = comm.getKey(c);
			tree.insert(v, c);
		}
		return tree;
	}

	private Node root;

	private Node findLeafNode(Comparable v) {
		Node currentNode = root;
		List<Comparable> keyList = currentNode.getKeyList();
		List recordList = currentNode.getChildren();
		while (!currentNode.isLeaf()) {
			int i = 0;
			boolean found = false;
			// the keyList is sorted in ascending order
			// so search from index 0, the first key that greater than or equal
			// to v is the smallest one
			for (; i < keyList.size(); i++) {
				if (v.compareTo(keyList.get(i)) <= 0) {
					found = true;
					break;
				}
			}
			if (found) {
				if (v.compareTo(keyList.get(i)) == 0) {
					currentNode = (Node) recordList.get(i + 1);
				} else {// v<ki
					currentNode = (Node) recordList.get(i);
				}
			} else {// no such number
				currentNode = (Node) recordList.get(recordList.size() - 1);
			}
			keyList = currentNode.getKeyList();
			recordList = currentNode.getChildren();
		}
		// now currentNode is leaf node
		return currentNode;
	}

	public Wrapped find(Comparable v) {
		Node currentNode = findLeafNode(v);
		List<Comparable> keyList = currentNode.getKeyList();
		List recordList = currentNode.getChildren();
		int i = 0;
		boolean found = false;
		for (; i < keyList.size(); i++) {
			if (v.compareTo(keyList.get(i)) == 0) {
				found = true;
				break;
			}
		}
		if (found) {
			return new Wrapped(currentNode, i);
		} else {
			return null;
		}
	}

	public void insert(Comparable v, Object record) {
		System.out.println("insert:" + v);
		if (root == null || root.getKeyList() == null || root.getKeyList().size() == 0) {
			root = new Node();
			root.setLeaf(true);
			root.setRoot(true);
			root.getKeyList().add(v);
			root.getChildren().add(record);
			// } else {
			// if (root.getKeyList().size() == 1 && root.getChildren().size() ==
			// 1) {
			// Comparable first = root.getKeyList().get(0);
			// Object r0 = root.getChildren().get(0);
			// int r = first.compareTo(v);
			// Comparable k = r > 0 ? first : v;
			//
			// Node n1 = new Node();
			// n1.setRoot(false);
			// n1.setLeaf(true);
			// Node n2 = new Node();
			// n2.setRoot(false);
			// n2.setLeaf(true);
			//
			// root.getKeyList().set(0, k);
			// root.getChildren().set(0, n1);
			// root.getChildren().add(n2);
			// root.setLeaf(false);
			//
			// if (r > 0) {
			// n1.getChildren().add(record);
			// n1.getKeyList().add(v);
			// n2.getChildren().add(r0);
			// n2.getKeyList().add(v);
			//
			// } else {
			// }
		} else {
			Node leaf = findLeafNode(v);
			insertInLeaf(leaf, v, record);
			if (leaf.getKeyList().size() == common.Constants.numberPerNode) {
				splitLeafNode(leaf);
			}
		}
		// }
	}

	// this function apply only to leaf nodes
	private void splitLeafNode(Node n) {
		System.out.println("splitLeafNode");
		Node newNode = new Node();
		newNode.setLeaf(true);
		newNode.setRoot(false);
		int halfn = (int) Math.ceil(common.Constants.numberPerNode / (double) 2);
		newNode.getKeyList().addAll(n.getKeyList().subList(halfn, n.getKeyList().size()));
		newNode.getChildren().addAll(n.getChildren().subList(halfn, n.getChildren().size()));

		n.setKeyList(n.getKeyList().subList(0, halfn));
		n.setChildren(n.getChildren().subList(0, halfn));
		// have the pn pointed to the next leaf node
		n.getChildren().add(newNode);

		insertInParent(n, newNode.getKeyList().get(0), newNode);
	}

	public void insertInLeaf(Node n, Comparable v, Object record) {
		if (v.compareTo(n.getKeyList().get(0)) < 0) {
			n.getKeyList().add(0, v);
			n.getChildren().add(0, record);
		} else {
			int i = 0;
			for (; i < n.getKeyList().size(); i++) {
				if (v.compareTo(n.getKeyList().get(i)) < 0) {
					// break to not update value of i, such that i = iprime+1
					// where k_iprime is the highest value that is less that v
					break;
				}
			}
			n.getKeyList().add(i, v);
			n.getChildren().add(i, record);
		}
	}

	public void insertInParent(Node n, Comparable vprime, Node nprime) {
		if (n.isRoot()) {
			// TODO something could be wrong here, I will change if necessary
			// after running test
			Node r = new Node();
			r.setLeaf(false);
			r.setRoot(true);
			root = r;

			r.getChildren().add(n);
			n.setParent(r);

			r.add(nprime, vprime);
			n.setRoot(false);
		} else {
			Node p = n.getParent();
			int nindex = p.getChildren().indexOf(n);
			try {
				p.add(nprime, vprime, nindex);
			} catch (Exception e) {
				System.out.println("exception");
			}
			if (p.getKeyList().size() == common.Constants.numberPerNode) {
				splitNonLeafNode(p);
			}
		}

	}

	private void splitNonLeafNode(Node p) {
		System.out.println("splitNonLeafNode");
		Node pprime = new Node();
		pprime.setLeaf(false);
		pprime.setRoot(false);
		int halfn = (int) Math.ceil(common.Constants.numberPerNode / (double) 2);

		Comparable vprime = p.getKeyList().get(halfn);
		pprime.getChildren().addAll(p.getChildren().subList(halfn + 1, p.getChildren().size()));
		pprime.getKeyList().addAll(p.getKeyList().subList(halfn + 1, p.getKeyList().size()));

		for (int index = 0; index < pprime.getChildren().size(); index++) {
			Node n = (Node) pprime.getChildren().get(index);
			n.setParent(pprime);
		}

		p.setKeyList(p.getKeyList().subList(0, halfn));
		p.setChildren(p.getChildren().subList(0, halfn + 1));

		insertInParent(p, vprime, pprime);
	}

	void printAllNode() {
		printNode(root, 0);
		for (StringBuilder sb : sbList) {
			System.out.println(sb);
		}
	}

	List<StringBuilder> sbList = new LinkedList<StringBuilder>();

	private void printNode(Node r, int level) {
		if (sbList.size() == level) {
			sbList.add(new StringBuilder());
		}
		for (Comparable a : r.getKeyList()) {
			sbList.get(level).append(a).append(",");
		}
		sbList.get(level).append("|");
		if (r.isLeaf()) {

		} else {
			for (int index = 0; index < r.getChildren().size(); index++) {
				Node c = (Node) r.getChildren().get(index);
				printNode(c, level + 1);
			}
		}
	}
}
