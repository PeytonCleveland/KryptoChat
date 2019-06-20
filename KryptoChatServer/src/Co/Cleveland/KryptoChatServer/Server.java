package Co.Cleveland.KryptoChatServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	
	private int serverPort = 8080;
	private ServerSocket serverSocket = null;
	protected boolean stopped = false;
	private Thread runningThread = null;
	
	public void run() {
		synchronized(this) {
			this.runningThread = Thread.currentThread();
		}
		openServerSocket();
		while(!stopped) {
			Socket client = null;
			try {
				client = this.serverSocket.accept();
			}catch(IOException ioe) {
				if(stopped) {
					System.out.println("Server Stopped");
					return;
				}
				throw new RuntimeException ("Error", ioe);
			}
			new Thread(new ServiceRequest(client)).start();
		}
		System.out.println("Stopped");
	}
	
	public synchronized void stop() {
		this.stopped = true;
		try {
			this.serverSocket.close();
		}catch(IOException ioe) {
			throw new RuntimeException("Error Closing Server", ioe);
		}
	}

	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
		}catch(IOException ioe) {
			throw new RuntimeException("Cannot open port", ioe);
		}
	}
	
	public static void main(String[] args) {
		Server s = new Server();
		new Thread(s).start();
	}
	
}
