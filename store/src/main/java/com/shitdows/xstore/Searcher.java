package com.shitdows.xstore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

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

        if (ispackageid==true) { //funcion para paquete

        }

        else { //funcion para nombre de familia
             
        }
    }

    // Primero detectamos si es packageID o packageName
    public void boolchecker(String packageName) {
        if ((packageName.length() != 12) || packageName.contains(".")) {
            this.ispackageid = false; // no es un paquete
        }
        System.out.println("Is package ID: " + this.ispackageid);
    }
}
