package com.shitdows.xstore;


import java.awt.Color;
import java.awt.Dimension;
import javax.swing.*;

public class Main {
        private static ListPanel listPanel;
        private static JFrame frame;
        private static JTextField searchPackage;
        

        public static void main(String[] args) {
            
        frame = new JFrame("Shitdows-XstORe by Emefedez"); // Creando instancia de JFrame
        StoreController.setMainWindow(frame);

        frame.setResizable(false);
        //Definiendo icono para el programa
        try {
            ImageIcon icono = new ImageIcon("icon.png");
            frame.setIconImage(icono.getImage());
            System.out.println("Icon loaded");
        } catch (Exception e) {
            System.err.println("Icon missing: " + e.getMessage());
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.decode("#00b0b9"));

        JButton searchButton = new JButton(); // Creando un botón
        JButton packageButton = new JButton(); // Creando un botón

        JTextField searchField = new JTextField(); // Creando un campo de búsqueda
        searchPackage = new JTextField();

        //Botón de busqueda
        searchButton.setBounds(130, 100, 100, 40);
        searchButton.setBackground(Color.lightGray);
        searchButton.setText("Search");
        searchButton.setToolTipText("Type your APPs name!!");


        //Botón para paquete
        packageButton.setBounds(130, 100, 100, 40);
        packageButton.setBackground(Color.GRAY);
        packageButton.setText("↓↓↓");
        packageButton.setForeground(Color.WHITE);
        packageButton.setToolTipText("Download");

        //Barra de busqueda por nombre
        String placeholder = "Search...";
        searchField.setBounds(10, 100, 100, 40);
        searchField.setBackground(Color.lightGray);
        searchField.setForeground(Color.GRAY);
        searchField.setText(placeholder);


        //Barra de busqueda por paquete
        String placeholderPackage = "Search (package ID or package name)..."; //si tiene puntos es packagename, si no es packageID
        searchPackage.setBounds(10, 500, 100, 40);
        searchPackage.setBackground(Color.GRAY);
        searchPackage.setForeground(Color.WHITE);
        searchPackage.setText(placeholderPackage);


        //List Panel y campo scrolleable, aqui saldran las apps.
        listPanel = new ListPanel();
        listPanel.setBackground(Color.lightGray);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBounds(10, 70, 570, 530); // Posición y tamaño
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setBackground(Color.GRAY);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        

        searchField.addKeyListener(
            new java.awt.event.KeyAdapter() {
                @Override
                public void keyTyped(java.awt.event.KeyEvent e) {
                    if (searchField.getText().equals(placeholder)) {
                        searchField.setText("");
                        searchField.setForeground(Color.BLACK);
                    }
                }
            }
        );

        // Listener para searchPackage
        searchPackage.addKeyListener(
            new java.awt.event.KeyAdapter() {
                @Override
                public void keyTyped(java.awt.event.KeyEvent e) {
                    if (searchPackage.getText().equals(placeholderPackage)) {
                        searchPackage.setText("");
                        searchPackage.setBackground(Color.WHITE);
                        searchPackage.setForeground(Color.BLACK);
                    }
                }
            }
        );

        // Añadiendo el botón y datos al marco
        searchButton.setBounds(500, 28, 80, 29);
        searchField.setBounds(10, 28, 490, 29);

        packageButton.setBounds(500, 5, 80, 20);
        searchPackage.setBounds(10, 5, 490, 20);

        frame.add(searchField);
        frame.add(searchPackage);
        frame.add(searchButton);
        frame.add(packageButton);
        frame.add(scrollPane);


        //Para quye la barra de busqueda funcione
        searchButton.addActionListener(e -> {
            String query = searchField.getText();
            
            StoreController.searchAndManageUI(query, listPanel);
        });
        
        // Para que funcione al dar Enter en la caja de texto tmb
        searchField.addActionListener(e -> searchButton.doClick());

        // para la barra de paquete
        packageButton.addActionListener(e -> {
            
            JPanel loadPanel = new JPanel();
            loadPanel.add(new JLabel("Loading, Please wait..."));
            loadPanel.setBackground(Color.decode("#39be46"));
            loadPanel.setOpaque(true);
            loadPanel.setPreferredSize(new Dimension(540, 50));

            // definir posicion concreta del panel, si no ocupa toda la lista
            listPanel.add(loadPanel, 0);
            listPanel.revalidate();
            listPanel.repaint();

            // Ejecuta la tarea en background para poder poner bloque de carga
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    System.out.println("Sending to Searcher: " + searchPackage.getText());
                    new Searcher(searchPackage.getText());
                    return null;
                }

                @Override
                protected void done() {
                    listPanel.remove(loadPanel);
                    listPanel.revalidate();
                    listPanel.repaint();
                    new ProgramParser();
                    new DownloadManager(searchPackage.getText());
                }
            }.execute();
        });
        
        // Para que funcione al dar Enter en la caja de texto tmb
        searchPackage.addActionListener(e -> packageButton.doClick());


        // Configuración del JFrame
        frame.setSize(600, 650);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public static void setPackageText(String text) {
        if (searchPackage == null) {
            return;
        }
        SwingUtilities.invokeLater(() -> searchPackage.setText(text));
    }
}
