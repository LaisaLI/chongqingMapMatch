package match;


import grid.DistanceP2P;
import grid.GpsPoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;




public class countLine {
	//public static int debug = 0;
	public static final int MAX_OF_MAP_SIZE = 16;//����
	public static String PATH = null;
	public static String PATH_OF_LINK = null;
	public static String PATH_OF_NEXTLINE_INDEX = null;
	public static String PATH_OF_NEXTLINE = null;
	public static String PATH_DIR =null;
	public static String RESULT_DIR = "./Result/";
	private static int errorNum = 0;
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private ArrayList<LineConnect> list = null;//
	private HashMap<Long, Double> currentMap = null;//
	private HashMap<Long, Double> pastMap = null;//
	private HashMap<Long, Integer> lineEnter = null;//
	private HashMap<Long, String> Snodes = null;
	private HashMap<Long, String> currentSnodes = null;
	private String carId = null; 
	private String beginTime = null;
	private String endTime = null;
	private NextLineOper nloper = null;
	private GpsPointOfCar pastPoint = null;
	private GpsPointOfCar currentPoint = null;
	private GpsPointOfCar nextPoint = null;
	
	//private disP2L dpl = null; 
	private Nearline nearLine = null; // get the lines around the GpsPointOfCar
	public countLine(String _carId, String _beginTime, String _endTime, NextLineOper _nlo)
	{
		carId = _carId;
		beginTime = _beginTime;
		endTime = _endTime;
		pastMap = new HashMap<Long, Double>(MAX_OF_MAP_SIZE);
		nloper = _nlo;
		//dpl = new disP2L();
		nearLine = new Nearline(PATH_OF_LINK);
		list = new ArrayList<LineConnect>(1024);
		lineEnter = new HashMap<Long, Integer>();
	}
	
	public void add(GpsPointOfCar p) throws ParserConfigurationException, SAXException, IOException
	{
		/******
		 * get the information of the GPS one by one
		 */
		
		if (p != null && p.equals(nextPoint))
		{			
			return;
		}
		
		pastPoint = currentPoint;
		currentPoint = nextPoint;
		nextPoint = p;
		if (currentPoint != null)
		{
			if (beginTime == null) beginTime = sdf.format(currentPoint.getCurrentTime());
			endTime = sdf.format(currentPoint.getCurrentTime());
			int errorFlag = isPointNormal(currentPoint, pastPoint, nextPoint);
			count(currentPoint, errorFlag);
		}
	}

	//lyl:������--tag--
	private final int isPointNormal(GpsPointOfCar c, GpsPointOfCar p, GpsPointOfCar n)
	{
		double pToc, nToc, pTon;
		pToc = nToc = pTon = -1;
//		if (p != null) pToc = DistanceP2P.GetDistance(c.getJD(), c.getWD(), p.getJD(), p.getWD());
//		if (n != null) nToc = DistanceP2P.GetDistance(c.getJD(), c.getWD(), n.getJD(), n.getWD());
//		if (p != null && n != null) pTon = DistanceP2P.GetDistance(n.getJD(), n.getWD(), p.getJD(), p.getWD());
		//lyl:������*3600000��DistanceP2P.GetDistance����Ĳ���Ӧ������ͨ�ľ�γ�ȡ�
		if (p != null) pToc = DistanceP2P.GetDistance(c.getJD()*3600000, c.getWD()*3600000, p.getJD()*3600000, p.getWD()*3600000);
		if (n != null) nToc = DistanceP2P.GetDistance(c.getJD()*3600000, c.getWD()*3600000, n.getJD()*3600000, n.getWD()*3600000);
		if (p != null && n != null) pTon = DistanceP2P.GetDistance(n.getJD()*3600000, n.getWD()*3600000, p.getJD()*3600000, p.getWD()*3600000);
		//System.out.println(pToc+","+pTon+","+nToc);
		if (p != null && n != null && pToc + nToc > 20 * pTon) return 1;
		if (p != null && pToc > 50) return 2;
		return 0;
	}
	
