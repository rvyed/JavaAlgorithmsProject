import java.util.Comparator;

public class StringComparator implements Comparator<NLNode<String>> {

	public int compare(NLNode<String> obj1, NLNode<String> obj2) {
		return obj1.getData().compareTo(obj2.getData());
	}
}
