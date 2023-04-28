import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Comparator;

public class ListNodes<T> {

	ArrayList<T> list;
	
	public ListNodes() {
		list = new ArrayList<T>();
	}
	
	public void add(T element) {
		list.add(element);
	}
	
	public Iterator<T> sortedList(Comparator<T> sort) {
		if (list.size() == 0) return null;
		Collections.sort(list,sort);
		return list.iterator();
	}
	
	public Iterator<T> getList() {
		return list.iterator();
	}
}
