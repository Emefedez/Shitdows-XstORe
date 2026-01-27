package com.shitdows.xstore;


import java.awt.Color;
import javax.swing.*;

public class Main {
        public static void main(String[] args) {
        JFrame frame = new JFrame("ShitdowsXstORe"); // Creando instancia de JFrame
        frame.setResizable(false);
        //Definiendo icono para el programa
        try {
            ImageIcon icono = new ImageIcon("icon.png");
            frame.setIconImage(icono.getImage());
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono");
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.decode("#00b0b9"));

        JButton searchButton = new JButton(); // Creando un botón
        JTextField searchField = new JTextField(); // Creando un campo de búsqueda
        JTextField searchPackage = new JTextField();

        //Botón de busqueda
        searchButton.setBounds(130, 100, 100, 40);
        searchButton.setBackground(Color.lightGray);
        searchButton.setText("Search");

        //Barra de busqueda por nombre
        String placeholder = "Search...";
        searchField.setBounds(10, 100, 100, 40);
        searchField.setBackground(Color.lightGray);
        searchField.setForeground(Color.GRAY);
        searchField.setText(placeholder);

        //Barra de busqueda por paquete
        String placeholderPackage = "Search (full package name)...";
        searchPackage.setBounds(10, 500, 100, 40);
        searchPackage.setBackground(Color.GRAY);
        searchPackage.setForeground(Color.WHITE);
        searchPackage.setText(placeholderPackage);

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
        searchField.setBounds(10, 35, 170, 29);
        searchButton.setBounds(180, 35, 80, 29);

        searchPackage.setBounds(10, 10, 250, 20);

        frame.add(searchField);
        frame.add(searchPackage);
        frame.add(searchButton);

        // Configuración del JFrame
        frame.setSize(600, 550);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}