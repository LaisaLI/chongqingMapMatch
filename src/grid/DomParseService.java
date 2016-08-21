package grid;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;   
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.ParserConfigurationException;

import match.TimePicker;

import org.w3c.dom.Document;  
import org.w3c.dom.Element;  
import org.w3c.dom.NodeList;  
import org.w3c.dom.Node;  
import org.xml.sax.SAXException;
  
public class DomParseService {  
	//在dom中使用穷举法搜索小网格号，效率太低
	public static final int MAX_DISTANCE = 100;
	public static final int NUM_OF_SUBMAP = JGrid.NUM_OF_SUBMAP;
	private String path = null;
	private InputStream input_submap = null;
	private DocumentBuilderFactory factory = null;
    private DocumentBuilder builder = null;
    private Document document = null;
    private Element element = null;
    private NodeList submapNodes = null;
    private Element submapElement = null;
    private NodeList childNodes = null;
    private HashMap<Long, Double> nearLineSet = null;
    private HashMap<Long, String> lineNodeMap = null;
	public DomParseService(String _path) throws ParserConfigurationException, SAXException, IOException
	{
		this.path = _path;
		//System.out.println(this.path);
		//System.out.println(path);
		this.input_submap = new FileInputStream(path);
		this.factory = DocumentBuilderFactory.newInstance();  
        this.builder = factory.newDocumentBuilder();  
        this.document = builder.parse(input_submap);  
        this.element = document.getDocumentElement(); 
        this.submapNodes = element.getElementsByTagName("submap"); 
	}
	public HashMap<Long, Double> getNear(int SID, GpsPoint p, int XOff, int YOff)
	{
		long t = System.currentTimeMillis();
		nearLineSet = new HashMap<Long,Double>();
		lineNodeMap = new HashMap<Long,String>();
		getNearLine(SID, p);
		int X = SID % NUM_OF_SUBMAP;
		int Y = SID / NUM_OF_SUBMAP;
		if (XOff != 0 && X + XOff >= 0 && X + XOff < NUM_OF_SUBMAP ) 
			{
			getNearLine(X + XOff + Y * NUM_OF_SUBMAP, p);
			if (YOff != 0 && Y + YOff >= 0 && Y + YOff < NUM_OF_SUBMAP) getNearLine(X + XOff + (Y + YOff) * NUM_OF_SUBMAP, p);
			}
		if (YOff != 0 && Y + YOff >= 0 && Y + YOff < NUM_OF_SUBMAP) getNearLine(X + (Y + YOff) * NUM_OF_SUBMAP, p);
		TimePicker.t3 += System.currentTimeMillis() - t;
		return nearLineSet;
	}
	
	public void getNearLine(int SID, GpsPoint p)
	{
		
		submapElement = (Element) submapNodes.item(SID);
		//if (submapElement == null) System.out.println(SID);
		//submapNodes.getLength();
        childNodes = submapElement.getChildNodes();
		
		for(int j=0;j<childNodes.getLength();j++){  
            if(childNodes.item(j).getNodeType()==Node.ELEMENT_NODE){
            	Element sublinkElement = (Element) childNodes.item(j);  
            	Long slid= Long.parseLong(sublinkElement.getAttribute("linkID"));
            	String points= sublinkElement.getFirstChild().getNodeValue();
            	Sublink slk=new Sublink(slid,sublinkElement.getAttribute("SnodeID"),sublinkElement.getAttribute("EnodeID"),sublinkElement.getAttribute("Direction"));
            	long t0 = System.currentTimeMillis();
            	slk.setPoints(points);
            	double distance = this.getDistance(p.getX(), p.getY(), slk, 0);
            	if (distance <= MAX_DISTANCE && (!nearLineSet.containsKey(slk.getID()) || nearLineSet.get(slk.getID()) > distance))
            	{
            		String node = null;
            		nearLineSet.put(slk.getID(), distance);
            		if (slk.getDirection() == 1)
            		{
            			node = slk.getSnodeID() + " " + slk.getEnodeID();
            		}
            		else if (slk.getDirection() == 2)
            		{
            			node = slk.getEnodeID();
            		}
            		else 
            		{
            			node = slk.getSnodeID();
            		}
            		lineNodeMap.put(slk.getID(), node);
            		//if (Double.valueOf(p.getX()).equals(419262148.0/3600000) && Double.valueOf(p.getY()).equals(143964257.0/3600000))
            		//	System.out.println(slk.getID() + " " + node);
            			
            	}
            	TimePicker.t6 += System.currentTimeMillis() - t0;
            }
        }
        
	}
	
	public HashMap<Long, String>getSnode()
	{
		return lineNodeMap;
	}
	
