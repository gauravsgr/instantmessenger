import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class InstantMessageDialog extends JDialog
{
	JButton send;
	JButton cancel;
	JTextArea message;
	public InstantMessageDialog(Frame fr,String str,String rec)
	{
		super(fr,str,false);
		send=new JButton("Send");
		cancel=new JButton("Close");
		message=new JTextArea();
		JPanel imd=new JPanel();
		imd.add(send);
		imd.add(cancel);
		add(imd,BorderLayout.SOUTH);
		JScrollPane center = new JScrollPane(message);
		add(center);
		JLabel lb=new JLabel(Login.u_n);//changed here by removing "gausag"
		setBounds(400,200,350,250);
		add(lb,BorderLayout.NORTH);
		SendMessage ob=new SendMessage(message,this,rec);
		send.addActionListener(ob);
		cancel.addActionListener(ob);
	}
}
	