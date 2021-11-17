import java.net.*;
import java.io.*;
import java.util.*;
// java DayTimeServerMT -port 12345
public class DayTimeServerMT extends Thread {

class Worker extends Thread	{
	Socket connectionToClientSocket;
	public Worker()	{}
	public Worker(Socket connectionToClientSocket)	{
		this.connectionToClientSocket = connectionToClientSocket;
	}
	public void run()	{
                System.out.println(connectionToClientSocket.toString());
		try {
			PrintWriter out = new PrintWriter(connectionToClientSocket.getOutputStream (), true);
			out.println("It is now: " + new Date());
			connectionToClientSocket.close();
		} catch ( IOException e )	{
			e.printStackTrace();
		}
	}
}
   ServerSocket 	aServerSocket;
   int			port     = 4242;

   public DayTimeServerMT()	{
   }

  
   public DayTimeServerMT(int port)	{
        try { 
            aServerSocket = new ServerSocket(port);
            System.out.println ("Listening on port: " + aServerSocket.getLocalPort());
        } catch(Exception e) {
            System.out.println(e);
        }
   }
   private void printMessage()	{
	System.out.println("-h		---->	help");
	System.out.println(" -port 		port");
	System.out.println(" {-port 		port}");
	System.out.println("or ");
	System.out.println(" no argument");
   }
	
   /**
     * Parse the commandlind arguments and sets variables.
     */
   private  void parseArgs(String args[]) {

	for (int i = 0; i < args.length; i ++) {
	   	if (args[i].equals("-h")) 
			printMessage();
	   	else if (args[i].equals("-port")) {
			port = Integer.parseInt(args[++i]);
			new DayTimeServerMT(port).start();
		}
	}
   }
   
   public void run()	{
        try {
            for(;;) {
                Socket connectionToClientSocket = aServerSocket.accept();
		Worker aWorker = new Worker(connectionToClientSocket);
		aWorker.start();
            }
        } catch(Exception e) {
            System.out.println(e);
	    e.printStackTrace();
        }
   }

    public static void main(String argv[]) {
	if ( argv.length == 0 )
		new DayTimeServerMT(0).start();
	else
		new DayTimeServerMT().parseArgs(argv);
    }
}
