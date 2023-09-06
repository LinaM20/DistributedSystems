import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Create a ServerSocket to listen for client connections
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("Server listening on port 8888");

        // Accept a client connection
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected");

        // Get input and output streams for the socket
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

        // Read a serialized Person object from the input stream
        Person person = (Person) in.readObject();
        System.out.println("Received Person object: " + person);

        // Close the socket and streams
        clientSocket.close();
        in.close();
        out.close();
    }
}

class Person implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public String name;
    public String address;
    public int personID;

    public Person(String name, String address, int personID) {
        this.name = name;
        this.address = address;
        this.personID = personID;
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", address=" + address + ", personID=" + personID + "]";
    }
}
