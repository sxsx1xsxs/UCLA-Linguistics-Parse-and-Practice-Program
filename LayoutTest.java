import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//import GrammarPanel.Difflabel;

/** @see http://stackoverflow.com/questions/8281886 */
public class LayoutTest extends JLabel {

    private static int SIZE = 40;
    private BufferedImage image;

    //constructor
    LayoutTest(String string, int size) {
        super(string);
        super.setBackground(Color.yellow);
        SIZE=size;
        image = createImage(super.getText());
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        image = createImage(super.getText());
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(image.getWidth() / 2, image.getHeight() / 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }

    private BufferedImage createImage(String label) {
        Font font = new Font(Font.SERIF, Font.PLAIN, SIZE);
        FontRenderContext frc = new FontRenderContext(null, true, true);
        TextLayout layout = new TextLayout(label, font, frc);
        Rectangle r = layout.getPixelBounds(null, 0, 0);
        BufferedImage bi = new BufferedImage(
            r.width + 1, r.height + 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) bi.getGraphics();
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        g2d.setColor(getForeground());
        layout.draw(g2d, 0, -r.y);
        g2d.dispose();
        return bi;
    }

    private static void display() {
        JFrame f = new JFrame("LayoutTest");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel back=new JPanel(new GridBagLayout());
        f.add(back);
        back.setPreferredSize(new Dimension(700,700));
        back.setBackground(Color.red);
        LayoutTest x=new LayoutTest("wo",1000);
        JLabel y=(JLabel) (x);
        GridBagConstraints c1=new GridBagConstraints();
        c1.gridx=0;
        c1.gridy=0;
        c1.gridheight=1;
        c1.fill=GridBagConstraints.BOTH;
       //
        //x.setBackground(Color.blue);
       // x.setSize(600,600);
        back.add(x,c1);
        
        JLabel xy=new JLabel("hhah");
        GridBagConstraints c2=new GridBagConstraints();
        c2.gridx=0;
        c2.gridy=1;
        back.add(xy,c2);

        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                display();
            }
        });
    }
}