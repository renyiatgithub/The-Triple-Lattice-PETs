package hexagonalpets;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.geom.*;


public class Output {
    FileOutputStream FS;
    String S;


    public Output(String S) {
	this.S=S;
	try {FS=new FileOutputStream(S);}
	catch(IOException e) {}
    }



    public void write(String S) {
	PrintStream PS=new PrintStream(FS);
	PS.print(S);
    }

    public void writeln(String S) {
	PrintStream PS=new PrintStream(FS);
	PS.println(S);
    }


    public void tclBgSet(Color C) {
	PrintStream PS=new PrintStream(FS);
        String S=tclConvert(C);
	PS.print(".plot itemconfigure bg -fill "+S);
    }


    public void lineWrite(GeneralPath X,Color C) {
	PrintStream PS=new PrintStream(FS);
	AffineTransform A=AffineTransform.getTranslateInstance(0,0);
	String S=tclConvert(C);
	PathIterator P=X.getPathIterator(A);
	double[] coords=new double[6];
	double x2=0;
	double y2=0;
	double x1=0;
	double y1=0;
	int count=0;
	while(P.isDone()==false) {
	    P.currentSegment(coords);
	    x2=x1;
	    y2=y1;
	    x1=coords[0];
	    y1=coords[1];
	    Double X1=new Double(x1);
	    Double Y1=new Double(y1);
	    Double X2=new Double(x2);
	    Double Y2=new Double(y2);
	    if(count>0) {
              PS.print(".plot create line ");
	      PS.print(X1.toString()+" ");
	      PS.print(Y1.toString()+" ");
	      PS.print(X2.toString()+" ");
	      PS.print(Y2.toString()+" ");
              PS.print("-fill "+S);
              PS.print(" -tag X");
	      PS.println("");
	      P.next();
	    }
	    ++count;
	}  
           PS.println("");
    }


    public void polyWrite(GeneralPath X,Color C) {
	polyWrite(X,C,Color.white);
    }


    public void polyWrite(GeneralPath X,Color C1,Color C2) {
	PrintStream PS=new PrintStream(FS);
	AffineTransform A=AffineTransform.getTranslateInstance(0,0);
	String S1=tclConvert(C1);	
        String S2=tclConvert(C2);
	PathIterator P=X.getPathIterator(A);
	double[] coords=new double[6];
	double x1=0;
	double y1=0;

        PS.print(".plot create polygon ");
	while(P.isDone()==false) {
	    P.currentSegment(coords);
	    x1=coords[0];
	    y1=coords[1];
	    Double X1=new Double(x1);
	    Double Y1=new Double(y1);
	    PS.print(X1.toString()+" ");
	    PS.print(Y1.toString()+" ");
            P.next();
	}
        PS.print("-fill "+S1);
        PS.print(" -outline "+S2);
        PS.print(" -tag X");
	PS.println("");
        PS.println("");
    }

    public String tclConvert(Color C) {
	int r=C.getRed();
	int g=C.getGreen();
	int b=C.getBlue();
	r=r/16;
	g=g/16;
	b=b/16;
	String[] hex={"a","b","c","d","e","f"};
	Integer R=new Integer(r);
	Integer G=new Integer(g);
	Integer B=new Integer(b);
	String RR=R.toString();
	String GG=G.toString();
	String BB=B.toString();
	if(r>9) RR=hex[r-10];
	if(g>9) GG=hex[g-10];
	if(b>9) BB=hex[b-10];
	String S="#"+RR+GG+BB;
	return(S);
    }
}
