public class Q3b extends Thread
{
	public static void main(String[] args)
	{
		Thread1 thread1 = new Thread1();
		Thread2 thread2 = new Thread2();
        Thread3 thread3 = new Thread3();

		thread1.start();
		thread2.start();
        thread3.start();
	}

}

class Thread1 extends Thread
{
	int  pause;

	public void run()
	{
		for (int i=0; i<300; i++)
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
		for (int i=0; i<30; i++)
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

class Thread3 extends Thread
{
	int  pause;

	public void run()
	{
		for (int i=0; i<3; i++)
		{
			try
			{
				System.out.println(i+1);
				pause = (int)(Math.random() * 6000);
				sleep(pause);
			}
			catch (InterruptedException interruptEx)
			{
					System.out.println(interruptEx.toString());
			}
		}
	}
}

