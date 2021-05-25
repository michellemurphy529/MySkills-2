import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;


public class BST extends BinaryTree{

	// You MUST have a zero argument constructor that
	// creates an empty binary search tree
	// You can add code to this if you want (or leave it alone).
  // We will create all BSTs for testing using this constructor 
  public BST(){
    super();
  }



  /* This method returns true if target string is in this tree, false otherwise.
  (i) this method must be efficient AND (ii) DO NOT USE RECURSION for this method. */
  @Override
  public boolean contains(String s){
    Node currentNode=root;

    while (currentNode != null) {
      if (s.compareTo(currentNode.getData())<0) {
        currentNode=currentNode.getLeft();
      }else if (s.compareTo(currentNode.getData())>0) {
        currentNode=currentNode.getRight();
      }else{
        return true;
        }
    }return false;
  }



  /* This method adds a new string to the BST, if not already in tree.
    new string is added as a leaf in the tree (if added) and returns nothing. */
  @Override
  public void add(String s){
    if (root == null) {
      root=new Node(s);
      size++;                      
    }else if (!contains(s)) {
      Node currentNode=root;
      Node addedNode=new Node(s);
      boolean isAdded=false;

      while (!isAdded) {
        //if string we are adding is less than the current node & there is no left node present we know it must be added in that empty position
        if (s.compareTo(currentNode.getData())<0) {
          if (currentNode.left == null) {
            currentNode.setLeft(addedNode);
            isAdded=true;
            size++;
          }else {
            currentNode=currentNode.getLeft();      //we keep searching through the BST trying to find the empty position
          }
        }else {
          //same as above but now for the right side
          if(currentNode.right == null) {
            currentNode.setRight(addedNode);
            isAdded=true;
            size++;
          }else {
            currentNode=currentNode.getRight();
          }
        }
      }
    }
  }



  /* Method that takes a BST and creates a new balanced BST that has the same strings and has minimal height & is a valid BST. */ 
  public BST makeBalanced(){
    if (!isBalancedBST(root)) {
      ArrayList<Node> nodesArray=new ArrayList<Node>();
		  createInorder(root, nodesArray);
      int lastIndexInArr=nodesArray.size() - 1;
      root=createBalancedBST(nodesArray, 0, lastIndexInArr);    //We are using the starting index and last index position to find the middle position (root) and create the subsequent left and right right subtree's the same way.
    }
    return this;
  }

  /* Helper method that creates an arraylist that provides the inorder traversal of the BST */
  private void createInorder(Node node, ArrayList<Node> nodesArr) {
		if (node != null) {
			createInorder(node.getLeft(), nodesArr);
			nodesArr.add(node);
			createInorder(node.getRight(), nodesArr);
		}
	}

  /* Helper method that checks to see if the BST is already balanced */
  public boolean isBalancedBST(Node node) {
    if (node == null) {
      return true;
    }

    int heightLeftBST=heightRecursive(node.getLeft())+1;
    int heightRightBST=heightRecursive(node.getRight())+1;

    boolean leftBalanced=isBalancedBST(node.getLeft());
    boolean rightBalanced=isBalancedBST(node.getRight());

    //if the difference between the LS and RS height is less than or equal to 1 & both sides are balanced we know we have a balanced BST.
    if (Math.abs(heightLeftBST - heightRightBST)<=1 && leftBalanced && rightBalanced) {
      return true;
    }
    return false;
  }

  /* helper method that creates a new balanced BST */
  private Node createBalancedBST(ArrayList<Node> inorderList, int firstNode, int lastNode) {
    
    if (firstNode>lastNode) {
      return null;
    }
    
    int newRootIndex=firstNode + (lastNode - firstNode)/2;    //middle index

    Node newRoot=(Node) inorderList.get(newRootIndex);      //set new root to our middle node
    int newLastIndex=newRootIndex - 1;          //creating our new first and last index's that we will pass back through recursively
    int newFirstIndex=newRootIndex + 1;

    newRoot.setLeft(createBalancedBST(inorderList, firstNode, newLastIndex));
    newRoot.setRight(createBalancedBST(inorderList, newFirstIndex, lastNode));

    return newRoot;
  }



  /* this method saves the current tree in text format in the current directory in a file fname
  (i) returns true if successful (saved to file), false otherwise (ii) loading the saved file with loadFromFile will exactly reconstruct this tree */
  public boolean saveToFile(String fname){
    
    FileWriter newFile=null;
    try {
      newFile=new FileWriter(fname);

      //creating preorder of nodes to add to the textfile
      ArrayList<Node> preorder=getPreOrderTraversal(root);
      
      //writing each node to the file one line at a time
      for (int i=0; i<preorder.size(); i++) {
        if (i == preorder.size()-1) {
          newFile.write(preorder.get(i).getData());
        }else {
          newFile.write(preorder.get(i).getData() + "\n");
        }
      }
      return true;
    }catch (FileNotFoundException e) {
      System.out.println("Error: You've encountered a " + e + "." + "\nCannot open file \"" + fname + "\" for writing.\n");
    }catch (IOException e) {
      System.out.println("Error: You've encountered a " + e + "." + "\nCannot write to file \"" + fname + "\"\n.");
    }finally{
      try{
        newFile.close();
      }catch (IOException e) {
        System.out.println("Error: You've encountered a " + e + "." + "\nCannot close file \"" + fname + "\" because a File I/O operation has failed.\n");
      }catch (NullPointerException e) {
        System.out.println("Error: You've encountered a " + e + "." + "\nCannot close file \"" + fname + "\" because the file was never properly created.\n");
      }
    }
    return false;
  }
  
  /* helper method that uses constructor chaining and recursion to obtain the preorder traversal array */
  private ArrayList<Node> getPreOrderTraversal() {
    return getPreOrderTraversal(root);
  }
  
  private ArrayList<Node> getPreOrderTraversal(Node node) {
    ArrayList<Node> preorderNodes = new ArrayList<Node>();

    if (node != null) {
        preorderNodes.add(node);
        preorderNodes.addAll(getPreOrderTraversal(node.left));
        preorderNodes.addAll(getPreOrderTraversal(node.right));
    }return preorderNodes;
  }


  @Override
  public void loadFromFile(String fname){
    BinaryTree bst = new BST();
    try{
      Scanner file = new Scanner( new File(fname) );
      while( file.hasNextLine() ){
        bst.add(file.nextLine().strip());
      }
    }catch(Exception e){
      System.out.println("Something went wrong!!");
    }
    this.root = bst.root;
    this.size = bst.size;
  }
}