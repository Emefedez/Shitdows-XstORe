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
        
        JPanel loadPanel = new JPanel();
        loadPanel.add(new JLabel("Loading, Please wait..."));
        loadPanel.setBackground(Color.decode("#39be46")); 
        listpanel.addPanel(loadPanel, 50);
        
        listpanel.revalidate();
        listpanel.repaint();


        new Thread(() -> {
            try {
                String json = ApiCaller.ApiSearch(query); 
                listedItem[] products = ProductParser.jsonParser(json);

                //cuando se termina la obtención de valores, volvemos al hilo principal y quitamos la caja de carga
                SwingUtilities.invokeLater(() -> {
                    listpanel.removePanel(loadPanel);

                    if (products.length == 0) {
                         JPanel err = new JPanel();
                         err.add(new JLabel("No results found."));
                         listpanel.addPanel(err, 50);
                    } else {
                        for (listedItem item : products) {
                            JPanel productBox = createProductBox(item);
                            listpanel.addPanel(productBox, 160);
                        }
                    }
                    listpanel.revalidate();
                    listpanel.repaint();
                });

            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    listpanel.removePanel(loadPanel);
                    JPanel errorPanel = new JPanel();
                    errorPanel.add(new JLabel("Error: " + e.getMessage()));
                    errorPanel.setBackground(Color.RED);
                    listpanel.addPanel(errorPanel, 50);
                });
                e.printStackTrace();
            }
        }).start();
    }

    private static JPanel createProductBox(listedItem product) {
        JPanel box = new JPanel();
        box.setLayout(new BorderLayout(10, 10));
        box.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        box.setBackground(Color.WHITE);

        // --- ICONO CORREGIDO ---
        JLabel iconLabel = new JLabel();
        iconLabel.setPreferredSize(new Dimension(84, 84)); // Un poco más grande para margen
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centrar imagen si es pequeña
        
        try {
            String urlStr = product.getIconUrl();
            
            if (urlStr != null && !urlStr.trim().isEmpty()) {
                // ✅ FIX: Si la URL empieza por "//", le agregamos "https:"
                if (urlStr.startsWith("//")) {
                    urlStr = "https:" + urlStr;
                }
                
                URL url = new URL(urlStr);
                BufferedImage img = ImageIO.read(url);
                
                if (img != null) {
                    Image scaledImg = img.getScaledInstance(72, 72, Image.SCALE_SMOOTH);
                    iconLabel.setIcon(new ImageIcon(scaledImg));
                } else {
                     iconLabel.setText("📷"); // Emoji si la imagen falla pero URL existe
                }
            } else {
                iconLabel.setText("No Icon");
            }
        } catch (Exception e) {
            iconLabel.setText("Err"); // Texto de error corto
        }
        
        JPanel iconContainer = new JPanel(new BorderLayout());
        iconContainer.setBackground(Color.WHITE);
        iconContainer.add(iconLabel, BorderLayout.NORTH);
        
        box.add(iconContainer, BorderLayout.WEST);

        // Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        String descText = product.getDescription();

        // Evitar descripciones demasiado largas
        if (descText != null && descText.length() > 500) {
            descText = descText.substring(0, 500) + "...";
        }

        JLabel title = new JLabel("<html><font size='4'><b>" + product.getTitle() + "</b></font></html>");
        JLabel pub = new JLabel("<html><b>Dev:</b> " + product.getPublisherName() + "</html>");
        JLabel price = new JLabel("<html><b>Price:</b> <font color='green'>" + product.getDisplayPrice() + "</font></html>");
        JLabel desc = new JLabel("<html><body style='width: 250px; font-size: 9px; font-style: italic; color: DimGray;'>"
                + descText + "</body></html>"); //html sirve para no meter todo en una linea
        JLabel installer = new JLabel("<html><b>Type:</b> " + product.getInstallerType() + "</html>");
        JLabel packageLabel = new JLabel(product.getMainPackageName().toString());
        JLabel pid = new JLabel("<html><b>PID:</b> <font size='2' color='gray'>" + product.getProductId() + "</font></html>");

        String packageLabelClean = packageLabel.getText().replace("[", " ").replace("]", " ").replace("{", " ").replace("}", " ");
        JLabel pkg = new JLabel("<html><b>Pkg:</b> <font size='2' color='gray'>" + packageLabelClean + "</font></html>"); //version a usar


        

        // Agregar al panel
        infoPanel.add(title);       
        infoPanel.add(pub);
        infoPanel.add(price);
        infoPanel.add(Box.createVerticalStrut(5)); 
        infoPanel.add(installer);
        infoPanel.add(pkg);
        infoPanel.add(pid);
        infoPanel.add(Box.createVerticalStrut(5)); 
        infoPanel.add(desc);        
        box.add(infoPanel, BorderLayout.CENTER);

        // Botón
        JButton dlBtn = new JButton("Get");
        dlBtn.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://apps.microsoft.com/detail/" + product.getProductId()));
            } catch (Exception ex) {
            }
        });
        box.add(dlBtn, BorderLayout.SOUTH);

        return box;
    }

}
