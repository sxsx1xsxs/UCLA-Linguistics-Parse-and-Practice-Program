package program.mainUI.drawingPanel.topComponents;

import java.awt.Point;

import javax.swing.JLabel;

//Labels that can be transferred to Top and moved around on Top
public class TopLabel extends JLabel implements TopMovable{
	
	public TopLabel(String text) {
		super(text);
		this.setSize(this.getPreferredSize());
	}

	@Override
	public void adjustLocation(Point x) {
		this.setLocation(x);
	}

	@Override
	public void changeByMouse(Point x) {
		Point location=new Point(x.x-this.getSize().width/2,x.y-this.getSize().height/2);
		this.setLocation(location);		
	}
}
