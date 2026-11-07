package com.salumita.mikimup.ui;

import com.salumita.mikimup.model.Cart;
import com.salumita.mikimup.model.Product;

import com.salumita.mikimup.payment.CashPayment;
import com.salumita.mikimup.payment.QrisPayment;

import com.salumita.mikimup.repository.ProductRepository;
import com.salumita.mikimup.repository.TransactionRepository;
import com.salumita.mikimup.repository.PaymentRepository;

import com.salumita.mikimup.service.OrderingService;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.*;

public class MainFrame extends JFrame {

    private final ProductRepository productRepository;

    private final Cart cart = new Cart();

    private final OrderingService orderingService
            = new OrderingService(
                    new TransactionRepository(),
                    new PaymentRepository()
            );

    private final DefaultListModel<String> cartListModel
            = new DefaultListModel<>();

    private final JLabel totalLabel
            = new JLabel("Total: Rp0");

    private final JList<String> cartList
            = new JList<>(cartListModel);

    public MainFrame(ProductRepository productRepository) {

        this.productRepository = productRepository;

        setTitle("Mikimup - Ordering System");
        setSize(850, 550);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        buildUI();
    }

    private void buildUI() {

        setLayout(new BorderLayout(12, 12));

        JLabel heading = new JLabel(
                "MIKIMUP",
                SwingConstants.CENTER
        );

        heading.setFont(
                new Font("SansSerif", Font.BOLD, 28)
        );

        add(heading, BorderLayout.NORTH);

        JPanel products = new JPanel(
                new GridLayout(0, 2, 10, 10)
        );

        products.setBorder(
                BorderFactory.createTitledBorder("Daftar Produk")
        );

        for (Product product : productRepository.findAll()) {

            JButton button = new JButton(
                    "<html><b>" + product.getName()
                    + "</b><br>Rp" + product.getPrice().toPlainString()
                    + "<br>Tambah ke keranjang</html>"
            );

            button.addActionListener(e -> {

                cart.addProduct(product);

                refreshCart();
            });

            products.add(button);
        }

        add(new JScrollPane(products), BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout(8, 8));

        right.setPreferredSize(new Dimension(270, 0));

        right.setBorder(
                BorderFactory.createTitledBorder("Keranjang")
        );

        right.add(
                new JScrollPane(cartList),
                BorderLayout.CENTER
        );

        JPanel bottom = new JPanel(
                new GridLayout(0, 1, 5, 5)
        );

        bottom.add(totalLabel);

        JButton cashButton
                = new JButton("Checkout - Tunai");

        cashButton.addActionListener(e -> checkout(true));

        JButton qrisButton
                = new JButton("Checkout - QRIS");

        qrisButton.addActionListener(e -> checkout(false));

        JButton clearButton
                = new JButton("Kosongkan Keranjang");

        clearButton.addActionListener(e -> {

            cart.clear();

            refreshCart();
        });

        bottom.add(cashButton);
        bottom.add(qrisButton);
        bottom.add(clearButton);

        right.add(bottom, BorderLayout.SOUTH);

        add(right, BorderLayout.EAST);
    }

    private void refreshCart() {

        cartListModel.clear();

        for (Cart.Item item : cart.getItems()) {

            cartListModel.addElement(
                    item.product().getName()
                    + " x" + item.quantity()
                    + " — Rp"
                    + item.product().getPrice()
                            .multiply(
                                    java.math.BigDecimal.valueOf(
                                            item.quantity()
                                    )
                            ).toPlainString()
            );
        }

        totalLabel.setText(
                "Total: "
                + NumberFormat.getCurrencyInstance(
                        new Locale("id", "ID")
                ).format(cart.getTotal())
        );
    }

    private void checkout(boolean cash) {

        try {

            int id = orderingService.checkout(
                    cart,
                    "DINE_IN",
                    cash ? new CashPayment() : new QrisPayment()
            );

            String message = cash
                    ? "Pembayaran tunai tercatat lunas."
                    : "QRIS masih berstatus pending (simulasi).";

            JOptionPane.showMessageDialog(
                    this,
                    "Pesanan berhasil dibuat.\n"
                    + "ID Transaksi: " + id + "\n"
                    + message
            );

            refreshCart();

        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Checkout Gagal",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
}
