import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
public class MasterServer extends JFrame implements Runnable
{
	static int port_num=12345;
	Socket clientSoc=null;
	ServerSocket servSoc=null;
	static ArrayList<ClientAddress> can=new ArrayList<ClientAddress>();
	
	public static void main(String[] args)
	{
		new MasterServer("MasterServer");
	}
	public MasterServer(String title)
	{
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(false);
		try
		{
			servSoc = new ServerSocket(port_num);
			JOptionPane.showMessageDialog(null,"Master Server Running at PORT: 12345");
			while(true)
			{
				clientSoc = servSoc.accept();	
				Thread t1=new Thread(this);
				t1.start();
			}			
		}
		catch(Throwable t)	
		{
			System.out.println("Exception: line 22\n"+t);
			System.out.println("\nOr Try, another port");
		}
	}
	public void run()
	{	
		try
		{	
			InputStream is = clientSoc.getInputStream();
			BufferedInputStream bis=new BufferedInputStream(is);
			ObjectInputStream ois = new ObjectInputStream(bis);
			Object obj=ois.readObject();
			if(obj instanceof ClientAddress) addToArrayList(obj);
			else if(obj instanceof String) fastDeliver(obj,ois);
			else sendMessage(obj,ois);
		}catch(Throwable t){	System.out.println("Exception: line 68\n"+t); }
	}
	public void addToArrayList(Object o) throws Exception
	{
		ClientAddress ca=(ClientAddress)o;
		can.add(ca);	
		sendListOfMembers();
	}
	public void sendMessage(Object obj,ObjectInputStream ois) throws Exception
	{
		String rec=(String)ois.readObject();
		Object sender=ois.readObject();
			for(ClientAddress c:can)
			{		
				if(rec.equals(c.Username))
				{
					Socket soc = new Socket(c.IP,c.port);
					//JUST TO CHECK THE WORKING OF THE STREAM
					System.out.println("got up with"+c.IP+" "+c.port);
					OutputStream os = soc.getOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(os);
					oos.writeObject(obj);
					oos.writeObject(rec);
					oos.writeObject(sender);
					oos.close();
				}
			}
	}
	public void sendListOfMembers() 
	{	
		ClientAddress []ob=new ClientAddress[(can.size())];
		System.out.println("No. of Users :"+can.size());
		ob=can.toArray(ob);
		String[] frnd=new String[can.size()];
		int i=0;
		for(ClientAddress o:ob)
			frnd[i++]=o.Username;
		for(ClientAddress c:can)
		{
			try
			{
				Socket soc = new Socket(c.IP,c.port);
				OutputStream os = soc.getOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(os);
				oos.writeObject(frnd);		
			}catch(Exception s){System.out.println("Error in line 114 "+s);}
		}
	}
	public void fastDeliver(Object obj,ObjectInputStream ois)
	{
		try
		{	
			String msge=(String)obj;
			if(msge.equals("leave"))
			{
				ClientAddress cremove=(ClientAddress)ois.readObject();
				for(ClientAddress c:can)
					if(c.equals(cremove)) 
						{
							System.out.println("Removed Address"+c.Username+" "+c.IP);
							can.remove(c);
							break;// break is necessary to avoid java.util.ConcurrentModificationExcepition 
						} 
						ois.close();
						sendListOfMembers();
			}
			else
			{
				String sender=(String)ois.readObject();
				JTextArea message=new JTextArea(msge);
				for(ClientAddress c:can)
				{		
					if(!(sender.equals(c.Username)))
					{
						Socket soc = new Socket(c.IP,c.port);
						//JUST TO CHECK THE WORKING OF THE STREAM
						System.out.println("got up with"+c.IP+" "+c.port);
						OutputStream os = soc.getOutputStream();
						ObjectOutputStream oos = new ObjectOutputStream(os);
						oos.writeObject(message);
						oos.writeObject(sender);
						oos.writeObject(c.Username);
						oos.close();
					}
				}
			}
		}catch(Exception s){System.out.println("Exception caught "+s);}			
	}
}