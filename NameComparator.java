import java.util.Comparator;

public class NameComparator implements Comparator<NLNode<FileObject>> {

	public int compare(NLNode<FileObject> obj1, NLNode<FileObject> obj2) {
		return obj1.getData().getName().compareTo(obj2.getData().getName());
	}
	
}
