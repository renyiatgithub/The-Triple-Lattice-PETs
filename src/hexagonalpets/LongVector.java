package hexagonalpets;

import java.util.ArrayList;
import java.util.List;


/*This is a class used in our rigorous computations.
  It is a vector in Z^3.  With each routine, we check
  to make sure that there is no overflow error*/

public class LongVector {
    double[] x=new double[3];

    public LongVector(){}

    public LongVector(double a0,double a1,double a2) {
	x[0]=a0;
	x[1]=a1;
	x[2]=a2;
    }

    public LongVector(double[] X) {
	for(int i=0;i<3;++i) x[i]=X[i];
    }

    public LongVector(LongVector V) {
	for(int i=0;i<3;++i) x[i]=V.x[i];
    }
    
    public LongVector(Vector V){
        for(int i=0; i<3; ++i) x[i] = V.x[i];
    }

    public static LongVector cross(LongVector v,LongVector w) {
	//v.checkRangeLittle();
	//w.checkRangeLittle();
	LongVector X=new LongVector();
	X.x[0]=v.x[1]*w.x[2]-w.x[1]*v.x[2];
	X.x[1]=v.x[2]*w.x[0]-w.x[2]*v.x[0];
	X.x[2]=v.x[0]*w.x[1]-w.x[0]*v.x[1];
	return(X);
    }


    public static LongVector plus(LongVector v1,LongVector v2) {
	v1.checkRangeBig();
	v2.checkRangeBig();
	LongVector w=new LongVector();
	for(int i=0;i<3;++i) w.x[i]=v1.x[i]+v2.x[i];
	return(w);
    }

    public static LongVector minus(LongVector v1,LongVector v2) {
//	v1.checkRangeBig();
//	v2.checkRangeBig();
	LongVector w=new LongVector();
	for(int i=0;i<3;++i) w.x[i]=v1.x[i]-v2.x[i];
	return(w);
    }

    public static LongVector scale(double r,LongVector v) {
	v.checkRangeLittle();
	checkRangeLittle(r);
	LongVector w=new LongVector();
	for(int i=0;i<3;++i) w.x[i]=r*v.x[i];
	return(w);
    }

    public LongVector scale(double r) {
	return(scale(r,this));
    }

    public static double dot(LongVector v,LongVector w) {	
//       v.checkRangeLittle();
//      w.checkRangeLittle();
	double d=0;
	for(int i=0;i<3;++i){
            d=d+v.x[i]*w.x[i];
        }
	return(d);
    }

    public static LongVector normal(LongVector[] V) {
        LongVector X = new LongVector(0,0,0);       
        for(int i=0; i<V.length; i++){
        LongVector W1=minus(V[(i+1)%V.length],V[i]);
	LongVector W2=minus(V[(i+2)%V.length],V[i]);
	X=cross(W1,W2);
        double t = LongVector.dot(X, X);
        if(t !=0){
            X=process(X);
            return(X);
        }

        }
	return(X);
    }
    
    
//        public static LongVector normal(LongVector[] V) {
//	LongVector W1=minus(V[1],V[0]);
//	LongVector W2=minus(V[2],V[0]);
//	LongVector X=cross(W1,W2);
//	X=process(X);
//	return(X);
//    }
    
    /*Improved Normals: at least two of the three coordinates lie in {-1,0,1}*/
    public static LongVector process(LongVector W) {
	LongVector V=new LongVector(W);
	for(int i=0;i<3;++i) {
	    if(V.x[i]<0) V.x[i]=-V.x[i];
	}
	double min=1000000000;
	for(int i=0;i<3;++i) {
	    if((V.x[i]>0)&&(min>V.x[i])) min=V.x[i];
	}
	for(int i=0;i<3;++i) {
	    double d=V.x[i]%min;
	    if(d!= 0) {
		return(W);
	    }
	}
	//first nonzero coefficient positive
	for(int i=0;i<3;++i) V.x[i]=W.x[i]/min;
	if(V.x[0]<0) {
	    for(int i=0;i<3;++i) V.x[i]=-V.x[i];
	}
	if((V.x[0]==0)&&(V.x[1]<1)) {
	    V.x[1]=-V.x[1];
	    V.x[2]=-V.x[2];
	}
	if((V.x[0]==0)&&(V.x[1]==0)) {
	    if(V.x[2] == 0) throw new ProofException("normal zero");
            V.x[2]=1;
	}

	return(V);
    }

    public void print() {
	for(int i=0;i<3;++i)	{
	    System.out.print(x[i]+" ");
	}
	System.out.println("");
    }

    /*Checks for avoiding overflow error*/

    /*This checks if all the coords are less than 2^{62} in
      absolute value.  We use this before we add vectors or
      subtract them to make sure there is no overflow.*/

    public void checkRangeBig() {
	for(int i=0;i<3;++i) checkRangeBig(x[i]);
    }


    /*This checks if all the coords are less than 2^{30} in
      absolute value.  We use this before we use the dot
      or cross product to make sure there is no overflow.*/

    public void checkRangeLittle() {
	for(int i=0;i<3;++i) checkRangeLittle(x[i]);
    }

    /*This checks if a long is less than 2^{62} in absolute value*/

    public static void checkRangeBig(double r) {
	double m= 4611686018427387903L;
	if(r>m)   {throw new ProofException("number out of range 1");}
	if(r<-m)  {throw new ProofException("number out of range 2");}
    }


    /*This checks if a long is less than 2^{30} in absolute value.*/

    public static void checkRangeLittle(double r) {
        double m=1073741824;
	if(r>m)   {throw new ProofException("number out of range 1");}
	if(r<-m)  {throw new ProofException("number out of range 2");}
    }

    public LongVector guidedDivide(double a) {
	LongVector v=new LongVector();
	for(int i=0;i<3;++i) v.x[i]=guidedDivide(this.x[i],a);
	return(v);
    }

    public static double guidedDivide(double p,double q) {
	double r=p/q;
	if(Math.abs(r*q-p)>0.0000001) {
//	    System.out.println(p+" "+q);
            throw new ProofException("division error");
	}
	return(r);
    }
    
    public static double norm(LongVector v){
        return Math.sqrt(v.x[0]*v.x[0]+v.x[1]*v.x[1]+v.x[2]*v.x[2]);
    }
    
    public static double dist(LongVector v, LongVector w){
        LongVector d = minus(v,w);
        return(norm(d));
    }
}





