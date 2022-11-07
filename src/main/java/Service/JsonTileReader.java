package Service;


import java.io.*;
import java.nio.file.Files;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class JsonTileReader {

    // see


    public JsonTileReader() throws IOException {
        File file = new File(
                JsonTileReader.class.getResource("/doolhof.json").getFile()
        );
        FileReader fileReader = new FileReader(file);



        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String resultContent="";
        String content;
        while ((content=bufferedReader.readLine()) != null) {
            resultContent = resultContent + content;
        }

        System.out.println(resultContent);








        //https://devqa.io/how-to-parse-json-in-java/

    }



}
