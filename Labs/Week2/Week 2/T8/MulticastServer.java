import java.net.*;
import java.io.*;
import java.util.Date;


public class MulticastServer {

	private DatagramSocket socket;
	private InetAddress group;
	private DatagramPacket packet;
	private int port;

	public MulticastServer(int port) {
		this.port = port;

		try {
			socket = new DatagramSocket();
			group = InetAddress.getByName("224.0.0.3");
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
			System.exit(1);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(2);
		}

		while(true) {
			sendTimeToClients();
		}
	}

	private void sendTimeToClients() {
		try {
			byte[] buffer = new Date().toString().getBytes();

			packet = new DatagramPacket(buffer, buffer.length, group, port);
			socket.send(packet);

			// sleep for a second
			try {
				Thread.sleep((long)(Math.random() * 3000));
			} catch (InterruptedException e) { }

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		new MulticastServer(Integer.parseInt(args[0]));
	}
}
