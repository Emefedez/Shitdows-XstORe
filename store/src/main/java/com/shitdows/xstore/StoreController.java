package com.shitdows.xstore;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
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

                SwingUtilities.invokeLater(() -> {
                    listpanel.removePanel(loadPanel);

                    for (int i = 0; i < products.length; i++) {
                        JPanel productBox = createProductBox(products[i], i); // por cada caja creamos una caja y
                                                                              // decimos en que linea esta la url de
                                                                              // este programa
                        listpanel.addPanel(productBox, 160);
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

    private static JPanel createProductBox(listedItem product, int lineNumber) {
        JPanel box = new JPanel();
        box.setLayout(new BorderLayout(10, 10));
        box.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        box.setBackground(Color.WHITE);

        // Imagen
        JLabel iconLabel = new JLabel("...");
        iconLabel.setPreferredSize(new Dimension(72, 72));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setForeground(Color.GRAY);

        // Botón
        JButton idButton = new JButton("Pass ID to Downloader");
        idButton.addActionListener(e -> {
            try {      
                Main.setPackageText(product.getProductId());
            } catch (Exception ex) {
            }
        });

        JButton pnButton = new JButton("Pass Package name to Downloader");
        pnButton.addActionListener(e -> {
            try {
                Main.setPackageText(product.getMainPackageName().toString().replace("[", " ").replace("]", " ").replace("{", " ")
                .replace("}", " "));
            } catch (Exception ex) {
            }
        });

        // Panel izquierdo (icono + botón) -> SOLO un WEST
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(iconLabel, BorderLayout.NORTH);
        leftPanel.add(idButton, BorderLayout.CENTER);
        leftPanel.add(pnButton, BorderLayout.SOUTH);
        box.add(leftPanel, BorderLayout.WEST);

        // Cargar imagen en hilo separado
        new Thread(() -> {
            try {
                String imageUrl = getLineFromFile("urls.txt", lineNumber);
                if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                    loadImage(imageUrl, iconLabel);
                } else {
                    SwingUtilities.invokeLater(() -> {
                        iconLabel.setText("No Icon");
                        iconLabel.setIcon(null);
                    });
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> iconLabel.setText("Error"));
                e.printStackTrace();
            }
        }).start();

        // Info (deja tu código tal cual)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
        infoPanel.setBackground(Color.WHITE);

        String descText = product.getDescription();
        if (descText != null && descText.length() > 500) {
            descText = descText.substring(0, 500) + "...";
        }
        JLabel rating = new JLabel(product.getAverageRating() + " Stars");
        JLabel title = new JLabel("<html><font size='4'><b>" + product.getTitle() + "</b></font></html>");
        JLabel pub = new JLabel("<html><b>Dev:</b> " + product.getPublisherName() + "</html>");
        JLabel price = new JLabel(
                "<html><b>Price:</b> <font color='green'>" + product.getDisplayPrice() + "</font></html>");
        JLabel desc = new JLabel(
                "<html><body style='width: 250px; font-size: 9px; font-style: italic; color: DimGray;'>"
                        + descText + "</body></html>");
        JLabel installer = new JLabel("<html><b>Type:</b> " + product.getInstallerType() + "</html>");
        JLabel packageLabel = new JLabel(product.getMainPackageName().toString());
        JLabel pid = new JLabel(
                "<html><b>PID:</b> <font size='2' color='gray'>" + product.getProductId() + "</font></html>");

        String packageLabelClean = packageLabel.getText().replace("[", " ").replace("]", " ").replace("{", " ")
                .replace("}", " ");
        JLabel pkg = new JLabel(
                "<html><b>Pkg:</b> <font size='2' color='gray'>" + packageLabelClean + "</font></html>");

        infoPanel.add(title);
        infoPanel.add(pub);
        infoPanel.add(price);
        infoPanel.add(rating);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(installer);
        infoPanel.add(pkg);
        infoPanel.add(pid);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(desc);
        box.add(infoPanel, BorderLayout.EAST);

        return box;
    }

    // Lee una línea específica del archivo
    private static String getLineFromFile(String filePath, int lineNumber) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int currentLine = 0;
            while ((line = reader.readLine()) != null) {
                if (currentLine == lineNumber) {
                    return line.trim();
                }
                currentLine++;
            }
        } catch (Exception e) {
            System.err.println("Error reading line " + lineNumber + ": " + e.getMessage());
        }
        return null;
    }

    private static void loadImage(String urlStr, JLabel iconLabel) {
        try {
            
            BufferedImage img = ImageIO.read(new URL(urlStr));

            if (img != null) {
                Image scaled = img.getScaledInstance(72, 72, Image.SCALE_SMOOTH);
                SwingUtilities.invokeLater(() -> {
                    iconLabel.setIcon(new ImageIcon(scaled));
                    iconLabel.setText("");
                    System.out.println("Image loaded successfully from: " + urlStr);
                });
            } else {
                SwingUtilities.invokeLater(() -> iconLabel.setText("XXX"));
                System.err.println("Failed to load image from: " + urlStr);
            }
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> iconLabel.setText("Error"));
            System.err.println("Error loading image: " + e.getMessage());
        }
    }

    private static JFrame mainWindow;

    public static void setMainWindow(JFrame window) {
        mainWindow = window;
    }

    public static JFrame getMainWindow() {
        return mainWindow;
    }
}