	/*public final void openFile(int oldFunction){
		try {
			input_submap = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public HashMap<Long , Sublink> getGrids(){
		myGrid submap = new myGrid();
		int i = 0;//////////////////////////////////////////
		if(input_submap!=null){
			try {			
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
		        DocumentBuilder builder = factory.newDocumentBuilder();  
		        Document document = builder.parse(input_submap);  
		        Element element = document.getDocumentElement(); 
		        NodeList submapNodes = element.getElementsByTagName("submap"); 
		        Element submapElement = (Element) submapNodes.item(i);    
	            String submapid=submapElement.getAttribute("submapID");
	            submap.setsubmapID(submapid);  
	            NodeList childNodes = submapElement.getChildNodes();
	            for(int j=0;j<childNodes.getLength();j++){  
	                if(childNodes.item(j).getNodeType()==Node.ELEMENT_NODE){
	                	Element sublinkElement = (Element) childNodes.item(j);  
	                	Long slid= Long.parseLong(sublinkElement.getAttribute("linkID"));
	                	String points= sublinkElement.getFirstChild().getNodeValue();
	                	Sublink slk=new Sublink(slid,sublinkElement.getAttribute("SnodeID"),sublinkElement.getAttribute("EnodeID"),sublinkElement.getAttribute("Direction"),sublinkElement.getAttribute("haveenode"));
	                	slk.setPoint(points);
	                	submap.addlinks(slid, slk);
	                }  
	            }
		        
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return submap.getlinks();
	}
	
    public HashMap<String, myGrid> getGrids(InputStream inputStream) throws Exception{  
    	long t1 = System.currentTimeMillis();
    	HashMap<String, myGrid> mygrids= new  HashMap<String, myGrid>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder = factory.newDocumentBuilder();  
        Document document = builder.parse(inputStream);  
        Element element = document.getDocumentElement();  
        long t2 = System.currentTimeMillis();
        System.out.println("d" + (t2-t1));
        NodeList submapNodes = element.getElementsByTagName("submap");  
        for(int i=0;i<submapNodes.getLength();i++){  
            Element submapElement = (Element) submapNodes.item(i);  
            myGrid submap = new myGrid();  
            String submapid=submapElement.getAttribute("submapID");
            submap.setsubmapID(submapid);  
            NodeList childNodes = submapElement.getChildNodes();  
//          System.out.println("*****"+childNodes.getLength());  
            for(int j=0;j<childNodes.getLength();j++){  
                if(childNodes.item(j).getNodeType()==Node.ELEMENT_NODE){
                	Element sublinkElement = (Element) childNodes.item(j);  
                	Long slid= Long.parseLong(sublinkElement.getAttribute("linkID"));
                	String points= sublinkElement.getFirstChild().getNodeValue();
                	Sublink slk=new Sublink(slid,sublinkElement.getAttribute("SnodeID"),sublinkElement.getAttribute("EnodeID"),sublinkElement.getAttribute("Direction"),sublinkElement.getAttribute("haveenode"));
                	slk.setPoint(points);
                	submap.addlinks(slid, slk);
                }  
            }//end for j  
            
            mygrids.put(submapid, submap); 
        }//end for i  
        long t3 = System.currentTimeMillis();
        System.out.println(t3-t2);
        return mygrids;  
    }  */
    public double getDistance(double X,double Y, Sublink line,int uselessSign){
		double Xtemp,Ytemp,distance = Double.MAX_VALUE,disTemp,preDistance = Double.MAX_VALUE,afterDistance = Double.MAX_VALUE;
    	int position = 0;
    	ArrayList<GpsPoint> points = line.getPointArray();
    	
    	double Xoff = 0.05; double Yoff = 0.03;//此处阀值为了见此计算次数
    	for(int i = 0;i<points.size();i++){
    		
    		Xtemp = Math.abs(points.get(i).getX() - X);
    		Ytemp = Math.abs(points.get(i).getY() - Y);
    		if(Xoff <= Xtemp && Yoff <= Ytemp) continue;
    		else if(Xoff > Xtemp && Yoff > Ytemp){
    			Xoff = Xtemp;
    			Yoff = Ytemp;
    			position = i;
    			distance = DistanceP2P.GetDistance(X, Y, points.get(i).getX(), points.get(i).getY());
    			
    		}else{
    			disTemp = DistanceP2P.GetDistance(X, Y, points.get(i).getX(), points.get(i).getY());
    			
    			if(disTemp < distance){
    				Xoff = Xtemp;
        			Yoff = Ytemp;
        			position = i;
        			distance = disTemp;
    			}
    		}
    	}
    	disTemp = distance*distance;
    	if(position != 0){
    		double preDis,pre,preDisTwice,preTwice;
    		preDis = DistanceP2P.GetDistance(X, Y, points.get(position-1).getX(), points.get(position-1).getY());
    		pre = DistanceP2P.GetDistance(points.get(position).getX(), points.get(position).getY(), points.get(position-1).getX(), points.get(position-1).getY());
    		preDisTwice = preDis*preDis;
    		preTwice = pre*pre;
    		if(!(disTemp > (preTwice+preDisTwice)||preDisTwice >(preTwice+disTemp))) preDistance = getOVD(preDis,distance,pre);
    	}
    	
    	//if (DistanceP2P.GetDistance(X, Y, points[0]., lng2))
    	if(position != points.size()-1){
    		double aftDis,aft,aftDisTwice,aftTwice;
    		aftDis = DistanceP2P.GetDistance(X, Y, points.get(position+1).getX(), points.get(position+1).getY());
    		aft = DistanceP2P.GetDistance(points.get(position).getX(), points.get(position).getY(), points.get(position+1).getX(), points.get(position+1).getY());
    		aftDisTwice = aftDis*aftDis;
    		aftTwice = aft*aft;
    		if(!(disTemp > (aftDisTwice+aftTwice)||aftDisTwice >(aftTwice+disTemp))) afterDistance = getOVD(aftDis,distance,aft);
    	}
    	
    	if(preDistance < distance) distance = preDistance;
    	if(afterDistance < distance) distance = afterDistance;
    	
    	//System.out.println("The distance of point(" + X + "," + Y + ") to line " + line.getID() + " is " + distance);
    	return distance;
    }
	 private double getOVD(double a,double b,double obj){
		 double p = (a+b+obj)/2;
		 return Math.sqrt(p*(p-a)*(p-b)*(p-obj))*2/obj;
	 }
}  
