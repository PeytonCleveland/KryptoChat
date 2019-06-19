# KryptoChat
Secure Instant Messaging Application 

Follow src > Cleveland/Co/KryptoChat to view classes

/* Currently Unfinished - Only Initial Server Handshake Is Complete */

KryptoChat is a secure messaging application that utilizes TLS and AES Encryption as well as a proprietary authentication protocol.  When a client attempts to login to the server, the client's application will first challenge the server with a random permutation number and random increment number.  The server will then use this information to respond with its own MAC in the permutation given by the client.  If the MAC recieved by the client matches the MAC expected the server is authenticated, thus ensuring that the client is not tricked into starting a session with a rogue server.  This challenge will continue for every segment sent between the two as a new increment value will be sent with every client data segment.  If at any time the response by the server does not match the expected response, the client will terminate the session by closing the connection and terminating itself entirely.  In the final release the initial challenge and incremet will follow the initial public key exchange, private AES key exchange, and then be encrypted just like the rest of the data.  The client will authenticate itself to the server with login credentials which will be saved as a hash on the server with a salt.  The client and server will also periodically change encryption keys.
