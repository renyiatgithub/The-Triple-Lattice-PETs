package hexagonalpets;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;


public class RotationAxis extends ScaleCanvas implements MouseListener,
        MouseMotionListener, KeyListener {

    Point JX;
    Complex pt;
    PolygonWrapper rect;
    static double move, theta;
    static CanvasGraph G;

    public void setCanvasGraph(CanvasGraph cg) {
        this.G = cg;
    }
    
    public RotationAxis() {
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setScales(0, 0, 1);

        G = new CanvasGraph();
        pt = new Complex();
        move = 200;
        rect = new PolygonWrapper();
        theta = 0;
    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);
        g.drawString("-pi", 20, 70);
        g.drawString("pi", 370, 70);
        g.drawString("'r'- reset",170,70);

        PolygonWrapper[] frame = new PolygonWrapper[3];
        frame[0] = new PolygonWrapper();
        frame[0].count = 2;
        frame[0].z[0] = new Complex(20, 40);
        frame[0].z[1] = new Complex(380, 40);

        frame[1] = new PolygonWrapper();
        frame[1].count = 2;
        frame[1].z[0] = new Complex(20, 30);
        frame[1].z[1] = new Complex(20, 50);

        frame[2] = BasicPolygon.move(frame[1], new Complex(360, 0));

        for (int i = 0; i < 3; i++) {
            paint_frame(g, frame[i], Color.white, 2);
        }
        rect.count = 2;
        rect.z[0] = new Complex(move, 0);
        rect.z[1] = new Complex(move, 80);
        paint_frame(g, rect, Color.yellow, 1);
        g.drawString(Double.toString(theta / Math.PI), (int) move, 30);
        g.drawString("*pi", (int) move, 60);
    }

    public void mouseClicked(MouseEvent e) {
        MouseData J = MouseData.process(e);
        JX = J.X;
        pt = unTransform(J.X);
    }

    public void mouseDragged(MouseEvent e) {
        MouseData J = MouseData.process(e);

        JX = new Point(J.X);
        if (JX.x < 380 && JX.x > 20) {
            move = JX.x;
            theta = -Math.PI + 2 * Math.PI * (move - 20) / 360;
            G.GraphRotation();
            G.repaint();
        }
        repaint();
    }

    public void mouseMoved(MouseEvent e) {
        MouseData J = MouseData.process(e);
        JX = new Point(J.X);
    }

    public void mouseEntered(MouseEvent e) {
        requestFocus();
    }

    public void doMouseClick(int mode) {
        if (mode == 1) {
            move = 200;
            theta = 0;
            G.GraphRotation();
            G.repaint();
        }

        repaint();
    }

    public void paint_frame(Graphics2D g, PolygonWrapper P, Color c, int n) {
        GeneralPath gp = P.toGeneralPath();
        gp = transform(gp);
        g.setColor(c);
        g.setStroke(new BasicStroke(n));
        g.draw(gp);
    }

    public void fill_frame(Graphics2D g, PolygonWrapper P, Color c) {
        GeneralPath gp = P.toGeneralPath();
        gp = transform(gp);
        g.setColor(c);
        g.fill(gp);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char ch = e.getKeyChar();
        int test = 0;
        if (ch == 'r') {
            test = 1;
        }
        if (test > 0) {
            doMouseClick(test);
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
