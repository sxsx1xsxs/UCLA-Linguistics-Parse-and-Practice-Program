package program;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Stack;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.TreePath;

import program.grammar.Grammar;
import program.mainUI.*;
import program.mainUI.drawingPanel.Line;
import program.mainUI.drawingPanel.NodeLabel;
import program.mainUI.drawingPanel.Plain;
import program.mainUI.inforTree.SentenceSetStorage;



///*this is the controlling source code
public class Total extends JPanel {
	//can be "" or "teacher"
	String permission="";
	Grammar grammar = new Grammar();
	LMenuBar menu = new LMenuBar();
	Plain plain = new Plain(grammar);
	Interface treeMode = new Interface(plain);

	static Preference pp=new Preference();

	File file;
	String universalpassword="";
	File ans;

	public Total() {

		setLayout(new GridBagLayout());
		setUIFont(new javax.swing.plaf.FontUIResource(pp.getString("TreeFontStyle"), Font.PLAIN, pp.getInt("TreeSize")));
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		add(treeMode, c);
		treeMode.practiceMode();
		menu.studentmode();

		treeMode.teacherMode();
		menu.teachermode();

		menu.topdown.setEnabled(false);
		menu.bottomup.setEnabled(false);
		menu.turnoffRC.setEnabled(true);
		menu.turnonRC.setEnabled(false);
		menu.parse.setEnabled(false);

		menu.turnoffRC.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				menu.turnonRC.setSelected(false);
				menu.turnoffRC.setSelected(true);

				treeMode.drawingPanel.turnoffRC();

				menu.turnonRC.setEnabled(true);
				menu.turnoffRC.setEnabled(false);
			}

		});

		menu.turnonRC.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				menu.turnonRC.setSelected(true);
				menu.turnoffRC.setSelected(false);

				treeMode.drawingPanel.turnonRC();

				menu.turnonRC.setEnabled(false);
				menu.turnoffRC.setEnabled(true);
			}

		});

		menu.format.addMouseMotionListener(new MouseAdapter(){
			@Override
			public void mouseMoved(MouseEvent e){
				if(treeMode.drawingPanel.completeTree()){
					menu.topdown.setEnabled(true);
					menu.bottomup.setEnabled(true);
				}else{
					menu.topdown.setEnabled(false);
					menu.bottomup.setEnabled(false);
				}
			}
		});


		//menu item Font/System/larger
		menu.large.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				int input=pp.getInt("SystemFontSize")+2;
				Font font = new Font(pp.getString("FontStyle"), Font.PLAIN, input);
				changeSystemFont(font);

				//get the drawing panel to the lowest point
				SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						Rectangle bounds = plain.drawroom.getViewport().getViewRect();
						JScrollBar horizontal =plain.drawroom.getHorizontalScrollBar();
						JScrollBar vertical = plain.drawroom.getVerticalScrollBar();
						vertical.setValue( (vertical.getMaximum() - bounds.height)  );
					}
				});

			}

		});

		menu.small.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				int input=pp.getInt("SystemFontSize")-2;
				Font font = new Font(pp.getString("FontStyle"), Font.PLAIN, input);
				changeSystemFont(font);




				SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						Rectangle bounds = plain.drawroom.getViewport().getViewRect();
						JScrollBar horizontal =plain.drawroom.getHorizontalScrollBar();
						JScrollBar vertical = plain.drawroom.getVerticalScrollBar();
						vertical.setValue( (vertical.getMaximum() - bounds.height)  );
					}
				});

			}

		});
		menu.resetFont.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				changeSystemFont(Preference.default_font);
			}
		});

		menu.tsmall.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int input=pp.getInt("TreeSize")-2;
				pp.setInt("TreeSize", input);
				updateTreeFonts();
			}
		});

		menu.tlarge.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int input=pp.getInt("TreeSize")+2;
				pp.setInt("TreeSize", input);
				updateTreeFonts();
			}
		});

		menu.tresetFont.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				changeTreeFont(Preference.default_font);
			}
		});


		menu.wi.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				treeMode.drawingPanel.widthIndex+=0.3;
				pp.setDouble("WidthIndex",treeMode.drawingPanel.widthIndex);
				treeMode.drawingPanel.adjust();
			}

		});

		menu.hi.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				treeMode.drawingPanel.heightIndex+=0.3;
				pp.setDouble("HeightIndex",treeMode.drawingPanel.heightIndex);
				treeMode.drawingPanel.adjust();
			}

		});


		menu.freedrawing.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				treeMode.freedrawingMode();
				revalidate();
				menu.parse.setEnabled(true);
				menu.freedrawing.setEnabled(false);
			}

		});


		menu.parse.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				treeMode.practiceMode();
				revalidate();
				menu.parse.setEnabled(false);
				menu.freedrawing.setEnabled(true);
			}

		});


		menu.hd.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				treeMode.drawingPanel.heightIndex-=0.3;
				pp.setDouble("HeightIndex",treeMode.drawingPanel.heightIndex);
				treeMode.drawingPanel.adjust();
			}

		});

		menu.wd.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				treeMode.drawingPanel.widthIndex-=0.3;
				pp.setDouble("WidthIndex",treeMode.drawingPanel.widthIndex);
				treeMode.drawingPanel.adjust();
			}

		});

		menu.drawIsland.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				plain.setClicked(12); // put it in mark constituent mode
			}
		});

		menu.drawArrow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				plain.arrowMode=true;
				plain.unclick();
			}
		});

		menu.topdown.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				treeMode.drawingPanel.topDown();
			}

		});

		menu.bottomup.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				treeMode.drawingPanel.bottomUp();;
			}

		});

		menu.customize.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				FontChooserComboBox fbox;
				JComboBox box;


				JPanel panel=new JPanel(new GridBagLayout());
				GridBagConstraints c=new GridBagConstraints();
				c.gridx=0;
				c.gridy=0;
				c.gridwidth=1;
				c.fill=GridBagConstraints.BOTH;
				c.insets=new Insets(10,10,10,10);
				panel.add(new JLabel("Please select style"), c);

				c.gridy=1;
				fbox=new FontChooserComboBox();
				panel.add(fbox, c);

				c.gridy=2;
				panel.add(new JLabel("Please select font size"), c);


				Vector<Integer> ints=new Vector<Integer>();
				for(int i=10;i<=40;i++){
					ints.addElement(i);

				}
				box=new JComboBox(ints);
				box.setSize(box.getPreferredSize());
				box.setSelectedItem(pp.getInt("SystemFontSize"));
				c.gridy=3;
				panel.add(box, c);


				//JOptionPane.showInputDialog(panel, "size");
				int result = JOptionPane.showConfirmDialog(null, panel, "Customize", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {

					String style=fbox.getSelectedFontName();
					int size=Integer.parseInt(box.getSelectedItem().toString());
					Font font = new Font(style, Font.PLAIN, size);
					changeSystemFont(font);

					SwingUtilities.invokeLater(new Runnable()
					{
						@Override
						public void run()
						{
							Rectangle bounds = plain.drawroom.getViewport().getViewRect();
							JScrollBar horizontal =plain.drawroom.getHorizontalScrollBar();
							JScrollBar vertical = plain.drawroom.getVerticalScrollBar();
							vertical.setValue( (vertical.getMaximum() - bounds.height)  );
						}
					});
				} else {
					return;
				}
			}

		});

		menu.tcustomize.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				FontChooserComboBox fbox;
				JComboBox box;


				JPanel panel=new JPanel(new GridBagLayout());
				GridBagConstraints c=new GridBagConstraints();
				c.gridx=0;
				c.gridy=0;
				c.gridwidth=1;
				c.fill=GridBagConstraints.BOTH;
				c.insets=new Insets(10,10,10,10);
				panel.add(new JLabel("Please select style"), c);

				c.gridy=1;
				fbox=new FontChooserComboBox();
				panel.add(fbox, c);

				c.gridy=2;
				panel.add(new JLabel("Please select font size"), c);


				Vector<Integer> ints=new Vector<Integer>();
				for(int i=10;i<=40;i++){
					ints.addElement(i);

				}
				box=new JComboBox(ints);
				box.setSize(box.getPreferredSize());
				box.setSelectedItem(pp.getInt("TreeSize"));
				c.gridy=3;
				panel.add(box, c);


				//JOptionPane.showInputDialog(panel, "size");
				int result = JOptionPane.showConfirmDialog(null, panel, "Customize", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {

					String style=fbox.getSelectedFontName();
					int size=Integer.parseInt(box.getSelectedItem().toString());
					Font font = new Font(style, Font.PLAIN, size);
					changeTreeFont(font);

					SwingUtilities.invokeLater(new Runnable()
					{
						@Override
						public void run()
						{
							Rectangle bounds = plain.drawroom.getViewport().getViewRect();
							JScrollBar horizontal =plain.drawroom.getHorizontalScrollBar();
							JScrollBar vertical = plain.drawroom.getVerticalScrollBar();
							vertical.setValue( (vertical.getMaximum() - bounds.height)  );
						}
					});
				} else {
					return;
				}
			}

		});


		menu.clauseentering.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeAll();
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 0;
				c.weightx = 1;
				c.weighty = 1;
				c.fill = GridBagConstraints.BOTH;
				revalidate();

			}
		});

		menu.teacher.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeAll();
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 0;
				c.weightx = 1;
				c.weighty = 1;
				c.fill = GridBagConstraints.BOTH;
				add(treeMode, c);
				treeMode.teacherMode();
				// necessary
				revalidate();

			}
		});

		menu.parse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				treeMode.status="practice";
				treeMode.sentenceTree.status="practice";
				removeAll();
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 0;
				c.weightx = 1;
				c.weighty = 1;
				c.fill = GridBagConstraints.BOTH;
				add(treeMode, c);
				treeMode.practiceMode();
				// necessary
				revalidate();

			}
		});

		menu.importgrammar.addActionListener(new ActionListener() {

			String grammarInput = "";

			@Override
			public void actionPerformed(ActionEvent ae) {

				JFileChooser fileChooser = new JFileChooser();
				URL location = Total.class.getProtectionDomain().getCodeSource().getLocation();
				fileChooser.setCurrentDirectory(new File(location.getFile()));
				fileChooser.setPreferredSize(new Dimension(700,700));
				int condition = fileChooser.showOpenDialog(null);
				if (condition == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {			
						grammarInput = Preference.readFile(file.getPath(), Charset.defaultCharset());
						Grammar newg=new Grammar(grammarInput);

						if (newg.successfully_created()) {
							grammar.changeToGrammar(newg);
							pp.setString("grammar_text", grammarInput);
							JOptionPane.showMessageDialog(null, new JLabel("new grammar successfully imported"));;
							treeMode.drawingPanel.updateGrammar();
						} 
						else {
							String error_message = newg.error_message()+"Grammar is not successfully imported";
							JTextArea text=new JTextArea(error_message);
							JScrollPane textp=new JScrollPane(text);
							JOptionPane.showMessageDialog(null,textp);
						}

					} catch (IOException ex) {

					}

				}
			}

		});

		menu.exportSentenceSet.addActionListener(new ActionListener() {

			public void saveMap() {
				treeMode.saveTree();
				SentenceSetStorage store = new SentenceSetStorage(grammar,menu);
				String output = store.outputTXT2(treeMode.sentenceTree.sentence);
				JFileChooser chooser = new JFileChooser();
				URL location = Total.class.getProtectionDomain().getCodeSource().getLocation();
				chooser.setCurrentDirectory(new File(location.getFile()));
				chooser.setPreferredSize(new Dimension(700,700));
				int retrival = chooser.showSaveDialog(null);
				if (retrival == JFileChooser.APPROVE_OPTION) {
					try {
						FileWriter fw = new FileWriter(addSuffixConditionally(chooser.getSelectedFile().toString() ,".txt"));
						fw.write(output.toString());
						fw.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				saveMap();
			}
		});

		menu.demo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// Set the font back to the system font for these system windows, then put it back to tree font
				setUIFont(new javax.swing.plaf.FontUIResource(pp.getString("FontStyle"), Font.PLAIN, pp.getInt("FontSize")));
				new Demo();
				setUIFont(new javax.swing.plaf.FontUIResource(pp.getString("TreeFontStyle"), Font.PLAIN, pp.getInt("TreeSize")));
			}

		});

		menu.hints.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// Set the font back to the system font for these system windows, then put it back to tree font
				setUIFont(new javax.swing.plaf.FontUIResource(pp.getString("FontStyle"), Font.PLAIN, pp.getInt("FontSize")));
				new Hints();
				setUIFont(new javax.swing.plaf.FontUIResource(pp.getString("TreeFontStyle"), Font.PLAIN, pp.getInt("TreeSize")));
			}

		});
		menu.saveanswer.addActionListener(new ActionListener() {

			public void saveMap2() {
				treeMode.saveTree();
				SentenceSetStorage store = new SentenceSetStorage(grammar,menu);
				String output = store.outputTXT3(treeMode.sentenceTree.sentence);
				JFileChooser chooser = new JFileChooser();
				chooser.setPreferredSize(new Dimension(700,700));
				URL location = Total.class.getProtectionDomain().getCodeSource().getLocation();
				chooser.setCurrentDirectory(new File(location.getFile()));




				int retrival = chooser.showSaveDialog(null);
				if (retrival == JFileChooser.APPROVE_OPTION) {
					try {
						ans=chooser.getSelectedFile();
						FileWriter fw = new FileWriter(addSuffixConditionally(ans.toString(),".txt"));
						fw.write(output.toString());
						fw.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				saveMap2();
			}
		});

		menu.save.addActionListener(new ActionListener(){
			String output;

			public void saved() throws IOException{
				JOptionPane.showMessageDialog(null,"File successfully saved as "+file.getName()+" and "+file.getName()+".txt " +"into designated directory" );
				FileWriter fw = new FileWriter(addSuffixConditionally(file.toString(), ".txt"));
				fw.write(output.toString());
				fw.close();
				String output2=new StrongAES().encrypt(output,universalpassword);
				FileWriter fw2 = new FileWriter(file);
				fw2.write(output2.toString());
				fw2.close();

			}



			public void saveMap() throws IOException {
				treeMode.saveTree();
				SentenceSetStorage store = new SentenceSetStorage(grammar,menu);
				output = store.outputTXT(treeMode.sentenceTree.sentence);
				saved();
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					saveMap();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		});

		menu.savetreeas.addActionListener(new ActionListener() {
			File file1;

			public void saveMap() {
				JFileChooser chooser = new JFileChooser();
				URL location = Total.class.getProtectionDomain().getCodeSource().getLocation();
				chooser.setCurrentDirectory(new File(location.getFile()));
				chooser.setPreferredSize(new Dimension(700,700));

				int retrival = chooser.showSaveDialog(null);
				if (retrival == JFileChooser.APPROVE_OPTION) {
					treeMode.drawingPanel.getlocationcor();
					file1 =chooser.getSelectedFile();
					//enterpassword();
					BufferedImage image = new BufferedImage(
							treeMode.drawingPanel.drawroom.canvas.getWidth(),
							treeMode.drawingPanel.drawroom.canvas.getHeight(),
							BufferedImage.TYPE_INT_RGB
							);
					if(menu.turnoffRC.isEnabled()){
						treeMode.drawingPanel.turnoffRC();
						treeMode.drawingPanel.drawroom.canvas.paint(image.getGraphics() );
						treeMode.drawingPanel.turnonRC();
					}else{ 
						treeMode.drawingPanel.drawroom.canvas.paint(image.getGraphics() );}
					Rectangle rec=treeMode.drawingPanel.drawroom.getViewport().getViewRect();
					Plain uu=treeMode.drawingPanel;
					int x=rec.x;
					int y=rec.y;
					int w=rec.width;
					int h=rec.height;
					image = image.getSubimage(uu.locationx,uu.locationy,uu.locationw,uu.locationh); // 500 x 500
					try {
						// write the image as a PNG
						ImageIO.write(
								image,
								"png",
								new File(addSuffixConditionally(file1.toString(),".png")));
					} catch(Exception ex) {
						ex.printStackTrace();
					}



				}
				else{
					//NOTsaved();
				}
			}


			@Override
			public void actionPerformed(ActionEvent e) {

				saveMap();

			};

		});

		treeMode.teacherPanel.check.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {				
				treeMode.saveTree();
				treeMode.teacherPanel.checkanswer();
				SentenceSetStorage store = new SentenceSetStorage(grammar,menu);
				String output = store.outputTXT3(treeMode.sentenceTree.sentence);

				try {
					FileWriter fw = new FileWriter(addSuffixConditionally(ans.toString(), ".txt"));
					fw.write(output.toString());
					fw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}


		});

		menu.saveas.addActionListener(new ActionListener(){
			File file;
			String output;
			String passwords;

			public void saved() throws IOException{
				JOptionPane.showMessageDialog(null,"File successfully saved as "+file.getName()+" and "+file.getName()+".txt " +"into designated directory" );
				FileWriter fw = new FileWriter(addSuffixConditionally(file.toString(), ".txt"));
				fw.write(output.toString());
				fw.close();
				String output2=new StrongAES().encrypt(output,passwords);
				FileWriter fw2 = new FileWriter(file);
				fw2.write(output2.toString());
				fw2.close();

			}
			public void NOTsaved(){
				JOptionPane.showMessageDialog(null,"Saving Action Cancelled." );
			}

			public void enterpassword(){
				JPanel passwordPanel=new JPanel();
				passwordPanel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				JTextField password=new JTextField();
				c.gridx = 0;
				c.gridy = 0;
				c.fill = GridBagConstraints.BOTH;
				passwordPanel.add(new JLabel("Please enter the password of your choice for the encrypted file"), c);
				c.gridx = 0;
				c.gridy = 1;
				c.fill = GridBagConstraints.BOTH;
				passwordPanel.add(password, c);

				int result = JOptionPane.showConfirmDialog(null, passwordPanel, "Password", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					try {
						passwords=password.getText();
						saved();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					NOTsaved();

				}
			}
			public void saveMap() {
				treeMode.saveTree();
				SentenceSetStorage store = new SentenceSetStorage(grammar,menu);
				output = store.outputTXT(treeMode.sentenceTree.sentence);
				JFileChooser chooser = new JFileChooser();
				chooser.setPreferredSize(new Dimension(700,700));
				URL location = Total.class.getProtectionDomain().getCodeSource().getLocation();
				chooser.setCurrentDirectory(new File(location.getFile()));

				int retrival = chooser.showSaveDialog(null);
				if (retrival == JFileChooser.APPROVE_OPTION) {

					file =chooser.getSelectedFile();
					enterpassword();

				}
				else{
					NOTsaved();
				}
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				saveMap();
			}

		});
		//the open for default student mode
		menu.open2.addActionListener(new ActionListener(){			

			String text;


			public void studentmode(){				
				treeMode.practiceMode();
				menu.studentmode();
				//revalidate();
				SentenceSetStorage tool=new SentenceSetStorage(grammar,menu);
				tool.setTree(text,treeMode.sentenceTree.sentence,1);
				treeMode.sentenceTree.sentence.studentupdate();
				treeMode.sentenceTree.sentence.expandPath(new TreePath(treeMode.sentenceTree.sentence.root));
				treeMode.sentenceTree.sentence.setSelectedNode(treeMode.sentenceTree.sentence.instructionNode);
				//treeMode.sentenceTree.sentence.setSelectionPath(new TreePath(treeMode.sentenceTree.sentence.instructionNode));
				treeMode.sentenceTree.presentNode=treeMode.sentenceTree.sentence.instructionNode;
				treeMode.sentenceTree.selectNode();
				revalidate();
			}


			public void action(){
				JFrame f=new JFrame();
				final JDialog loading = new JDialog(f);
				JPanel p1 = new JPanel();
				p1.add(new JLabel("Loading..."));
				loading.setUndecorated(true);
				loading.getContentPane().add(p1);
				loading.pack();
				loading.setLocationRelativeTo(null);
				loading.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				loading.setModal(true);

				SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
					@Override
					protected String doInBackground() throws InterruptedException {
						/** Execute some operation */   studentmode();
						return "e";
					}
					@Override
					protected void done() {
						loading.dispose();
						treeMode.sentenceTree.sentence.setPreferredSize(treeMode.sentenceTree.sentence.getPreferredSize());
						revalidate();
					}
				};
				worker.execute();
				loading.setVisible(true);
				try {
					worker.get();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}


			public void saveMap2() {
				treeMode.saveTree();
				SentenceSetStorage store = new SentenceSetStorage(grammar,menu);
				String output = store.outputTXT3(treeMode.sentenceTree.sentence);
				JFileChooser chooser = new JFileChooser();
				URL location = Total.class.getProtectionDomain().getCodeSource().getLocation();
				chooser.setCurrentDirectory(new File(location.getFile()));
				chooser.setPreferredSize(new Dimension(700,700));
				chooser.setDialogTitle("Please designate a place to save answer in progress file");

				int retrival = chooser.showSaveDialog(null);

				if (retrival == JFileChooser.APPROVE_OPTION) {
					try {
						ans=chooser.getSelectedFile();
						FileWriter fw = new FileWriter(addSuffixConditionally(ans.toString(), ".txt"));
						fw.write(output.toString());
						fw.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}



			@Override
			public void actionPerformed(ActionEvent e) {
				treeMode.teacherPanel.clean.doClick();
				JFrame f = new JFrame();
				JFileChooser fileChooser = new JFileChooser();
				URL location = Total.class.getProtectionDomain().getCodeSource().getLocation();
				fileChooser.setCurrentDirectory(new File(location.getFile()));
				fileChooser.setPreferredSize(new Dimension(700,700));

				int result = fileChooser.showOpenDialog(f);
				if (result == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile();
					try {
						byte bt[] = Files.readAllBytes(file.toPath());
						String en = new String(bt, "UTF-8");
						StrongAES key=new StrongAES();
						if(!file.getName().contains(".txt")){
							String input=key.decrypt(en);
							text=input;
						}else{
							text=en;
						}

						if(key.containPassword(text)){
							universalpassword=key.getpassword(text);
							text=key.getTextWithoutPassword(text);
						}else{
							universalpassword="";
						}

						action();
						saveMap2();

					} catch (IOException ex) {

					}

				}

			}



		});

		menu.openanswerp.addActionListener(new ActionListener(){


			String text;


			public void studentmode(){				
				treeMode.practiceMode();
				menu.studentmode();
				//revalidate();
				SentenceSetStorage tool=new SentenceSetStorage(grammar,menu);
				tool.setTree(text,treeMode.sentenceTree.sentence,2);
				treeMode.sentenceTree.sentence.studentupdate();
				treeMode.sentenceTree.sentence.expandPath(new TreePath(treeMode.sentenceTree.sentence.root));
				treeMode.sentenceTree.sentence.setSelectedNode(treeMode.sentenceTree.sentence.instructionNode);
				treeMode.sentenceTree.presentNode=treeMode.sentenceTree.sentence.instructionNode;
				treeMode.sentenceTree.selectNode();
				revalidate();
			}


			public void action(){


				JFrame f=new JFrame();
				final JDialog loading = new JDialog(f);
				JPanel p1 = new JPanel();
				p1.add(new JLabel("Loading..."));
				loading.setUndecorated(true);
				loading.getContentPane().add(p1);
				loading.pack();
				loading.setLocationRelativeTo(null);
				loading.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				loading.setModal(true);

				SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
					@Override
					protected String doInBackground() throws InterruptedException {
						/** Execute some operation */   studentmode();
						return "e";
					}
					@Override
					protected void done() {
						loading.dispose();
						treeMode.sentenceTree.sentence.setPreferredSize(treeMode.sentenceTree.sentence.getPreferredSize());
						revalidate();
					}
				};
				worker.execute();
				loading.setVisible(true);
				try {
					worker.get();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}



			@Override
			public void actionPerformed(ActionEvent e) {
				treeMode.teacherPanel.clean.doClick();
				JFrame f = new JFrame();
				JFileChooser fileChooser = new JFileChooser();
				URL location = Total.class.getProtectionDomain().getCodeSource().getLocation();
				fileChooser.setCurrentDirectory(new File(location.getFile()));
				fileChooser.setPreferredSize(new Dimension(700,700));

				int result = fileChooser.showOpenDialog(f);
				if (result == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile();
					try {
						byte bt[] = Files.readAllBytes(file.toPath());
						String en = new String(bt, "UTF-8");
						StrongAES key=new StrongAES();
						if(!file.getName().contains(".txt")){
							String input=key.decrypt(en);
							text=input;
						}else{
							text=en;
						}

						if(key.containPassword(text)){
							universalpassword=key.getpassword(text);
							text=key.getTextWithoutPassword(text);
						}else{
							universalpassword="";
						}

						action();

					} catch (IOException ex) {

					}

				}

			}




		});


		menu.open.addActionListener(new ActionListener(){			
			String passwords;
			String text;
			public void teachermode(){
				treeMode.teacherMode();
				menu.teachermode();

				permission="teacher";
				SentenceSetStorage tool=new SentenceSetStorage(grammar,menu);
				tool.setTree(text,treeMode.sentenceTree.sentence,1);
				treeMode.sentenceTree.sentence.teacherupdate();
				treeMode.sentenceTree.sentence.expandPath(new TreePath(treeMode.sentenceTree.sentence.root));
				revalidate();
			}

			public void studentmode(){				
				treeMode.practiceMode();
				menu.studentmode();
				//revalidate();
				SentenceSetStorage tool=new SentenceSetStorage(grammar,menu);
				tool.setTree(text,treeMode.sentenceTree.sentence,1);
				treeMode.sentenceTree.sentence.studentupdate();
				treeMode.sentenceTree.sentence.expandPath(new TreePath(treeMode.sentenceTree.sentence.root));
				treeMode.sentenceTree.sentence.setSelectionPath(new TreePath(treeMode.sentenceTree.sentence.instructionNode));
				treeMode.sentenceTree.presentNode=treeMode.sentenceTree.sentence.instructionNode;
				treeMode.sentenceTree.selectNode();
				revalidate();
			}

			public void enterPassword(){
				final JPanel modePanel=new JPanel();
				modePanel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				final JTextField password=new JTextField();
				c.gridx = 0;
				c.gridy = 0;
				c.gridwidth=2;
				c.fill = GridBagConstraints.BOTH;
				modePanel.add(new JLabel("With password:"), c);

				final JButton student=new JButton("Student Mode");
				c.gridx = 1;
				c.gridy = 2;
				c.fill = GridBagConstraints.BOTH;
				c.gridwidth=1;
				modePanel.add(student, c);

				c.gridx = 0;
				c.gridy = 1;
				c.fill = GridBagConstraints.BOTH;
				c.gridwidth=1;
				modePanel.add(password, c);

				final JButton teacher=new JButton("Teacher Mode");
				c.gridx = 1;
				c.gridy = 1;
				c.fill = GridBagConstraints.BOTH;
				c.gridwidth=1;
				modePanel.add(teacher, c);




				c.gridx = 0;
				c.gridy = 2;
				c.fill = GridBagConstraints.BOTH;
				c.gridwidth=1;
				modePanel.add(new JLabel("Without password:"), c);
				teacher.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						if(passwords.equals(password.getText())){
							text=new StrongAES().getTextWithoutPassword(text);
							teachermode();
							Window w = SwingUtilities.getWindowAncestor(teacher);
							if (w != null) {
								w.setVisible(false);
							}
						}else{
							JOptionPane.showMessageDialog(null, new JLabel("password not correct, please enter again or choose student mode"));;
						}


					}

				});

				student.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						text=new StrongAES().getTextWithoutPassword(text);
						studentmode();
						Window w = SwingUtilities.getWindowAncestor(student);
						if (w != null) {
							w.setVisible(false);
						}

					}

				});


				JOptionPane.showOptionDialog(null, 
						modePanel, 
						"Mode Chooser", 
						JOptionPane.OK_CANCEL_OPTION, 
						JOptionPane.INFORMATION_MESSAGE, 
						null, 
						new String[]{"Cancel"}, // this is the array
						"default");




			}


			public void simpleModeChoose(){

				final JPanel modePanel=new JPanel();
				modePanel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();


				final JButton student=new JButton("Student Mode");
				c.gridx = 0;
				c.gridy = 0;
				c.fill = GridBagConstraints.BOTH;
				c.gridwidth=1;
				modePanel.add(student, c);


				final JButton teacher=new JButton("Teacher Mode");
				c.gridx = 1;
				c.gridy = 0;
				c.fill = GridBagConstraints.BOTH;
				c.gridwidth=1;
				modePanel.add(teacher, c);



				teacher.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						teachermode();
						Window w = SwingUtilities.getWindowAncestor(teacher);
						if (w != null) {
							w.setVisible(false);
						}

					}

				});

				student.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						Window w = SwingUtilities.getWindowAncestor(student);
						if (w != null) {
							w.setVisible(false);
						}
						JFrame f=new JFrame();
						final JDialog loading = new JDialog(f);
						JPanel p1 = new JPanel();
						p1.add(new JLabel("Loading..."));
						loading.setUndecorated(true);
						loading.getContentPane().add(p1);
						loading.pack();
						loading.setLocationRelativeTo(null);
						loading.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
						loading.setModal(true);

						SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
							@Override
							protected String doInBackground() throws InterruptedException {
								/** Execute some operation */   studentmode();
								return "e";
							}
							@Override
							protected void done() {
								loading.dispose();
								treeMode.sentenceTree.sentence.setPreferredSize(treeMode.sentenceTree.sentence.getPreferredSize());
								revalidate();
							}
						};
						worker.execute();
						loading.setVisible(true);
						try {
							worker.get();
						} catch (Exception e1) {
							e1.printStackTrace();
						}



					}

				});


				JOptionPane.showOptionDialog(null, 
						modePanel, 
						"Mode Chooser", 
						JOptionPane.OK_CANCEL_OPTION, 
						JOptionPane.INFORMATION_MESSAGE, 
						null, 
						new String[]{"Cancel"}, // this is the array
						"default");





			}



			@Override
			public void actionPerformed(ActionEvent e) {
				treeMode.teacherPanel.clean.doClick();
				JFrame f = new JFrame();
				JFileChooser fileChooser = new JFileChooser();
				URL location = Total.class.getProtectionDomain().getCodeSource().getLocation();
				fileChooser.setCurrentDirectory(new File(location.getFile()));
				fileChooser.setPreferredSize(new Dimension(700,700));

				int result = fileChooser.showOpenDialog(f);
				if (result == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile();
					try {
						byte bt[] = Files.readAllBytes(file.toPath());
						String en = new String(bt, "UTF-8");
						StrongAES key=new StrongAES();
						if(!file.getName().contains(".txt")){
							String input=key.decrypt(en);
							text=input;
						}else{
							text=en;
						}

						if(key.containPassword(text)){
							passwords=key.getpassword(text);
							enterPassword();
						}else{
							simpleModeChoose();
						}

					} catch (IOException ex) {

					}

				}

			}



		});

		menu.edit.addMouseMotionListener(new MouseAdapter(){
			@Override
			public void mouseMoved(MouseEvent e){
				menu.undo.setEnabled(plain.canUndo());
				menu.redo.setEnabled(plain.canRedo());
			}
		});

		menu.redo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(plain.canRedo()){
					plain.redo();
				}
			}

		});


		menu.undo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(plain.canUndo()){
					plain.undo();
				}
			}

		});
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK),
				"Undo Pressed");
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK),
				"Redo Pressed");
		getActionMap().put("Undo Pressed", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if(plain.canUndo()){
					plain.undo();
				}
			}
		});
		getActionMap().put("Redo Pressed", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if(plain.canRedo()){
					plain.redo();
				}
			}
		});
	}
	private void changeTreeFont(Font font){

		String style=font.getFontName();
		int size=font.getSize();

		//set preference's SystemFontSize and FontStyle
		pp.setInt("TreeSize", size);
		pp.setString("TreeFontStyle", style);

		//change the font size of the display
		updateTreeFonts();
	}
	private void changeSystemFont(Font font){

		String style=font.getFontName();
		int size=font.getSize();

		//set preference's SystemFontSize and FontStyle
		pp.setInt("SystemFontSize", size);
		pp.setString("FontStyle", style);

		//change the font size of the display
		{
			//part1. everything except drawing panel font size and menubar font size
			Stack<Component> stack=new Stack<Component>();
			for (Component comp : Total.this.getComponents()) {
				stack.add(comp);
			}
			while(stack.size()>0){
				Component now=stack.pop();
				now.setFont(font);
				for (Component comp : ((Container) now).getComponents()) {
					//avoiding changing the font size of the drawing panel
					if(!comp.equals(plain.drawroom.canvas))
						stack.add(comp);
				}

				//part2. menubar font size. because menu_items are not component of menus, so they are special
				//and a special recursive-font-size-changing function is defined in LMenuBar class
				menu.setFont(font);

			}	

		}


		//get the drawing panel to the lowest point
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				Rectangle bounds = plain.drawroom.getViewport().getViewRect();
				JScrollBar horizontal =plain.drawroom.getHorizontalScrollBar();
				JScrollBar vertical = plain.drawroom.getVerticalScrollBar();
				vertical.setValue( (vertical.getMaximum() - bounds.height)  );
			}
		});


	}


	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}

	private void createAndShowUI() {
		JFrame frame = new JFrame("UCLA-PPP Parsing Practice Program");
		Total total = Total.this;
		Toolkit tk = Toolkit.getDefaultToolkit();
		int xSize = ((int) tk.getScreenSize().getWidth()-400);
		int ySize = ((int) tk.getScreenSize().getHeight());
		frame.setPreferredSize(new Dimension(xSize, ySize));
		frame.add(total, BorderLayout.CENTER);
		setUIFont(new javax.swing.plaf.FontUIResource(pp.getString("FontStyle"), Font.PLAIN, pp.getInt("SystemFontSize")));
		frame.setJMenuBar(menu);
		setUIFont(new javax.swing.plaf.FontUIResource(pp.getString("TreeFontStyle"), Font.PLAIN, pp.getInt("TreeSize")));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);


		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				Rectangle bounds = plain.drawroom.getViewport().getViewRect();
				JScrollBar vertical = plain.drawroom.getVerticalScrollBar();
				vertical.setValue( (vertical.getMaximum() - bounds.height)  );
			}
		});

	}
	private void updateTreeFonts(){
		Font font = new Font(pp.getString("TreeFontStyle"), Font.PLAIN, pp.getInt("TreeSize"));

		for(NodeLabel x : plain.list){
			x.setFonts(font);
			Dimension prefSize = x.label.getPreferredSize();
			x.label.setSize(prefSize);
			x.setSize(x.getPreferredSize());
		}
		// update after we change all the fonts
		setUIFont(new javax.swing.plaf.FontUIResource(pp.getString("TreeFontStyle"), Font.PLAIN, pp.getInt("TreeSize")));
		plain.adjust();
	}
	public static void main(String[] args) {
		setUIFont(new javax.swing.plaf.FontUIResource(pp.getString("FontStyle"), Font.PLAIN, pp.getInt("SystemFontSize")));
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				final Total t=new Total();
				t.createAndShowUI();
			}
		});
	}

	private static String addSuffixConditionally(String input,String suffix) {
		return input.endsWith(suffix) ? input : input + suffix;
	}

}
