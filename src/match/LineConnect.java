package match;


import java.util.Date;


public class LineConnect {

	
	
	
	//public static int MAXCROSSLINE = 10;//����������룬�ݶ�Ϊ10
	public long LineIdBegin = 0;//·�����ӿ�ʼ·��ID link����line
	public long LineIdEnd = 0;//·�����ӽ���·��ID
	public Date seprateTime = null; //�����������·����ʱ��
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
