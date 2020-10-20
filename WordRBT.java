// --== CS400 File Header Information ==--
// Name: Nicholas Bloedel
// Email: nbloedel@wisc.edu
// Team: HE
// TA: Na
// Lecturer: Florian Heimerl
// Notes to Grader: Changed original rotate


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
// import org.junit.Test;
// import static org.junit.Assert.*;

/**
 * Binary Search Tree implementation with a Node inner class for representing the nodes within a
 * binary search tree. You can use this class' insert method to build a binary search tree, and its
 * toString method to display the level order (breadth first) traversal of values in that tree.
 */
public class WordRBT<T extends Comparable<Definition>> {

  /**
   * This class represents a node holding a single value within a binary tree the parent, left, and
   * right child references are always be maintained.
   */
  protected static class Node<T> {
    public Definition data;
    public Node<Definition> parent; // null for root node
    public Node<Definition> leftChild;
    public Node<Definition> rightChild;

    public Node(Definition data) {
      this.data = data;
    }

    public boolean isBlack = false;

    /**
     * @return true when this node has a parent and is the left child of that parent, otherwise
     *         return false
     */
    public boolean isLeftChild() {
      return parent != null && parent.leftChild == this;
    }

    /**
     * This method performs a level order traversal of the tree rooted at the current node. The
     * string representations of each data value within this tree are assembled into a comma
     * separated string within brackets (similar to many implementations of java.util.Collection).
     * 
     * @return string containing the values of this tree in level order
     */
    @Override
    public String toString() { // display subtree in order traversal
      String output = "[";
      LinkedList<Node<Definition>> q = new LinkedList<Node<Definition>>();
      q.add((Node<Definition>) this);
      while (!q.isEmpty()) {
        Node<Definition> next = q.removeFirst();
        if (next.leftChild != null)
          q.add(next.leftChild);
        if (next.rightChild != null)
          q.add(next.rightChild);
        output += next.data.getKeyWord();
        if (!q.isEmpty())
          output += ", ";
      }
      return output + "]";
    }
  }

  protected Node<Definition> root; // reference to root node of tree, null when empty

  /**
   * Performs a naive insertion into a binary search tree: adding the input data value to a new node
   * in a leaf position within the tree. After this insertion, no attempt is made to restructure or
   * balance the tree. This tree will not hold null references, nor duplicate data values.
   * 
   * @param data to be added into this binary search tree
   * @throws NullPointerException     when the provided data argument is null
   * @throws IllegalArgumentException when the tree already contains data
   */
  public void insert(Definition data) throws NullPointerException, IllegalArgumentException {
    // null references cannot be stored within this tree
    if (data == null)
      throw new NullPointerException("This RedBlackTree cannot store null references.");

    Node<Definition> newNode = new Node<Definition>(data);
    if (root == null) {
      root = newNode;
    } // add first node to an empty tree
    else
      insertHelper(newNode, root); // recursively insert into subtree
    root.isBlack = true;
  }

  /**
   * Private helper method that enforces the RBT properties after the insertion of a node
   * 
   * @param redNode
   */
  private void enforceRBTreePropertiesAfterInsert(Node<Definition> redNode) {
    if (redNode.parent == null || redNode.parent.isBlack) {
      return;
    } else {
      // black or null uncle on right
      if ((redNode.parent.parent.rightChild == null || redNode.parent.parent.rightChild.isBlack)) {
        // left right rotate and recolor
        if (!redNode.isLeftChild()) {
          rotate(redNode, redNode.parent);
          redNode.leftChild.isBlack = false;
          redNode.rightChild.isBlack = false;
          redNode.isBlack = true;
        } else {
          // right rotate and recolor
          rotate(redNode.parent, redNode.parent.parent);
          if (redNode.parent.rightChild != null) {
            redNode.parent.rightChild.isBlack = false;
            redNode.parent.isBlack = true;
          }
        }
        // black or null uncle on left
      } else if (redNode.parent.parent.leftChild == null
          || redNode.parent.parent.leftChild.isBlack) {
        // right left rotate and recolor
        if (redNode.isLeftChild()) {
          rotate(redNode, redNode.parent);
          redNode.leftChild.isBlack = false;
          redNode.rightChild.isBlack = false;
          redNode.isBlack = true;
        } else {
          // left rotate and recolor
          rotate(redNode.parent, redNode.parent.parent);
          if (redNode.parent.leftChild != null) {
            redNode.parent.leftChild.isBlack = false;
            redNode.parent.isBlack = true;
          }
        }

      }
      // if sibling is red, recolor
      else {
        redNode.parent.parent.isBlack = false;
        redNode.parent.isBlack = true;

        if (redNode.parent.isLeftChild()) {
          redNode.parent.parent.rightChild.isBlack = true;
        } else {
          redNode.parent.parent.leftChild.isBlack = true;
        }
        // recursively call if red higher in tree
        if (!redNode.parent.equals(root)) {
          enforceRBTreePropertiesAfterInsert(redNode.parent.parent);
        }
      }
    }
  }



