import java.io.*;
import java.util.*;


public class Q4b
{
	public static void main(String[] args)
			throws 	IOException, ClassNotFoundException
	{
		ObjectOutputStream outStream =
			new ObjectOutputStream(
			   new FileOutputStream("studentList.dat"));
		ArrayList<Student> studentListOut = 
							    		new ArrayList<>();
		ArrayList<Student> studentListIn = 
									new ArrayList<>();

		Student[] student =
			{new Student(123456,"Smith", "10 Sunny Road")};

		for (int i=0; i<student.length; i++)
			studentListOut.add(student[i]);

		outStream.writeObject(studentListOut);

		outStream.close();

		ObjectInputStream inStream =
			new ObjectInputStream(
				new FileInputStream("studentList.dat"));

		int personCount = 0;

		try
		{
			
			
			studentListIn =
			  (ArrayList<Student>)inStream.readObject();

		}
		catch (EOFException eofEx)
		{
			System.out.println(
							"\n\n*** End of file ***\n");
			inStream.close();
		}

		
	}
}

