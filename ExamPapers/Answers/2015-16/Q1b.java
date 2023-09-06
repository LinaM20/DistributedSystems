import java.io.*;
import java.util.*;

public class Q1b
{
	public static void main(String[] args)
			throws 	IOException, ClassNotFoundException
	{
		ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream("voterList.dat"));
		ArrayList<Voter> voterListOut = new ArrayList<>();
		ArrayList<Voter> voterListIn = new ArrayList<>();

		Voter[] voter = { new Voter(123456, "Smith", "10 Sunny Road")};

		for (int i=0; i<voter.length; i++)
			voterListOut.add(voter[i]);

		outStream.writeObject(voterListOut);

		outStream.close();

		ObjectInputStream inStream = new ObjectInputStream(new FileInputStream("voterList.dat"));
		try
		{
			voterListIn = (ArrayList<Patient>)inStream.readObject();

		}
		catch (EOFException eofEx)
		{
			System.out.println(
							"\n\n*** End of file ***\n");
			inStream.close();
		}

		
	}
}