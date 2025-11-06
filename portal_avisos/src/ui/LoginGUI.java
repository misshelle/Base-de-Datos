package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import database.Conexion;

public class LoginGUI extends JFrame {
    private JTextField txtCorreo;
    private JButton btnEntrar;

    public LoginGUI() {
        // Intenta aplicar Nimbus (o el L&F por defecto si Nimbus no está)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        setTitle("🔐 Ingreso al Portal de Avisos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 230);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con padding y color de fondo suave
        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(new Color(250, 245, 255)); // lila muy suave
        main.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        add(main);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Título
        JLabel titulo = new JLabel("Ingreso al Portal de Avisos", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setForeground(new Color(75, 35, 90));
        gbc.gridy = 0;
        gbc.weightx = 1;
        main.add(titulo, gbc);

        // Etiqueta
        JLabel lblCorreo = new JLabel("Correo institucional:");
        lblCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblCorreo.setForeground(new Color(70, 60, 90));
        gbc.gridy = 1;
        main.add(lblCorreo, gbc);

        // Campo de texto centrado y con estilo
        txtCorreo = new JTextField();
        txtCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCorreo.setHorizontalAlignment(JTextField.CENTER);
        txtCorreo.setColumns(20);
        txtCorreo.setPreferredSize(new Dimension(300, 34));
        txtCorreo.setBackground(Color.WHITE);
        txtCorreo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 215, 235), 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        gbc.gridy = 2;
        main.add(txtCorreo, gbc);

        // Botón estilizado
        btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setBackground(new Color(148, 85, 207));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        btnEntrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEntrar.setPreferredSize(new Dimension(300, 38));

        // Hacer que el botón tenga aspecto plano en algunos L&F
        btnEntrar.setOpaque(true);
        btnEntrar.setBorderPainted(false);

        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        main.add(btnEntrar, gbc);

        // Pie con texto de ayuda pequeño
        JLabel footer = new JLabel("Usa tu correo institucional para identificarte", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(110, 100, 120));
        gbc.gridy = 4;
        main.add(footer, gbc);

        // Acciones
        btnEntrar.addActionListener(e -> autenticar());

        // Permitir pulsar ENTER en el campo para enviar
        txtCorreo.addActionListener(e -> autenticar());

        // Efecto visual on hover para el botón
        btnEntrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnEntrar.setBackground(new Color(170, 110, 230));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnEntrar.setBackground(new Color(148, 85, 207));
            }
        });
    }

    private void autenticar() {
        String correo = txtCorreo.getText().trim();
        if (correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa tu correo.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            String sql = "SELECT nombre, rol FROM usuario WHERE correo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String rol = rs.getString("rol");

                dispose();
                // Abrir la ventana principal (tu código actual)
                SwingUtilities.invokeLater(() -> new PortalAvisosGUI(nombre, rol).setVisible(true));
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al autenticar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }
}

