import java.util.Comparator;

public class SuffixComparator implements Comparator<String> {

	public int compare(String s1, String s2) {
		int i;
		String p, q;
		i = s1.length() - 1;
		while (i >= 0 && s1.charAt(i) != '\\' && s1.charAt(i) != '/')
			--i;
		p = s1.substring(i + 1, s1.length());

		i = s2.length() - 1;
		while (i >= 0 && s2.charAt(i) != '\\' && s2.charAt(i) != '/')
			--i;
		q = s2.substring(i + 1, s2.length());

		return p.compareTo(q);
	}

}
