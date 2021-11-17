/*

Only one output is possible, however there are 3 different execution
possibilities, all resulting in a deadlock

# possibility 1: thread 0 goes first, gets lock on o_2, releases it after
calling wait, then 1 comes and lock o_2 and can not get lock on o_1 because 0
has it and is waiting on o_2.

# possibility 2: 1 goes first, meanwhile 0 sets oneIsRunning, then 1 gets
lock on o_2, tries to get lock on o_1 but 1 has it and is now trying to lock
o_2.

# possibility 3: same as before, but 1 does not set oneIsRunning yet, so 1
spawns a new Thread. The new thread however will halt before getting the
first lock which is hold by 0 (o_2 and o_1 are flipped again on this new
thread).

*/

public class XX extends Thread	{
	private String info;
	Object o_1;
	Object o_2;
	static boolean oneIsRunning = false;
	Object stop;

	public XX (String info, Object o_1, Object o_2, Object stop) {
		this.info    = info;
		this.o_1    = o_1;
		this.o_2    = o_2;
		this.stop    = stop;
	}

	public void run () {
		synchronized ( o_1 ) {
			System.out.println(info);
			try {
				if ( ! oneIsRunning )	{
					new XX("1", o_2, o_1, stop).start();
					oneIsRunning = true;
					}
				
				synchronized ( o_2 ) {
					o_2.wait();
					System.out.println("I will not get there");
				}
			} catch ( Exception e ) { }
		}
	}

	public static void main (String args []) {
		Object o_1 = new Object();
		Object o_2 = new Object();
		Object stop = new Object();
		new XX("0", o_1, o_2, stop).start();
	}
}
