package Co.Cleveland.KryptoChatServer;

public class credential {
	
	private byte [] username;
	private byte [] hash;
	//private String salt;
	
	public credential(byte [] usr, byte [] hash) {//, String salt) {
		this.username = usr;
		this.hash = hash;
		//this.salt = salt;
	}
	
	public byte [] getUsername() {
		return this.username;
	}
	
	public byte [] getHash() {
		return this.hash;
	}
	
}
