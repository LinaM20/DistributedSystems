import java.io.Serializable;
import java.util.ArrayList;


public class AuctionItem implements Serializable
{	
	private String itemName;
	private double currentBid;
	private static int currentItem = 0;
	private double price;
	private long startTimer;
	private boolean isSold;
		
	private static ArrayList<AuctionItem> items = new ArrayList<>();
	private static ArrayList<AuctionItem> soldItems = new ArrayList<>();

	public AuctionItem (String name, double price)
	{
		this.itemName = name;
		this.price = price;
		currentBid = 0;
		startTimer = 0;
		isSold = false;
		
		items.add(this);
	}
	
	public static AuctionItem getCurrentItem()
	{		
		return items.get(currentItem);
	}
	
	public String getName()
	{
		return itemName;
	}

	public void setName(String name)
	{
		this.itemName = name;
	}

	public double getCurrentBid()
	{
		return currentBid;
	}

	public void setCurrentBid(double currentBid)
	{
		this.currentBid = currentBid;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public boolean isSold()
	{
		return isSold;
	}

	public void setSold(boolean sold)
	{
		this.isSold = sold;
	}

	public long getStartTimer()
	{
		return startTimer;
	}

	public void setStartTimer(long startTimer)
	{
		this.startTimer = startTimer;
	}

	//Returns the next auction item
	public static AuctionItem nextItem()
	{
		AuctionItem item = items.get(currentItem);
		if (item.isSold())
		{
			soldItems.add(items.remove(currentItem));
		}
		else
		{
			currentItem++;
		}
		
		//calculates the amount of items left to return
		if (currentItem >= items.size()) {
			if ((items.size() > 0)) {

				currentItem = currentItem % items.size();
			}
			else {
				currentItem = 0;
			}
		}
		else {
			currentItem = items.size() - 1;
		}
		
		//if there are items left to sell then return the current item
		if (items.size() > 0) {

			return items.get(currentItem);
		}
		else {
			return null;
		}
		
	}
	
}