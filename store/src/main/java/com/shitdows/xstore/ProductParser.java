package com.shitdows.xstore;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class ProductParser {
    public static listedItem[] jsonParser(String jsonString) {
        List<listedItem> productsList = new ArrayList<>();

        try {
            Gson gson = new Gson();
            JsonObject root = gson.fromJson(jsonString, JsonObject.class);
            JsonArray productsArray = root.getAsJsonArray("productsList"); //donde comienza la parte del json interesante
            System.out.println("Found Apps: "+ productsArray.size());

            for (int i=0; i<productsArray.size(); i++) {
                JsonObject itemJson = productsArray.get(i).getAsJsonObject(); //una vez tenemos el json, recolectamos cada miembro del array como su propio elemento
                listedItem item = gson.fromJson(itemJson, listedItem.class);

                //caso para installer pues installer es un objeto
                if (itemJson.has("installer")) {
                JsonObject installer = itemJson.getAsJsonObject("installer");
                String installerType = installer.get("type").getAsString();
                item.setInstallerType(installerType); 
                }
                

                //si da error no se copia el item
                if (item != null && item.getTitle() != null && !item.getTitle().isEmpty()) {
                    productsList.add(item);
                    System.out.println("Added item:" + item.getTitle());
                }


            }

        
        }
        catch (Exception e) {
        System.err.println("Error parsing JSON: " + e.getMessage());
        e.printStackTrace();
        }
        
        listedItem[] resultArray = new listedItem[productsList.size()];
        return productsList.toArray(resultArray);
    }

}
