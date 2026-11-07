package com.salumita.mikimup.app;

import com.salumita.mikimup.database.DatabaseInitializer;
import com.salumita.mikimup.repository.ProductRepository;
import com.salumita.mikimup.ui.MainFrame;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        DatabaseInitializer.initialize();

        SwingUtilities.invokeLater(() -> {

            MainFrame frame = new MainFrame(
                    new ProductRepository()
            );

            frame.setVisible(true);
        });
    }
}
