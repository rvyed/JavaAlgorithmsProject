import java.util.Comparator;
import java.util.Iterator;

public class NLNode<T> {
    private NLNode<T> parent; // The parent node
    private ListNodes<NLNode<T>> children; // A list of child nodes
    private T data; // The data stored in this node

    // Default constructor, initializes an empty node
    public NLNode() {
        this.parent = null;
        this.data = null;
        this.children = new ListNodes<>();
    }

    // Constructor that initializes a node with data and a parent
    public NLNode(T d, NLNode<T> p) {
        this.data = d;
        this.parent = p;
        this.children = new ListNodes<>();
    }

    // Setter for the parent node
    public void setParent(NLNode<T> p) {
        this.parent = p;
    }

    // Getter for the parent node
    public NLNode<T> getParent() {
        return this.parent;
    }

    // Adds a child node to the current node
    public void addChild(NLNode<T> newChild) {
        newChild.setParent(this);
        this.children.add(newChild);
    }

    // Returns an iterator for the child nodes
    public Iterator<NLNode<T>> getChildren() {
        return this.children.getList();
    }

    // Returns a sorted iterator for the child nodes, based on a provided comparator
    public Iterator<NLNode<T>> getChildren(Comparator<NLNode<T>> sorter) {
        return this.children.sortedList(sorter);
    }

    // Getter for the data stored in this node
    public T getData() {
        return this.data;
    }

    // Setter for the data stored in this node
    public void setData(T d) {
        this.data = d;
    }
}
