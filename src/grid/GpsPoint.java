package grid;

public class GpsPoint {
    private double X;
    private double Y;
    
    public GpsPoint(String s) {
    	if(s.contains(",")){
    		this.X = Double.parseDouble(s.split(",")[0]);
        	this.Y = Double.parseDouble(s.split(",")[1]);
    	}
    	else{
    		this.X = Double.parseDouble(s.split(" ")[0]);
			this.Y = Double.parseDouble(s.split(" ")[1]);
		
    		
    	}
    }
    public GpsPoint(double a,double b){
    	this.X=a;
    	this.Y=b;
    }
    
    public double getX(){
    	return X;
    }
    public double getY(){
    	return Y;
    }
    
    public String toString(){  
    	return this.getX()+" "+this.getY();
    }
    
    /*public String subidcount(GpsPoint base,GpsPoint x){
		double j= (x.getX()-base.getX())/0.0025;//0.015625
		double w= (x.getY()-base.getY())/0.00166672;//0.010417
		int a=(int)Math.floor(j);
		int b=(int)Math.floor(w);
		if(a==-1)
			a=0;
		if(b==-1)
			b=0;
		if(a==50)
			a=49;
		if(b==50)
			b=49;
		if(a<0||b<0){
			System.out.println("a:"+a+" b:"+b+" "+base.getX()+" "+base.getY()+","+x.getX()+" "+x.getY());
			System.out.println("SUBID:"+String.valueOf(a)+String.valueOf(b));
			//return String.valueOf(a)+String.valueOf(b)+"wrong";
		}
    	return String.format("%02d", a)+String.format("%02d", b);
    	
    }*/
    
}
