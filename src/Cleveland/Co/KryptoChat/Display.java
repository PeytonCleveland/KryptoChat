package Cleveland.Co.KryptoChat;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.security.NoSuchAlgorithmException;

import javax.swing.JFrame;


public class Display extends JFrame {
	
	private static final long serialVersionUID = 1L;
	Dimension screenSize = new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().width / 1.25), (int)(Toolkit.getDefaultToolkit().getScreenSize().height / 1.25));
	LaunchPanel launch = new LaunchPanel(screenSize);

	public Display() {
		this.setTitle("KryptoChat v3.19");
		this.setSize(screenSize);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.add(launch);
		this.setFocusable(true);
		this.requestFocus();
		this.setVisible(true);
		Image icon = Toolkit.getDefaultToolkit().getImage("src/KryptoChatIcon.png");
		this.setIconImage(icon);
	}

	public void run() throws NoSuchAlgorithmException {
		launch.load();
		MainMenu m = new MainMenu(screenSize);
		this.add(m);
		this.remove(launch);
		repaint();
	}

}
