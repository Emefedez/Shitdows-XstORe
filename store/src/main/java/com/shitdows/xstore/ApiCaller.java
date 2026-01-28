package com.shitdows.xstore;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.PrintWriter;



public class ApiCaller {
    public static String ApiSearch (String query) throws IOException { //Esto generará un json que leeremos
    String cleanQuery = query.trim();
    String Base_Url1half = "https://apps.microsoft.com/api/products/search?query=";
    String Base_Url2half = "&mediaType=all&age=all&price=all&category=all&subscription=all&cursor=&gl=ES&hl=en-ES&exp=1811522136";

    if (cleanQuery.endsWith("\n")) {
    cleanQuery = StringUtils.chop(cleanQuery); //Quita el caracter de enter si existe
    }

    cleanQuery = cleanQuery.replace(" ", "%20"); //Lo que se usa en vez de espacios en URLs

    String Final_Url = (Base_Url1half + cleanQuery + Base_Url2half).trim();

    try (PrintWriter writer = new PrintWriter(new FileWriter("lastList.json"))) { //Intentamos obtener la URL y la escribimos a un log.txt 
    Document log = Jsoup.connect(Final_Url).ignoreContentType(true).get();
    String jsonText = log.body().text();
   
   
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Object jsonObject = gson.fromJson(jsonText, Object.class);
    String prettyJson = gson.toJson(jsonObject);


    
    System.out.println("URL: " + Final_Url);
    System.out.println("API answer written to lastList.json\n");  
    writer.println(prettyJson);
    return prettyJson;
 


    } catch (IOException e) {
        System.err.println("Error loading URL...");
        throw e;
        }
    }

    
    public static void main(String[] args) throws IOException {
        ApiSearch("chatGPT"); 
    }
        

}
