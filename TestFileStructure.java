import java.util.Iterator;

public class TestFileStructure {
	// String representation of sevaral file systems to be used for testing
	private static String[] files = {"folder1:0:", 
			"folder2:3:file1.java:file2.txt:file3.java:", "folder2/file1.java:0:",
			"folder2/file2.txt:0:", "folder2/file3.java:0:", "folder3:3:folderA:folderB:folderC:",
			"folder3/folderA:2:folderB:folderD:", "folder3/folderB:1:file1.gif:",
			"folder3/folderC:2:folderE:file7.java:", "folder3/folderA/folderB:1:file1.gif:",
			"folder3/folderA/folderD:2:folderF:file2.java:", "folder3/folderB/file1.gif:0:",
			"folder3/folderC/folderE:2:file6.java:folderD:", "folder3/folderC/file7.java:0:",
			"folder3/folderA/folderB/file1.gif:0:", "folder3/folderA/folderD/folderF:3:file5.gif:file4.txt:file3.java:",
			"folder3/folderA/folderD/file2.java:0:", "folder3/folderC/folderE/file6.java:0:",
			"folder3/folderC/folderE/folderD:2:folderF:file2.java:", "folder3/folderA/folderD/folderF/file5.gif:0:",
			"folder3/folderA/folderD/folderF/file4.txt:0:", "folder3/folderA/folderD/folderF/file3.java:0:",
			"folder3/folderC/folderE/folderD/folderF:3:file3.java:file4.txt:file5.gif:",
			"folder3/folderC/folderE/folderD/file2.java:0:", "folder3/folderC/folderE/folderD/folderF/file3.java:0:",
			"folder3/folderC/folderE/folderD/folderF/file4.txt:0:",
			"folder3/folderC/folderE/folderD/folderF/file5.gif:0:" };
	
	/* Test that FileStructure objects created have the correct structure, so all nodes are 
	   created and they have the correct set of children. Test also methods filesOfType and
	   findFile from class FileStructure.                                                   */
	public static void main(String[] args) {
		String[] data = {"child1", "child5", "child2", "child4", "child3"};
		int[] sortedIndex = {0, 2, 4, 3, 1};

		// Test1 is for NLNode class
		// =====================
		// Create the root node of a tree that will have 6 nodes
		NLNode<String> root = new NLNode<String>("root", null), node;
		Iterator<NLNode<String>> children;
		boolean testPassed = true;
		int index = 0;

		// Add 5 children to the root node
		for (int i = 0; i < 5; ++i)
			root.addChild(new NLNode<String>(data[i], root));

		// Test getChildren method. Children must be retrieved in the proper order
		children = root.getChildren();
		while (children.hasNext() && testPassed) {
			node = children.next();
			if (!node.getData().equals(data[index]))
				testPassed = false;
			else
				++index;
		}

		if (index < data.length)
			testPassed = false;

		// Test getChildren(Comparator). Children must be retrieved in the proper order
		StringComparator comparator = new StringComparator();
		children = root.getChildren(comparator);

		index = 0;
		while (children.hasNext() && testPassed) {
			node = children.next();
			if (!node.getData().equals(data[sortedIndex[index]]))
				testPassed = false;
			else
				++index;
		}
		if (index < data.length)
			testPassed = false;

		// Test setData and getParent
		children = root.getChildren();
		node = children.next();
		node.setData("child6");

		children = root.getChildren(comparator);
		node = children.next();
		if (!node.getData().equals("child2"))
			testPassed = false;

		if (node.getParent() != root)
			testPassed = false;

		System.out.println("\nTests for classes NLNode and FileStructure");
		System.out.println("--------------------------------------------");
		
		if (testPassed)
			System.out.println("Test 1 passed");
		else
			System.out.println("Test 1 failed");
		

		// Test 2. Check that the program correctly builds a tree with a single
		// node storing "folder1"
		// ====================================================================
		FileObject.setTestingMode(1);
		FileObject.setFiles(files);
		NLNode<FileObject> r;
		testPassed = true;
		try {
			FileStructure tree = new FileStructure("folder1");
			if (FileObject.testFailed())
				System.out.println("Test 2 failed: Inexistent file object name");
			else {
				r = tree.getRoot();
				// Root node has no children, it must be a file
				if (!r.getData().isFile())
					testPassed = false;
				if (!r.getData().getName().equals("folder1"))
					testPassed = false;
				if (testPassed)
					System.out.println("Test 2 passed");
				else
					System.out.println("Test 2 failed");
			}
		} catch (Exception e) {
			System.out.println("Test 2 failed");
		}

		tests3_5();
		test6();
		test7();
		test8();
	}
	