	public void count(GpsPointOfCar _g, int errorFlag) throws  ParserConfigurationException, SAXException, IOException 
	{
		//TODO

		//GpsPoint p = new GpsPoint(_g.getJD(), _g.getWD());
		//lyl:�޸�Ϊ����һ��
		GpsPoint p = new GpsPoint(_g.getJD()*3600000, _g.getWD()*3600000);
		long t1 = System.currentTimeMillis();
		currentMap = nearLine.getNear(p);//����
		currentSnodes = nearLine.getSnode(p);
		TimePicker.t1 += System.currentTimeMillis() - t1;
		long t2 = System.currentTimeMillis();
		if (currentMap.isEmpty()) return;
		
		if (pastMap.isEmpty())
		{
			//System.out.println("PastMap is null in the time of " + g.getCurrentTime());
			pastMap = currentMap;
			Snodes = currentSnodes;
		}
		else 
		{		
			HashMap<Long, Double> _pastMap = getConnection(_g);
			//��֮ǰ���е��������·����PastMap�͵�ǰ�����������ڵ���·���Ϻϲ�������
			/*if (Long.parseLong(sdf.format(_g.getCurrentTime())) >= 120120401081940L && 
					Long.parseLong(sdf.format(_g.getCurrentTime())) <= 20120401082000L)
				//if (true)
				{
					//System.out.println("PastMap is null in the time of " + g.getCurrentTime());
					System.out.println("The size of PastMap = " + pastMap.size());
					System.out.println("The size of CurrentMap = " + currentMap.size() + " " + _g.getCurrentTime());
					
					if (_pastMap.isEmpty()) System.out.println("Empty in " + _g.getCurrentTime());
					for (Iterator<Long> it = pastMap.keySet().iterator(); it.hasNext();)
					{
						long tmp = (Long)it.next();
						System.out.print(tmp + " " + pastMap.get(tmp) + " ");
						
					}
					System.out.println();
					for (Iterator<Long> it = currentMap.keySet().iterator(); it.hasNext();)
					{
						long tmp = (Long)it.next();
						System.out.print(tmp + " " + currentMap.get(tmp) + " ");
					}
					System.out.println();
				}*/
			if (_pastMap.isEmpty())
			{
				if (errorFlag != 1)
				{
					errorPrint("D:/1_link_error.csv", _g);
					/*if (errorFlag == 2)
					{
						System.out.println("Error Flag is 2 in " + _g.getCurrentTime() + carId );
						System.out.println("Error Map size " + pastMap.size() + " " + currentMap.size());
					}*/
					if (errorFlag == 0)
					{
						//this.printLine(PATH.substring(0, PATH.lastIndexOf('.')) + "_link.csv", false);
					
						//if (errorFlag == 0) System.out.println("Error in " + carId + " " + _g.getCurrentTime());
					}
					pastMap = currentMap;
					Snodes = currentSnodes;
				}
				else currentPoint = pastPoint;
			}
			else 
			{
				pastMap = _pastMap;
			
				Snodes = currentSnodes;
			}
		}
		TimePicker.t2 += System.currentTimeMillis() - t2;
	}
		
	//lyl:������ʲô��
	private void errorPrint(String path, GpsPointOfCar p) throws IOException
	{
		//System.out.println("3333333333333333333333333334");
		errorNum++;
		FileWriter fileWriter = new FileWriter(path, true);
		fileWriter.write(errorNum + "," + pastMap.size() + "," + currentMap.size() + "\n");
		for (Iterator<Long> it = pastMap.keySet().iterator(); it.hasNext();)
		{
			long lineID = it.next();
			fileWriter.write(lineID + "," + pastMap.get(lineID) + "\n");
		}
		for (Iterator<Long> it = currentMap.keySet().iterator(); it.hasNext();)
		{
			long lineID = it.next();
			fileWriter.write(lineID + "," + currentMap.get(lineID) + "\n");
			
		}
		fileWriter.close();
		for (Iterator<Long> itp = pastMap.keySet().iterator(); itp.hasNext();)
		{
			long s = itp.next();
			for (Iterator<Long> itc = currentMap.keySet().iterator(); itc.hasNext();)
			{
				
				LineConnect lct = new LineConnect();
				long e = itc.next();
				lct.set(s, e, p.getCurrentTime(), null);
				lct.errorSymbol = errorNum;
				list.add(lct);
				
			}
		}
	}

