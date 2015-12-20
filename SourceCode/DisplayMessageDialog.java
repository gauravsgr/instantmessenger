 import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class DisplayMessageDialog extends MouseAdapter
{	
	public void mouseClicked(MouseEvent e)
	{
		int i=e.getClickCount();
		if(i>1) 
		{
		List l=(List)e.getSource();//returns the source of the event in terms of Object datsy typecasting[STYJ PG-359]
		String rec=l.getSelectedItem();
		InstantMessageDialog id=new InstantMessageDialog(null,"Chat Freely @ "+rec,rec);//passed in the name of the receipient
		id.show();
		}
	}
}