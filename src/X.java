public class X extends Thread    {
	static Object o = new Object();
	static int counter = 0;
	int id;

	public X(int id)	{
		this.id = id;
		o       = new Object();
	}

	public void run () {
		if ( id == 0 )	{
			new X(1).start();
			new X(2).start();
			return;
		}
		synchronized ( o ) {
			System.err.println(id + " --->");
			try {
				if ( counter == 0 )	{
					counter = 1;
					o.wait();
				} else
					o.notifyAll();
			}
			catch (  InterruptedException e ) {
			}
			System.err.println(id + " <---");
		}
	}

	public static void main (String args []) {
		new X(0).start();
	}
}

