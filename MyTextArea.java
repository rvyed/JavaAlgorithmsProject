import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JTextArea;

public class MyTextArea extends JTextArea {

	public MyTextArea(int a, int b) {
		super(a, b);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Color leftBorder = new Color(224, 245, 255);
		Color rightBorder = new Color(139, 197, 224);
		Graphics2D g2d = (Graphics2D) g;
		GradientPaint gp;
		gp = new GradientPaint(0, 0, leftBorder, getWidth(), 0, rightBorder);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}
}