  /**
   * Recursive helper method to find the subtree with a null reference in the position that the
   * newNode should be inserted, and then extend this tree by the newNode in that position.
   * 
   * @param newNode is the new node that is being added to this tree
   * @param subtree is the reference to a node within this tree which the newNode should be inserted
   *                as a descenedent beneath
   * @throws IllegalArgumentException when the newNode and subtree contain equal data references (as
   *                                  defined by Comparable.compareTo())
   */
  private void insertHelper(Node<Definition> newNode, Node<Definition> subtree) {
    int compare = newNode.data.compareTo(subtree.data);
    // do not allow duplicate values to be stored within this tree
    if (compare == 0)
      throw new IllegalArgumentException("This RedBlackTree already contains that value.");

    // store newNode within left subtree of subtree
    else if (compare < 0) {
      if (subtree.leftChild == null) { // left subtree empty, add here
        subtree.leftChild = newNode;
        newNode.parent = subtree;
        // otherwise continue recursive search for location to insert
      } else
        insertHelper(newNode, subtree.leftChild);
    }

    // store newNode within the right subtree of subtree
    else {
      if (subtree.rightChild == null) { // right subtree empty, add here
        subtree.rightChild = newNode;
        newNode.parent = subtree;
        // otherwise continue recursive search for location to insert
      } else
        insertHelper(newNode, subtree.rightChild);
    }
    enforceRBTreePropertiesAfterInsert(newNode);
  }

  /**
   * This method performs a level order traversal of the tree. The string representations of each
   * data value within this tree are assembled into a comma separated string within brackets
   * (similar to many implementations of java.util.Collection, like java.util.ArrayList, LinkedList,
   * etc).
   * 
   * @return string containing the values of this tree in level order
   */
  @Override
  public String toString() {
    return root.toString();
  }

  /**
   * Performs the rotation operation on the provided nodes within this BST. When the provided child
   * is a leftChild of the provided parent, this method will perform a right rotation (sometimes
   * called a left-right rotation). When the provided child is a rightChild of the provided parent,
   * this method will perform a left rotation (sometimes called a right-left rotation). When the
   * provided nodes are not related in one of these ways, this method will throw an
   * IllegalArgumentException.
   * 
   * @param child  is the node being rotated from child to parent position (between these two node
   *               arguments)
   * @param parent is the node being rotated from parent to child position (between these two node
   *               arguments)
   * @throws IllegalArgumentException when the provided child and parent node references are not
   *                                  initially (pre-rotation) related that way
   */
  private void rotate(Node<Definition> child, Node<Definition> parent) throws IllegalArgumentException {
    if (!child.isLeftChild() && !parent.rightChild.equals(child)) {
      throw new IllegalArgumentException();
    }
    // right rotation
    else if (child.isLeftChild() && (parent.isLeftChild() || parent.parent == null)) {
      rightRotate(child, parent);
      return;
    }
    // right left rotation
    else if (!parent.isLeftChild() && child.isLeftChild()) {
      rightLeft(child, parent);
      return;
    }
    // left right
    else if (!child.isLeftChild() && parent.isLeftChild()) {
      leftRight(child, parent);
    }
    // left rotation
    else {
      leftRotate(child, parent);
    }
  }

