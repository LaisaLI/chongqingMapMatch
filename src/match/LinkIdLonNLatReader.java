package match;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/8/29.
 */
public class LinkIdLonNLatReader {
    private HashMap<String,String> linkIdLonNLatMap =null;//用于存储nodeid和对应的经纬度，大约30万个

    public LinkIdLonNLatReader() {
        try {
            linkIdLonNLatMap = new HashMap<String, String>();
            readLinkIdLonNLatMap();
            System.out.println(linkIdLonNLatMap.size());
        }
        catch (IOException e){
            System.out.print(e.getMessage());
        }
    }


    private void readLinkIdLonNLatMap() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File("./Data/chongqingLinkIdWithLonNLat.txt")));
        String line = null;
        while ((line = reader.readLine()) != null){
            String[] lineArray  = line.split(",");
            linkIdLonNLatMap.put(lineArray[0],lineArray[1]+","+lineArray[2]+","+lineArray[3]+","+lineArray[4]);
        }

    }

    public String getLonNLatAsString(String nodeId){
        return linkIdLonNLatMap.get(nodeId);
    }

}
