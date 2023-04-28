import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ControlPanel extends JPanel {
	// Create left side of the GUI that contains the file chooser and
	// buttons to select different functions

	private static String fileObjectName = "";
	private static File fileObject = null; // File object chosen as the root of
	// the file tree
	private static FileStructure tree = null;

	private JLabel fileChosen; // Displays the name of the file object chosen
	private JFileChooser chooser; // GUI object to select File object
	private JButton dirByNameButton; // Buttons to print the file tree
	private JButton dirBySizeButton;
	private JButton typeButton;
	private JTextField typeField;
	private JButton findButton;
	private JTextField findField;
	private JButton dupFiles;
	private JButton dupFolders;

	private String fileMssg = "<html>File object selected: </html>";
	private String fileMssgNo = "File object selected: ";

	/**
	 * Creates left panel of the graphical user interface.
	 */
	public ControlPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// GUI for choosing the file object
		chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.addActionListener(new FileChooserListener());
		chooser.setApproveButtonText("Select");
		chooser.setCurrentDirectory(new File(".")); // Strarting directory is
		// current directory

		// Label for displaying the name of the initial file object
		JPanel choice = new JPanel();
		choice.setLayout(new FlowLayout(FlowLayout.LEFT)); // Panel needed to
		// print file object
		// name left
		// justified
		fileChosen = new JLabel(fileMssg);
		fileChosen.setHorizontalAlignment(SwingConstants.LEFT);
		fileChosen.setHorizontalTextPosition(SwingConstants.LEFT);
		fileChosen.setFont(new Font("Helvetica", Font.BOLD, 14));
		fileChosen.setForeground(Color.blue);
		choice.add((fileChosen));

		// Panel to display the buttons to print the file tree
		JPanel printPanelName = new JPanel();
		printPanelName.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel printPanelSize = new JPanel();
		printPanelSize.setLayout(new FlowLayout(FlowLayout.LEFT));
		dirByNameButton = new JButton("Print File Objects Ordered by Name");
		dirBySizeButton = new JButton("Print File Objects Ordered by Size   ");
		ButtonListener listener = new ButtonListener();
		dirByNameButton.addActionListener(listener);
		dirBySizeButton.addActionListener(listener);
		printPanelName.add(dirByNameButton);
		printPanelSize.add(dirBySizeButton);

		// Panel to display the button to choose files by type
		JPanel typePanel = new JPanel();
		typePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		typeField = new JTextField(6);
		JLabel typeLabel = new JLabel("Find files of this type: ");
		typeButton = new JButton("Find files");
		typeButton.addActionListener(listener);
		typePanel.add(typeLabel);
		typePanel.add(typeField);
		typePanel.add(typeButton);

		// Panel to display the button to find files by name
		JPanel findPanel = new JPanel();
		findPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		findField = new JTextField(25);
		JLabel findLabel = new JLabel("File name: ");
		findButton = new JButton("Find file");
		findButton.addActionListener(listener);
		findPanel.add(findLabel);
		findPanel.add(findField);
		findPanel.add(findButton);

		// Add the file chooser, label, and buttons to the GUI
		add(chooser);
		add(Box.createVerticalGlue());
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(choice);
		add(Box.createVerticalGlue());
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(printPanelName);
		add(Box.createVerticalGlue());
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(printPanelSize);
		add(typePanel);
		add(findPanel);
		// add(duplicatedPanel);
	}

	public static String getFileObjectName() {
		return fileObjectName;
	}

	public static File getFileObject() {
		return fileObject;
	}

	private class ButtonListener implements ActionListener {
		// Invokes the methods to print the File Structure
		public void actionPerformed(ActionEvent event) {
			JTextArea canvas = SplitPanel.getPage();
			String text = "";

			try {

				// Check that a file object has been selected and that a file
				// tree has been built
				if (fileObject != null)
					if (tree == null || !tree.getRoot().getData().getLongName().equals(fileObject.getCanonicalPath())) {
						text = ".";
						// SplitPanel.setPageText(text);
						FileObject.resetNumFileObjects();
						tree = new FileStructure(fileObject.getCanonicalPath());
					}
				if (tree == null) {
					text = "Select File Object";
					canvas.setText(text);
					return;
				}

				// Print directory sorted by name or by size
				PrintFileStructure p = null;
				if (event.getSource() == dirByNameButton || event.getSource() == dirBySizeButton) {
					if (event.getSource() == dirByNameButton)
						p = new PrintFileStructure(PrintFileStructure.SORT_BY_NAME);
					else if (event.getSource() == dirBySizeButton)
						p = new PrintFileStructure(PrintFileStructure.SORT_BY_SIZE);

					text = "";
					text = p.printTree(tree.getRoot(), 0);
					text = p.formatTreeOutput(text);
				} else if (event.getSource() == typeButton) {
					// Find files of specified type

					p = new PrintFileStructure(0);
					Iterator<String> list = tree.filesOfType(typeField.getText());
					text = p.printIterator(list);
					if (text.length() == 0)
						text = "No files found";
				} else if (event.getSource() == findButton) {
					// Find specified dile

					String result = tree.findFile(findField.getText());
					if (result.equals(""))
						text = "File not found";
					else
						text = "File found: " + result + "\n";
/*				} else if (event.getSource() == dupFiles) {
					p = new PrintFileStructure(0);
					Iterator<String> list = tree.duplicatedFiles();
					text = p.printIterator(list);
					if (text.length() == 0)
						text = "No files found";
				} else if (event.getSource() == dupFolders) {
					p = new PrintFileStructure(0);
					Iterator<String> list = tree.duplicatedFolders();
					text = p.printIterator(list);
					if (text.length() == 0)
						text = "No folders found";
*/					
				} 

				if (!text.equals("No files found") && !text.equals("No folders found"))
					text = text + "\n Number of files: " + FileObject.getNumFileObjects() + "\n";
				canvas.setText(text);

			} catch (FileObjectException | IOException e) {
				text = "Error accessing file object " + ControlPanel.getFileObjectName();
				canvas.setText(text);
			}
		}
	}

	private class FileChooserListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			String command = event.getActionCommand();
			String selection = fileMssgNo;
			if (command.equals(JFileChooser.APPROVE_SELECTION)) {

				fileObject = chooser.getSelectedFile();
				fileObjectName = fileObject.getName();
				selection = selection + fileObjectName;
			} else if (command.equals(JFileChooser.CANCEL_SELECTION)) {
				fileObjectName = "";
				fileObject = null;
			}
			fileChosen.setText("<html>" + fileMssgNo + "<font color=\"red\">" + fileObjectName + "</font></html>");
		}
	}

}
