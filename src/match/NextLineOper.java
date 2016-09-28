package match;

//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class NextLineOper {
	public static int LINE = 62;//LYL：代表NextLineResult函数一行的字符数，没有经纬度的是43，有经纬度的是62
	private String path1,path2;
	private File f1;
	private String preGridId = null;
	//private BufferedWriter bwr;
	private HashMap<String, Integer> map = null;
	private HashMap<String, ArrayList<Long>> record = null;
	private ArrayList<Long> arl= null;
	RandomAccessFile raf = null;
	//这个构造函数未被使用
	public NextLineOper() throws FileNotFoundException{
		//this.path1 = "E:/Data/mapIndex.txt";///////////
		//this.path2 = "E:/Data/NextLineResult.txt";///////////
	//	this.path1 = "D:/EclipseWS/MapMatch/Data/mapIndex.txt";///////////
	//	this.path2 = "D:/EclipseWS/MapMatch/Data/NextLineResult.txt";///////////
		this.path1 = "G://Workplace//MapMatch//Data//mapIndex.txt";//�Ѹ�Ϊ��config�ļ��ж�ȡ
		this.path2 = "G://Workplace//MapMatch//Data//NextLineResult.txt";//�Ѹ�Ϊ��config�ļ��ж�ȡ
		
		loadWorkMap();
		raf = new RandomAccessFile(path2,"r");
		record = new HashMap<String, ArrayList<Long>>();
	}
	public NextLineOper(String p1,String p2) throws FileNotFoundException{
		this.path1 = p1;
		this.path2 = p2;
		loadWorkMap();
		raf = new RandomAccessFile(path2,"r");
		record = new HashMap<String, ArrayList<Long>>();
	}
	//���ɹ�����ϣ��
	//�ؼ��֣��ļ��е�ƫ��ֵ
	public void loadWorkMap(){
		String temp;
		map = new HashMap<String,Integer>(256);
		arl = new ArrayList<Long>(256);
//		map = new HashMap<String,Integer>();
//		arl = new ArrayList<Long>();
		int num = 0;
		Scanner scan = openFile(path1);
		while(scan.hasNext()){
			temp = scan.nextLine();
			map.put(temp.split(",")[0], num);
			arl.add(Long.parseLong(temp.split(",")[1]) / LINE);
			num++;
		}
		//return map;
	}
	//����һ��Scanner��
	public Scanner openFile(String path){
		Scanner scanner = null;
		f1 = new File(path);
		if(f1.exists() && !f1.isDirectory()){
			try {
				scanner = new Scanner(new FileReader(f1));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println(path + "��ʧ�ܣ�" + "----" + e.toString());
			}
		}
		return scanner;
	}
	//

/*	public void setPosition(String sPath,String dPath){
		//RandomAccessFile raf;
		String key="",mapTemp,temp;
		long position = 0;
		try {
			//raf = new RandomAccessFile(sPath,"r");
			bwr = new BufferedWriter(new FileWriter(new File(dPath)));
			while((temp = raf.readLine())!=null){
				mapTemp = temp.split(",")[0];
				if(!mapTemp.equals(key)){
					position = raf.getFilePointer() - 34;
					key = mapTemp;
					//System.out.println(key + "," + position);
					bwr.write(key + "," + position);
					bwr.newLine();
					bwr.flush();
				}
			}
			//raf.close();
			bwr.close();
			System.out.println("OK!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(sPath + "��ʧ�ܣ�" + "----" + e.toString());
		}
	}*/
	
	
	
	public long twoDivide(String _node) throws IOException
	{
		if (_node.length() < 7) return -1;
		int posInArl = map.get(_node.substring(0, 6));//posInArl��¼submap��λ�ã�
		if (preGridId != null && !preGridId.equals(_node.substring(0, 6))) record.clear();
		Long node = Long.parseLong(_node);
		long font = arl.get(posInArl);//font��¼submap��node�ĸ�����
		long tail =  0;
		//long offset = 0;
		if (posInArl == arl.size() - 1)
		{
			tail = raf.length()/ LINE - 1;
		}
		else 
		{
			tail = arl.get(posInArl + 1) - 1;
		}
		long mid = 0;
		
		if (node > getNodeId(tail * LINE)) return -1;
		//�ҳ�kʹ��getNodeId(k*LINE)>=node>getNodeId((k-1)*LINE)
		while (font < tail)
		{
			mid = (font + tail) / 2;
			if (node > getNodeId(mid * LINE))
			{
				font = mid + 1;
			}
			else 
			{
				tail = mid;
			}
		}
		if (font != tail) System.out.println("twoDivideError!");
		
		return tail;
	}
	private long getNodeId(long offset) throws IOException
	{
		raf.seek(offset);
		String temp = raf.readLine();
		return Long.parseLong(temp.split(",")[1]);
		
	}
	
	public ArrayList<Long> getNextLine(String node)
	{
		if (!record.containsKey(node)) 
			record.put(node, getNext(node));
		return record.get(node);
	}
	
	public void remove(String nodes)
	{
		if (nodes == null) return;
		if (nodes.contains(" "))
		{
			record.remove(nodes.split(" ")[0]);
			record.remove(nodes.split(" ")[1]);
		}
		record.remove(nodes);
	}
	
	public ArrayList<Long> getNext(String Snode)
	{
		long t = System.currentTimeMillis();
		ArrayList<Long> result = new ArrayList<Long>();
		try {
		long offset = 0;
		if (Snode != null)
		{
			offset = twoDivide(Snode);//��������
		}
		else offset = -1;
		//System.out.println(offset);
		String temp = null;
		if (offset >= 0){ 
		raf.seek(offset * LINE);
		while ((temp = raf.readLine()) != null)
		{
			String tempLinkId = temp.split(",")[3].trim();

			if (!temp.split(",")[1].equals(Snode)) break;
			if (!temp.split(",")[2].equals("3")) result.add(Long.parseLong(tempLinkId));
		}
		}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TimePicker.t4 += System.currentTimeMillis() - t;
		return result;
	}
	/*public ArrayList<Long> getNextLine(HashMap<String,Long> map,String sPath,String node){
		
		Long offset = map.get(node.substring(0, 6));
		
		//System.out.println(node.substring(0, 6));
		
		String temp;
		ArrayList<Long> result = new ArrayList<Long>();
		try {
			//raf = new RandomAccessFile(sPath,"r");
			raf.seek(offset);
			while((temp = raf.readLine())!= null){
				//System.out.println(temp);
			    if(temp.split(",")[1].equals(node) && !temp.split(",")[2].equals("2")){
			    	result.add(Long.parseLong(temp.split(",")[3]));	
			    }
			    if(Long.parseLong(temp.split(",")[1]) > Long.parseLong(node)) break;
			}
			//raf.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(sPath + "��ʧ�ܣ�" + "----" + e.toString());
		}
		return result;
	}
	//public ArrayList<Long> getNextLine1(HashMap<String, Long> map, String sPath, String node);
	public ArrayList<Long> getNextLine(Long _lineId)
	{
		return getNextLine(map, "E:/Data/NextLineResult2.txt" , _lineId.toString());
	}*/
}
