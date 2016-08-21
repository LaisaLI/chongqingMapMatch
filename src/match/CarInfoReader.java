package match;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CarInfoReader {

	/**
	 * @param args
	 */
	
	static final double changeNum = 3600 * 1000;
	////////////////
	//shushu��ԭ���Ĵ����У�maxW-minJ�ĸ������ǲ�����changeNum�ģ����������������GPS���ǲ����������ĸ������涨�ķ�Χ�ڵġ����������Ϊԭ���������ļ��о�γ�ȵı�ʾ�����ǲ�һ���ġ�
	static final double maxW = 33/changeNum;//�������꣺41
	static final double minW = 28/changeNum;//�������꣺38
	static final double maxJ = 115/changeNum;//�������꣺120
	static final double minJ = 105/changeNum;//�������꣺110
	/////////////////////////////
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
	
	private double jd = 0;
	private double wd = 0;
	private String carId = null;
	private String recordBeginTime = null;
	private String recordEndTime = null;
	private String currentTime = null;
	//private String carInformation = null;
	private BufferedReader reader;
	
	
	public CarInfoReader(String fileName) throws IOException
	{
		
		reader = new BufferedReader(new FileReader(new File(fileName)));
	}
	
	public CarInfoReader(InputStream is)
	{
		reader = new BufferedReader(new InputStreamReader(is));
	}
	
//	public boolean getLine() throws IOException
//	{
//		//System.out.println("Cycle in readCarInfo " + countLine.debug);
//		String carInformation = reader.readLine();
//		if (carInformation != null)
//		{
//			carInformation = carInformation.substring(carInformation.indexOf(',') + 1);
//			String[] s = carInformation.split(",");
//			if (s.length < 12) 
//			{
//				jd = wd = 0;
//				return true;
//			}
//			jd = Double.parseDouble(s[11])/changeNum;
//			wd = Double.parseDouble(s[10])/changeNum;
//			carId = s[0];
//			//recordBeginTime = s[1];
//			//recordEndTime = s[2];
//			currentTime = s[3] + "/" + s[4] + "/" + s[5] + "/" + s[6] + "/" + s[7] + "/" + s[8];
//			
//			//�������ݸ�ʽ����ȡ��Ӧ��Ϣ��
//			return true;
//		}
//		else
//		{
//			reader.close();
//			return false;
//		}
//	}
	//��ȡ������ÿһ��GPS����
	public boolean getLine() throws IOException
	{
		//System.out.println("Cycle in readCarInfo " + countLine.debug);
		String carInformation = reader.readLine();
		if (carInformation != null)
		{
			String[] s = carInformation.split(",");
			
			jd = Double.parseDouble(s[2])/changeNum;
			wd = Double.parseDouble(s[3])/changeNum;
			carId = s[0];
			//recordBeginTime = s[1];
			//recordEndTime = s[2];
			currentTime = s[1];
			
			//�������ݸ�ʽ����ȡ��Ӧ��Ϣ��
			return true;
		}
		else
		{
			reader.close();
			return false;
		}
	}
	
	public GpsPointOfCar getGpsPointOfCar() throws ParseException
	{
		return new GpsPointOfCar(this.getCurrentTime(), this.getWD(), this.getJD());
	}
	
	public double getJD()
	{
		return jd;
	}
	public double getWD()
	{
		return wd;
	}
	public String getCarId()
	{
		return carId;
	}
	public String getBeginTime()
	{
		return recordBeginTime;
	}
	public String getEndTime()
	{
		return recordEndTime;
	}
	public Date getCurrentTime() throws ParseException
	{
		return sdf.parse(currentTime);
	}
	
	//�жϾ�γ���ǲ�����������Χ��
	public boolean isGpsFormal()
	{
		if (jd > minJ && jd < maxJ && wd > minW && wd <maxW) return true;
		else return false;
	}
	
	//�ж�tripId�ǲ�����ͬ
	public boolean isTheSameCar(String _carId, Date d) throws ParseException
	{
		if (carId.equals(_carId) && Math.abs(sdf.parse(currentTime).getTime() - d.getTime()) <= 10000) return true;
		else return false;
	}
	
	

}
