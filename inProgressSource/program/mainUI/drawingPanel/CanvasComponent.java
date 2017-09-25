package program.mainUI.drawingPanel;

import java.awt.Point;

public interface CanvasComponent {
	//delete from the canvas
	public void removeFromCanvas();
	//show the right click menu on canvas for that component
	public void showRightClickMenu(Point location);
	//change the component to chosen form
	public void chosen();
	//change the component to default form
	public void setToDefault();
}
