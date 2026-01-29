package com.shitdows.xstore;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ProductParser {
    
    private static final Object fileLock = new Object(); //objeto que impide escribir con varios hilos a la vez

    public static listedItem[] jsonParser(String jsonString) {
        List<listedItem> productsList = new ArrayList<>();

        // Limpiar archivo antes de empezar
        File urls = new File("urls.txt");
        if (urls.exists()) {
            System.out.println("Url file already existed, deleting it...");
            if (!urls.delete()) {
                try { new PrintWriter(urls).close(); } catch (IOException e) {}
            }
        }

        try {
            Gson gson = new Gson();
            JsonObject root = gson.fromJson(jsonString, JsonObject.class);
            JsonArray productsArray = root.getAsJsonArray("productsList");
            System.out.println("Found Apps: "+ productsArray.size());

            for (int i=0; i<productsArray.size(); i++) {
                JsonObject itemJson = productsArray.get(i).getAsJsonObject();
                listedItem item = gson.fromJson(itemJson, listedItem.class);

                if (item != null && item.getTitle() != null && !item.getTitle().isEmpty()) {
                    productsList.add(item);
                    System.out.println("Added item:" + item.getTitle());
                }

                if (itemJson.has("installer")) {
                    JsonObject installer = itemJson.getAsJsonObject("installer");
                    String installerType = installer.get("type").getAsString();
                    item.setInstallerType(installerType); 
                }

                if (itemJson.has("images")) {
                   new Thread(() -> {
                       parseImages(itemJson, item);
                   }).start(); 
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
        }

        listedItem[] resultArray = new listedItem[productsList.size()];
        return productsList.toArray(resultArray);
    }

    private static void parseImages(JsonObject itemJson, listedItem item) {
        JsonArray imagesArray = itemJson.getAsJsonArray("images");
        boolean logoFound = false;
        
        for (int j = 0; j < imagesArray.size() && !logoFound; j++) {
            JsonObject imageObj = imagesArray.get(j).getAsJsonObject();
            if (imageObj.has("imageType") && imageObj.get("imageType").getAsString().equals("logo")) {
                String iconUrl = imageObj.has("url") ? imageObj.get("url").getAsString() : "";
                item.setIconUrl(iconUrl);
                System.out.println("\nLogo found for: " + item.getTitle() + " -> " + iconUrl);
                logoFound = true;
                
                //Escribe un solo hilo a la vez
                synchronized (fileLock) {
                    try (PrintWriter writer = new PrintWriter(new FileWriter("urls.txt", true))) {
                        writer.println(iconUrl);
                    } catch (IOException e) {
                        System.err.println("Error writing URL to file: " + e.getMessage());
                    }
                }
            }
        }
    }
}
