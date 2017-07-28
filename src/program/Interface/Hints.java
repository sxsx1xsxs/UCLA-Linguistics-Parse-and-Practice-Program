package program.Interface;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Hints extends JFrame{
	
	String newline = System.getProperty("line.separator");
	public Hints(){
	
	super.setTitle("Hints");	
	String text="Try putting a NP node above every N, a VP node above every V, etc."+newline;
	text+="This will give you a phrase and its head."+newline;
	text+="Then you can start finding sister for the head to go in the phrase."+newline;
	text+=newline;
	text+="Carefully watch the warning window.  It will tell you:"+newline;
	text+="— when you have a drawn an incomplete structure (finish it!)"+newline;
	text+="— when you have drawn an impossible structure (go back!  drag lines to Trash.)"+newline;
	text+=newline;
	text+="The warning window also tells you what structures really are legal."+newline;
	text+=newline;
	text+="Don’t try to do this without preparation. You should study your notes and your textbook so that you really know the syntax before using it to parse."+newline;
	text+=newline;
	text+="Click anywhere on the main screen to return to the program.";

	JTextArea t=new JTextArea(text);
	t.setLineWrap(true);
	t.setPreferredSize(new Dimension(700,700));
	t.setEditable(false);
	JScrollPane s=new JScrollPane(t);
		
	JPanel panel=new JPanel();
	panel.setLayout(new GridBagLayout());
	GridBagConstraints c=new GridBagConstraints();
	c.gridx=0;
	c.gridy=0;
	c.weightx=1;
	c.weighty=1;
	c.fill=GridBagConstraints.BOTH;
	panel.add(s,c);
	
	
	JButton exit=new JButton("Exit");
	c.gridy=1;
	c.weighty=0;
	panel.add(exit,c);
	exit.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Hints.this.dispose();
		}
		
	});
	
	
	
		
		
	super.add(panel);
	super.pack();
	super.setLocationRelativeTo(null);
	super.setVisible(true);}
	
	
	public static void main (String args[]){
		Hints hints=new Hints();
	}
}
