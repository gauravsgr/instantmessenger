import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class MessageDialog extends JDialog implements ActionListener
{
	JButton reply;
	JLabel lsender;
	String sender;
	public MessageDialog(Object obj,String send,List jlt)
	{
		super(new Frame(),"MessageDialog",false);
		sender=send;
		reply=new JButton("Reply");
		JTextArea label=(JTextArea)obj;
		JScrollPane center = new JScrollPane(label);
		lsender=new JLabel("Send By: "+sender);
		JPanel top=new JPanel(new BorderLayout());
		top.add(lsender,BorderLayout.NORTH);
		this.add(reply,BorderLayout.SOUTH);
		this.add(top,BorderLayout.NORTH);
		top.add(center,BorderLayout.CENTER);
		setSize(400,150);
		show();
		reply.addActionListener(this);
		//EVENT HANDLING OF REPLY BUTTON STILL REMAINING DUDE
	}
	public void actionPerformed(ActionEvent e)
	{
		InstantMessageDialog id=new InstantMessageDialog(null,"Chat Freely @ "+sender,sender);
		id.show();
		this.hide();
	}
}