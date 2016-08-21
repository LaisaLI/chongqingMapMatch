package match;

import java.util.Date;
import java.util.HashMap;

public class GpsPointOfCar {
	
	private Double jd = (double) 0;
	private Double wd = (double) 0;
	
	//private String[] arrayS = new String[12];
	private Date currentTime = null;
	public class Distant
	{
		public long lineID;
		public double distant;
		
		public void set()
		{
			//long i = lineID + lineID;
		}
		
		
	}
	
	public GpsPointOfCar(Date _currentTime, double _wd, double _jd)
	{
		currentTime = _currentTime;
		jd = _jd;
		wd = _wd;
	}
	
	public boolean equals(GpsPointOfCar g)
	{
		if (g == null) return false;
		if (jd.equals(g.getJD()) && wd.equals(g.getWD())) return true;
		else return false;
	}
	
	public HashMap<Long, Double> getPointAround() //获得Gps点周围的路链
	{
		//缺少
		return new HashMap<Long, Double>(256);
	}
	
	public double getJD()
	{
		return jd;
	}
	public double getWD()
	{
		return wd;
	}
	
	
	public Date getCurrentTime()
	{
		return currentTime;
	}
	
	
	
}
