import java.io.*;
import java.net.*;
import java.util.*;

public class DualClient implements Runnable
{
	static int server_port;	//--- recieving port
	static int client_port=12345;	//--- sending port
	static ClientAddress cadd=null;	
	static String clientName=null;	
	static ServerSocket server_socket=null;	
	
	public static void main(String... ar) throws Exception
	{	
		if(ar.length!=3) 
		{
			System.out.println("Usage: java DualClient <USERNAME> <IP ADDRESS> <PORT NO.>");
			System.exit(0);
		}
		server_port=Integer.parseInt(ar[2]);
		cadd=new ClientAddress(ar[0],ar[1],server_port);
		System.out.println("Welcome "+ar[0]+"\n");
		System.out.println("Initializing Listening Port: "+server_port+" ...");		
		setUpListener();
		System.out.println("\n***************************\n");	
		Thread t1=new Thread(new DualClient(),"Listener");		
		t1.start();
		
		System.out.println("Initializing Sending Port: "+client_port+" ...");		
		Thread t2=new Thread(new DualClient(),"Sender");		
		t2.start();
	}
			
	private static void setUpListener()
	{
		server_socket=null;		
		try
		{
			server_socket=new ServerSocket(server_port);
		}
		catch(IOException e)
		{
			System.out.println("Problem Starting Listener at Port: "+server_port);
			System.out.println(e.getClass()+" " +e.getMessage());
			System.exit(0);
		}
		System.out.println("Listener Started at Port: "+server_port);
	}
	
	public void run()
	{
		String threadName = Thread.currentThread().getName();		
		if(threadName.equals("Listener"))
		{
			startListening();
		}
		else
		{
			startSending();
		}
	}

	private void startListening()
	{		
		ObjectInputStream streamFromClient=null;
			
		while(true)
		{
			try
			{
				//System.out.println("Waiting for client Request.....");
				Socket clientReq=server_socket.accept();
				//System.out.println("Got a client Request, sending response to client..");
				
				streamFromClient=new ObjectInputStream(clientReq.getInputStream());												
				String msg_from_client=(String)streamFromClient.readObject();
				System.out.println("\n\n----> "+msg_from_client+"\n");				
			}
			catch(Exception e)
			{
				System.out.println(e.getClass()+" " +e.getMessage());
				System.exit(0);
			}
		}		
	}
	
	public void startSending()
	{
		Socket client_socket;
		ObjectOutputStream streamToServer;
		String input_msg=null;
		//---------------------
		try
		{
			client_socket=new Socket("127.0.0.1",client_port);
			streamToServer=new ObjectOutputStream(client_socket.getOutputStream());
			streamToServer.writeObject(cadd);
			streamToServer.close();
			System.out.println("Client IP :"+cadd.IP+" Client Port "+cadd.port);
			do	
			{
				System.out.print("Enter Message or Type 'QUIT' to exit: ");
				Scanner kbd=new Scanner(System.in);										
				input_msg=kbd.nextLine();
				client_socket=new Socket("127.0.0.1",client_port);
				streamToServer=new ObjectOutputStream(client_socket.getOutputStream());
				streamToServer.writeObject(input_msg);
				if(input_msg.equalsIgnoreCase("quit"))
				{	
					streamToServer.writeObject(cadd);
					Thread.currentThread().sleep(2000);//this was necessary in order to get the response from the server, else the listner also got exited 
					System.exit(0);
				}
								/*}
				else
				{
					System.exit(0);
				}*/			
			}while(!input_msg.equalsIgnoreCase("quit"));	
		}catch(IOException e)
		{
			System.out.println("\nProblem In Connecting to Server at Port: "+server_port);
			System.out.println(e.getClass()+" " +e.getMessage());				
		}
		catch(Exception e)
		{
			System.out.println("\nProblem In Connecting to Server at Port: "+server_port);
			System.out.println(e.getClass()+" " +e.getMessage());
		}			
	}
}