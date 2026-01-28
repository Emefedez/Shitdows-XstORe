package com.shitdows.xstore;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class StoreController {
    public static void searchAndManageUI(String query, ListPanel listpanel) {
        listpanel.clearPanels();
        try {
            JPanel loadPanel = new JPanel();
            loadPanel.add(new JLabel("⏳ Loading, Please wait..."));
            loadPanel.setBackground(Color.decode("#0c8318")); 
            listpanel.addPanel(loadPanel, 50);

            String json = ApiCaller.ApiSearch(query); //primero paso el query al ApiCaller y json se vuelve el return de la salida
            listedItem[] products = ProductParser.jsonParser(json); //metemos el json a leer en jsonParser
            listpanel.removePanel(loadPanel);


            for (listedItem item : products) {
                JPanel productBox = createProductBox(item);
                listpanel.addPanel(productBox, 120);
            }
           

        }
        catch (Exception e) {
            JPanel errorPanel = new JPanel();
            errorPanel.add(new JLabel("Unknown Error: " + e.getMessage()));
            errorPanel.setBackground(Color.decode("#a12b2b")); 
            listpanel.addPanel(errorPanel, 50);
            e.printStackTrace();
        }
    }

    private static JPanel createProductBox(listedItem product) {
        JPanel box = new JPanel();
        box.setLayout(new BorderLayout(10, 10));
        box.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        box.setBackground(Color.WHITE);

        // Icono
        JLabel iconLabel = new JLabel();
        iconLabel.setPreferredSize(new Dimension(80, 80));
        try {
            if (product.getIconUrl() != null && !product.getIconUrl().isEmpty()) {
                URL url = new URL(product.getIconUrl());
                BufferedImage img = ImageIO.read(url);
                Image scaledImg = img.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new ImageIcon(scaledImg));
            }
        } catch (Exception e) { iconLabel.setText("No Icon"); }
        box.add(iconLabel, BorderLayout.WEST);

        // Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("<html><b>" + product.getTitle() + "</b></html>");
        JLabel pub = new JLabel(product.getPublisherName());
        JLabel desc = new JLabel(product.getDescription());
        JLabel price = new JLabel("Price: " + product.getDisplayPrice());
        JLabel installer = new JLabel(product.getInstallerType());
        JLabel packageLabel = new JLabel(product.getMainPackageName().toString());
        JLabel pid = new JLabel(product.getProductId());

        String packageLabelClean = packageLabel.getText().replace("[", " ").replace("]", " ");
        
        infoPanel.add(price);
        infoPanel.add(title);
        infoPanel.add(pub);
        infoPanel.add(new JLabel("Installer Type: " + installer.getText()));
        infoPanel.add(new JLabel("Package: " + packageLabelClean.trim()));
        infoPanel.add(new JLabel("PID: " + pid.getText()));
        infoPanel.add(desc);
        box.add(infoPanel, BorderLayout.CENTER);

        // Botón
        JButton dlBtn = new JButton("Get");
        dlBtn.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://apps.microsoft.com/detail/" + product.getProductId()));
            } catch (Exception ex) {}
        });
        box.add(dlBtn, BorderLayout.EAST);

        return box;
    }

}
