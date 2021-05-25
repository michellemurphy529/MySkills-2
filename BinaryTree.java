import java.io.File;   
import java.util.Scanner;
import java.util.ArrayList;


public class BinaryTree {
	protected Node root = null;
    protected int  size = 0;

	public BinaryTree(){
		size = 0;
	}

  	public BinaryTree(String s){
		root = new Node(s);
		size = 1;
	}

	public int getSize(){ return this.size; }
	public Node getRoot(){ return this.root; }


	
	
	/* This method returns true if the binary tree contains the target string and false if not.
	MUST USE RECURSION */
	public boolean contains(String s){
		
		if (root == null) {return false;}

		if (root.getData().equals(s)) {return true;}

		if (root.left != null) {
			String tempString=searchBT(root.getLeft(), s);
			if (tempString != null) {
				return tempString.contains(s);		//USING RECURSION
			}
		}if (root.right != null) {
			String tempString=searchBT(root.getRight(), s);
			if (tempString != null) {
				return tempString.contains(s);		//USING RECURSION
			}
		}return false;
	}

	/* Helper function to recursively search through the Binary Tree to find target string. 
					USING RECURSION */
	private String searchBT(Node root, String target) {
		if (root == null) {return null;}
		if (root.getData().equals(target)) {return root.getData();}

		String leftTree=searchBT(root.getLeft(), target);
		if (leftTree != null) {return leftTree;}

		String rightTree=searchBT(root.getRight(), target);
		if (rightTree != null) {return rightTree;}

		return null;
	}



	/* This method returns true if the Binary Tree is a valid BST or false if not.
	
	  A binary tree is 1. empty (base case), or 2. an item and two binary trees (called left and right)
	  A BST is valid if 1. n.data is greater than the data value of all nodes in the sub-tree rooted with n.left. and 2. n.data is less than the data value of all nodes in the sub-tree rooted with n.right.
      NOTE: an empty tree is a valid BST & A BST object might NOT satisfy the binary search tree property.
      THIS METHOD WILL NOT USE instanceof. It should determine if the values and structure of this tree satisfy the binary search tree property. 
	*/
	public boolean isBST(){
		if (size == 0 || size == 1 ) { return true; }

		ArrayList<String> nodesArray=new ArrayList<String>();
		inorderTraversal(root, nodesArray);
		
		return isSorted(nodesArray);
	}

	/* Helper method that creates an arraylist that tracks the inorder traversal of the Binary Tree for isBST() */
	private ArrayList<String> inorderTraversal(Node root, ArrayList<String> nodesArray) {
		if (root != null) {
			inorderTraversal(root.getLeft(), nodesArray);
			nodesArray.add(root.getData());		//adding our node to the ArrayList
			inorderTraversal(root.getRight(), nodesArray);
		}return nodesArray;
	}

	/* Helper method that determines if the inorder traversal array is valid and in turn the BT is a valid BST (i.e. checking to see it's a sorted array) */
	private boolean isSorted(ArrayList<String> stringArray) {
		for (int i=1; i<stringArray.size(); i++) {
			String largerNum=(String) stringArray.get(i);		//checks at index position 1 and compares to the previous string at index 0.
			String smallerNum=(String) stringArray.get(i-1);
			if (largerNum.compareTo(smallerNum)<0) {
				return false;
			}
		}return true;
	}





	public void loadFromFile(String fname){
		BinaryTree bt = new BinaryTree();
		try{
			Scanner file = new Scanner( new File(fname) );
			while( file.hasNextLine()){
				bt.add(file.nextLine().strip());
			}
		}catch(Exception e){
			System.out.println("Something went wrong!!");
		}
		this.root = bt.root;
		this.size = bt.size;
	}

	public void add(String s){
		addRandom(s);
	}

	
	/* add a node in a random place in the tree. */
	private void addRandom(String s){
		if(root == null && size == 0){
			root = new Node(s);
		}else{
		  Node tmp = root;
		  boolean left = Math.random() < 0.5; 
		  Node child = left ? tmp.getLeft() : tmp.getRight();
		  while(child != null){
			tmp = child;
			left = Math.random() < 0.5;
			child = left ? tmp.getLeft() : tmp.getRight();
		  }
		  // assert: child == null
		  // yea! we have a place to add s
		  if(left){
		  	tmp.setLeft(new Node(s));
		  }else{
			  tmp.setRight(new Node(s));
		  }
		}
		size += 1;
	}

	/** Computes the height of the binary tree
	  *
		* The height is the length of the longest path from
		* the root of the tree to any other node.
		*
		* @return the height of the tree
		*/
	public final int height(){
	  if( root == null ){ return -1; }
	  if( size == 1){ return 0; }
	  return heightRecursive(root);
	}
	protected final static int heightRecursive(Node root){
		if( root == null ){
			return -1;
		}
		int leftHeight = heightRecursive(root.getLeft());
		int rightHeight = heightRecursive(root.getRight());
		if( leftHeight < rightHeight){
			return 1 + rightHeight;
		}else{
			return 1 + leftHeight;
		}
	}

	
	@Override
	public String toString() {
		return PrintBinaryTree.toString(this);
	}
}