package match;


import java.util.Date;


public class LineConnect {

	
	
	
	//public static int MAXCROSSLINE = 10;//最大搜索距离，暂定为10
	public long LineIdBegin = 0;//路链连接开始路链ID link连成line
	public long LineIdEnd = 0;//路链连接结束路链ID
	public Date seprateTime = null; //车辆到达结束路链的时间
	public String enterNode = null;
	public int errorSymbol = 0;
	public LineConnect()
	{
		
	}
	
	public LineConnect(long _b, long _e)
	{
		this.LineIdBegin = _b;
		this.LineIdEnd = _e;
	}
	public void set(long _begin, long _end, Date _t, String _e)
	{
		LineIdBegin = _begin;
		LineIdEnd = _end;
		seprateTime = _t;
		enterNode = _e;
	}
	
	
	//public void 
}
