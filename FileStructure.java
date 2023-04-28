import java.util.ArrayList;
import java.util.Iterator;

public class FileStructure {
    // The root node of our file structure
    private NLNode<FileObject> root;

    // Constructor to initialize the file structure with the given file object name
    public FileStructure(String fileObjectName) throws FileObjectException {
        FileObject fileObject = new FileObject(fileObjectName);
        root = new NLNode<>(fileObject, null);
        // If the file object is a directory, build the file structure
        if (fileObject.isDirectory()) {
            buildFileStructure(root);
        }
    }

    // Builds the file structure by traversing the directory
    private void buildFileStructure(NLNode<FileObject> node) {
        FileObject fileObject = node.getData();
        // If it's a file, we don't need to build the structure
        if (fileObject.isFile()) {
            return;
        }

        Iterator<FileObject> iterator = fileObject.directoryFiles();
        iteratorTraversal(iterator, node, true);
    }

    // Traverses the iterator to build the file structure
    private void iteratorTraversal(Iterator<FileObject> iterator, NLNode<FileObject> node, boolean hasNextChild) {
        if (!iterator.hasNext()) {
            return;
        }

        // Get the next file object and create a new child node
        FileObject childFileObject = iterator.next();
        NLNode<FileObject> childNode = new NLNode<>(childFileObject, node);
        node.addChild(childNode);
        buildFileStructure(childNode);

        if (hasNextChild) {
            iteratorTraversal(iterator, node, iterator.hasNext());
        }
    }

    // Getter for the root node of the file structure
    public NLNode<FileObject> getRoot() {
        return root;
    }

    // Returns an iterator of files with the specified type
    public Iterator<String> filesOfType(String type) {
        ArrayList<String> files = new ArrayList<>();
        filesOfTypeRecursive(root, type, files, true);
        return files.iterator();
    }

    // Recursively find files of the specified type
    private void filesOfTypeRecursive(NLNode<FileObject> node, String type, ArrayList<String> files, boolean includeChildren) {
        FileObject fileObject = node.getData();
        // If the file object matches the type, add it to the list
        if (fileObject.isFile() && fileObject.getLongName().endsWith(type)) {
            files.add(fileObject.getLongName());
        }

        // If includeChildren is true, search the children
        if (includeChildren) {
            Iterator<NLNode<FileObject>> iterator = node.getChildren();
            while (iterator.hasNext()) {
                filesOfTypeRecursive(iterator.next(), type, files, true);
            }
        }
    }

    // Finds the specified file by name
    public String findFile(String name) {
        return findFileRecursive(root, name, true);
    }

    // Recursively find the file with the given name
    private String findFileRecursive(NLNode<FileObject> node, String name, boolean searchChildren) {
        FileObject fileObject = node.getData();
        // If the file object matches the name, return it
        if (fileObject.isFile() && fileObject.getName().equals(name)) {
            return fileObject.getLongName();
        }

        String result = "";
        // If searchChildren is true, search the children
        if (searchChildren) {
            Iterator<NLNode<FileObject>> iterator = node.getChildren();
            while (iterator.hasNext()) {
                result = findFileRecursive(iterator.next(), name, true);
                // If we found the file, break the loop
                if (!result.isEmpty()) {
                    break;
                }
            }
        }

        return result;
    }
}
