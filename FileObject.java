import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class FileObject {

	// testingMode = 0: normal operation using the file system
	// testingMode = 1: test methods with virtual file system
	private static int testingMode = 0;
	private static boolean testFailed = false;
	private static String[] files;

	private int numChildren;
	private String name;
	private int numFile;
	private ArrayList<FileObject> children;

	private File fileRef;                 // File object representing this FileObject
	public static int numFileObjects = 0; // Number of file objects in the file system
	private static final int WAITING_INTERVAL = 1000; // Number of files to scan before the
													  // program prints an indication that it
													  // is still running

	

	/**
	 * Create a FileObject for the file object with the given name
	 * 
	 * @param name:
	 *            name of the file or folder to be represented by this
	 *            FileObject
	 * @throws FileObjectException
	 *             when FileObject cannot be created
	 */
	public FileObject(String name) throws FileObjectException {
		String[] fi = files;
		if (testingMode > 0) {
			numFile = findFile(name);
			// Get the attributes of this file object
			this.name = name;
			children = new ArrayList<FileObject>();
			numChildren = 0;
			int begin = 0, end = 0;
			int segment = 0;
			String file = files[numFile];
			// Format of each line is file name: # children : children
			while (begin < file.length()) {
				while (file.charAt(end) != ':')
					++end;
				if (segment == 1)
					numChildren = Integer.parseInt(file.substring(begin, end));
				else if (segment > 1)
					children.add(new FileObject(name + "/" + file.substring(begin, end)));
				++segment;
				begin = end + 1;
				end = begin + 1;
			}

		} else
			try {
				fileRef = new File(name);
				if (!fileRef.exists())
					throw new FileObjectException("File object " + name + " could not be created");
				++numFileObjects;
				if ((numFileObjects % WAITING_INTERVAL) == WAITING_INTERVAL - 1) {
					String text = SplitPanel.getPageText();
					text = text + ".";
					SplitPanel.setPageText(text);
				}
			} catch (NullPointerException e) {
				throw new FileObjectException("File object " + name + " could not be created");
			}
	}

	/**
	 * Create a FileObject for the given File
	 * 
	 * @param f:
	 *            File representing a virtual file or folder
	 * @throws FileObjectException
	 *             when FileObject cannot be created
	 */
	public FileObject(File f) throws FileObjectException {
		try {
			fileRef = f;
			++numFileObjects;
			if ((numFileObjects % WAITING_INTERVAL) == WAITING_INTERVAL - 1) {
				String text = SplitPanel.getPageText();
				text = text + ".";
				SplitPanel.setPageText(text);
			}
		} catch (Exception e) {
			throw new FileObjectException("File object could not be created");
		}
	}

	/**
	 * 
	 * @return True if this FileObject is a file.
	 */
	public boolean isFile() {
		if (testingMode > 0)
			return (numChildren == 0);
		return fileRef.isFile();
	}

	/**
	 * 
	 * @return True if this FileObject is a directory or folder
	 */
	public boolean isDirectory() {
		if (testingMode > 0)
			return (numChildren > 0);
		return fileRef.isDirectory();
	}

	/**
	 * 
	 * @return Short name of this file, without the absolute path to it
	 */
	public String getName() {
		if (testingMode > 0) {
			int i = name.length() - 1;
			while ((i >= 0) && name.charAt(i) != '/')
				--i;
			if (i < 0)
				return name;
			else
				return name.substring(i + 1);
		}
		return fileRef.getName();
	}

	/**
	 * 
	 * @return Long name for this file including the absolute path to it
	 */
	public String getLongName() {
		if (testingMode > 0)
			return name;
		try {
			return fileRef.getCanonicalPath();
		} catch (IOException e) {
			System.out.println("Error getting name of object file");
			return "";
		}
	}

	/**
	 * 
	 * @return Size of the file in bytes. Returns zero if thif FIleObject is a
	 *         directory.
	 */
	public long getSize() {
		if (testingMode > 0)
			return 0;
		if (isDirectory())
			return fileRef.length();
		else
			return fileRef.length();
	}

	/**
	 * 
	 * @return Number of files in the directory represented by this FileObject.
	 *         If this FileObject represents a file the value returned is zero.
	 */
	public int numFilesInDirectory() {
		if (testingMode > 0)
			return numChildren;
		if (isDirectory())
			return fileRef.list().length;
		else
			return 0;
	}

	/**
	 * 
	 * @return List of FileObjects inside the directory represented by this
	 *         FileObject. If this FileObject represents a file, a null value is
	 *         returned.
	 */
	public Iterator<FileObject> directoryFiles() {
		if (testingMode > 0)
			return children.iterator();
		try {
			if (isDirectory()) {
				File[] files = fileRef.listFiles();
				ArrayList<FileObject> fileObjects = new ArrayList<FileObject>();
				if (files == null)
					return fileObjects.iterator();
				for (int i = 0; i < files.length; ++i)
					if (files[i] != null)
						fileObjects.add(new FileObject(files[i]));
				return fileObjects.iterator();
			} else
				return null;
		} catch (FileObjectException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public static void resetNumFileObjects() {
		numFileObjects = 0;
	}

	public static int getNumFileObjects() {
		return numFileObjects;
	}
	
		public static void setTestingMode(int mode) {
		testingMode = mode;
	}

	public static boolean testFailed() {
		return testFailed;
	}

    // Initializes the virutal file system used for testing
	public static void setFiles(String[] theFiles) {
		files = theFiles;
	}
	
	/* Find the position of name in the files array used for testing*/
	private int findFile(String name) {
		int i = 0;
		String[] s = files;
		for (i = 0; i < files.length; ++i) {
			int pos = 0;
			// Name of file object ends at character before the colon symbol
			while (files[i].charAt(pos) != ':')
				++pos;
			if (name.equals(files[i].substring(0, pos)))
				break;
		}
		if (i == files.length)
			testFailed = true;
		return i;

	}
	
}
