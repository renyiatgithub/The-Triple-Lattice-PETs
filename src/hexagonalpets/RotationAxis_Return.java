package hexagonalpets;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class RotationAxis_Return extends ScaleCanvas implements MouseListener,
        MouseMotionListener, KeyListener {

    Point JX;
    Complex pt;
    PolygonWrapper rect;
    static double move, s;
    static CanvasReturnDomain RD;

    public void setReturnDomain(CanvasReturnDomain rd) {
        this.RD = rd;
    }

    public RotationAxis_Return() {
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setScales(0, 0, 1);

        RD = new CanvasReturnDomain();
        pt = new Complex();
        move = 20;
        rect = new PolygonWrapper();
        s = 1;
    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);
        g.drawString("1/2", 20, 70);
        g.drawString("2/3", 470, 70);

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
        g.drawString(Double.toString(s / 2), (int) move, 30);

    }

    public void InverseRenorm() {
        s = RotationAxis_Max.s;
        s = 2.0 / (1 + 1.0 * s / 2);
        move = (s - 1.0) * (230 * 6.0) + 20;
        RD.doSlice();
        RD.repaint();
    }

    public void mouseClicked(MouseEvent e) {
        MouseData J = MouseData.process(e);
        JX = J.X;
    }

    public static void Slice(double s) {
        s = Main.value;
        move = (s - 1.0) * (230 * 6.0) + 20;
        RD.doSlice();
        RD.repaint();
    }

    public void mouseDragged(MouseEvent e) {
        MouseData J = MouseData.process(e);
        JX = new Point(J.X);
        if (JX.x < 480 && JX.x > 20) {
            move = JX.x;
            s = (move - 20) / (230 * 6.0) + 1.0;
            RD.doSlice();
            RD.repaint();
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