	//���linkId
	public void printLine(String fileName) throws IOException
	{
		/*if (currentMap == null)
		{
			System.out.println(bbb);
			if (currentPoint != null) System.out.println(currentPoint.getCurrentTime());
			else System.out.println("Null");
			System.out.println(carId + " " + nextPoint.getCurrentTime());
		}*/
		//��ȡȨֵ��С��·��endLine
		long t0 = System.currentTimeMillis();
		long endLine = 0;
		double minDistant = Double.MAX_VALUE; 
		for (Iterator<Long> it = pastMap.keySet().iterator(); it.hasNext();) 
		{
			long pos = it.next();
			if (pastMap.get(pos) < minDistant) 
			{
				minDistant = pastMap.get(pos);
				endLine = pos;
			}
        }
		
		//��list���ɺ���ǰѰ�ҵ�ǰtripId��Ӧ��startLine�������漰��list��LineConnect�����λ�÷���ջstackOfPosInList
		long startLine = endLine;
		
		Stack<Integer> stackOfPosInList = new Stack<Integer>();
		
		int numOfLine = 1;
		
		for (int i = list.size() - 1; i >= 0; i--)
		{
			LineConnect lineConnect = list.get(i);
			//System.out.println(lineConnect.LineIdBegin + " " + lineConnect.LineIdEnd + " " + lineConnect.seprateTime);
			if (lineConnect.LineIdEnd == startLine)
			{
				//for (int j = lineConnect.i - 1; j >= 0; j--) linkedList.addLast(lineConnect.LineCross[j]);
				stackOfPosInList.push(i);
				startLine = lineConnect.LineIdBegin;
				numOfLine += 1;
			}
		}
		
		//����ջ�����ڵ�λ����list��Ѱ��LineConnect����������ļ�
		FileWriter fileWriter = new FileWriter(fileName, true);
		
		fileWriter.write(carId + "," + beginTime + "," + endTime + "," + numOfLine + "," + startLine + "," + endLine  + "\n");
		
		
		//lineID,beginTime,endTime
		fileWriter.write(startLine + ",0," + beginTime + ",");
		while (!stackOfPosInList.empty())
		{
			int i = stackOfPosInList.pop();
			LineConnect lineConnect = list.get(i);
			fileWriter.write(sdf.format(lineConnect.seprateTime) + "\n");
			/*for (int i1 = 0; i1 < lineConnect.i; i1++)
			{
				fileWriter.write(lineConnect.LineCross[i1] + "," + sdf.format(lineConnect.seprateTime) + "," + sdf.format(lineConnect.seprateTime) + "\n");
			}*/
			fileWriter.write(lineConnect.LineIdEnd + "," + lineConnect.errorSymbol + "," + sdf.format(lineConnect.seprateTime) + ",");
			//System.out.println(lineConnect.LineIdBegin + " " + lineConnect.LineIdEnd);
		} 
		fileWriter.write(endTime + "\n");
		
		fileWriter.close();
		list.clear();
		if (pastMap != null) pastMap.clear();
		if (currentMap != null) currentMap.clear();
		this.lineEnter.clear();
		TimePicker.t7 += System.currentTimeMillis() - t0;
		
	}

	
	
