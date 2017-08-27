package hexagonalpets;


import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.geom.*;
import java.math.*;
import java.io.*;



public class Polyhedron {
    Vector[] V=new Vector[1000];
    int count;
    int orbit; 


    public Polyhedron() {}

    public Polyhedron(Polyhedron P) {
	this.count=P.count;
	for(int i=0;i<count;++i) V[i]=new Vector(P.V[i]);
    }
    public void print() {
	for(int i=0;i<count;++i) {
	    V[i].print();
	}
    }

    public Vector center() {
	Vector W=new Vector();
	for(int i=0;i<count;++i) {
	    for(int j=0;j<3;++j) {
		W.x[j]=W.x[j]+V[i].x[j];
	    }
	}
	for(int j=0;j<3;++j) W.x[j]=W.x[j]/count;
	return(W);
    }



}
