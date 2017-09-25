package program.mainUI.interactiveTreeDrawing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import program.mainUI.Interface.Tree;

public class TeacherTreeDesigning extends InteractiveTreeDrawing {


	public TeacherTreeDesigning() throws Exception {
		super();
		teacherModeInitial();
		addActionListener();
		
	}

	private void addActionListener() {
		sn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO
				String instruction=dialogue.getText();
				System.out.println("action needed to send out this instruction");
				System.out.println(instruction);
				
				dialogue.setText("");
				teacherMode();
			}
		});	
		
		clean.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				drawingPanel.clean();
				sentence.setText("");
				meaning.setText("");
			}

		});
	}

	public void teacherModeInitial() {
		dialogue.setEditable(true);
		String text="--To change the instruction on your problem set: type the new instruction in the box."
		+System.getProperty("line.separator")
		+"--To create new sentences: click [new].";
		dialogue.setText(text);
		
		sentence.setEditable(false);
		meaning.setEditable(false);
		meaning.setVisible(true);
		
		mlabel.setVisible(true);
		
		sn.setVisible(true);
		savechange.setVisible(false);
		saveasnew.setVisible(false);
		
		check.setVisible(false);
		next.setVisible(false);
		
		tlabel.setText("Teacher Mode");
		
		start.setVisible(false);
		sl.setVisible(false);
		clean.setVisible(true);
		cleanExceptSentence.setVisible(false);		
	}
	
	public void teacherMode(){
		sentence.setEditable(true);
		meaning.setEditable(true);
		savechange.setVisible(true);
		saveasnew.setVisible(true);
		start.setVisible(true);
		sl.setVisible(true);
	}
	
	

	static public void main(String args[]) throws Exception{
		JFrame frame = new JFrame("InteractiveTreeDrawing");
		TeacherTreeDesigning interactiveDrawing=new TeacherTreeDesigning();
		frame.setPreferredSize(new Dimension(1200, 900));
		frame.add(interactiveDrawing);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		interactiveDrawing.drawingPanel.setDividersLocation();
	}

}
