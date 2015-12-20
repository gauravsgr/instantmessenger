import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

class InstantMessageFrame extends JDialog implements WindowListener
{
	JTextField message;
	List friends;
	JButton send;
	InstantMessageFrame(Frame fr,String str)
	{
		super(fr,str,false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public JPanel getMessagePanel()
	{
		JPanel js=new JPanel(new BorderLayout());
		message=new JTextField();
		send=new JButton("SEND");
		js.add(send,BorderLayout.EAST);
		js.add(message);
		return js;
	}
	public JScrollPane getFriendsPane(String[] strfrn)
	{
		friends=new List();
		for(String s:strfrn) friends.add(s);
		JScrollPane js=new JScrollPane(friends);
		return js;
	}
	
	public void constructBody(String[] obj)
	{
		add(getMessagePanel(),BorderLayout.SOUTH);
		add(getFriendsPane(obj),BorderLayout.CENTER);
		IMHandler ij=new IMHandler(message,friends);
		DisplayMessageDialog dm=new DisplayMessageDialog();
		friends.addMouseListener(dm);
		send.addActionListener(ij);
		message.addActionListener(ij);
		this.addWindowListener(this);
		setBounds(220,450,200,300);
	}
	
	void reDrawList(String[] frnd)
	{
		friends.removeAll();
		for(String sn:frnd) friends.add(sn);
		friends.remove(Login.u_n);
	}
	
	public void windowClosing(WindowEvent r)
	{
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
			System.exit(0);
	}
	public void windowOpened(WindowEvent r){}
	public void windowClosed(WindowEvent r){}	
	public void windowIconified(WindowEvent r){}
	public void windowDeiconified(WindowEvent r){}
	public void windowActivated(WindowEvent r){}	
	public void windowDeactivated(WindowEvent r){}
}