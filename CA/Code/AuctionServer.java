import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class AuctionServer implements Runnable
{
	private static ServerSocket serverSock;
	private Thread thread = null;
	private static ArrayList<ClientHandler> clientList;
	
	private static final long timerLength = 45000;
	private Timer timer;
	private boolean startAuction = true;
	private AuctionItem auctionItem;
	private ClientHandler currentBidClient = null;
	

	public AuctionServer(int port)
	{
		clientList = new ArrayList<ClientHandler>();
		
		try
		{
			serverSock = new ServerSocket(port);
			start();
		}
		catch (IOException e)
		{
			System.out.println("Not able to set up port " + port);
			System.exit(1);
		}
	}
	
	//Starts the thread
	public void start()
	{
		if (thread == null)
		{
			thread = new Thread(this);
			thread.start();
		}
	}

		
	//When the timer is up, select the next item
	public void nextAuctionItem()
	{
		if (auctionItem.getCurrentBid() >= auctionItem.getPrice())
		{
			auctionItem.setSold(true);
			for (ClientHandler ch : clientList)
			{
				if (ch ==  currentBidClient)
				{
					ch.sendMessage("You won the '" + auctionItem.getName() + "'. Congratulations!\n\n");
				}
				else
				{
					ch.sendMessage("Unforunately, '" + auctionItem.getName() + "' has been sold to another bidder :(\n\n");
				}
			}
		}

		auctionItem = AuctionItem.nextItem();

		if (auctionItem == null)
		{
			broadcastMessage("This concludes the auction! Thank you for participating and come again!\n");
		}
		else if (clientList.size() < 2)
		{
			broadcastMessage("There are not enough bidders, the auction will resume when at least two people are present.\n");
		}
		else
		{
			auctionItem.setStartTimer(System.currentTimeMillis());
			sendItem(auctionItem);
			runTimer();
		}
	}
	
		
	//Server thread: makes sure two or more clients are present to start auction
	public void run()
	{
		while (thread != null)
		{
			try
			{
				addThread(serverSock.accept());
			}
			catch (IOException e)
			{
				System.out.println("Server error" + e);
				stop();
			}
			
			//check if two or more clients are present and then starts the auction
			if (clientList.size() >= 2 && startAuction)
			{
				System.out.println("\nBid has started!\n");
				auctionItem = AuctionItem.getCurrentItem();
				auctionItem.setStartTimer(System.currentTimeMillis());
				sendItem(auctionItem);
				
				runTimer();
				startAuction = false;
			}
			else if (!startAuction)
			{
				clientList.get(clientList.size() - 1).sendItem(auctionItem);
			}
		}
	}

	//Stops the thread by breaking the connection
	public void stop()
	{
		thread = null;
	}
	
	private void addThread(Socket socket)
	{
		clientList.add(new ClientHandler(socket, this));
		clientList.get(clientList.size() - 1).start();
	}
	
	public synchronized void broadcastMessage(String msg)
	{
		for (ClientHandler ch : clientList)
		{
			ch.sendMessage(msg);
		}
	}
	

	//If there are not enough clients, end the auction
	public synchronized void leaveAuction(ClientHandler ch)
	{
		System.out.println("A bidder has left the auction.\nOnly " + (clientList.size()-1) + " bidder(s) left.");
		clientList.remove(ch);

		if (clientList.size() == 0) {
			System.out.println("\nAuction has suspended since no bidder is present.\n");
		}

		try
		{
			ch.close();
		}
		catch (IOException e)
		{
			System.out.println("Error closing thread");
			e.printStackTrace();
		}
		
		if (clientList.size() < 2)
		{
			startAuction = true;
		}
		
		notifyAll();
	}
	
	
	//Send item
	public synchronized void sendItem(AuctionItem item)
	{
		for (ClientHandler ch : clientList)
		{
			ch.sendItem(item);
		}
		notifyAll();
	}
	
	
	//Messages relating to the bid
	public synchronized void broadcastBidInfo()
	{
		for (ClientHandler ch : clientList)
		{
			if (ch == currentBidClient)
			{
				ch.sendMessage("\n\nRight now, your bid is the current bid.\n\n");
			}
			else
			{
				ch.sendMessage("\nA new bid has been placed.\nSee current bid.\n\n");
			}
		}
		notifyAll();
	}
	
	
	//Relays messaged to the client
	public void newBid(String bidString, ClientHandler ch)
	{
		double bid = Double.parseDouble(bidString);
		if (bid > auctionItem.getCurrentBid())
		{
			auctionItem.setCurrentBid(bid);
			auctionItem.setStartTimer(System.currentTimeMillis());
			currentBidClient = ch;
			broadcastBidInfo();
			sendItem(auctionItem);
			timer.cancel();
			runTimer();
		}
		
	}
	
	private void runTimer()
	{
		timer = new Timer("Time left");
		timer.schedule(new TimerTask() {
			
			@Override
			public void run()
			{
				nextAuctionItem();
			}

		}, timerLength);
	}
	

	private static void loadItems()
	{
		Scanner reader = null;
		try
		{
			//Reads the auction items from AuctionItems.txt 
			reader = new Scanner(new File("AuctionItems.txt"));
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Unable to load Items");
		}

		String line = "";
		String[] args = new String[2];
		
		while (reader.hasNextLine())
		{
			line = reader.nextLine();
			args = line.split("\t");
			new AuctionItem(args[0], Double.parseDouble(args[1]));
		}
		reader.close();
	}

	public static void main(String[] args) throws IOException
	{
		if (args.length != 1)
		{
			System.out.println("Command line argument must be missing, try java AuctionServer port");
		}
		else
		{
			loadItems();
	
			new AuctionServer(Integer.parseInt(args[0]));
		}
	}

}