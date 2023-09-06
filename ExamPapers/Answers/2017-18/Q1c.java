import java.io.*;
import java.util.*;

public class Q1c
{
	public static void main(String[] args)
			throws 	IOException, ClassNotFoundException
	{
		ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream("personnelList.dat"));
		ArrayList<Patient> personListOut = new ArrayList<>();
		ArrayList<Patient> personListIn = new ArrayList<>();

		Patient[] people = { new Patient(123456, "Smith", "10 Sunny Road")};

		for (int i=0; i<people.length; i++)
			personListOut.add(people[i]);

		outStream.writeObject(personListOut);

		outStream.close();

		ObjectInputStream inStream = new ObjectInputStream(new FileInputStream("patientList.dat"));

		int personCount = 0;

		try
		{
			personListIn =
			  (ArrayList<Patient>)inStream.readObject();

		}
		catch (EOFException eofEx)
		{
			System.out.println(
							"\n\n*** End of file ***\n");
			inStream.close();
		}

		
	}
}