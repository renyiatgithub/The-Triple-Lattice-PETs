package hexagonalpets;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;


public class RotationAxis_FB extends ScaleCanvas implements MouseListener,
        MouseMotionListener, KeyListener {

    Point JX;
    Complex pt;
    PolygonWrapper rect;
    static double move, s;
    static CanvasFiberBundle FB;

    public void setFiberBundle(CanvasFiberBundle fb) {
        this.FB = fb;
    }

    public RotationAxis_FB() {
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setScales(0, 0, 1);

        FB = new CanvasFiberBundle();
        pt = new Complex();
        move = 20;
        rect = new PolygonWrapper();
        s = 0;
    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);
        g.drawString("0", 20, 70);
        g.drawString("1", 470, 70);

        PolygonWrapper[] frame = new PolygonWrapper[3];
        frame[0] = new PolygonWrapper();
        frame[0].count = 2;
        frame[0].z[0] = new Complex(20, 40);
        frame[0].z[1] = new Complex(480, 40);

        frame[1] = new PolygonWrapper();
        frame[1].count = 2;
        frame[1].z[0] = new Complex(20, 30);
        frame[1].z[1] = new Complex(20, 50);

        frame[2] = BasicPolygon.move(frame[1], new Complex(460, 0));

        for (int i = 0; i < 3; i++) {
            paint_frame(g, frame[i], Color.white, 2);
        }
        rect.count = 2;
        rect.z[0] = new Complex(move, 0);
        rect.z[1] = new Complex(move, 80);
        paint_frame(g, rect, Color.yellow, 1);
        g.drawString(Double.toString(s/2), (int) move, 30);

    }

    public void mouseClicked(MouseEvent e) {
        MouseData J = MouseData.process(e);
        JX = J.X;
    }

    public static void Slice(double s) {
        s = Main.value;
        move = (s) * 230 + 20;
        FB.doSlice();
        FB.repaint();
    }

    public void mouseDragged(MouseEvent e) {
        MouseData J = MouseData.process(e);
        JX = new Point(J.X);
        if (JX.x < 480 && JX.x > 20) {
            move = JX.x;
            s = (move - 20) / 230;
            FB.doSlice();
            FB.repaint();
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
