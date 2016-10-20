import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class LMenuBar extends JMenuBar {
	// File
	JMenuItem newfile=new JMenuItem("New");
	//teacher mode open 
	JMenuItem open=new JMenuItem("Open");
	//student mode open homework
	JMenuItem open2=new JMenuItem("Open Homework");
	JMenuItem openanswerp=new JMenuItem("Open Answering Progress");
	JMenuItem save=new JMenuItem("Save");
	JMenuItem saveas=new JMenuItem("Save as");
	JMenuItem saveanswer=new JMenuItem("Save answering progress as");
	JMenuItem savetreeas=new JMenuItem("Save Tree as image");
	JMenuItem importgrammar = new JMenuItem("Use a Different Grammar");
	JMenuItem importSentenceSet = new JMenuItem("Import a Sentence Set from Txt");
	JMenuItem importSentenceSet2 = new JMenuItem("Import a Sentence Set from Practice File");
	
	JMenuItem exportSentenceSet = new JMenuItem("Export the homework");
	JMenuItem exportSentenceSet2 = new JMenuItem("Export the Sentence Set as Practice File");
	JMenuItem exit = new JMenuItem("Exit");
	
	// Edit
	JMenuItem redo = new JMenuItem("redo");
	JMenuItem undo = new JMenuItem("undo");
	

	// View
	JMenuItem large = new JMenuItem("Larger");
	JMenuItem small = new JMenuItem("Smaller");
	JMenuItem customize = new JMenuItem("Customize (Please wait)");
	
	
	JCheckBoxMenuItem turnoffRC=new JCheckBoxMenuItem("Turn off rule checking");
	JCheckBoxMenuItem turnonRC=new JCheckBoxMenuItem("Turn on rule checking");

	// Format
	JMenu format = new JMenu("Spacing");
	
	JMenuItem topdown = new JMenuItem("Show Top Down Form");
	JMenuItem bottomup = new JMenuItem("Show Bottom Up Form");
	JMenuItem bracket = new JMenuItem("Show Bracket Form");
	JMenuItem wi = new JMenuItem("Width+");
	JMenuItem hi = new JMenuItem("Height+");
	JMenuItem wd = new JMenuItem("Width-");
	JMenuItem hd = new JMenuItem("Height-");
	

	// Mode
	JMenuItem freedrawing = new JMenuItem("Draw Tree");
	JMenuItem clauseentering = new JMenuItem("Type with Brackets");
	JMenuItem teacher = new JMenuItem("Teacher Mode");
	JMenuItem parse = new JMenuItem("Practice Mode");

	//Setting
	JMenuItem automaticLine = new JMenuItem("Enable Automatic Line Drawing");
	
	JMenu treedrawing = new JMenu("Draw Tree");



public void setLFont(Font font){
	open.setFont(font);
	open2.setFont(font);
	newfile.setFont(font);
	importgrammar.setFont(font);
	save.setFont(font);
	saveas.setFont(font);
	savetreeas.setFont(font);
	openanswerp.setFont(font);
	saveanswer.setFont(font);

	exportSentenceSet.setFont(font);
	exit.setFont(font);
	
	redo.setFont(font);
	undo.setFont(font);
	
	
	large.setFont(font);
	small.setFont(font);	
	customize.setFont(font);
	
	topdown.setFont(font);
	bottomup.setFont(font);
	bracket.setFont(font);	
	wi.setFont(font);
	hi.setFont(font);
	wd.setFont(font);
	hd.setFont(font);
	
	freedrawing.setFont(font);
	clauseentering.setFont(font);	
	teacher.setFont(font);
	parse.setFont(font);
	
	automaticLine.setFont(font);
	treedrawing.setFont(font);
	
	turnoffRC.setFont(font);
	turnonRC.setFont(font);

	
}

	LMenuBar() {
		String path = "/Users/yizhang/Desktop/icon.png";
        Icon pie = new ImageIcon(path);
        saveanswer.setSelectedIcon(pie);
        saveanswer.setSelected(true);
        JCheckBoxMenuItem aiMode = new JCheckBoxMenuItem("AI Mode");
        aiMode.setSelected(true);
       
//        secondLabel = new JLabel("Text with icon",pie,SwingConstants.LEFT);
//		/Users/yizhang/Desktop
		JMenu file = new JMenu("File");
		//file.add(aiMode);
		file.add(newfile);
		file.add(open);
		file.add(open2);
		file.add(openanswerp);
		file.add(save);
		file.add(saveas);
		file.add(savetreeas);
		file.add(saveanswer);
		file.add(exportSentenceSet);
		file.add(importgrammar);
		//file.add(importSentenceSet);
		//file.add(importSentenceSet2);

		//file.add(exportSentenceSet2);
		file.add(exit);
		
		
		JMenu edit = new JMenu("Edit");
		edit.add(undo);
		edit.add(redo);
		
		
		JMenu view = new JMenu("Font");
		view.add(large);
		view.add(small);
		view.add(customize);
		
		JMenu setting=new JMenu("Settings");
		turnonRC.setSelected(true);
		setting.add(turnonRC);
		setting.add(turnoffRC);
			

		
		//format.add(bracket);


		format.add(wi);
		format.add(wd);
		format.add(hi);
		format.add(hd);
		format.add(topdown);
		format.add(bottomup);


		JMenu mode = new JMenu("Task");
		
		mode.add(freedrawing);
//		treedrawing.add(freedrawing);
//		treedrawing.add(clauseentering);
//		mode.add(teacher);
		mode.add(parse);

		//JMenu setting = new JMenu("Settings");
		//setting.add(automaticLine);
		
		
		exit.setToolTipText("Exit application");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		super.add(file);
		//super.add(edit);
		super.add(view);
		super.add(format);
		super.add(mode);
		super.add(setting);
		super.setBorder(new MatteBorder(0, 0, 1, 0, Color.black));

	}
	
	public void studentmode(){
		save.setVisible(false);
		saveas.setVisible(false);
		newfile.setVisible(false);
		open.setVisible(false);
		open2.setVisible(true);
		exportSentenceSet.setVisible(true);
		
	}
	
	public void teachermode(){
		save.setVisible(true);
		saveas.setVisible(true);
		newfile.setVisible(true);
		open2.setVisible(false);
		open.setVisible(true);
		exportSentenceSet.setVisible(false);
		
	}

}
