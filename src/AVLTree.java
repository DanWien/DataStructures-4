import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Deque;
import java.util.ArrayDeque;
public class AVLTree implements Iterable<Integer> {
    // You may edit the following nested class:
    protected class Node {
    	public Node left = null;
    	public Node right = null;
    	public Node parent = null;
    	public int height = 0;
    	public int value;
        protected int size=0;

    	public Node(int val) {
            this.value = val;
        }

        public void updateHeight() {
            int leftHeight = (left == null) ? -1 : left.height;
            int rightHeight = (right == null) ? -1 : right.height;

            height = Math.max(leftHeight, rightHeight) + 1;
        }

        public void updateSize() {
            int leftSize = (left == null) ? 0 : left.size;
            int rightSize = (right == null) ? 0 : right.size;

            size = leftSize + rightSize + 1;
        }

        public int getBalanceFactor() {
            int leftHeight = (left == null) ? -1 : left.height;
            int rightHeight = (right == null) ? -1 : right.height;

            return leftHeight - rightHeight;
        }

        public int Rank(int value) {
            if(this.value==value) {
                if(this.left==null)
                    return 0;
                else
                    return this.left.size;
            }
            else if (this.value>value) {
                if (this.left == null)
                    return 0;
                else
                    return this.left.Rank(value);
            }
            else {
                if(this.right==null) {
                    if(this.left==null)
                        return 1;
                    else
                        return this.left.size+1;
                }
                else {
                    if(this.left==null)
                        return right.Rank(value)+1;
                    else
                        return this.right.Rank(value) + this.left.size + 1;
                }
            }
        }

        public int Select(int index) {
            int curr;
            if (this.left==null)
                curr=1;
            else
                curr = this.left.size+1;
            if(curr == index)
                return this.value;
            else if (index<curr)
                return this.left.Select(index);
            else
                return this.right.Select(index-curr);
        }
    }
    
    protected Node root;
    protected Deque<Object> b;
    protected Boolean wasRotated = false;
    //You may add fields here.
    
    public AVLTree() {
    	this.root = null;
        b = new ArrayDeque<Object>();
    }
    
    /*
     * IMPORTANT: You may add code to both "insert" and "insertNode" functions.
     */
	public void insert(int value) {
    	root = insertNode(root, value);
        b.push(wasRotated);
        wasRotated = false;
    }
	
	protected Node insertNode(Node node, int value) {
	    // Perform regular BST insertion
        if (node == null) {
        	Node insertedNode = new Node(value);
            insertedNode.size++;
            b.push(insertedNode);
            return insertedNode;
        }

        if (value < node.value) {
            node.left = insertNode(node.left, value);
            node.left.parent = node;
        }
        else {
            node.right = insertNode(node.right, value);
            node.right.parent = node;
        }
            
        node.updateHeight();
        node.updateSize();

        /* 
         * Check For Imbalance, and fix according to the AVL-Tree Definition
         * If (balance > 1) -> Left Cases, (balance < -1) -> Right cases
         */
        
        int balance = node.getBalanceFactor();
        
        if (balance > 1) {
            wasRotated = true;
            if (value > node.left.value) {
                b.push(ImbalanceCases.LEFT_RIGHT);
                node.left = rotateLeft(node.left);
            }
            else
                b.push(ImbalanceCases.LEFT_LEFT);
            node = rotateRight(node);
            b.push(node);
        } else if (balance < -1) {
            wasRotated = true;
            if (value < node.right.value) {
                b.push(ImbalanceCases.RIGHT_LEFT);
                node.right = rotateRight(node.right);
            }
            else
                b.push(ImbalanceCases.RIGHT_RIGHT);
            node = rotateLeft(node);
            b.push(node);
        }
        return node;
    }
    
	// You may add additional code to the next two functions.
    protected Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;
        
        //Update parents
        if (T2 != null) {
        	T2.parent = y;
        }

        x.parent = y.parent;
        y.parent = x;
        
        y.updateHeight();
        x.updateHeight();
        y.updateSize();
        x.updateSize();

