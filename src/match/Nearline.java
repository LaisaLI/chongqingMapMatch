package match;




/*import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.InputStream;
import java.util.Iterator;
*/
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import grid.*;

public class Nearline {

	//private Map<String, JGrid> grids;
	//private HashMap<String, myGrid> mygrids; 
	private int gridID = 0;
	//private String mygridID;
	//private GpsPoint p;
	private int preGridID = 0;
	private JGrid grid = null;
	private DomParseService dom = null;
	private String linkPath = null;
	public Nearline(String path)
	{	
		this.linkPath = path;
		//test
		//Sublink slk = new Sublink
		/*GpsPoint p = new GpsPoint(418659628.0/3600000, 144359879.0/3600000);
		grid = new JGrid(p);
		System.out.println("In 06:50 " + grid.getMapID() + String.format("%04d", grid.subMapIdOfPoint(p)));
		System.out.println("The offset is " + grid.getOff(p, 'X') + " " + grid.getOff(p, 'Y'));*/
	}
	public void test() throws ParserConfigurationException, SAXException, IOException
	{
		for (int i = 0; i < 20; i++)
			for (int j = 0; j < 17; j++)
			if (JGrid.GRIDS[i][j] > 0)
			{
				dom = new DomParseService(linkPath + Integer.toString(JGrid.GRIDS[i][j]) + ".xml");
			}
	}
	public HashMap<Long, Double> getNear(GpsPoint p) throws ParserConfigurationException, SAXException, IOException
	{
		if ((gridID = JGrid.isInMap(p)) == 0) return new HashMap<Long, Double>();
		if (gridID != preGridID)
		{
			grid = new JGrid(p);
			preGridID = gridID;
			//System.out.println(linkPath + Integer.toString(gridID) + ".xml");
			dom = new DomParseService(linkPath + Integer.toString(gridID) + ".xml");
			
			
		}
		//if (grid == null) System.out.println(gridID + " " + preGridID);
		int subMapID = grid.subMapIdOfPoint(p);


		return dom.getNear(subMapID, p, grid.getOff(p, 'X'), grid.getOff(p, 'Y'));
		
	}
	
	public HashMap<Long, String>getSnode(GpsPoint p)
	{
		if (JGrid.isInMap(p) == 0) return new HashMap<Long, String>();
		else return dom.getSnode();
	}

	public HashMap<Long, Double>getAngleSet(GpsPoint p)
	{
		if (JGrid.isInMap(p) == 0) return new HashMap<Long, Double>();
		else return dom.getAngleSet();
	}
	
	
	
	
	/*public HashMap<Long , Sublink> getNear(){
		try {
			this.init();
			this.isInmap();
			this.gotmyGrid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(this.getGridID());
		GpsPoint base = this.getGrids().get(this.getGridID()).getspoint();
		//System.out.println(base.toString());
		String gridID=this.getGridID();
		String subID=base.subidcount(base, this.p);
		
		StringBuffer sb = new StringBuffer();
		sb.append(gridID).append(subID);
		String myid=sb.toString();
		//System.out.println(myid+"SS   ");
		myGrid mg=this.getmyGrids().get(myid);
		return mg.getlinks();
	}
	
	public void setP(GpsPoint p){
		this.p=p;
	}
	
	public void init() throws Exception{
		Mymap sax = new Mymap();
		InputStream input_area =new FileInputStream("E:/Data/2012_map_area.xml");
		this.grids = sax.getGrids(input_area);
		
		JGrid grid = grids.get("595564");
		//System.out.println(grid.toString());
		
		
	}
	
	public GpsPoint getp(){
		return this.p;
	}
	
	public Map<String, JGrid> getGrids(){
		return this.grids;
	}
	
	public HashMap<String, myGrid> getmyGrids(){
		return this.mygrids;
	}
	
	public String getGridID(){
		return this.gridID;
	}
	
	public String getmyGridID(){
		return this.mygridID;
	}
	
	public Boolean isInmap(){
		Iterator iter = this.grids.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    String key = entry.getKey().toString(); 
		    JGrid grid = this.grids.get(key);
		    GpsPoint base=grid.getspoint();
		    if(this.p.isInsameGrid(base, this.p)){
		    	this.gridID=key;
		    	return true;
		    	}
		}
		return false;//-1表示改点不再原地图网格内
	}
	

	
	public void gotmyGrid() throws Exception{

		
		InputStream input_submap = null;
		try {
			input_submap = new FileInputStream("E:/Data/submap/submap"+this.getGridID()+".xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        DomParseService dom = new DomParseService();  
        this.mygrids= dom.getGrids(input_submap);  
        
	}


	public HashMap<Long, Double>getNearLine(GpsPoint _g)//获取置信区间内的路链
	{
		return new HashMap<Long, Double>();
	}
	public String getSnode(long lineID)//返回开始结束节点编号，受具体方向约束。
	{
		return new String();
	}*/
	
	

}
