package match;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipData {
	
	private ZipFile zipfile = null;
	Enumeration<? extends ZipEntry> entries = null;
	ZipEntry presentEntry = null;
	
	private static final int buffer = 2048;  
    
	public ZipData(File file)
	{
		try {
			zipfile = new ZipFile(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		entries = zipfile.entries();
	}
	
	public ZipData(String zipPath)
	{
		try {
			
			zipfile = new ZipFile(zipPath);
			if (zipfile == null) System.out.println("null");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		entries = zipfile.entries();
	}
	public ZipData()
	{
		
	}
	public void changeFile(String path)
	{
		
		try {
			if (zipfile != null) zipfile.close();
			zipfile = new ZipFile(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		entries = zipfile.entries();
	}
	
	protected InputStream getInStream() throws IOException
	{
		if (!entries.hasMoreElements()) return null;
		presentEntry = entries.nextElement();
		return zipfile.getInputStream(presentEntry);
	}
	 
	public InputStream getInStream(ZipEntry entry) throws IOException
	{
		return zipfile.getInputStream(entry);
	}
	
	public ZipEntry getPresentEntry()
	{
		//presentEntry.getName();
		return presentEntry;
	}
	
	public ZipEntry getNextEntry()
	{
		return entries.nextElement();
	}
	
	public void unZip(String path,int newproperty)
	{
		
	}
	
	public boolean hasNextEntry()
	{
		return entries.hasMoreElements();
	}
	
	 public static void unZip(String path)  
	 {  
	        int count = -1;  
	        int index = -1;  
	        String savepath = "";  
	        boolean flag = false;  
	          
	        File file = null;   
	        InputStream is = null;    
	        FileOutputStream fos = null;    
	        BufferedOutputStream bos = null;  
	          
	        savepath = path.substring(0, path.lastIndexOf("\\")) + "\\";  
	  
	        try  
	        {   
	            ZipFile zipFile = new ZipFile(path);   
	  
	            Enumeration<? extends ZipEntry> entries = zipFile.entries();  
	              
	            while(entries.hasMoreElements())  
	            {   
	                byte buf[] = new byte[buffer];   
	                  
	                ZipEntry entry = (ZipEntry)entries.nextElement();   
	                  
	                String filename = entry.getName();  
	                index = filename.lastIndexOf("/");  
	                if(index > -1)  
	                    filename = filename.substring(index+1);  
	                  
	                filename = savepath + filename;  
	                  
	                flag = isPics(filename);  
	                if(!flag)  
	                    continue;  
	                  
	                file = new File(filename);   
	                file.createNewFile();  
	                  
	                is = zipFile.getInputStream(entry);   
	                  
	                fos = new FileOutputStream(file);   
	                bos = new BufferedOutputStream(fos, buffer);  
	                  
	                while((count = is.read(buf)) > -1)  
	                {   
	                    bos.write(buf, 0, count );   
	                }   
	                  
	                fos.close();   
	  
	                is.close();   
	            }   
	              
	            zipFile.close();   
	              
	        }catch(IOException ioe){   
	            ioe.printStackTrace();   
	        }   
	 }   
	  
	    public static boolean isPics(String filename)  
	    {  
	        boolean flag = false;  
	          
	        if(filename.endsWith(".jpg") || filename.endsWith(".gif")  || filename.endsWith(".bmp") || filename.endsWith(".png"))  
	            flag = true;  
	          
	        return flag;  
	    }  
	
}