	public HashMap<Long, Double> getConnection(GpsPointOfCar _g)
	{
		//int debugInhm = 0;
		HashMap<Long, Double> current = new HashMap<Long, Double>(currentMap.size());
		//HashMap<Long, String> nextSnodes = new HashMap<Long, String>();
		for (Iterator<Long> it = pastMap.keySet().iterator(); it.hasNext();)
		{
			
			//debugInhm = 0;
			long lineId = (long)it.next();
			
			if (currentMap.containsKey(lineId))
			{
				double currentDistant = currentMap.get(lineId) + pastMap.get(lineId);
				current.put(lineId, currentDistant);
			}
			//System.out.println("Cycle in getNextLine1 " + System.currentTimeMillis());
		}
		for (Iterator<Long> it = pastMap.keySet().iterator(); it.hasNext();)
		{
			long lineId = (long)it.next();
			ArrayList<Long> nextLine = null; 
			String sNode = Snodes.get(lineId); 
			String node = null;

			//�����line��˫��ģ���snode�����������ÿո����
			if (sNode.contains(" "))
			{
				node = sNode.split(" ")[0];
				//lyl:�����·��ͬһ�˵�������ҽ���ʱ����С��30�뼴�ж�EnterError,������true
				if (!isEnterError(lineId, node))
				{
					nextLine = nloper.getNextLine(node);
				
					for (int i = 0; i < nextLine.size(); i++)
					{
						long next = nextLine.get(i);
						if (!pastMap.containsKey(next) && currentMap.containsKey(next))
						{
							double currentDistant = pastMap.get(lineId) + currentMap.get(next);
							if (!current.containsKey(next) || currentDistant < current.get(next))
							{
								current.put(next, currentDistant);
								lineEnter.put(next, list.size());
								LineConnect lct = new LineConnect();
								lct.set(lineId, next, _g.getCurrentTime(), node);
								list.add(lct);
								//nloper.remove(Snodes.get(lineId));
							}
						}
					}
				}
				node = sNode.split(" ")[1];
			}
			else node = sNode;
			//lyl:�����·��ͬһ�˵�������ҽ���ʱ����С��30�뼴�ж�EnterError,������true
			if (!isEnterError(lineId, node))
			{
				nextLine = nloper.getNextLine(node);
				for (int i = 0; i < nextLine.size(); i++)
				{
				
					long next = nextLine.get(i);
					//System.out.println(lineId + " connect " + next);
					if (next != lineId && currentMap.containsKey(next))
					{
						double currentDistant = pastMap.get(lineId) + currentMap.get(next);
						if (!pastMap.containsKey(next) || currentDistant < current.get(next))
						{
							current.put(next, currentDistant);
							lineEnter.put(next, list.size());
							LineConnect lct = new LineConnect();
							lct.set(lineId, next, _g.getCurrentTime(), node);
							//nloper.remove(Snodes.get(lineId));
							list.add(lct);
						}
					}
				}
			}
			//if (!current.containsKey(lineId)) nloper.remove(Snodes.get(lineId));
		}
		//System.out.println("Cycle in countLine.add " + debugInhm + " "+ System.currentTimeMillis());
		
		return current;
	}
	/*
	public HashMap<Long, Double> getConnection(HashMap<Long, Double> _past, HashMap<Long, Double> _current, GpsPointOfCar currentPoint, long _maxDistant)
	{
		//���������������Լ��ʱ��ϳ������
		int maxDistant = (int)_maxDistant;
		HashMap<Long, Double> current = new HashMap<Long, Double>(_past.size() * maxDistant * 4);//
		
		HashSet<Long> isExist = new HashSet<Long>(_past.size() * 4); //�ж�·��ID�Ƿ�����ڶ���
		ArrayList<QueueNode> queue = new ArrayList<QueueNode>();
		int font = 0, tail = _past.size();
		
		for (Iterator<Long> it = _past.keySet().iterator(); it.hasNext();) {
            long next = (long)it.next();
        	QueueNode qNode = new QueueNode(next, 0, -1);
        	queue.add(qNode);
        	isExist.add(next);
        	
        }
		
		while (font < tail)
		{
			//�������past��ʵ���е�next
			QueueNode fontNode = queue.get(font); //��ȡ���׽ڵ�
			if (_current.containsKey(fontNode.lineID))//���ƥ��ɹ�������ǰ���ѵ��Ľڵ�Id������_past
			{
				maxDistant = (maxDistant > fontNode.distant * 2 + 1) ? (fontNode.distant * 2 + 1) : maxDistant;
				//����LineConnect���󣬼���list.
				int currentLinePos = font; //���ڲ��ҵ�ǰ�ڵ����һ���ڵ���queue�е�λ��
				double distantOfCrossLine = 0; //���·��Ȩֵ����ֵ
				if (fontNode.distant > 0) //������������������0������ʼ·���л�
				{
					LineConnect lineConnect = new LineConnect(fontNode.distant - 1); //������·���Ӷ���
					currentLinePos = fontNode.pastID; 
					int tmpDistant = fontNode.distant -1;  //��������������1�����м��������·��������ΪtmpDistant
					while (currentLinePos >= 0 && tmpDistant > 0)//���м����·
					{
						QueueNode currentNode = queue.get(currentLinePos);//��ȡpastLinePosλ�õ�QueueNode����
						lineConnect.add(currentNode.lineID);//����LineCross��
						distantOfCrossLine++;//getLengthOfLine(currentNode.lineID);//�õ�LineCross�е�·�����ȣ���������Ȩֵ��ȱ��
						currentLinePos = currentNode.pastID; //����Ѱ�ҹ��ѹ����еĸ��ڵ㣬pastLineCross == -1 ��ʾѰ�ҽ���
						tmpDistant--;//tmpDistant == 0 ��ʾѰ�ҽ�������������� tmpDistant == 0 ʱ��pastLineCross ��ֵ���ڳ�ʼfont��tail֮��
					}
					//���ÿ�ʼ��·��������·ID���Լ�ת����ʱ��
					lineConnect.set(fontNode.lineID, queue.get(currentLinePos).lineID, currentPoint.getCurrentTime());
					//���뵽����
					countLine.list.add(lineConnect);
				}
				
				long lineInPastMap = fontNode.lineID;
				long lineInCurrentMap = queue.get(currentLinePos).lineID;
				double distantOfCurrentPointToLine = _past.get(lineInCurrentMap) + _current.get(lineInPastMap);//��ǰ���������Ȩֵ
				if (!_current.containsKey(lineInCurrentMap) || distantOfCurrentPointToLine < current.get(lineInCurrentMap))//��������ڻ���Ȩֵ����
				{
					current.put(lineInCurrentMap, distantOfCurrentPointToLine);
				}
			}
			
			//�������
			if (fontNode.distant <= maxDistant)
			{
			
			
				ArrayList<Long> pastLine = null;
				//pastLine = nloper.getNextLine(Long.toString(fontNode.lineID));//ȱ�� 
				for (int i = 0; i < pastLine.size(); i++)
					if (!isExist.contains(pastLine.get(i)))
					{
						isExist.add(pastLine.get(i));
						queue.add(new QueueNode(pastLine.get(i), fontNode.distant + 1, font));
					}
			}
		
		
		}
		
		return current;
		
		
		
	}*/
	
