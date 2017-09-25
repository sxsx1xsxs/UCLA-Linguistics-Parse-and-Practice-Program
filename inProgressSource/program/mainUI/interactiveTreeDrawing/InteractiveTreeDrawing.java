package program.mainUI.interactiveTreeDrawing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import program.grammar.Grammar;
import program.mainUI.Interface.Tree;
import program.mainUI.drawingPanel.Plain;

public abstract class InteractiveTreeDrawing extends JPanel{
	
	// the label denoting mode can be "Teacher Mode" or "Practice Mode" or "Free-drawing Mode"
	protected JLabel tlabel = new JLabel("");
	
	// the left side buttons
	// for practice mode
	protected JButton check = new JButton("Check Answer");
	protected JButton next = new JButton("Next");
	// for edit mode
	protected JButton sn = new JButton("New");
	protected JButton savechange = new JButton("Save Change");
	protected JButton saveasnew = new JButton("Save as New");
	
	// the right side
	protected JButton sl = new JButton("Straighten Lines");
	// for edit mode
	protected JButton start = new JButton("Start");
	protected JButton clean = new JButton("Clear");
	// for practice mode
	protected JButton cleanExceptSentence = new JButton("Clear");

	// the lower components
	protected JLabel slabel = new JLabel("Sentence");
	protected JTextField sentence = new JTextField();
	protected JLabel mlabel = new JLabel("Meaning");
	protected JTextField meaning = new JTextField();
		
	//the top part holding all the buttons and interface except for Plain
	private JPanel top=new JPanel();
	//the drawing area
	public Plain drawingPanel;
	//the dialogue window inside plain, listed separately because sometimes whether it is editable needs to be controled
	protected JTextArea dialogue;

	//set the layout of the top part above Plain
	private void topSetLayout() {
		top.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		top.add(sn, c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		top.add(savechange, c);

		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		top.add(saveasnew, c);

		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		top.add(check, c);

		c.gridx = 4;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		top.add(next, c);

		c.gridx = 5;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.NONE;
		top.add(tlabel, c);

		c.gridx = 6;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		c.fill = GridBagConstraints.BOTH;
		top.add(start, c);

		c.gridx = 7;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		c.fill = GridBagConstraints.BOTH;
		top.add(sl, c);

		c.gridx = 8;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		c.fill = GridBagConstraints.BOTH;
		top.add(clean, c);

		c.gridx = 9;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		top.add(cleanExceptSentence, c);

		// the panel holding 2 textfields
		JPanel enter = new JPanel();
		enter.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 10;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		top.add(enter, c);

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		c.fill = GridBagConstraints.BOTH;
		enter.add(slabel, c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		enter.add(sentence, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		c.fill = GridBagConstraints.BOTH;
		enter.add(mlabel, c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		enter.add(meaning, c);

	}
	
	//set layout to top on top and Plain below top
	private void setBasicLayout() {
		Grammar grammar = new Grammar();
		try {
			drawingPanel = new Plain(grammar);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dialogue=drawingPanel.dialogueP.dialogue;
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 5, 10);
		add(top, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 0, 0);
		add(drawingPanel, c);
	}
	
	public InteractiveTreeDrawing() {
		topSetLayout();
		setBasicLayout();
		addActionListener();
		

	}

	private void addActionListener() {
		sl.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				drawingPanel.drawingArea.back.straightenLine();
			}

		});
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String s = sentence.getText();
				drawingPanel.drawingArea.inputSentence(s);
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

}
