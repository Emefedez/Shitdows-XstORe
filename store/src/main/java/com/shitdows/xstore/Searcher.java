package com.shitdows.xstore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

public class Searcher {
    private boolean ispackageid = true; // Variable de instancia
    private String packageName; // Variable de instancia

    public Searcher(String packageName) { // constructor
        this.packageName = packageName;
        searcher(packageName);
    }

    public void searcher(String packageName) {
        File downloadLinks = new File("downloadLinks.txt");
        if (downloadLinks.exists()) {
            System.out.println("Url file already existed, deleting it...");
            if (!downloadLinks.delete()) {
                try { new PrintWriter(downloadLinks).close(); } catch (IOException e) {}
            }
        }
        boolchecker(packageName);
        try (PrintWriter writer = new PrintWriter(new FileWriter("downloadLinks.txt"))) {
            writer.println(this.getStoreFiles());
            
        } catch (Exception e) {
            System.out.println("Error getting store files: " + e.getMessage());
            e.printStackTrace();
        }

        
    }

    // Primero detectamos si es packageID o packageName
    public void boolchecker(String packageName) {
        if ((packageName.length() != 12) || packageName.contains(".")) {
            this.ispackageid = false; // no es un paquete
        }
        System.out.println("Is package ID: " + this.ispackageid);
    }

    public String getStoreFiles() throws Exception {
        String resolvedType = ispackageid ? "ProductId" : "PackageFamilyName";
        String encoded = URLEncoder.encode(packageName, StandardCharsets.UTF_8);
        String formData = String.format("type=%s&url=%s&ring=%s&lang=%s",
                resolvedType, encoded, "RP", "en-EN");

        System.out.println("type=" + resolvedType);
        System.out.println("url=" + packageName);

        //construimos request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://store.rg-adguard.net/api/GetFiles"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Origin", "https://store.rg-adguard.net")
                .header("Referer", "https://store.rg-adguard.net/")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        //mandamos request y obtenemos respuesta
        HttpResponse<String> response =
                HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("HTTP " + response.statusCode());
        return response.body();
    }
}
