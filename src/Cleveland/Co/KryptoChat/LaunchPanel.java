package Cleveland.Co.KryptoChat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class LaunchPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	Dimension screenSize;
	public Image scaled;
	Color custom = new Color(87, 87, 87);
	boolean l1, l2, l3, l4, l5 = false;
	
	public LaunchPanel(Dimension d) {
		this.screenSize = new Dimension(d.width + 1, d.height + 1);
		try {
			scaled = ImageIO.read(new File("src/KryptoChat.png"));
		}catch(IOException e) {
			
		}
		setPreferredSize(screenSize);
		setMaximumSize(screenSize);
		setMinimumSize(screenSize);
		setSize(screenSize);
		setLayout(null);
		scaled = scaled.getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
	}
	
	public void load() {
		long timer = System.nanoTime();
		while(System.nanoTime() - timer < 500000000) {}
		l1 = true;
		repaint();
		timer = System.nanoTime();
		while(System.nanoTime() - timer < 500000000) {}
		l2 = true;
		repaint();
		timer = System.nanoTime();
		while(System.nanoTime() - timer < 500000000) {}
		l3 = true;
		repaint();
		timer = System.nanoTime();
		while(System.nanoTime() - timer < 500000000) {}
		l4 = true;
		repaint();
		timer = System.nanoTime();
		while(System.nanoTime() - timer < 500000000) {}
		l5 = true;
		repaint();
		timer = System.nanoTime();
		while(System.nanoTime() - timer < 1000000000) {}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(scaled, 0, 0, null);
		g.setColor(custom);
		if(l1) {
			g.fillOval((int) ((screenSize.width / 2) - screenSize.width * .12) , (int)((screenSize.height / 2) * 1.25), (int) (screenSize.height * .02), (int) (screenSize.height * .02));
		}
		if(l2) {
			g.fillOval((int) ((screenSize.width / 2) - screenSize.width * .07) , (int)((screenSize.height / 2) * 1.25), (int) (screenSize.height * .02), (int) (screenSize.height * .02));
		}
		if(l3) {
			g.fillOval((int) ((screenSize.width / 2) - screenSize.width * .02) , (int)((screenSize.height / 2) * 1.25), (int) (screenSize.height * .02), (int) (screenSize.height * .02));
		}
		if(l4) {
			g.fillOval((int) ((screenSize.width / 2) + screenSize.width * .03) , (int)((screenSize.height / 2) * 1.25), (int) (screenSize.height * .02), (int) (screenSize.height * .02));
		}
		if(l5) {
			g.fillOval((int) ((screenSize.width / 2) + screenSize.width * .08) , (int)((screenSize.height / 2) * 1.25), (int) (screenSize.height * .02), (int) (screenSize.height * .02));
		}
	}
	
}
