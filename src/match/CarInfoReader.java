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
	//shushu：原来的代码中，maxW-minJ四个变量是不除以changeNum的，但是在这种情况下GPS点是不可能在这四个变量规定的范围内的。这可能是因为原来的输入文件中经纬度的表示方法是不一样的。
	static final double maxW = 33/changeNum;//北京坐标：41
	static final double minW = 28/changeNum;//北京坐标：38
	static final double maxJ = 115/changeNum;//北京坐标：120
	static final double minJ = 105/changeNum;//北京坐标：110
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
//			//根据数据格式，获取相应信息。
//			return true;
//		}
//		else
//		{
//			reader.close();
//			return false;
//		}
//	}
	//读取并解析每一行GPS数据
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
			
			//根据数据格式，获取相应信息。
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
	
	//判断经纬度是不是在正常范围内
	public boolean isGpsFormal()
	{
		if (jd > minJ && jd < maxJ && wd > minW && wd <maxW) return true;
		else return false;
	}
	
	//判断tripId是不是相同
	public boolean isTheSameCar(String _carId, Date d) throws ParseException
	{
		if (carId.equals(_carId) && Math.abs(sdf.parse(currentTime).getTime() - d.getTime()) <= 10000) return true;
		else return false;
	}
	
	

}
