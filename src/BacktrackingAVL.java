import java.util.ArrayList;
import java.util.List;

public class BacktrackingAVL extends AVLTree {
    // For clarity only, this is the default ctor created implicitly.
    public BacktrackingAVL() {
        super();
    }

    //You are to implement the function Backtrack.
    public void Backtrack() {
        if (!b.isEmpty()) {
            if ((Boolean) b.pop().equals(false)) {
                Node toRemove = (Node) b.pop();
                if (toRemove.parent == null)
                    root = null;
                else if (toRemove.parent.value < toRemove.value)
                    toRemove.parent.right = null;
                else
                    toRemove.parent.left = null;
                updateSizeAndHeight(toRemove.parent);
            } else {
                Node toRotate = (Node) b.pop();
                ImbalanceCases currCase = (ImbalanceCases) b.pop();
                Node toRemove = (Node) b.pop();
                if (currCase == ImbalanceCases.RIGHT_RIGHT) {
                    toRotate = this.rotateRight(toRotate);
                    updateParent(toRotate);
                } else if (currCase == ImbalanceCases.LEFT_LEFT) {
                    toRotate = this.rotateLeft(toRotate);
                    updateParent(toRotate);
                } else if (currCase == ImbalanceCases.RIGHT_LEFT) {
                    toRotate = this.rotateRight(toRotate);
                    updateParent(toRotate);
                    toRotate = this.rotateLeft(toRotate.right);
                    updateParent(toRotate);
                } else {
                    toRotate = this.rotateLeft(toRotate);
                    updateParent(toRotate);
                    toRotate = this.rotateRight(toRotate.left);
                    updateParent(toRotate);
                }
                if(toRemove.value > toRemove.parent.value)
                    toRemove.parent.right = null;
                else
                    toRemove.parent.left = null;
                updateSizeAndHeight(toRotate);
            }
        }
    }


    //Change the list returned to a list of integers answering the requirements
    public static List<Integer> AVLTreeBacktrackingCounterExample() {
        List<Integer> example = new ArrayList<>();
        example.add(5);
        example.add(4);
        example.add(3);
        return example;
    }

    public int Select(int index) {
        if (root != null)
            return root.Select(index);
        return 0;
    }

    public int Rank(int value) {
        if (root != null)
            return root.Rank(value);
        return 0;
    }

    private void updateParent(Node toUpdate) {
        if (toUpdate.parent == null)
            root = toUpdate;
        else {
            if (toUpdate.value > toUpdate.parent.value)
                toUpdate.parent.right = toUpdate;
            else
                toUpdate.parent.left = toUpdate;
        }
    }

    private void updateSizeAndHeight(Node node) {
        Node curr = node;
        while (curr != null) {
            curr.updateHeight();
            curr.updateSize();
            curr = curr.parent;
        }
    }
}