        // Return new root
        return x;
    }

    protected Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;
        
        //Update parents
        if (T2 != null) {
        	T2.parent = x;
        }
        
        y.parent = x.parent;
        x.parent = y;
        
        x.updateHeight();
        y.updateHeight();
        x.updateSize();
        y.updateSize();

        // Return new root
        return y;
    }
    
    public void printTree() {
    	TreePrinter.print(this.root);
    }

    /***
     * A Printer for the AVL-Tree. Helper Class for the method printTree().
     * Not relevant to the assignment.
     */
    private static class TreePrinter {
        private static void print(Node root) {
            if(root == null) {
                System.out.println("(XXXXXX)");
            } else {    
                final int height = root.height + 1;
                final int halfValueWidth = 4;
                int elements = 1;
                
                List<Node> currentLevel = new ArrayList<Node>(1);
                List<Node> nextLevel    = new ArrayList<Node>(2);
                currentLevel.add(root);
                
                // Iterating through the tree by level
                for(int i = 0; i < height; i++) {
                    String textBuffer = createSpaceBuffer(halfValueWidth * ((int)Math.pow(2, height-1-i) - 1));
        
                    // Print tree node elements
                    for(Node n : currentLevel) {
                        System.out.print(textBuffer);
        
                        if(n == null) {
                            System.out.print("        ");
                            nextLevel.add(null);
                            nextLevel.add(null);
                        } else {
                            System.out.printf("(%6d)", n.value);
                            nextLevel.add(n.left);
                            nextLevel.add(n.right);
                        }
                        
                        System.out.print(textBuffer);
                    }
        
                    System.out.println();
                    
                    if(i < height - 1) {
                        printNodeConnectors(currentLevel, textBuffer);
                    }
        
                    elements *= 2;
                    currentLevel = nextLevel;
                    nextLevel = new ArrayList<Node>(elements);
                }
            }
        }
        
        private static String createSpaceBuffer(int size) {
            char[] buff = new char[size];
            Arrays.fill(buff, ' ');
            
            return new String(buff);
        }
        
        private static void printNodeConnectors(List<Node> current, String textBuffer) {
            for(Node n : current) {
                System.out.print(textBuffer);
                if(n == null) {
                    System.out.print("        ");
                } else {
                    System.out.printf("%s      %s",
                            n.left == null ? " " : "/", n.right == null ? " " : "\\");
                }
    
                System.out.print(textBuffer);
            }
    
            System.out.println();
        }
    }

    /***
     * A base class for any Iterator over Binary-Search Tree.
     * Not relevant to the assignment, but may be interesting to read!
     * DO NOT WRITE CODE IN THE ITERATORS, THIS MAY FAIL THE AUTOMATIC TESTS!!!
     */
    private abstract class BaseBSTIterator implements Iterator<Integer> {
        private List<Integer> values;
        private int index;
        public BaseBSTIterator(Node root) {
            values = new ArrayList<>();
            addValues(root);
            
            index = 0;
        }
        
        @Override
        public boolean hasNext() {
            return index < values.size();
        }

        @Override
        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            
            return values.get(index++);
        }
        
        protected void addNode(Node node) {
            values.add(node.value);
        }
        
        abstract protected void addValues(Node node);
    }
    
    public class InorderIterator extends BaseBSTIterator {
        public InorderIterator(Node root) {
            super(root);
        }

        @Override
        protected void addValues(Node node) {
            if (node != null) {
                addValues(node.left);
                addNode(node);
                addValues(node.right);
            }
        }    
      
    }
    
    public class PreorderIterator extends BaseBSTIterator {

        public PreorderIterator(Node root) {
            super(root);
        }

        @Override
        protected void addValues(AVLTree.Node node) {
            if (node != null) {
                addNode(node);
                addValues(node.left);
                addValues(node.right);
            }
        }        
    }
    
    @Override
    public Iterator<Integer> iterator() {
        return getInorderIterator();
    }
    
    public Iterator<Integer> getInorderIterator() {
        return new InorderIterator(this.root);
    }
    
    public Iterator<Integer> getPreorderIterator() {
        return new PreorderIterator(this.root);
    }

    public static void main(String[] args) {
        AVLTree t = new AVLTree();
        t.insert(1);
        t.insert(3);
        t.insert(2);
        t.printTree();
        t.root=t.rotateRight(t.root);
        t.printTree();
        t.root.right=t.rotateLeft(t.root.right);
        t.printTree();
        t.root.right.left=null;
        t.printTree();
    }


}
