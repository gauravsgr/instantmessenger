import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
class IMHandler implements ActionListener
{
	JTextField jt;
	List jr;
	public IMHandler(JTextField tf,List tr)
	{
		jt=tf;
		jr=tr;
	}
	public void actionPerformed(ActionEvent ke)
	{
		JTextArea ja=new JTextArea(jt.getText());
		for(int i=0;i<jr.getItemCount();i++)
			if (Login.u_n.equals(jr.getItem(i))) continue;
			else 
			try
			{
				Socket client_socket=new Socket(Login.sIP,12345);
				OutputStream os=client_socket.getOutputStream();
				ObjectOutputStream streamToServer = new ObjectOutputStream(os);
				streamToServer.writeObject(ja);
				streamToServer.writeObject(jr.getItem(i));
				streamToServer.writeObject(Login.u_n);
				streamToServer.close();
			}
			catch(Exception ej){System.out.println("Exception caught "+ej);}		
		jt.setText("");
	}
}
	