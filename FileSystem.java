import javax.swing.JFrame;

public class FileSystem {

	public static void main(String[] args) {
		JFrame frame = new JFrame("File Directory Tree");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SplitPanel panel = new SplitPanel();
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}

}
