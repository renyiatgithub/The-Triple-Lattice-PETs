package hexagonalpets;



import java.applet.Applet;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;

/*This is a basic class for a rectangular button button*/

public class ListenSquare {
    public double x,y,w,h;

    public ListenSquare () {}

    public ListenSquare(double x,double y,double w,double h) {
	this.x=x;
	this.y=y;
	this.w=w;
	this.h=h;
    }

    public boolean inside(Point p) {
	if((p.x>x)&&(p.x<x+w)&&(p.y>y)&&(p.y<y+h)) return(true);
	return(false);
    }

    public void render(Graphics g,Color C) {
	g.setColor(C);
	g.fillRect((int)(x),(int)(y),(int)(w),(int)(h));
	g.setColor(Color.white);
	g.drawRect((int)(x),(int)(y),(int)(w),(int)(h));
    }
    
        public void render1(Graphics g,Color C) {
	g.setColor(C);
	g.fillRect((int)(x),(int)(y),(int)(w),(int)(h));
        g.setColor(Color.darkGray);
	g.drawRect((int)(x),(int)(y),(int)(w),(int)(h));
    }
}
