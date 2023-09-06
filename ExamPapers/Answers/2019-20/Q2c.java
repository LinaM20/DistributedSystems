import java.io.*;
import java.util.*;


public class Q2c
{
	public static void main(String[] args)
			throws 	IOException, ClassNotFoundException
	{
		ObjectOutputStream outStream =
			new ObjectOutputStream(
			   new FileOutputStream("personnelList.dat"));
		ArrayList<Personnel> personListOut = 
							    		new ArrayList<>();
		ArrayList<Personnel> personListIn = 
									new ArrayList<>();

		Personnel[] people =
			{new Personnel(123456,"Smith", "10 Sunny Road")};

		for (int i=0; i<people.length; i++)
			personListOut.add(people[i]);

		outStream.writeObject(personListOut);

		outStream.close();

		ObjectInputStream inStream =
			new ObjectInputStream(
				new FileInputStream("personnelList.dat"));

		int personCount = 0;

		try
		{
			
			
			personListIn =
			  (ArrayList<Personnel>)inStream.readObject();

		}
		catch (EOFException eofEx)
		{
			System.out.println(
							"\n\n*** End of file ***\n");
			inStream.close();
		}

		
	}
}

