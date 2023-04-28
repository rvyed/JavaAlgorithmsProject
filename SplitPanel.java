import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SplitPanel extends JPanel {

	private static MyTextArea page = null;
	private static String pageText = ""; // Text used to show program is still running

	/**
	 * Creates the control panel and text are of the graphical user interface
	 */
	public SplitPanel() {
		// Get the monitor's resolution
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();
		Color textColor = new Color(55, 1, 91);
		Color backgroundColor = new Color(220, 230, 232);
		Font textFont = new Font("Courier", Font.BOLD, 14);

		// Select size of the panel depending on monitor resolution
		if (height > 1100)
			page = new MyTextArea(50, 120);
		else if (height > 900)
			page = new MyTextArea(40, 100);
		else
			page = new MyTextArea(30, 80);

		// Add control panel and text area to the GUI. Add a scroll pane to the
		// text area
		page.setOpaque(false);
		page.setFont(textFont);
		page.setForeground(textColor);
		JScrollPane scroll = new JScrollPane(page, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		ControlPanel control = new ControlPanel();
		control.setBackground(backgroundColor);
		add(control, BorderLayout.WEST);
		add(scroll, BorderLayout.CENTER);
	}

	/**
	 * 
	 * @return Text area of teh GUI
	 */
	public static JTextArea getPage() {
		return page;
	}

	/**
	 * 
	 * @return Text being displayed in text area while program is running
	 */
	public static String getPageText() {
		return pageText;
	}

	/**
	 * Change static text used to indicate the program is still running
	 * 
	 * @param newText:
	 *            new text to display
	 */
	public static void setPageText(String newText) {
		pageText = newText;
		System.out.print(".");
		// page.setText(newText);
		// page.update(page.getGraphics());
	}
}
