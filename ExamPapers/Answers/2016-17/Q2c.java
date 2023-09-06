public class Q2c extends Thread
{
	public static void main(String[] args)
	{
		Thread1 thread1 = new Thread1();
		Thread2 thread2 = new Thread2();

		thread1.start();
		thread2.start();
	}

}

class Thread1 extends Thread
{

	public void run()
	{
		for (int i=0; i<2016; i++)
		{
			try
			{
				System.out.println(i+1);
			}
			catch (InterruptedException interruptEx)
			{
					System.out.println(interruptEx.toString());
			}
		}
	}
}

class Thread2 extends Thread
{
	int  pause;

	public void run()
	{
		for (int i=0; i<2017; i++)
		{
			try
			{
				System.out.println(i+1);
				pause = (int)(Math.random() * 3000);
				sleep(pause);
			}
			catch (InterruptedException interruptEx)
			{
					System.out.println(interruptEx.toString());
			}
		}
	}
}

