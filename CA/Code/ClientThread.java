import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.io.IOException;


public class ClientThread extends Thread
{
	private static final String object = "___Object___";

	private Socket socket;
	private AuctionClient client;
	private ObjectInputStream inputStream;
	private DataInputStream input;
	
	//initialise objects
	public ClientThread(AuctionClient client, Socket socket)
	{
		this.client = client;
		this.socket = socket;
		open();
	}

	//Get the item data and display to client
	public void getItem()
	{
		AuctionItem auctionItem = null;
		
		try
		{
			auctionItem = (AuctionItem) inputStream.readObject();
			client.setItem(auctionItem);
		}
		catch (ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			close();
		}
	
	}
	
	//open stream
	public void open()
	{
		try
		{
			inputStream = new ObjectInputStream(socket.getInputStream());
			input = new DataInputStream(socket.getInputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	//runs object 
	public void run()
	{
		String message = "";
		while (client != null)
		{
			try
			{
				message = (String) inputStream.readObject();
				
				if (message.equals(object))
				{
					getItem();
				}
				else
				{
					client.showMsg(message);
				}
			}
			catch (IOException e)
			{
				System.out.println("Leaving auction...");
				close();
			}
			catch (ClassNotFoundException cnfe)
			{
				cnfe.printStackTrace();
			}
		}
	}
	
	//close stream
	public void close()
	{
		try
		{
			if (inputStream != null)
			{
				inputStream.close();
			}
			if (input != null)
			{
				input.close();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		client = null;
	}
	
}