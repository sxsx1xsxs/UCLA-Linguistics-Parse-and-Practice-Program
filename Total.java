import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Stack;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.tree.TreePath;

public class Total extends JPanel {
	//can be "" or "teacher"
	String permission="";
	Grammar2 grammar = new Grammar2();
	final LMenuBar menu = new LMenuBar();
	final Type type = new Type();
	final Type font = new Type();
	final Int size = new Int();
	Plain plain = new Plain(grammar);
	TreeMode treeMode = new TreeMode(plain);
	//Grammar grammar1=new Grammar();
	//Interface.WindowL clause = new Interface().new WindowL(grammar1, menu);
	

	Preference pp=new Preference();
	File file;
	String universalpassword="";
	File ans;

	 


	static class Type {
		String type = "";

		public void change(String x) {
			type = x;
		}
	}

	static class Int {
		int type = 0;

		public void change(int x) {
			type = x;
		}
	}

	public Total() {
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		add(treeMode, c);
		treeMode.practiceMode();
		menu.studentmode();
		
		//treeMode.teacherMode();
		//menu.teachermode();
		
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
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				treeMode.drawingPanel.turnonRC();
				menu.turnonRC.setEnabled(false);
				menu.turnoffRC.setEnabled(true);
			}
			
		});
		
		menu.format.addMouseMotionListener(new MouseAdapter(){
			
			public void mouseMoved(MouseEvent e){
				System.out.println("spacing hit");
				// TODO Auto-generated method stub
				if(treeMode.drawingPanel.completeTree()){
					System.out.println("true");
					menu.topdown.setEnabled(true);
					menu.bottomup.setEnabled(true);
				}else{
					System.out.println("false");
					menu.topdown.setEnabled(false);
					menu.bottomup.setEnabled(false);
				}
			}
		});
		
		menu.large.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				int input=pp.getInt("SystemFontSize")+2;
				pp.setInt("SystemFontSize", input);
				setUIFont(new javax.swing.plaf.FontUIResource(pp.getString("FontStyle"), Font.PLAIN, pp.getInt("SystemFontSize")));
				Font font = new Font(pp.getString("FontStyle"), Font.PLAIN, pp.getInt("SystemFontSize"));
				Stack<Component> hi=new Stack<Component>();
				for (Component comp : Total.this.getComponents()) {
				    hi.add(comp);
				}
				for(Component comp : menu.getComponents()){
					hi.add(comp);
				}
				menu.setLFont(font);
				while(hi.size()>0){
					Component now=hi.pop();
					now.setFont(font);
					for (Component comp : ((Container) now).getComponents()) {
					    hi.add(comp);
					}
					
					
				}
				
				SwingUtilities.invokeLater(new Runnable()
				{
				    public void run()
				    {System.out.println(10);
				    	System.out.println("i am here scrollbar");
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
				// TODO Auto-generated method stub

				int input=pp.getInt("SystemFontSize")-2;
				pp.setInt("SystemFontSize", input);
				setUIFont(new javax.swing.plaf.FontUIResource(pp.getString("FontStyle"), Font.PLAIN, pp.getInt("SystemFontSize")));
				Font font = new Font(pp.getString("FontStyle"), Font.PLAIN, pp.getInt("SystemFontSize"));
				Stack<Component> hi=new Stack<Component>();
				for (Component comp : Total.this.getComponents()) {
				    hi.add(comp);
				}
				for(Component comp : menu.getComponents()){
					hi.add(comp);
				}
				menu.setLFont(font);
				while(hi.size()>0){
					Component now=hi.pop();
					now.setFont(font);
					for (Component comp : ((Container) now).getComponents()) {
					    hi.add(comp);
					}
					
					
				}
				
				SwingUtilities.invokeLater(new Runnable()
				{
				    public void run()
				    {System.out.println(10);
				    	System.out.println("i am here scrollbar");
				        Rectangle bounds = plain.drawroom.getViewport().getViewRect();
				        JScrollBar horizontal =plain.drawroom.getHorizontalScrollBar();
				        JScrollBar vertical = plain.drawroom.getVerticalScrollBar();
				        vertical.setValue( (vertical.getMaximum() - bounds.height)  );
				    }
				});
			
			}
			
		});
		
	
		
		menu.wi.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(2);
				// TODO Auto-generated method stub
				treeMode.drawingPanel.widthIndex+=0.3;
				pp.setDouble("WidthIndex",treeMode.drawingPanel.widthIndex);
				treeMode.drawingPanel.adjust();
			}
			
		});
		
		menu.hi.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(3);
				treeMode.drawingPanel.heightIndex+=0.3;
				pp.setDouble("HeightIndex",treeMode.drawingPanel.heightIndex);
				treeMode.drawingPanel.adjust();
			}
			
		});
		
		
		menu.freedrawing.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				treeMode.freedrawingMode();
				revalidate();
				menu.parse.setEnabled(true);
				menu.freedrawing.setEnabled(false);
			}
			
		});
		

		menu.parse.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				treeMode.practiceMode();
				revalidate();
				menu.parse.setEnabled(false);
				menu.freedrawing.setEnabled(true);
			}
			
		});
		
		
		menu.hd.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println(4);
				treeMode.drawingPanel.heightIndex-=0.3;
				pp.setDouble("HeightIndex",treeMode.drawingPanel.heightIndex);
				treeMode.drawingPanel.adjust();
			}
			
		});
		
		menu.wd.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println(5);
				treeMode.drawingPanel.widthIndex-=0.3;
				pp.setDouble("WidthIndex",treeMode.drawingPanel.widthIndex);
				treeMode.drawingPanel.adjust();
			}
			
		});
		
		menu.topdown.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println(6);
				treeMode.drawingPanel.topDown();
			}
			
		});
		
		menu.bottomup.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println(7);
				treeMode.drawingPanel.bottomUp();;
			}
			
		});
		
		
		menu.customize.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println(8);
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
					System.out.println(9);
					String i=fbox.getSelectedFontName();
					pp.setString("FontStyle",i);
					System.out.println(i);
					int input=Integer.parseInt(box.getSelectedItem().toString());
					System.out.println(input);
					pp.setInt("SystemFontSize", input);
					setUIFont(new javax.swing.plaf.FontUIResource(pp.getString("FontStyle"), Font.PLAIN, pp.getInt("SystemFontSize")));
					Font font = new Font(pp.getString("FontStyle"), Font.PLAIN, pp.getInt("SystemFontSize"));
					Stack<Component> hi=new Stack<Component>();
					for (Component comp : Total.this.getComponents()) {
					    hi.add(comp);
					}
					for(Component comp : menu.getComponents()){
						hi.add(comp);
					}
					menu.setLFont(font);
					while(hi.size()>0){
						Component now=hi.pop();
						now.setFont(font);
						for (Component comp : ((Container) now).getComponents()) {
						    hi.add(comp);
						}
						
						
					}
					
					SwingUtilities.invokeLater(new Runnable()
					{
					    public void run()
					    {System.out.println(10);
					    	System.out.println("i am here scrollbar");
					        Rectangle bounds = plain.drawroom.getViewport().getViewRect();
					        JScrollBar horizontal =plain.drawroom.getHorizontalScrollBar();
					        JScrollBar vertical = plain.drawroom.getVerticalScrollBar();
					        vertical.setValue( (vertical.getMaximum() - bounds.height)  );
					    }
					});
				} else {
					return;

				}
				//JOptionPane.showMessageDialog(null, new JLabel("new grammar successfully imported"));
			}
			
		});
