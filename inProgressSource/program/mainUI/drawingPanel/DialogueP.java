package program.mainUI.drawingPanel;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import interactiveAction.InAction;
import interactiveAction.Interactive;

public class DialogueP extends JScrollPane implements Interactive{

	public JTextArea dialogue;
	
	public DialogueP(){
		dialogue=new JTextArea();
		dialogue.setEditable(false);
		dialogue.setBackground(Color.decode("0xA0A0A0"));
		dialogue.setLineWrap(true);
		dialogue.setWrapStyleWord(true);
		this.setViewportView(dialogue);
	}

	@Override
	public void receiveAction(InAction action) {
		//currently do nothing
		
	}
	
}
