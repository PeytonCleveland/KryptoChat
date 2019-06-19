package Cleveland.Co.KryptoChat;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class MainMenu extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private Dimension screenSize;
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    private RSAKey key;
    private PrivateKey privKey;
    private PublicKey pubKey;
    private final byte[] serverMac1 = {-117, -56, -49, -36, 18, 40};
    private final byte[] serverMac2 = {0, 0, 0, 7, 10, 39};
    private byte[] challenge1 = null;
    private byte[] challenge2 = null;
    private int lehmer = -1;
    private int increment = -1;
    Random r = new Random();
	Lehmer l = new Lehmer();
	
	public MainMenu(Dimension d) throws NoSuchAlgorithmException {
		key = new RSAKey();
		key.createKeys();
		this.pubKey = key.getPublicKey();
		this.privKey = key.getPrivateKey();
		lehmer = r.nextInt(720) + 1;
		increment = r.nextInt(4) + 2;
		this.screenSize = new Dimension(d.width + 1, d.height + 1);
		setPreferredSize(screenSize);
		setMaximumSize(screenSize);
		setMinimumSize(screenSize);
		setSize(screenSize);
		setLayout(null);
		run();
	}

	private void connectToServer(char[] cs, String username) {
		try {
			initMac();
			socket = new Socket("127.0.0.1", 8080);
			input = new DataInputStream(this.socket.getInputStream());
			output = new DataOutputStream(this.socket.getOutputStream());
			byte[] mac = new byte[6];
			output.writeInt(lehmer);
			output.writeInt(increment);
			input.read(mac);
			if(!checkMac(mac)) {
				input.close();
				output.close();
				socket.close();
				System.exit(0);
			}else{
				byte[] tmp = new byte[1024];
				tmp = username.getBytes();
				output.write(tmp);
				tmp = cs.toString().getBytes();
				output.write(tmp);
			}	
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String hash(char[] cs) {
		return cs.toString();
	}

	private byte[] decrypt(byte[] bytes) {
		try {
			Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
			rsaCipher.init(Cipher.DECRYPT_MODE, privKey);
			return rsaCipher.doFinal(bytes);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void initMac() {
		int [] fact = l.numberToFactoradic(lehmer);
		challenge1 = l.factoradicToPermutation(fact, serverMac1);
		challenge2 = l.factoradicToPermutation(fact, serverMac2);
	}

	private boolean checkMac(byte[] mac) {
		if(Arrays.equals(mac, challenge1) || Arrays.equals(mac, challenge2)) {
			lehmer += increment;
			int [] fact = l.numberToFactoradic(lehmer);
			challenge1 = l.factoradicToPermutation(fact, serverMac1);
			challenge2 = l.factoradicToPermutation(fact, serverMac2);
			return true;
		}
		return false;
	}

	public void run() {
		loadPanel();
	}
	
	private void login(char[] pass, String username) {
		connectToServer(pass, username);
	}
		
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image login = null;
		try {
			login = ImageIO.read(new File("src/login.png"));
		}catch(IOException e) {}
		login = login.getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
		g.drawImage(login, 0, 0, null);
	}
	
	private void loadPanel() {
		
		Color custom = new Color(87, 87, 87);
		Font f, f2, f3;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		try {
			ge.registerFont(Font.createFont(Font.PLAIN, new File("src/MontserratAlternates-Light.ttf")));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
		f = new Font("Montserrat Alternates Light", Font.PLAIN, 60);
		f2 = new Font("Montserrat Alternates Light", Font.PLAIN, 70);
		f3 = new Font("Montserrat Alternates Light", Font.PLAIN, 150);
		
		Border b = BorderFactory.createMatteBorder(3, 3, 3, 3, custom);
		
		JPasswordField p = new JPasswordField();
		p.setSize((int) (screenSize.width / 4), screenSize.height / 20);
		p.setLocation((screenSize.width / 2) - (p.getWidth() / 2), (int) ((screenSize.height / 5) * 2.85));
		p.setBorder(b);
		p.setForeground(custom);
		p.setFont(f3);
		p.setHorizontalAlignment(JTextField.CENTER);
		this.add(p);
		
		JTextField u = new JTextField();
		u.setSize((int) (screenSize.width / 4), screenSize.height / 20);
		u.setLocation((screenSize.width / 2) - (p.getWidth() / 2), (int) ((screenSize.height / 5) * 1.9));
		u.setBorder(b);
		u.setHorizontalAlignment(JTextField.CENTER);
		u.setForeground(custom);
		u.setFont(f);
		this.add(u);
		
		JButton button = new JButton("login");
		button.setSize(screenSize.width / 8, screenSize.height / 18);
		button.setLocation((screenSize.width / 2) - (button.getSize().width / 2), (int) ((screenSize.height / 5) * 3.25));
		button.setBackground(custom);
		button.setBorder(null);
		button.setFont(f);
		button.setForeground(Color.white);
		button.setText("Login");
		button.setFocusPainted(false);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				login(p.getPassword(), u.getText());
			}
		});
		button.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				button.setSize((int) (button.getWidth() * 1.1), (int) (button.getHeight() * 1.1));
				button.setLocation((screenSize.width / 2) - (button.getSize().width / 2), (int) ((screenSize.height / 5)* 3.24));
				button.setFont(f2);
			}
			public void mouseExited(MouseEvent e) {
				button.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				button.setSize(screenSize.width / 8, screenSize.height / 18);
				button.setLocation((screenSize.width / 2) - (button.getSize().width / 2), (int) ((screenSize.height / 5) * 3.25));
				button.setFont(f);
			}
		});
		this.add(button);
	}
	
}