//		menu.customize.addItemListener(new FontChooserComboBox());



		menu.clauseentering.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(12);
				removeAll();
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 0;
				c.weightx = 1;
				c.weighty = 1;
				c.fill = GridBagConstraints.BOTH;
				//add(clause, c);
				revalidate();

			}
		});

		menu.teacher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(13);
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
			public void actionPerformed(ActionEvent e) {
				System.out.println(14);
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

			public void actionPerformed(ActionEvent ae) {
				System.out.println(15);
				JFrame f = new JFrame();
				JFileChooser fileChooser = new JFileChooser();
				URL location = Total.class.getProtectionDomain().getCodeSource().getLocation();
		        System.out.println(location.getFile());
		        fileChooser.setCurrentDirectory(new File(location.getFile()));
		        fileChooser.setPreferredSize(new Dimension(700,700));

				int result = fileChooser.showOpenDialog(f);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						byte bt[] = Files.readAllBytes(file.toPath());
						grammarInput = new String(bt, "UTF-8");
						pp.setString("grammar", grammarInput);
						Grammar2 newg=new Grammar2(grammarInput);
						String x = "";
						for (int i : newg.wrongLine) {
							x+="Line "+i+":"+newg.lines.elementAt(i-1).error+newg.newline;
						}
						x+="Grammar is not successfully imported";
						if (newg.wrongLine.size() == 0) {
							grammar.changeGrammar(grammarInput);
							JOptionPane.showMessageDialog(null, new JLabel("new grammar successfully imported"));;
							treeMode.drawingPanel.updateGrammar();
						} else {
							JTextArea text=new JTextArea(x);
							JScrollPane textp=new JScrollPane(text);
							JOptionPane.showMessageDialog(null,
									textp);
						}

					} catch (IOException ex) {

					}

				}
			}

		});

		menu.exportSentenceSet.addActionListener(new ActionListener() {

			public void saveMap() {
				treeMode.saveTree();
				System.out.println(16);
				SentenceSetStorage store = new SentenceSetStorage(grammar,menu);
				String output = store.outputTXT2(treeMode.sentenceTree.sentence);
				JFileChooser chooser = new JFileChooser();
				URL location = Total.class.getProtectionDomain().getCodeSource().getLocation();
		        System.out.println(location.getFile());
		        chooser.setCurrentDirectory(new File(location.getFile()));
		        chooser.setPreferredSize(new Dimension(700,700));
				int retrival = chooser.showSaveDialog(null);
				if (retrival == JFileChooser.APPROVE_OPTION) {
					try {
						FileWriter fw = new FileWriter(chooser.getSelectedFile() + ".txt");
						fw.write(output.toString());
						fw.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

			public void actionPerformed(ActionEvent e) {
				saveMap();
			}
		});
		
		menu.saveanswer.addActionListener(new ActionListener() {

			public void saveMap2() {
				treeMode.saveTree();
				System.out.println(16);
				SentenceSetStorage store = new SentenceSetStorage(grammar,menu);
				String output = store.outputTXT3(treeMode.sentenceTree.sentence);
				JFileChooser chooser = new JFileChooser();
				chooser.setPreferredSize(new Dimension(700,700));
				URL location = Total.class.getProtectionDomain().getCodeSource().getLocation();
		        System.out.println(location.getFile());
		        chooser.setCurrentDirectory(new File(location.getFile()));
		        
		        
		        
		        
				int retrival = chooser.showSaveDialog(null);
				if (retrival == JFileChooser.APPROVE_OPTION) {
					try {
						ans=chooser.getSelectedFile();
						FileWriter fw = new FileWriter(ans+ ".txt");
						fw.write(output.toString());
						fw.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

			public void actionPerformed(ActionEvent e) {
				saveMap2();
			}
		});
		
		
		menu.save.addActionListener(new ActionListener(){
			String output;
			
			public void saved() throws IOException{
				JOptionPane.showMessageDialog(null,"File successfully saved as "+file.getName()+" and "+file.getName()+".txt " +"into designated directory" );
				FileWriter fw = new FileWriter(file + ".txt");
				fw.write(output.toString());
				fw.close();
				String output2=new StrongAES().encrypt(output,universalpassword);
				FileWriter fw2 = new FileWriter(file);
				fw2.write(output2.toString());
				fw2.close();
				
			}
			

		
			public void saveMap() throws IOException {
				treeMode.saveTree();
				System.out.println(16);
				SentenceSetStorage store = new SentenceSetStorage(grammar,menu);
				output = store.outputTXT(treeMode.sentenceTree.sentence);
				saved();
			}

			public void actionPerformed(ActionEvent e) {
				try {
					saveMap();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		
		menu.savetreeas.addActionListener(new ActionListener() {
			 File file1;
			 
			 public void saveMap() {
//					treeMode.saveTree();
//					System.out.println(16);
//					SentenceSetStorage store = new SentenceSetStorage(grammar,menu);
//					output = store.outputTXT(treeMode.sentenceTree.sentence);
				 	
					JFileChooser chooser = new JFileChooser();
					URL location = Total.class.getProtectionDomain().getCodeSource().getLocation();
			        System.out.println(location.getFile());
			        chooser.setCurrentDirectory(new File(location.getFile()));
			        chooser.setPreferredSize(new Dimension(700,700));

					int retrival = chooser.showSaveDialog(null);
					if (retrival == JFileChooser.APPROVE_OPTION) {
						treeMode.drawingPanel.getlocationcor();
							file1 =chooser.getSelectedFile();
							//enterpassword();
							
							 BufferedImage image = new BufferedImage(
			            		      treeMode.drawingPanel.drawroom.back.getWidth(),
			            		      treeMode.drawingPanel.drawroom.back.getHeight(),
			            		      BufferedImage.TYPE_INT_RGB
			            		      );
			            		 
			             	treeMode.drawingPanel.drawroom.back.paint(image.getGraphics() );
			             	Rectangle rec=treeMode.drawingPanel.drawroom.getViewport().getViewRect();
			             	Plain uu=treeMode.drawingPanel;
			             	int x=rec.x;
			             	int y=rec.y;
			             	int w=rec.width;
			             	int h=rec.height;
			             	System.out.println(x+"."+y+"."+w+"."+h);
//			             	System.out.println(treeMode.drawingPanel.drawroom.getViewport().getViewRect().getX());
//			             	System.out.println(treeMode.drawingPanel.drawroom.getViewport().getViewRect().getY());
//			             	System.out.println(treeMode.drawingPanel.drawroom.getViewport().getViewRect().getWidth());
//			             	System.out.println(treeMode.drawingPanel.drawroom.getViewport().getViewRect().getHeight());
			             	    image = image.getSubimage(uu.locationx,uu.locationy,uu.locationw,uu.locationh); // 500 x 500
			              	try {
			                      // write the image as a PNG
			                      ImageIO.write(
			                        image,
			                        "png",
			                        new File(file1+".png"));
			                    } catch(Exception ex) {
			                      ex.printStackTrace();
			                    }

			               
						
					}
					else{
						//NOTsaved();
					}
				}

			
             public void actionPerformed(ActionEvent e) {
             	
             	saveMap();
             	
             };
		
		 });
		
		treeMode.teacherPanel.check.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
					treeMode.saveTree();
					System.out.println(16);
					treeMode.teacherPanel.checkanswer();
					SentenceSetStorage store = new SentenceSetStorage(grammar,menu);
					String output = store.outputTXT3(treeMode.sentenceTree.sentence);
					
							try {
								
								System.out.println("I am here");
								FileWriter fw = new FileWriter(ans + ".txt");
								fw.write(output.toString());
								fw.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
					
//					}

			}
			
			
		}
		
);
		
		menu.saveas.addActionListener(new ActionListener(){
			File file;
			String output;
			String passwords;
			
			public void saved() throws IOException{
				JOptionPane.showMessageDialog(null,"File successfully saved as "+file.getName()+" and "+file.getName()+".txt " +"into designated directory" );
				FileWriter fw = new FileWriter(file + ".txt");
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					NOTsaved();

				}
			}
			public void saveMap() {
				treeMode.saveTree();
				System.out.println(16);
				SentenceSetStorage store = new SentenceSetStorage(grammar,menu);
				output = store.outputTXT(treeMode.sentenceTree.sentence);
				JFileChooser chooser = new JFileChooser();
				chooser.setPreferredSize(new Dimension(700,700));
				URL location = Total.class.getProtectionDomain().getCodeSource().getLocation();
		        System.out.println(location.getFile());
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
			    loading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
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
					//	System.out.println("stree's preferred size"+treeMode.sentenceTree.sentence.getPreferredSize());
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
				System.out.println(16);
				SentenceSetStorage store = new SentenceSetStorage(grammar,menu);
				String output = store.outputTXT3(treeMode.sentenceTree.sentence);
				JFileChooser chooser = new JFileChooser();
				URL location = Total.class.getProtectionDomain().getCodeSource().getLocation();
		        System.out.println(location.getFile());
		        chooser.setCurrentDirectory(new File(location.getFile()));
		        chooser.setPreferredSize(new Dimension(700,700));

				int retrival = chooser.showSaveDialog(null);
				
				if (retrival == JFileChooser.APPROVE_OPTION) {
					try {
						ans=chooser.getSelectedFile();
						FileWriter fw = new FileWriter(ans + ".txt");
						fw.write(output.toString());
						fw.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

			

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				treeMode.teacherPanel.clean.doClick();
				JFrame f = new JFrame();
				JFileChooser fileChooser = new JFileChooser();
				URL location = Total.class.getProtectionDomain().getCodeSource().getLocation();
		        System.out.println(location.getFile());
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
							System.out.println("ji"+text+"lp");
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
			    loading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
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
					//	System.out.println("stree's preferred size"+treeMode.sentenceTree.sentence.getPreferredSize());
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
				// TODO Auto-generated method stub
				treeMode.teacherPanel.clean.doClick();
				JFrame f = new JFrame();
				JFileChooser fileChooser = new JFileChooser();
				URL location = Total.class.getProtectionDomain().getCodeSource().getLocation();
		        System.out.println(location.getFile());
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
							System.out.println("ji"+text+"lp");
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
					    loading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
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
							//	System.out.println("stree's preferred size"+treeMode.sentenceTree.sentence.getPreferredSize());
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
				// TODO Auto-generated method stub
				treeMode.teacherPanel.clean.doClick();
				JFrame f = new JFrame();
				JFileChooser fileChooser = new JFileChooser();
				URL location = Total.class.getProtectionDomain().getCodeSource().getLocation();
		        System.out.println(location.getFile());
		        fileChooser.setCurrentDirectory(new File(location.getFile()));
		        fileChooser.setPreferredSize(new Dimension(700,700));

				int result = fileChooser.showOpenDialog(f);
				if (result == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile();
					try {
						byte bt[] = Files.readAllBytes(file.toPath());
						String en = new String(bt, "UTF-8");
//						byte bt[] = Files.readAllBytes(file.toPath());
//						String input = new String(bt, "UTF-8");
//						SentenceSetStorage tool=new SentenceSetStorage(grammar,menu);
//						tool.setTree(input,treeMode.sentenceTree.sentence);
//						treeMode.sentenceTree.sentence.expandPath(new TreePath(treeMode.sentenceTree.sentence.root));
						StrongAES key=new StrongAES();
						if(!file.getName().contains(".txt")){
							String input=key.decrypt(en);
							text=input;
						}else{
							text=en;
							System.out.println("ji"+text+"lp");
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
	
	}

	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		System.out.println(18);
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}

	private void createAndShowUI() {
		System.out.println(19);
		JFrame frame = new JFrame("UCLA-PPP Parsing Practice Program");
		Total total = Total.this;
		Toolkit tk = Toolkit.getDefaultToolkit();
		int xSize = ((int) tk.getScreenSize().getWidth()-400);
		int ySize = ((int) tk.getScreenSize().getHeight());
		frame.setPreferredSize(new Dimension(xSize, ySize));
		frame.add(total.menu, BorderLayout.NORTH);
		frame.add(total, BorderLayout.CENTER);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		//System.out.println(pp.getPreference("SystemFontSize"));
		
		SwingUtilities.invokeLater(new Runnable()
		{
		    public void run()
		    {
		    	System.out.println(20);
		        Rectangle bounds = plain.drawroom.getViewport().getViewRect();
		        JScrollBar horizontal =plain.drawroom.getHorizontalScrollBar();
		        JScrollBar vertical = plain.drawroom.getVerticalScrollBar();
		        vertical.setValue( (vertical.getMaximum() - bounds.height)  );
		    }
		});

	}

	public static void main(String[] args) {
		System.out.println(21);
		Preference pp=new Preference();
		System.out.println(pp.getInt("SystemFontSize"));
		setUIFont(new javax.swing.plaf.FontUIResource(pp.getString("FontStyle"), Font.PLAIN, pp.getInt("SystemFontSize")));
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				final Total t=new Total();
				t.createAndShowUI();
				

			}
		});
	}

}
