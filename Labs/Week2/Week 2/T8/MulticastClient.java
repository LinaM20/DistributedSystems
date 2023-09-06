import java.io.*;
import java.net.*;



public class MulticastClient {

	private MulticastSocket socket;
	private InetAddress address;
	private DatagramPacket packet;
	int port;

	public MulticastClient(int port) {
		this.port = port;
		
		try {
			address = InetAddress.getByName("224.0.0.3");
			socket = new MulticastSocket(port);
			socket.joinGroup(address);
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
			System.exit(1);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(2);
		}

		while(true) {
			System.out.println(getTime());
		}
	}
	public static void main(String[] args) {
		new MulticastClient(Integer.parseInt(args[0]));
	}
}
