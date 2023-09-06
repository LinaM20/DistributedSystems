import java.io.*;
import java.net.*;
import java.util.*;



public class TCPEchoServer
{
   private static ServerSocket servSock;
   private static final int PORT = 1234;

   public static void main(String[] args)
   {
      //person object
      ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream("person.dat"));

      //person array
      Person[] person = 
         {new Person("John Doe", 53, "10 Sunset Cottage"),
         new Person("Mary Smith", 32, "16 Park Lane"),
         new Person("Sarah Walker", 19, "20 Row Street")};

      System.out.println("Opening port...\n");
      try
      {
         servSock = new ServerSocket(PORT);      //Step 1.
      }
      catch(IOException ioEx)
      {
         System.out.println("Unable to attach to port!");
         System.exit(1);
      }
      do
      {
         handleClient();
      }while (true);
   }

   private static void handleClient()
   {
      Socket link = null;                        //Step 2.

      try
      {
         link = servSock.accept();               //Step 2.

         Scanner input = new Scanner(
			 				link.getInputStream()); //Step 3.
         PrintWriter output =
         		new PrintWriter(
                   	link.getOutputStream(),true); //Step 3.

         int numMessages = 0;
         String message = input.nextLine();      //Step 4.
         while (!message.equals("***CLOSE***"))
         {
            System.out.println("Message received.");
            numMessages++;
            output.println("Message " + numMessages
                          	+ ": " + message);   //Step 4.
            message = input.nextLine();
         }
         output.println(numMessages
						+ " messages received.");//Step 4.
		}
		catch(IOException ioEx)
		{
			ioEx.printStackTrace();
		}

		finally
		{
			try
			{
				System.out.println(
								"\n* Closing connection... *");
				link.close();				    //Step 5.
			}
			catch(IOException ioEx)
			{
				System.out.println("Unable to disconnect!");
				System.exit(1);
			}
		}
	}
}

class Person implements Serializable
{
   private String name;
   private long age;
   private String address;

   public Person(String pName, long pAge, String pAddress)
   {
      name = pName;
      age = pAge;
      address = pAddress;
   }
}
