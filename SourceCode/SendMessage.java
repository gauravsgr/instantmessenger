import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
class SendMessage implements ActionListener
{
	JTextArea jt;
	InstantMessageDialog jd;
	String receiver=null;
	
	public SendMessage(JTextArea ja, JDialog j,String rec)
	{
	jt=ja;
	jd=(InstantMessageDialog)j;
	receiver=rec;
	}
	public void actionPerformed(ActionEvent e) 
	{
		JButton source = (JButton)e.getSource();
		if(source==jd.send)
		{
			try
			{
				Socket client_socket=new Socket(Login.sIP,12345);
				OutputStream os=client_socket.getOutputStream();
				ObjectOutputStream streamToServer = new ObjectOutputStream(os);
				streamToServer.writeObject(jt);
				streamToServer.writeObject(receiver);
				streamToServer.writeObject(Login.u_n);
				streamToServer.close();
				jd.hide();
			}
			catch(Exception ej){System.out.println("Exception caught "+ej);}
		}
		else if(source==jd.cancel)
		{
			jd.hide();
		}
	}
}