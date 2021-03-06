package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings, and which values should be encoded as integers!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            JSONObject json = new JSONObject();
            JSONArray colHeaders = new JSONArray();
            JSONArray rowHeaders = new JSONArray();
            JSONArray data = new JSONArray();
            
            int c = 0;
            int r = 0;
            
            String[] colHeadersCSV = iterator.next();
            for (int i = 0; i < colHeadersCSV.length; i++){
                colHeaders.add(colHeadersCSV[i]);
            }
            
            while (iterator.hasNext()){
                
                JSONArray ints = new JSONArray();
                String[] rowCSV = iterator.next();
                rowHeaders.add(rowCSV[r]);
                for( int i = 1; i < rowCSV.length; i++ ){
                    ints.add(Integer.valueOf(rowCSV[i]));
                }
                data.add(ints);
                
            }
            
            json.put("colHeaders", colHeaders);
            json.put("rowHeaders", rowHeaders);
            json.put("data", data);
            
            results = String.valueOf(json);
            
        }        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            // parse json data
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject)parser.parse(jsonString);
            
            // getting json data
            JSONArray colHeaders = (JSONArray)json.get("colHeaders");
            JSONArray rowHeaders = (JSONArray)json.get("rowHeaders");
            JSONArray data = (JSONArray)json.get("data");
            
            String[] colHeadersArray = new String[colHeaders.size()];
            String[] rowHeadersArray = new String[rowHeaders.size()];
            String[] dataArray = new String[data.size()];
            
            for ( int i = 0; i < colHeaders.size(); i++ ) {
                colHeadersArray[i] = colHeaders.get(i).toString();
            }
            
            csvWriter.writeNext(colHeadersArray);
            
            for ( int i = 0; i < rowHeaders.size(); i++ ){
                rowHeadersArray[i] = rowHeaders.get(i).toString();
                dataArray[i] = data.get(i).toString();
            }
            
            for ( int i = 0; i < dataArray.length; i++){
                
                JSONArray dataVal = (JSONArray)parser.parse(dataArray[i]);
                String[] row = new String[dataVal.size() + 1];
                
                row[0] = rowHeadersArray[i];
                
                for ( int j = 0; j < dataVal.size(); j++ ){
                    row[j+1] = dataVal.get(j).toString();
                }
                
                csvWriter.writeNext(row);
                
            }
            
            results = writer.toString();
            
        }
        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }

}