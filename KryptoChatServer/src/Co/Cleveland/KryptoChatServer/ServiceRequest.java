package Co.Cleveland.KryptoChatServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ServiceRequest implements Runnable {
		
	private Socket socket;
	private int Lehmer = -1;
	private int increment = -1;
	private Lehmer l = new Lehmer();
	private RSA r;
	private PrivateKey privKey;
	private PublicKey pubKey;
	private PublicKey clientKey;
	private CredentialServer cs;
	
	public ServiceRequest(Socket s) {
		this.socket = s;
		try {
			r = new RSA();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		r.createKeys();
		privKey = r.getPrivateKey();
		pubKey = r.getPublicKey();
		cs = new CredentialServer();
	}
	
	/* Run() is responsible for all server logic and
	   closes connection when finished servicing request */

	public void run() {
		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			handshake(input, output);
			output.close();
			input.close();
			socket.close();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/* handshake() is used to authenticate both the
	   client and server to each other
	   
	   The server reads in the Lehmer and increment from
	   the client and uses them to answer client challenges */

	private void handshake(DataInputStream input, DataOutputStream output) {
		try {
			Lehmer = input.readInt();
			increment = input.readInt();
			output.write(getMAC());
			byte [] username = new byte[1024];
			input.read(username);
			byte [] hash = new byte[1024];
			input.read(hash);
			if(cs.grantAccess(username, hash)) {
				System.out.println("Access Granted");
			}else {
				System.out.println("Access Denied");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private byte[] encrypt(byte[] d) {
		try {
			Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
			rsaCipher.init(Cipher.ENCRYPT_MODE, pubKey);
			return rsaCipher.doFinal(d);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* getMac() returns the hardware address of the server 
	   in the challenge format given by the client */

	private byte[] getMAC() {
		byte [] mac = null;
		try {
			InetAddress address = InetAddress.getLocalHost();
			NetworkInterface ni = NetworkInterface.getByInetAddress(address);
			mac = ni.getHardwareAddress();
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
		}
		mac = normalizeMac(mac);
		int[] fact = l.numberToFactoradic(Lehmer);
		byte[] ret = l.factoradicToPermutation(fact, mac);
		Lehmer += increment;
		return ret;
	}

	/* NormalizeMac() turns the MAC address into a
	   sorted array from lowest to highest using a
	   hard-coded network sort which is faster and 
	   more efficient for an array this small */
	
	private byte[] normalizeMac(byte[] m) {
		byte t;
		if(m[1] > m[2]) { t = m[1]; m[1] = m[2]; m[2] = t; }
		if(m[4] > m[5]) { t = m[4]; m[4] = m[5]; m[5] = t; }
		if(m[0] > m[2]) { t = m[0]; m[0] = m[2]; m[2] = t; }
		if(m[3] > m[5]) { t = m[3]; m[3] = m[5]; m[5] = t; }
		if(m[0] > m[1]) { t = m[0]; m[0] = m[1]; m[1] = t; }
		if(m[3] > m[4]) { t = m[3]; m[3] = m[4]; m[4] = t; }
		if(m[2] > m[5]) { t = m[2]; m[2] = m[5]; m[5] = t; }
		if(m[0] > m[3]) { t = m[0]; m[0] = m[3]; m[3] = t; }
		if(m[1] > m[4]) { t = m[1]; m[1] = m[4]; m[4] = t; }
		if(m[2] > m[4]) { t = m[2]; m[2] = m[4]; m[4] = t; }
		if(m[1] > m[3]) { t = m[1]; m[1] = m[3]; m[3] = t; }
		if(m[2] > m[3]) { t = m[2]; m[2] = m[3]; m[3] = t; }
		return m;
	}
	
}
