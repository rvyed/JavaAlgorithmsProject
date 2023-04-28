import java.util.Iterator;

public class PrintFileStructure {

	public static final int SORT_BY_NAME = 1;
	public static final int SORT_BY_SIZE = 2;
	public static final int WAITING_INTERVAL = 5000; // Waiting time before
														// printing a "."

	private int howSort; // If value = 1 print the file tree with file objects
							// sorted by name, else sort them by size

	private int longestName = 0;
	private int numFiles = 0;

	/**
	 * If sorting = SORT_BY_NAME the file tree is printed with file object
	 * sorted by name If sorting = SORT_BY_SIZE the file tree is printed with
	 * file object sorted by size
	 */
	public PrintFileStructure(int sorting) {
		howSort = sorting;
	}

	/**
	 * 
	 * @return length of longest name of the file objects inside the root file
	 *         object
	 */
	public int getLongestName() {
		return longestName;
	}

	/*
	 * Returns a String object storing the file tree represented by the tree
	 * specified in the first parameter. If instance variable howSort =
	 * SORT_BY_NAME the file objects are sorted by name inside each folder,
	 * otherwise they are sorted by size
	 * 
	 * @return String object storing the file tree with root r
	 * 
	 * @param r: root of the file tree
	 * 
	 * @param indent: indentation for printing the name of file object
	 * represented by r
	 */
	public String printTree(NLNode<FileObject> r, int indent) {
		String list = "";
		String spaces = "";
		if (indent == 0) {
			longestName = 0;
			numFiles = 0;
		} else
			++numFiles;
		if (numFiles % WAITING_INTERVAL == (WAITING_INTERVAL - 1))
			System.out.print(".");

		// Set the proper number of spaces for the required indentation
		for (int i = 0; i < indent; ++i)
			spaces = spaces + "   ";
		if (r == null)
			return "";

		list = spaces + r.getData().getName() + "|" + r.getData().getSize() + "\n";

		// Compute the length of longest name inside given FileObject
		if (longestName < r.getData().getName().length() + spaces.length())
			longestName = r.getData().getName().length() + spaces.length();

		if (r.getData().isDirectory()) {
			// If current FileObject is a directory, recursively get information
			// from subdirectories
			Iterator<NLNode<FileObject>> dir;
			if (howSort == SORT_BY_NAME)
				dir = r.getChildren(new NameComparator());
			else
				dir = r.getChildren(new SizeComparator());
			if (dir == null)
				return list;
			while (dir.hasNext()) {
				NLNode<FileObject> child = dir.next();
				list = list + printTree(child, indent + 1);
			}
		}
		return list;
	}

	/**
	 * Returns a String representation of the iterator list, containing a list
	 * of files
	 * 
	 * @return String representation of the iterator list
	 * @param list:
	 *            iterator containing a list of files
	 */
	public String printIterator(Iterator<String> list) {
		String s = "";
		if (list != null)
			while (list.hasNext())
				s = s + list.next() + "\n";
		return s;
	}

	/**
	 * Formats the given text for printing in the GUI
	 * 
	 * @param text
	 *            to be formatted
	 * @param longest
	 *            length of the longest file object name in the given text
	 */
	public String formatTreeOutput(String text) {
		int numFiles = 0, size;
		// Count number of file names in the input text
		for (int i = 0; i < text.length(); ++i)
			if (text.charAt(i) == '|')
				++numFiles;

		size = (numFiles + 1) * (longestName + 18);
		char[] buffer = new char[size];

		// Format the names so they appear with the proper indentation
		int count = 0, textCount = 0;
		char c;
		int d;

		for (int i = 0; i < longestName + 1; ++i)
			buffer[count++] = ' ';
		buffer[count++] = 'S';
		buffer[count++] = 'i';
		buffer[count++] = 'z';
		buffer[count++] = 'e';
		buffer[count++] = '\n';

		for (int i = 0; i < numFiles; ++i) {
			if (i % WAITING_INTERVAL == (WAITING_INTERVAL - 1))
				System.out.print(".");
			// Copy the name of the file to buffer
			d = 0;
			while (count < size && ((c = text.charAt(textCount)) != '|')) {
				buffer[count++] = c;
				++textCount;
				++d;
			}
			++textCount; // Skip the symbol '|'

			// Add enough spaces so all file names appear aligned
			while (d < longestName + 1) {
				buffer[count++] = ' ';
				++d;
			}

			d = 0;
			while (count < size && ((c = text.charAt(textCount)) != '\n')) {
				buffer[count++] = c;
				++textCount;
				++d;
			}
			++textCount; // Skip the '\n';

			// Add enough spaces so all file sizes appear aligned
			while (d < 15) {
				buffer[count++] = ' ';
				++d;
			}

			buffer[count++] = '\n';

		}
		return new String(buffer);
	}

}
