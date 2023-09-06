import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;


//Creates a thread to handle connections for the clients
public class ClientHandler extends Thread
{
	private static final String object = "___Object___";
	private static final String leave = "leave";

	private Socket client;
	private AuctionServer server;	
	private DataInputStream input;
	private ObjectOutputStream outstream;
	private Thread thread = null;
	

	public ClientHandler(Socket socket, AuctionServer server)
	{
		client = socket;
		this.server = server;

		try
		{
			input = new DataInputStream(new BufferedInputStream(client.getInputStream()));
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			outstream = new ObjectOutputStream(client.getOutputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	//Runs until it gets data from the client
	public void run()
	{
		thread = new Thread(this);
		String message;
		
		while (thread != null)
		{
			try
			{
				message = input.readUTF();

				if (message.equals(leave))
				{
					server.leaveAuction(this);
					thread = null;
				}
				else
				{
					server.newBid(message, this);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
				server.leaveAuction(this);
				thread = null;
			}	
		}
	}
	
		//Sends the auction items to the client 
	public void sendItem(AuctionItem item)
	{
		sendMessage(object);
		
		try
		{
			outstream.writeObject(item);
			outstream.flush();
			outstream.reset();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			server.leaveAuction(this);
			thread = null;
		}
	}
	
	
	public void sendMessage(String message)
	{
		try
		{			
			outstream.writeObject(message);
			outstream.flush();
			outstream.reset();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//Close the connection
	public void close() throws IOException
	{
		if (input != null)
		{
			input.close();
		}
		if (outstream != null)
		{
			outstream.close();
		}
		if (client != null)
		{
			client.close();
		}
	}
	
}