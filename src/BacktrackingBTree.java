import java.util.Deque;
import java.util.List;
import java.util.ArrayList;
import java.util.ArrayDeque;
public class BacktrackingBTree<T extends Comparable<T>> extends BTree<T> {
	// For clarity only, this is the default ctor created implicitly.
	public BacktrackingBTree() {
		super();
	}

	public BacktrackingBTree(int order) {
		super(order);
	}

	//You are to implement the function Backtrack.
	public void Backtrack() {
		if(!b.isEmpty()) {
			T toRemove = (T)b.pop();
			Node<T> target = getNode(toRemove);
			target.removeKey(toRemove);
			while(b.peek() instanceof Node<?>) {
				Node<T> original = (Node<T>)b.pop();
				original.numOfChildren=0;
				for(int i = 0 ; i<original.numOfKeys ; i++) {
					if(i!=original.numOfKeys/2){
						Node<T> removeChildren=getNode(original.getKey(i));
						if(removeChildren!=null) {
							for (int j = 0; j < removeChildren.numOfChildren; j++)
								original.addChild(removeChildren.getChild(j));
							removeChildren.parent.removeChild((removeChildren));
						}
					}
					else {
						Node<T> tmp = getNode(original.getKey(i));
						tmp.removeKey(original.getKey(i));
						original.parent=tmp;
					}
				}
				if(original.parent == null || original.parent.numOfKeys==0)
					root = original;
				else
				    original.parent.addChild(original);
			}
			size--;
			if(size() == 0)
				root=null;
		}
    }
	
	//Change the list returned to a list of integers answering the requirements
	public static List<Integer> BTreeBacktrackingCounterExample(){
		List<Integer> example = new ArrayList<Integer>();
		example.add(3);
		example.add(6);
		example.add(9);
		example.add(12);
		example.add(15);
		example.add(7);
		return example;
	}

	public static void main(String[] args) {
		BacktrackingBTree<Integer> b = new BacktrackingBTree<>();
		b.insert(1);
		b.insert(2);
		b.Backtrack();
		b.Backtrack();
	}
}
