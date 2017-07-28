package program.Interface;

import java.awt.Component;
import java.awt.image.BufferedImage;

public class ScreenShot {

	
	 public static BufferedImage getScreenShot(
			    Component component) {

			    BufferedImage image = new BufferedImage(
			      component.getWidth(),
			      component.getHeight(),
			      BufferedImage.TYPE_INT_RGB
			      );
			    // call the Component's paint method, using
			    // the Graphics object of the image.
			    component.paint( image.getGraphics() ); // alternately use .printAll(..)
			    return image;
			  }
}
