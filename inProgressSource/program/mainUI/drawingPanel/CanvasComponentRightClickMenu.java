package program.mainUI.drawingPanel;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class CanvasComponentRightClickMenu extends JPopupMenu{
	CanvasComponent owner;
	JMenuItem delete = new JMenuItem("delete");
	
	
	public CanvasComponentRightClickMenu (CanvasComponent x){
		owner=x;
		CanvasComponentRightClickMenu.this.add(delete);
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				owner.removeFromCanvas();
			}
		});
		
		
		
		if(x instanceof CanvasLine){
			
		}else if (x instanceof CanvasNode){
			
		}
	}
	
	
	public void showRightClickMenu(Component container, Point location){
		CanvasComponentRightClickMenu.this.show(container, location.x, location.y);
	}
	
}