	private boolean isEnterError(long lineId, String node)
	{
		//�����·��ͬһ�˵�������ҽ���ʱ����С��30�뼴�ж�EnterError,������true
		int pos = 0;
		if (!lineEnter.containsKey(lineId)) return false;
		pos = lineEnter.get(lineId);
		//if (list.size() <= pos) System.out.println("Pos " + pos);
		if (!node.equals(list.get(pos).enterNode)) return false;
		/*if (lineId == 1159567340388L)
		{
			System.out.println(currentPoint.getCurrentTime() + " " + list.get(pos).seprateTime);
			System.out.println(currentPoint.getCurrentTime().compareTo(list.get(pos).seprateTime));
		}*/
		if (currentPoint.getCurrentTime().getTime() - list.get(pos).seprateTime.getTime() >= 200000) return false;
		return true;
	}
	
//	public static void main2(String[] args) {
//		// TODO Auto-generated method stub
//		//PATH = "E:/Data/run_track_20120401_1.csv";//
//		//System.out.println(System.currentTimeMillis());
//		
//		//File configFile = new File("./config.dat");
//		try {
//			BufferedReader reader = new BufferedReader(new FileReader(new File("./config.dat")));
//			PATH_OF_NEXTLINE_INDEX = reader.readLine();
//			PATH_OF_NEXTLINE = reader.readLine();
//			PATH_OF_LINK = reader.readLine();
//			PATH_DIR = reader.readLine();
//			reader.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			System.out.println("File config.dat not found.");
//			e.printStackTrace();
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		
//		
//		
//		
//		NextLineOper nextLineOper = null;//��ȡ��һ��
//		try {
//			nextLineOper = new NextLineOper(PATH_OF_NEXTLINE_INDEX, PATH_OF_NEXTLINE);
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		ZipData zd = new ZipData();
//		PATH = "./Data/GPSData/data/";
//		File folder = new File("./Data/GPSData/data/");
//		String[] files = folder.list();
//		for (int i = 0; i < files.length; i++)
//		{
//			if(files[i].startsWith(".")) continue;
//			zd.changeFile(PATH + files[i]);
//			while (zd.hasNextEntry())
//			{
//				System.out.println(System.currentTimeMillis());
//				ZipEntry ze = zd.getNextEntry();
//				if (ze.isDirectory())
//				{
//					File f = new File("./Data/GPSData/result/" + files[i].substring(0, files[i].indexOf('.')) + ze.getName());
//					if (!f.exists())
//					{
//						f.mkdirs();
//					}
//				}
//				else 
//				{
//					if (ze.getName().contains("link")) continue;
//					try {
//						countLine.m(zd.getInStream(ze), nextLineOper, "./Data/GPSData/result/" + files[i].substring(0, files[i].indexOf('.')) + ze.getName());
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//		//TimePicker.print();
//	//System.out.println(System.currentTimeMillis());
//	}

	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//PATH = "E:/Data/run_track_20120401_1.csv";//
		//System.out.println(System.currentTimeMillis());
		
		//File configFile = new File("./config.dat");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("./config.dat")));
			PATH_OF_NEXTLINE_INDEX = reader.readLine();
			PATH_OF_NEXTLINE = reader.readLine();
			PATH_OF_LINK = reader.readLine();
			PATH_DIR = reader.readLine();
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File config.dat not found.");
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//HashSet<String> can = new HashSet<>(); 
		
/*		BufferedReader r = new BufferedReader(new FileReader("E:/EclipseData/MapMatch/CAN.txt"));
		String s = null;
		while ((s = r.readLine()) != null)
		{
			can.add(s);
		}
		r.close();*/
		
