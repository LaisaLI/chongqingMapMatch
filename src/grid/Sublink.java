package grid;


import java.util.ArrayList;

//import Grid.IsCross;

public class Sublink {
    private long mapID;
	private long ID;//id
	private String SnodeID;
	private String EnodeID;
	private int Direction;
	private ArrayList<GpsPoint> points;
	
	public Sublink(){
		this.points = new ArrayList<GpsPoint>();
	}
	
	
	public Sublink(Long ID,String SnodeID,String EnodeID,String Direction){

		this.setID(ID);
		this.points = new ArrayList<GpsPoint>();
		this.SnodeID=SnodeID;
		this.EnodeID=EnodeID;
		this.Direction=Integer.parseInt(Direction);
		
	}
	
	
	
	public void setMapID(long _id)
	{
		this.mapID = _id;
	}
	
	public long getMapID()
	{
		return this.mapID;
	}
	
	public void setID(long _id)
	{
		this.ID = _id;
	}
	public long getID()
	{
		return this.ID;
	}

	public String getSnodeID() {
		return SnodeID;
	}
	public void setSnodeID(String snodeID) {
		SnodeID = snodeID;
	}
	public String getEnodeID() {
		return EnodeID;
	}
	public void setEnodeID(String enodeID) {
		EnodeID = enodeID;
	}
	public int getDirection() {
		return Direction;
	}
	public void setDirection(int direction) {
		Direction = direction;
	}
	public GpsPoint getStartPoint(){
		return points.get(0);
	}
	public GpsPoint getEndPoint(){
		return points.get(points.size()-1);
	}
	public void addPoint(GpsPoint g){
		this.points.add(g);
	}
	public void addPath(ArrayList<GpsPoint> al){
		this.points.addAll(al);
	}
	public GpsPoint getPoint(int index){
		return this.points.get(index);
	}
	public ArrayList<GpsPoint> getPointArray(){
		return this.points;
	}
	public int getPointsLength(){
		return this.points.size();
	}
	public void setPoints(String list)
	{
		String[] s = list.split(",");
		for (int i = 0; i < s.length; i++)
		{
			this.points.add(new GpsPoint(s[i]));
		}
	}
	/*public String toString(int subMapID)
	{  
		StringBuffer sb= new StringBuffer();
		int startPointPos = 0;
		int endPointPos = points.size() - 1;
		while (startPointPos <= endPointPos)
		{
			int subIdOfStart = IsCross.grid.subMapIdOfPoint(points.get(startPointPos));
			int subIdOfEnd = IsCross.grid.subMapIdOfPoint(points.get(endPointPos));
			if (subIdOfEnd == subMapID && subIdOfStart == subMapID) break;
			else 
			{
				//System.out.println(subIdOfEnd + " " + subIdOfStart + " " + subMapID);
				if (subIdOfEnd != subMapID) endPointPos--;
				if (subIdOfStart != subMapID) startPointPos++;
			}
			
		}
		if (startPointPos <= endPointPos)
		{
			for (int i = Math.max(startPointPos - 1,  0); i <= Math.min(endPointPos + 1, points.size() - 1); i++)
			{
				sb.append(this.points.get(i).toString()+",");
			}
		}
		else
		{
			startPointPos = 0;
			endPointPos = points.size() - 1;
			while (startPointPos < endPointPos)
			{
				boolean bol_1 = IsCross.grid.isCrossSubMap(points.get(startPointPos), points.get(startPointPos + 1), subMapID);
				boolean bol_2 = (startPointPos + 1 == endPointPos) || IsCross.grid.isCrossSubMap(points.get(endPointPos -1), points.get(endPointPos), subMapID);
				if (bol_1 && bol_2) break;
				else 
				{
					if (!bol_1) startPointPos++;
					if (!bol_2) endPointPos--;
				}
			}
			if (startPointPos < endPointPos)
			{
				
			
				for (int i = startPointPos; i <= endPointPos; i++)
				{
					sb.append(this.points.get(i).toString()+",");
				}
				
			}
			
		}
	    if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
	    return sb.toString();  
	}*/
	
}