  /**
   * Private helper method to implement a right rotation within the RBT
   * 
   * @param child
   * @param parent
   */
  private void rightRotate(Node<Definition> child, Node<Definition> parent) {
    Node<Definition> temp = child.rightChild;
    if (temp != null) {
      parent.leftChild = null;
      child.rightChild.parent = parent;
    }
    if (parent.parent == null) {
      root = child;
      child.parent = null;
    } else if (parent.parent.rightChild.equals(parent)) {
      parent.parent.rightChild = child;
      child.parent = parent.parent;
    } else {
      parent.parent.leftChild = child;
      child.parent = parent.parent;
    }
    child.rightChild = parent;
    parent.parent = child;
    parent.leftChild = temp;
    return;
  }

  /**
   * Private helper method that implements the left rotation within an RBT
   * 
   * @param child
   * @param parent
   */
  private void leftRotate(Node<Definition> child, Node<Definition> parent) {
    Node<Definition> temp = child.leftChild;
    if (temp != null) {
      child.leftChild.parent = parent;
      parent.rightChild = temp;
    }
    if (parent.parent == null) {
      root = child;
      child.parent = null;
    } else if (parent.parent.leftChild.equals(parent)) {
      parent.parent.leftChild = child;
      child.parent = parent.parent;
    } else {
      parent.parent.rightChild = child;
      child.parent = parent.parent;
    }
    parent.parent = child;
    child.leftChild = parent;
    parent.rightChild = temp;
    return;
  }

  /**
   * Private helper method that implements the right-left rotation within an RBT
   * 
   * @param child
   * @param parent
   */
  private void rightLeft(Node<Definition> child, Node<Definition> parent) {
    rightRotate(child, parent);
    leftRotate(child, child.parent);
  }

  /**
   * Private helper method that implements the left-right rotation within an RBT
   * 
   * @param child
   * @param parent
   */
  private void leftRight(Node<Definition> child, Node<Definition> parent) {
    leftRotate(child, parent);
    rightRotate(child, child.parent);
  }
  
  
  
  /** This method prints out each node in order
   * @param rbtNode
   */
  private void inOrderPrint(Node rbtNode) {
    if(rbtNode == null) {
      return;
    }
    inOrderPrint(rbtNode.leftChild);
    System.out.println(rbtNode.data.getKeyWord() + rbtNode.data.getinfo());
    inOrderPrint(rbtNode.rightChild);
  }
  
  /** Helper method for the inOrderPrint method
   * 
   */
  public void inOrderAccessor() {
    inOrderPrint(root);
  }
  
  
  /** This method prints out our RBT inorder reversed.
   * @param rbtNode
   */
  private void backwardsPrint(Node rbtNode) {
    if(rbtNode == null) {
      return;
    }
    backwardsPrint(rbtNode.rightChild);
    System.out.println(rbtNode.data.getKeyWord() + rbtNode.data.getinfo());
    backwardsPrint(rbtNode.leftChild);
  }
  
  
  /** Helper method for the backwardsPrint method
   * 
   */
  public void backWardsAccessor() {
    backwardsPrint(root);
  }
  
  
  
  /** This method checks our RBT and returns whether or not the word is within it
   * @param node
   * @param word
   * @return
   */
  private boolean searchWord(Node node, String word) {
    if(node == null) {
      return false;
    }
    if(node.data.getKeyWord().equalsIgnoreCase(word)) {
      return true;
    }
   boolean leftSearch = searchWord(node.leftChild, word);
   if(leftSearch) {
     return true;
   }
   boolean rightSearch = searchWord(node.rightChild, word);
    return rightSearch;
    
  }
  
  /** Helper method for the searchWord, prints whether word was found or not
   * @param string
   */
  public void searchWordHelper(String string) {
    if(searchWord(root, string)){
      System.out.println("The word you searched for is included in the dictionary! Be sure to print out the definitions and look for it!");
    }
    else {
      System.out.println("The word you searched for is not included in the dictionary! Consider adding it!");
    }
  }
  
  /**
   * This method takes two string (word and definition) and writes it to file that contains our dictionary.
   * @param word
   * @param def
   */
  public void writeToFile(String word, String def) {
    File data1File = new File("data1.txt");
    try {
      FileWriter fw = new FileWriter(data1File, true);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.newLine();
      bw.write(word + " " + def);
      bw.close();
      System.out.println("Word has been successfully added to the dictionary!");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
