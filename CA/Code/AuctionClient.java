import java.io.BufferedReader;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;

public class AuctionClient implements Runnable
{
	private static final long bidTime = 45;
	private static final String leave = "leave";
	private static final Object time = "time";
	private static final String help = "help";

	private Socket socket = null;
	private BufferedReader reader = null;
	private DataOutputStream output = null;

	private Thread thread;
	private ClientThread client;
	private AuctionItem auctionItem;
	

	//Sets the auction item and displays the time and item
	//sends a reminder message about time every time the item is displayed
	public void setItem(AuctionItem item)
	{
		auctionItem = item;
		displayItem();
		System.out.println("\nTime on bid is 45 seconds.\nType 'time' to see the time left on this item\n");
	}

	//Displays the item, price and current bid on items
	public void displayItem()
	{
		System.out.println(auctionItem.getName().toUpperCase() + " is up for auction");
		System.out.println("Current bid on item: " + auctionItem.getCurrentBid());
		System.out.println("Reserve Price on item: " + auctionItem.getPrice());
	}
	

	//Displays the time left for the bidding
	public void showTime()
	{
		if (auctionItem != null)
		{
			System.out.println("\nTIME: Only " + (bidTime - ((System.currentTimeMillis() - auctionItem.getStartTimer()) / 1000)) + " seconds left to bid\n");
		}

	}
	
	//prints messages
	public void showMsg(String message)
	{
		System.out.println(message);
	}
	
	//Displays the menu
	public void showMenu()
	{
		System.out.println("To view the instructions again enter '" + help + "'");
		System.out.println("To see the time left during the auction enter '" + time + "'");
		System.out.println("To leave the auction enter '" + leave + "'\n\n");
	}
	
	
	//Creates the socket and joins the auction
	public AuctionClient(String host, int port)
	{
		try
		{
			socket = new Socket(host, port);
			joinAuction();
		} 
		catch (UnknownHostException uhe)
		{
			System.out.println("Host not found");
			System.exit(1);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	//Reads the inputs from clients
	public void run()
	{
		showMenu();
		
		while(client != null)
		{
			try
			{
				String message = reader.readLine();

				if (message.equals(leave) || message.matches("-?\\d+(\\.\\d+)?")) //regex to check for numbers and characters
				{
					output.writeUTF(message);
					output.flush();
					if (message.equals(leave))
					{
						leaveAuction();
						break;
					}
				}
				else if (message.equals(time))
				{
					showTime();
				}
				else if (message.equals(help))
				{
					showMenu();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
				leaveAuction();
			}
		}
	}
	
	
	
	//Creates a user input and output stream
	private void joinAuction()
	{
		try
		{
			reader = new BufferedReader(new InputStreamReader(System.in)); 
			output = new DataOutputStream(socket.getOutputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if (thread == null)
		{
			client = new ClientThread(this, socket);
			client.start();
			thread = new Thread(this);
			thread.start();		
		}
	}
	
	
	//Closes the socket and streams
	public void leaveAuction()
	{
		try
		{
			if (socket != null)
			{
				socket.close();
			}
			if (reader != null)
			{
				reader.close();
			}
			if (output != null)
			{
				output.close();
			}
		}
		catch (IOException e)
		{
			client.close();
			thread = null;
		}
		client.close();
		thread = null;
	}

	public static void main(String[] args)
	{
		if (args.length != 2)
		{
			System.out.println("Command line argument must be missing, try java AuctionClient host port");
		}
		else
		{
			new AuctionClient(args[0], Integer.parseInt(args[1]));
		}
	}
}