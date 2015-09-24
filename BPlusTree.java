import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BPlusTree {
	class Node {
		public int mNumKeys = 0;
		public String[] mKeys = new String[2 * T - 1];
		public Object[] mObjects = new Object[2 * T - 1];
		public Node[] mChildNodes = new Node[2 * T];
		public boolean mIsLeafNode;
		public Node mNextNode;
	}

	private Node mRootNode;
	private static final int T = 4;

	public BPlusTree() {
		mRootNode = new Node();
		mRootNode.mIsLeafNode = true;          
	}

	public void add(String key, Object object) {
		Node rootNode = mRootNode;
		if (rootNode.mNumKeys == (2 * T - 1)) {
			Node newRootNode = new Node();
			mRootNode = newRootNode;
			newRootNode.mIsLeafNode = false;
			mRootNode.mChildNodes[0] = rootNode;
			splitChildNode(newRootNode, 0, rootNode); // Split rootNode and move its median (middle) key up into newRootNode.
			insertIntoNonFullNode(newRootNode, key, object); // Insert the key into the B-Tree with root newRootNode.
		} else {
			insertIntoNonFullNode(rootNode, key, object); // Insert the key into the B-Tree with root rootNode.
		}
	}

	// Split the node, node, of a B-Tree into two nodes that contain T-1 (and T) elements and move node's median key up to the parentNode.
	// This method will only be called if node is full; node is the i-th child of parentNode.
	// All internal keys (elements) will have duplicates within the leaf nodes.
	void splitChildNode(Node parentNode, int i, Node node) {
		Node newNode = new Node();
		newNode.mIsLeafNode = node.mIsLeafNode;
		newNode.mNumKeys = T;
		for (int j = 0; j < T; j++) { // Copy the last T elements of node into newNode. Keep the median key as duplicate in the first key of newNode.
			newNode.mKeys[j] = node.mKeys[j + T - 1];
			newNode.mObjects[j] = node.mObjects[j + T - 1];
		}
		if (!newNode.mIsLeafNode) {
			for (int j = 0; j < T + 1; j++) { // Copy the last T + 1 pointers of node into newNode.
				newNode.mChildNodes[j] = node.mChildNodes[j + T - 1];
			}
			for (int j = T; j <= node.mNumKeys; j++) {
				node.mChildNodes[j] = null;
			}
		} else {
			// Manage the linked list that is used e.g. for doing fast range queries.
			newNode.mNextNode = node.mNextNode;
			node.mNextNode = newNode;
		}
		for (int j = T - 1; j < node.mNumKeys; j++) {
			node.mKeys[j] = "0";
			node.mObjects[j] = null;
		}
		node.mNumKeys = T - 1;

		// Insert a (child) pointer to node newNode into the parentNode, moving other keys and pointers as necessary.
		for (int j = parentNode.mNumKeys; j >= i + 1; j--) {
			parentNode.mChildNodes[j + 1] = parentNode.mChildNodes[j];
		}
		parentNode.mChildNodes[i + 1] = newNode;        
		for (int j = parentNode.mNumKeys - 1; j >= i; j--) {
			parentNode.mKeys[j + 1] = parentNode.mKeys[j];
			parentNode.mObjects[j + 1] = parentNode.mObjects[j];
		}
		parentNode.mKeys[i] = newNode.mKeys[0];
		parentNode.mObjects[i] = newNode.mObjects[0];
		parentNode.mNumKeys++;
	}

	// Insert an element into a B-Tree. (The element will ultimately be inserted into a leaf node).
	void insertIntoNonFullNode(Node node, String key, Object object) {
		int i = node.mNumKeys - 1;
		if (node.mIsLeafNode) {
			// Since node is not a full node insert the new element into its proper place within node.
			while (i >= 0 && (key.compareTo(node.mKeys[i]) < 0)) {
				node.mKeys[i + 1] = node.mKeys[i];
				node.mObjects[i + 1] = node.mObjects[i];
				i--;
			}
			i++;
			node.mKeys[i] = key;
			node.mObjects[i] = object;
			node.mNumKeys++;
		} else {
			// Move back from the last key of node until we find the child pointer to the node
			// that is the root node of the subtree where the new element should be placed.
			while (i >= 0 && (key.compareTo(node.mKeys[i]) < 0)) {
				i--;
			}
			i++;
			if (node.mChildNodes[i].mNumKeys == (2 * T - 1)) {
				splitChildNode(node, i, node.mChildNodes[i]);
				if (key.compareTo(node.mKeys[i]) > 0) {
					i++;
				}
			}
			insertIntoNonFullNode(node.mChildNodes[i], key, object);
		}
	}      

	// Recursive search method.
	public Object search(Node node, String key) {              
		int i = 0;
		while (i < node.mNumKeys && (key.compareTo(node.mKeys[i]) > 0)) {
			i++;
		}
		if (i < node.mNumKeys && key == node.mKeys[i]) {
			return node.mObjects[i];
		}
		if (node.mIsLeafNode) {
			return null;
		} else {
			return search(node.mChildNodes[i], key);
		}      
	}

	public Object search(String key) {
		return search(mRootNode, key);
	}

	// Iterative search method.
	public Object search2(Node node, int key) {
		while (node != null) {
			int i = 0;
			while (i < node.mNumKeys && (Integer.toString(key).compareTo(node.mKeys[i]) > 0)) {
				i++;
			}
			if (i < node.mNumKeys && (Integer.toString(key) == node.mKeys[i])) {
				return node.mObjects[i];
			}
			if (node.mIsLeafNode) {
				return null;
			} else {
				node = node.mChildNodes[i];
			}
		}
		return null;
	}

	public Object search2(int key) {
		return search2(mRootNode, key);
	}

	// Inorder walk over the tree.
	public String toString() {
		String string = "";
		Node node = mRootNode;          
		while (!node.mIsLeafNode) {                    
			node = node.mChildNodes[0];
		}              
		while (node != null) {
			for (int i = 0; i < node.mNumKeys; i++) {
				string += node.mObjects[i] + ", ";
			}
			node = node.mNextNode;
		}
		return string;
	}

	// Inorder walk over parts of the tree.
	public String toString(int fromKey, int toKey) {
		String string = "";
		Node node = getLeafNodeForKey(fromKey);
		while (node != null) {
			for (int j = 0; j < node.mNumKeys; j++) {
				string += node.mObjects[j] + ", ";
				if (node.mKeys[j] == Integer.toString(toKey)) {
					return string;
				}
			}
			node = node.mNextNode;
		}
		return string;
	}

	Node getLeafNodeForKey(int key) {
		Node node = mRootNode;
		while (node != null) {
			int i = 0;
			while (i < node.mNumKeys && (Integer.toString(key).compareTo(node.mKeys[i]) > 0)) {
				i++;
			}
			if (i < node.mNumKeys && Integer.toString(key) == node.mKeys[i]) {
				node = node.mChildNodes[i + 1];
				while (!node.mIsLeafNode) {                    
					node = node.mChildNodes[0];
				}
				return node;
			}
			if (node.mIsLeafNode) {
				return null;
			} else {
				node = node.mChildNodes[i];
			}
		}
		return null;
	}

	public static void main(String[] args) {
		BPlusTree bPlusTree = new BPlusTree();
		FileReader f;
		FileWriter w;
		BufferedWriter wr;
		BufferedReader br = null;
		ArrayList<String> arr;
		String primeNumbers[] = new String[20];
		//ArrayList<String> primeNumbers = new ArrayList<String>();
		try {
			f= new FileReader("ip1.txt");
			try {
				arr =  new ArrayList<String>();
				w=new FileWriter("op.txt");
				wr = new BufferedWriter(w);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			br =  new BufferedReader(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String st,op;
		int k=0;
		String temp = null;
		try {
			while((st = br.readLine()) != null)
			{
				primeNumbers[k++]=st;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < primeNumbers.length; i++) {
			temp=primeNumbers[i];                      
				bPlusTree.add(temp,temp);
			//System.out.println(bPlusTree.search(temp));
		}            

		System.out.println(temp);
		String temp2 =temp;//"96979899100";
		System.out.println(bPlusTree.search(temp2));


	}
}

