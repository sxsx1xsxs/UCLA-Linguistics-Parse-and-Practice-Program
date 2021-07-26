package program.mainUI.drawingPanel;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

public class Arrow extends Line{
    static int BOTTOM_PAD=20;
    @Override
    public void paintSelf(Graphics2D g){
        if(parent == null || children == null) {
            return;
        }
        BasicStroke stroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        g.setStroke(stroke);
        // black plain form
        if (color == 0) {
            g.setStroke(new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
            g.setColor(Color.black);
        }
        // orange bold ready form
        else if (color == 3) {
            g.setStroke(new BasicStroke(3.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
            g.setColor(Color.decode("0x994C00"));
        } else if (color == 6) {
            g.setStroke(new BasicStroke(3.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
            g.setColor(Color.decode("0xFF66FF"));
        }
        // blue bold chosen form
        else {
            g.setStroke(new BasicStroke(2.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
            g.setColor(Color.blue);
        }
        // roadmap for the lines we draw
        // 1. a straight line down to our lowest point
        // 2. a curved line from our lowest point down to lowest point + padding
        // 3. another straight line up to the middle
        // all locations are understood to be at the *bottom middle* of the node
        int parentX = parent.location.x + parent.getWidth()/2;
        int parentY = parent.location.y + parent.getHeight();
        int childrenX = children.location.x + children.getWidth()/2;
        int childrenY = children.location.y + children.getHeight();
        int minY = parentY > childrenY ? parentY : childrenY;
        start = new Point(parentX,parentY);
        end = new Point(parentX,minY + BOTTOM_PAD/2);
        Path2D.Float path = new Path2D.Float();
        path.moveTo(parentX,parentY);
        path.lineTo(parentX,minY + BOTTOM_PAD/2);
        path.quadTo((parentX+childrenX)/2,minY+BOTTOM_PAD,childrenX,minY+BOTTOM_PAD/2);
        path.lineTo(childrenX,childrenY);
        g.draw(path);
        drawArrowHead(g,new Point(childrenX,childrenY), new Point(childrenX,minY + BOTTOM_PAD));
    }
    // function from https://coderanch.com/t/340443/java/Draw-arrow-head-line
    // minor modifications to remove color customization
    private void drawArrowHead(Graphics2D g2, Point tip, Point tail)
    {
        double phi = Math.toRadians(40);
        double barb = 10;
        double dy = tip.y - tail.y;
        double dx = tip.x - tail.x;
        double theta = Math.atan2(dy, dx);
        //System.out.println("theta = " + Math.toDegrees(theta));
        double x, y, rho = theta + phi;
        for(int j = 0; j < 2; j++)
        {
            x = tip.x - barb * Math.cos(rho);
            y = tip.y - barb * Math.sin(rho);
            g2.draw(new Line2D.Double(tip.x, tip.y, x, y));
            rho = theta - phi;
        }
    }
}
