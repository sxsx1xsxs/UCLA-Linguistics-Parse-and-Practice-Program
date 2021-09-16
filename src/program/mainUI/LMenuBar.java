package program.mainUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;


//This is the class for the menubar in UCLA-PPP
//most of the action of the menu itmes will be implemented in class Total's constructor
//except the help section of the menubar
public class LMenuBar extends JMenuBar {
	//File
	public JMenuItem newfile=new JMenuItem("New");
	//teacher mode open 
	public JMenuItem open=new JMenuItem("Open");
	//student mode open homework
	public JMenuItem open2=new JMenuItem("Open Homework");
	public JMenuItem openanswerp=new JMenuItem("Open Answer in Progress");
	public JMenuItem save=new JMenuItem("Save");
	public JMenuItem saveas=new JMenuItem("Save as");
	public JMenuItem saveanswer=new JMenuItem("Save Answer in Progress as");
	public JMenuItem savetreeas=new JMenuItem("Save Tree as image");
	public JMenuItem importgrammar = new JMenuItem("Use a Different Grammar");
	//	JMenuItem importSentenceSet = new JMenuItem("Import a Sentence Set from Txt");
	//	JMenuItem importSentenceSet2 = new JMenuItem("Import a Sentence Set from Practice File");

	public JMenuItem exportSentenceSet = new JMenuItem("Export the homework");
	public JMenuItem exportSentenceSet2 = new JMenuItem("Export the Sentence Set as Practice File");
	public JMenuItem exit = new JMenuItem("Exit");

	//Edit
	public JMenu edit = new JMenu("Edit");
	public JMenuItem redo = new JMenuItem("redo");
	public JMenuItem undo = new JMenuItem("undo");

	//Font
	public JMenuItem large = new JMenuItem("Larger");
	public JMenuItem small = new JMenuItem("Smaller");
	public JMenuItem customize = new JMenuItem("Customize (Please wait)");
	public JMenuItem resetFont = new JMenuItem("Reset to Default");
	public JMenuItem tlarge=new JMenuItem("Larger");
	public JMenuItem tsmall = new JMenuItem("Smaller");
	public JMenuItem tcustomize = new JMenuItem("Customize (Please wait)");
	public JMenuItem tresetFont = new JMenuItem("Reset to Default");

	//Setting
	public JCheckBoxMenuItem turnoffRC=new JCheckBoxMenuItem("Turn off rule checking");
	public JCheckBoxMenuItem turnonRC=new JCheckBoxMenuItem("Turn on rule checking");

	// Format
	public JMenu format = new JMenu("Spacing");	
	public JMenuItem topdown = new JMenuItem("Show Top Down Form");
	public JMenuItem bottomup = new JMenuItem("Show Bottom Up Form");
	public JMenuItem bracket = new JMenuItem("Show Bracket Form");
	public JMenuItem wi = new JMenuItem("Width+");
	public JMenuItem hi = new JMenuItem("Height+");
	public JMenuItem wd = new JMenuItem("Width-");
	public JMenuItem hd = new JMenuItem("Height-");

	// Movement mode
	public JMenu movement = new JMenu("Movements");
	public JMenuItem drawArrow = new JMenuItem("Make Movement Arrow (Click Start and End)");
	public JMenuItem drawIsland = new JMenuItem("Place Box Around Constituent (Click on the Node)");

	// Mode
	public JMenuItem freedrawing = new JMenuItem("Draw Tree");
	public JMenuItem clauseentering = new JMenuItem("Type with Brackets");
	public JMenuItem teacher = new JMenuItem("Teacher Mode");
	public JMenuItem parse = new JMenuItem("Practice Mode");

	//Setting
	public JMenuItem automaticLine = new JMenuItem("Enable Automatic Line Drawing");	
	public JMenu treedrawing = new JMenu("Draw Tree");

	//help
	public JMenuItem demo = new JMenuItem("Demo");	
	public JMenuItem hints = new JMenuItem("Hints");
	public JMenuItem manual = new JMenuItem("Connect to Online Manual");	




	public void setFont(Font font){

		int count=LMenuBar.this.getMenuCount();
		Stack<Object> stack=new Stack<Object>();
		for(int i=0;i<count;i++){
			JMenu now=LMenuBar.this.getMenu(i);
			stack.push(now);		
		}

		while(!stack.isEmpty()){
			Object now=stack.pop();
			if(now instanceof JMenu ){
				JMenu now_menu=(JMenu) now;
				now_menu.setFont(font);
				int item_count= now_menu.getItemCount();
				for (int i=0; i<item_count; i++){
					stack.push(now_menu.getItem(i));
				}
			}
			else if(now instanceof JMenuItem){
				JMenuItem now_menu_item=(JMenuItem) now;
				now_menu_item.setFont(font);
			}

		}


	}

	public LMenuBar() {

		JMenu file = new JMenu("File");

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



		edit.add(undo);
		edit.add(redo);


		JMenu view = new JMenu("Font");
		JMenu system=new JMenu("System");
		JMenu tree=new JMenu("Tree");
		view.add(system);		
		view.add(tree);

		system.add(large);
		system.add(small);
		system.add(customize);
		system.add(resetFont);

		tree.add(tlarge);
		tree.add(tsmall);
		tree.add(tcustomize);
		tree.add(tresetFont);


		JMenu setting=new JMenu("Settings");
		turnonRC.setSelected(true);
		setting.add(turnonRC);
		setting.add(turnoffRC);


		format.add(wi);
		format.add(wd);
		format.add(hi);
		format.add(hd);
		format.add(topdown);
		format.add(bottomup);

		movement.add(drawArrow);
		movement.add(drawIsland);

		JMenu mode = new JMenu("Task");
		mode.add(freedrawing);
		//		mode.add(teacher);
		mode.add(parse);


		exit.setToolTipText("Exit application");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});


		manual.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(java.awt.Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
						java.awt.Desktop.getDesktop().browse(URI.create("http://www.linguistics.ucla.edu/people/hayes/UCLA-PPP/index.htm"));
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				/* deprecated embedded browser version
				JEditorPane jep = new JEditorPane();
				jep.setPreferredSize(new Dimension(800,800));

				jep.setEditable(false);   

				try {
					jep.setPage("http://www.linguistics.ucla.edu/people/hayes/UCLA-PPP/index.htm");
				}catch (IOException ed) {
					jep.setContentType("text/html");
					jep.setText("<html>Could not load</html>");
				} 

				JScrollPane scrollPane = new JScrollPane(jep);     
				JFrame f = new JFrame("Manual");
				f.getContentPane().add(scrollPane);
				f.pack();
				f.setPreferredSize(new Dimension(800,600));
				f.setVisible(true);
				f.setLocationRelativeTo(null);
				 */
			}

		});


		JMenu help=new JMenu("Help");
		help.add(demo);
		help.add(hints);
		help.add(manual);

		super.add(file);
		super.add(edit);
		super.add(view);
		super.add(format);
		super.add(movement);
		super.add(mode);
		super.add(setting);
		super.add(help);
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


//main method as a tester for the class
	public static void main(String[] args){
		final JFrame frame = new JFrame("Menubar");
		frame.setPreferredSize(new Dimension(1200, 300));
		final LMenuBar menuB = new LMenuBar();
		frame.add(menuB, BorderLayout.NORTH);

		//to test the setFont function
		JButton button=new JButton("click to change font size of the menubar"); 
		frame.add(button);
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Font font = new Font("Courier", Font.BOLD,40);
				menuB.setFont(font);
			}

		});


		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}
}
