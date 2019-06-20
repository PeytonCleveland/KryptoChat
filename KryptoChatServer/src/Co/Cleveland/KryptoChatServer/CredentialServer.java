package Co.Cleveland.KryptoChatServer;

import java.util.ArrayList;
import java.util.Arrays;

public class CredentialServer {

	private ArrayList<credential> credentials = new ArrayList<credential>();
	
	public CredentialServer() {
		byte [] tmp1 = new byte[1024];
		byte [] tmp2 = new byte[1024];
		tmp1 = "pcleveland".getBytes();
		tmp2 = "password1".getBytes();
		credentials.add(new credential(tmp1, tmp2));
	}
	
	public boolean grantAccess(byte [] usr, byte [] hash) {
		int i = 0;
		for(credential c: credentials) {
			if(Arrays.equals(usr, c.getUsername()) == true) {
				return true;
			}
		}
		System.out.println(false);
		return false;
	}

}