		NextLineOper nextLineOper = null;//��ȡ��һ��
		try {
			nextLineOper = new NextLineOper(PATH_OF_NEXTLINE_INDEX, PATH_OF_NEXTLINE);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//ԭʼ��������·��    
		//"F:/NedoHdpOut/"ۧ������·��
		//ZipData zd = new ZipData();
		PATH = "./Data/GPSData/data/";//////////
		//PATH ="C://Users//Administrator//Desktop//data//";
		File folder = new File(PATH);
		System.out.println(folder.getAbsolutePath());
		String[] files = folder.list();
		for (int i = 0; i < files.length; i++)
		{
			//macOS���ܻ����һЩ�����ļ�������.DS_Store
			if(!files[i].startsWith("."))
			{
				String carID = files[i].substring(0, files[i].indexOf('-'));
				//if (!can.contains(carID)) continue;
				System.out.println(System.currentTimeMillis());
					//ZipEntry ze = zd.getNextEntry();
					{
						GZIPInputStream is = null;   
			            is = new GZIPInputStream(new FileInputStream(PATH + files[i]));
						countLine.m(is, nextLineOper, "./Data/GPSData/result/" +  carID + ".csv");///////////
						is.close();
					}
			}
			
			
		}
	//TimePicker.print();
	//System.out.println(System.currentTimeMillis());
	}
	public static void m(InputStream is, NextLineOper nextLineOper, String path)
	{

		//PATH = "D:/run_track_20120401_7.csv";
		errorNum = 0;
		
		String currentCarId = null;
		String currentBeginTime = null;
		String currentEndTime = null;
		Date currentTime = null;
		countLine counting = null;//�������
		//int debug = 0;
		
		try {
			CarInfoReader carInformReader = new CarInfoReader(is);
			while (carInformReader.getLine())
			{
				//debug++;
				//if (debug % 10000 == 0) System.out.println(debug + System.currentTimeMillis());
				if (!carInformReader.isGpsFormal()) continue;//�ж�GPS���Ƿ����shushu:�жϸ���GPS�ľ�γ���Ƿ������ֵ����Сֵ��Χ��
				if (currentCarId == null || !carInformReader.isTheSameCar(currentCarId, currentTime))//shushu:�Ƿ���Ҫ�Ͽ������¿�ʼһ���켣������ȡ�����ļ��ĵ�һ������ʱcurrentCarIDΪ�գ���Ҫ���¿�ʼһ���켣������ͨ��isTheSameCar()�����ж��Ƿ���Ҫ���½���һ���켣��
				{
					if (counting != null)
					{
						//tͨ��һ��null�㣬ʹ�켣�Ͽ���������һ���µĵ�ʱ������һ����û����ϵ����ִ��counting����
						counting.add(null);
						counting.printLine(path);
						counting = null;
					}
					//currentBeginTime = carInformReader.getBeginTime();
					currentCarId = carInformReader.getCarId();
					//currentEndTime = carInformReader.getEndTime();
					counting= new countLine(currentCarId, null, null, nextLineOper);
				}
				currentTime = carInformReader.getCurrentTime();
				//System.out.println()
				long t0 = System.currentTimeMillis();
				GpsPointOfCar gpt = carInformReader.getGpsPointOfCar();
				TimePicker.t5 += System.currentTimeMillis() - t0;
				counting.add(gpt);	
				
			}
			
			if (counting != null)
			{
				counting.add(null);
				counting.printLine(path);
				counting = null;
			}
		} catch (IOException e) {
			//  Auto-generated catch block
			e.printStackTrace();
			
		} catch (ParseException e) {
			//  TODO generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			//  �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (SAXException e) {
			//  �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		//TimeCount.print();
		}
	
	
	public static void main1(String[] args) {
		long t = System.currentTimeMillis();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File("./config.dat")));
			PATH_OF_NEXTLINE_INDEX = reader.readLine();
			PATH_OF_NEXTLINE = reader.readLine();
			PATH_OF_LINK = reader.readLine();
			PATH_DIR = reader.readLine();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File config.dat not found.");
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Nearline nl = new Nearline(PATH_OF_LINK);
		try {
			nl.test();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis() - t);
	}
}
