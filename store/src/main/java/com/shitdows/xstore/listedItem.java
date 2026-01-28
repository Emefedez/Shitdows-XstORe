package com.shitdows.xstore;

import java.util.List; // Asegúrate de importar esto arriba

public class listedItem {


    //sobre app
    private String title;
    private String publisherName;
    private String description;
    private String iconURL;
    private String displayPrice;
    private double averageRating;



    //datos de paquete
    private String productId; //mismo que el ID del instalador
    private List<String> packageFamilyNames;
    private String installerType;

    public listedItem() { //para que listedItems no tenga datos basura, igualmente usamos gson
    }
    
    public void setInstallerType(String type) {
    this.installerType = type;
    }




    //Getters necesarios para que desde fuera puedan llamar a listedItems
    public String getTitle() { return title; }
    public String getPublisherName() { return publisherName; }
    public String getDescription() { return description; }
    public String getIconUrl() { return iconURL; }
    public String getDisplayPrice() { return displayPrice; }
    public double getAverageRating() { return averageRating; }
    public String getProductId() { return productId; }
    public String getInstallerType() { return installerType; }
    public List<String> getMainPackageName() { return packageFamilyNames; }
    

}
