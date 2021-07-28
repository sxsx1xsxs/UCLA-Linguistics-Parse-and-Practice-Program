package program.mainUI.drawingPanel;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class Arrow extends Line{
    @Override
    public Line makeCopy(Plain p){
        return new Arrow(p);
    }
    public Arrow(){

    }
    public Arrow(Plain p){
        attachTo(p);
    }
    static int BOTTOM_PAD=20;
    @Override
    public void paintSelf(Graphics2D g){
        if(parent == null || children == null) {
            plain.removeline(this);
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
        int parentX;
        int parentY;
        if(parent.children.isEmpty() || parent.grammarType==1){
            //draw from the bottom if we have no children
            parentX = parent.location.x + parent.getWidth()/2;
            parentY = parent.location.y + parent.getHeight();
        } else {
            //draw from the side if not
            parentX = parent.location.x;
            if(parent.location.x > children.location.x){
                parentX+=parent.getWidth(); // if we are to the right of the child, it needs to come out on the right side
            }
            parentY = parent.location.y + parent.getHeight()/2;
        }
        // the same thing again but for the children
        int childrenX;
        int childrenY;
        if(children.children.isEmpty() || children.grammarType==1){
            //draw from the bottom if we have no children
            childrenX = children.location.x + children.getWidth()/2;
            childrenY = children.location.y + children.getHeight();
        } else {
            //draw from the side if not
            childrenX = children.location.x;
            if(children.location.x > parent.location.x){
                childrenX+=children.getWidth(); // if we are to the right of the child, it needs to come out on the right side
            }
            childrenY = children.location.y + children.getHeight()/2;
        }
        int lowX = parent.location.x < children.location.x ? parent.upperLeftCorner().x : children.upperLeftCorner().x;
        int highX = parent.location.x > children.location.x ? parent.lowerRightCorner().x : children.lowerRightCorner().x;
        int minY = plain.lowestPointBetweenTwoX(lowX,highX);
        final int radius = 3;
        Path2D.Float path = new Path2D.Float();
        path.moveTo(parentX,parentY);
        // determine if we need to draw an extra segment to go out to the right
        int leftSegment;
        if(parent.children.isEmpty() || parent.grammarType==1){
            leftSegment = parentX;
        } else {
            leftSegment = parentX < childrenX ? parent.upperLeftCorner().x : parent.lowerRightCorner().x;
            int dist = leftSegment-parentX;
            path.curveTo(parentX + dist/3,parentY - BOTTOM_PAD,parentX + (2*dist)/2,parentY - BOTTOM_PAD/2,leftSegment,parentY);
        }
        start = new Point(leftSegment,parentY);
        end = new Point(leftSegment,minY + BOTTOM_PAD/2);
        path.lineTo(leftSegment,minY + BOTTOM_PAD/2);
        int rightSegment;
        if(children.children.isEmpty() || children.grammarType == 1){
            rightSegment = childrenX;
        } else {
            rightSegment = childrenX < parentX ? children.upperLeftCorner().x : children.lowerRightCorner().x;
        }
        path.quadTo((parentX+rightSegment)/2,minY+BOTTOM_PAD,rightSegment,minY+BOTTOM_PAD/2);
        path.lineTo(rightSegment,childrenY);
        if(rightSegment!=childrenX){
            int dist = rightSegment - childrenX;
            path.curveTo(childrenX + dist/3,childrenY - BOTTOM_PAD,childrenX + (2*dist)/2,childrenY - BOTTOM_PAD/2,childrenX,childrenY);
            drawArrowHead(g,new Point(childrenX,childrenY), new Point(rightSegment,childrenY));
        } else {
            drawArrowHead(g,new Point(childrenX,childrenY), new Point(childrenX,minY + BOTTOM_PAD));
        }
        g.draw(path);
        g.setColor(Color.red);
        g.fillOval(start.x-radius,start.y,radius*2,radius*2);
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