	// Test 3. Build a tree for the following file system:
	// folder2
	// --file1.java
	// --file2.txt
	// --file3.java
	//
	// ===================================================
	// Tests 4 and 5 use the same tree
	private static void tests3_5() {
		FileStructure tree;
		NLNode<FileObject> r;
		Iterator<NLNode<FileObject>> child;
		boolean testPassed = true;
		String[] childrenNames = { "folder2/file1.java", "folder2/file2.txt", "folder2/file3.java" };
		String s;
		try {
			tree = new FileStructure("folder2");
			// Check that the structure of the system is correct
			if (FileObject.testFailed())
				System.out.println("Test 3 failed: Inexistent file object name");
			else {
				r = tree.getRoot();
				if (r.getData().isFile())
					testPassed = false;
				if (!r.getData().getName().equals("folder2"))
					testPassed = false;
				child = r.getChildren();
				for (int i = 0; i < 3; ++i) {
					s = child.next().getData().getLongName();
					if (!s.equals(childrenNames[i]))
						testPassed = false;
				}
				if (testPassed)
					System.out.println("Test 3 passed");
				else
					System.out.println("Test 3 failed");
			}
		} catch (Exception e) {
			System.out.println("Test 3 failed");
		}

		// Test 4. Test findFile
		// ======================
		testPassed = true;
		Iterator<String> listFiles;
		String s1;
		try {
			tree = new FileStructure("folder2");
			s = tree.findFile("file2.txt");
			if (!s.equals("folder2/file2.txt"))
				testPassed = false;
			s = tree.findFile("nonExistent");
			if (!s.equals(""))
				testPassed = false;

			if (testPassed)
				System.out.println("Test 4 passed");
			else
				System.out.println("Test 4 failed");
		} catch (Exception e) {
			System.out.println("Test 4 failed");
		}

		// Test 5. Test filesOfType
		// ========================
		testPassed = true;
		try {
			tree = new FileStructure("folder2");
			listFiles = tree.filesOfType(".java");
			s = listFiles.next();
			if (!s.equals("folder2/file1.java") && !s.equals("folder2/file3.java"))
				testPassed = false;
			s1 = listFiles.next();
			if ((!s1.equals("folder2/file1.java") && !s1.equals("folder2/file3.java")) || s.equals(s1))
				testPassed = false;

			listFiles = tree.filesOfType(".docx");
			if (listFiles.hasNext())
				testPassed = false;

			if (testPassed)
				System.out.println("Test 5 passed");
			else
				System.out.println("Test 5 failed");
		} catch (Exception e) {
			System.out.println("Test 5 failed");
		}
	}
	
	// Test 6. Build a tree for the following file system. The same tree is
	// used in tests 7-10.
	// folder3
	// --folderA
	// ----folderB
	// ------file1.gif
	// ----folderD
	// ------folderF
	// --------file5.gif
	// --------file4.txt
	// --------file3.java
	// ------file2.java
	// --folderB
	// ----file1.gif
	// -- folderC
	// ----folderE
	// ------file6.java
	// ------folderD
	// --------folderF
	// ----------file3.java
	// ----------file4.txt
	// ----------file5.gif
	// --------file2.java
	// ----file7.java
	// ========================================================================
	
	private static void test6() {
		String[] childrenNames = {"folder3/folderA", "folder3/folderB", "folder3/folderC"};
		boolean testPassed = true;
		NameComparator comp = new NameComparator();
		Iterator<NLNode<FileObject>> child;
		FileStructure tree;
		NLNode<FileObject> r;
		String s;
		try {
			tree = new FileStructure("folder3");
			// Check that the structure of the system is correct
			if (FileObject.testFailed())
				System.out.println("Test 6 failed: Inexistent file object name");
			else {
				r = tree.getRoot();
				if (r.getData().isFile())
					testPassed = false;
				if (!r.getData().getName().equals("folder3"))
					testPassed = false;
				child = r.getChildren();
				for (int i = 0; i < 3; ++i) {
					s = child.next().getData().getLongName();
					if (!s.equals(childrenNames[i]))
						testPassed = false;
				}

				child = r.getChildren(comp);
				r = child.next(); // folderA
				child = r.getChildren(comp);
				r = child.next();
				r = child.next(); // folderD
				child = r.getChildren(comp);
				r = child.next();
				if (!r.getData().getName().equals("file2.java"))
					testPassed = false;

				if (testPassed)
					System.out.println("Test 6 passed");
				else
					System.out.println("Test 6 failed");
			}
		} catch (Exception e) {
			System.out.println("Test 6 failed");
		}
		
	}
	
	// Test 7. Test findFile
	// =====================
	private static void test7() {
	
		boolean testPassed = true;
		FileStructure tree;
		String s;

		try {
			tree = new FileStructure("folder3");
			if (FileObject.testFailed())
				System.out.println("Test 7 failed: Inexistent file object name");
			else {
				s = tree.findFile("file6.java");
				if (!s.equals("folder3/folderC/folderE/file6.java")) testPassed = false;
				s = tree.findFile("file5.gif");
				if (!s.equals("folder3/folderA/folderD/folderF/file5.gif") &&
						!s.equals("folder3/folderC/folderE/folderD/folderF/file5.gif"))
					testPassed = false;
				s = tree.findFile("file.java");
				if (!s.equals("")) testPassed = false; 
				if (testPassed)
					System.out.println("Test 7 passed");
				else
					System.out.println("Test 7 failed");
			}
		} catch (Exception e) {
			System.out.println("Test 7 failed");
		}	
	}
	
	// Test 8. Test filesOfType
	// =========================
	private static void test8() {
		boolean testPassed = true;
		Iterator<String> list;
		String[] output = {"folder3/folderA/folderD/folderF/file4.txt",
				"folder3/folderC/folderE/folderD/folderF/file4.txt"};
		FileStructure tree;
		String s;
		try {
			tree = new FileStructure("folder3");
			if (FileObject.testFailed())
				System.out.println("Test 8 failed: Inexistent file object name");
			else {
				list = tree.filesOfType(".txt");
				
				// Check that correct files were found
				for (int i = 0; i < 2; ++i) {
					s = list.next();
					for (int j = 0; j < 2; ++j)
						if (s.equals(output[j])) output[j] = "";
				}
				
				// Check that all files were found
				if (list.hasNext() || !output[0].equals("") || !output[1].equals(""))
					testPassed = false;
				
				if (testPassed)
					System.out.println("Test 8 passed");
				else
					System.out.println("Test 8 failed");
			}
		} catch (Exception e) {
			System.out.println("Test 8 failed");
		}
	}
	
	// Returns the short form of a file name
	private static String getShortName(String name) {
		int i = name.length() -1;
		
		// short name starts after character '/' or at position 0
		while ((i >= 0) && (name.charAt(i) != '/')) --i;
		return name.substring(i);
	}

}
