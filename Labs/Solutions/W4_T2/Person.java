//W4_T2
// Possible solution
import java.io.*;

class Person implements Serializable
{
	private String name;
	private int age;
	private String address;

	public Person(String sName, int nAge, String sAddress){
		name = sName;
		age = nAge;
		address = sAddress;
	}

	public String getName(){
		return name;
	}
	public int getAge(){
		return age;
	}

	public String getAddress(){
		return address;
	}

}
