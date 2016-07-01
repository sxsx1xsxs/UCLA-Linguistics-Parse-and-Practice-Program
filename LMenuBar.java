import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class LMenuBar extends JMenuBar{
    Grammar g;
	String grammarInput;
	JFrame f= new JFrame();
	JMenuItem importgrammar = new JMenuItem("Import Grammar");
	JMenuItem freedrawing=new JMenuItem("Free Drawing");
	JMenuItem clauseentering=new JMenuItem("Clause Entering");
	JMenuItem parse=new JMenuItem("Parse Practice");
	JMenuItem large = new JMenuItem("Large");
    JMenuItem medium = new JMenuItem("Medium");
    JMenuItem small = new JMenuItem("Small");
    JMenuItem customize=new JMenuItem("Customize");
	
	final JFileChooser fileChooser = new JFileChooser();
	JMenuItem exit;

	
	
	
	
	LMenuBar(){

   	 
   	 
        JMenu file = new JMenu("File");
        JMenu view= new JMenu("View");
        
        
        JMenu mode=new JMenu("Mode");
        JMenu treedrawing=new JMenu("Tree Drawing");
        mode.add(treedrawing);
        
        
        treedrawing.add(freedrawing);
        treedrawing.add(clauseentering);
        
        mode.add(parse);
       
//        final JFileChooser fileChooser = new JFileChooser();

        
        file.add(importgrammar);
        
      
        view.add(large);
        view.add(medium);
        view.add(small);
        view.add(customize);
        

        exit = new JMenuItem("Exit");
       // exit.setFont(font);
        file.add(exit);
        exit.setToolTipText("Exit application");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

       

       
       super.add(file);
       super.add(view);
       super.add(mode);
       super.setBackground(Color.CYAN);
       super.setBorder(new MatteBorder(0,0,1,0,Color.black));
      
   
	}
	
}
