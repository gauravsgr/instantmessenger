import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
class Login extends JDialog implements ActionListener, Runnable
{
	JButton enter;
	JLabel username,ipadd,port_no,sipadd;
	JTextField user_nam,ip,port,sip;
	static ClientAddress cadd=null;
	static String u_n,iP,sIP;
	static int pot;
	static int count=1;
	static ServerSocket servSoc=null;
	static Socket clientSoc=null;
	static InstantMessageFrame im=null;
	public static void main(String...arg)
	{
		new Login();
	}
	
	public Login()
	{	
		super(new Frame(),"LOGIN ",false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		enter=new JButton("Push To Enter LAUNCH PAD");
		username=new JLabel("USERNAME");
		sipadd=new JLabel("IP SERVER ");
		ipadd=new JLabel("IP CLIENT  ");
		port_no=new JLabel("PORT NO.  ");
		user_nam=new JTextField(15);
		sip=new JTextField(15);
		ip=new JTextField(15);
		port=new JTextField(15);
		JPanel top=new JPanel();	top.add(username);top.add(user_nam);
		JPanel above=new JPanel();	above.add(sipadd);above.add(sip);
		JPanel below=new JPanel();		below.add(ipadd);below.add(ip);
		JPanel ipa=new JPanel(new BorderLayout());
		ipa.add(above,BorderLayout.NORTH);
		ipa.add(below,BorderLayout.SOUTH);
		JPanel bottom=new JPanel();	bottom.add(port_no); bottom.add(port);
		JPanel da=new JPanel(new BorderLayout());
		da.add(top,BorderLayout.NORTH);
		da.add(ipa,BorderLayout.CENTER);
		da.add(bottom,BorderLayout.SOUTH);
		add(da,BorderLayout.NORTH);
		add(enter,BorderLayout.CENTER);
		add(enter,BorderLayout.SOUTH);
		setSize(300,200);
		setVisible(true);
		//--------------Doing up the Event Handling 
		enter.addActionListener(this);
		user_nam.addActionListener(this);
		ip.addActionListener(this);
		sip.addActionListener(this);
		port.addActionListener(this);
	}
	public void actionPerformed(ActionEvent e)
	{
		try
		{	u_n=user_nam.getText();	iP=ip.getText();	sIP=sip.getText();	pot=Integer.parseInt(port.getText());
			cadd=new ClientAddress(u_n,iP,pot);
			Socket client_socket=new Socket(sIP,12345);
			OutputStream os=client_socket.getOutputStream();
			ObjectOutputStream streamToServer=new ObjectOutputStream(os);
			streamToServer.writeObject(cadd);
			streamToServer.close();
			System.out.println("Client Username"+cadd.Username+"Client IP :"+cadd.IP+" Client Port "+cadd.port);
			Thread t=new Thread(this,"FRAMECONSTRUCTOR");
			t.start();
		}
		catch(Exception s)
		{System.out.println("Exception found by line 57 "+s);}
		this.dispose();
	}	
	public void run()
	{
		if(Thread.currentThread().getName().equals("FRAMECONSTRUCTOR"))
		{	
			try
			{
				servSoc = new ServerSocket(pot);
				System.out.println("Initializing Server at port :"+pot);
				System.out.println("\nServer Started.......");
				System.out.println("\nWaiting for client..");
				im=new InstantMessageFrame(null,Login.u_n +"@ Gaush IM ;-->");
				im.constructBody(new String[0]);
				im.show();
				while(true)
				{
					clientSoc = servSoc.accept();	
					System.out.println("\nObtained Message\n");
					Thread t1=new Thread(this,"CLIENTSERVICE");
					t1.start();
				}			
			}
			catch(Throwable t)	
			{
				System.out.println("Exception: line 22\n"+t);
				System.out.println("\nOr Try, another port");
				try
				{
					Socket client_socket=new Socket(Login.sIP,12345);
					ObjectOutputStream streamToServer=new ObjectOutputStream(client_socket.getOutputStream());
					streamToServer.writeObject("leave");
					streamToServer.writeObject(Login.cadd);
					streamToServer.close();
					Thread.currentThread().sleep(500);
					/*this was necessary in order to get the response from the server, else the listner also got exited*/
				}
				catch(Exception ej){System.out.println("Exception caught "+ej);}
				JOptionPane.showMessageDialog(null,"The PORT NO. You entered is busy, Plz try some other PORT");
				new Login();
			}	
		}
		else if(Thread.currentThread().getName().equals("CLIENTSERVICE"));
		{
			try
			{
				InputStream is = clientSoc.getInputStream();
				BufferedInputStream bis=new BufferedInputStream(is);
				ObjectInputStream ois = new ObjectInputStream(bis);
				Object obj=ois.readObject();
				if(obj instanceof String[])
				{
					System.out.println("String of Friends received");
					String[] frnd=(String[])obj;
					for(String s:frnd) System.out.print("\t"+s);
					im.reDrawList(frnd);
				}
				else 
				{
					String rec=(String)ois.readObject();
					String sender=(String)ois.readObject();
					new MessageDialog(obj,sender,im.friends);
				}
			}
			catch(Exception e){System.out.println("Exception at line 103 "+e);}
		}
		
	}
}