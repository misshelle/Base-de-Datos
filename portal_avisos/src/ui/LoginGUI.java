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
        // Look and Feel moderno (Nimbus)
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

        // Panel principal
        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(new Color(250, 245, 255));
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

        // Etiqueta de correo
        JLabel lblCorreo = new JLabel("Correo institucional:");
        lblCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblCorreo.setForeground(new Color(70, 60, 90));
        gbc.gridy = 1;
        main.add(lblCorreo, gbc);

        // Campo de texto
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

        // Botón
        btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setBackground(new Color(148, 85, 207));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        btnEntrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEntrar.setPreferredSize(new Dimension(300, 38));
        btnEntrar.setOpaque(true);
        btnEntrar.setBorderPainted(false);

        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        main.add(btnEntrar, gbc);

        JLabel footer = new JLabel("Usa tu correo institucional para identificarte", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(110, 100, 120));
        gbc.gridy = 4;
        main.add(footer, gbc);

        // Acciones
        btnEntrar.addActionListener(e -> autenticar());
        txtCorreo.addActionListener(e -> autenticar());

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
            // 1️⃣ Buscar en tabla profesor
            String sqlProfesor = "SELECT CONCAT(nombre_profesor, ' ', apellido_profesor) AS nombre FROM profesor WHERE correo_profesor = ?";
            PreparedStatement stmtP = conn.prepareStatement(sqlProfesor);
            stmtP.setString(1, correo);
            ResultSet rsP = stmtP.executeQuery();

            if (rsP.next()) {
                String nombre = rsP.getString("nombre");
                dispose();
                SwingUtilities.invokeLater(() -> new PortalAvisosGUI(nombre, "profesor").setVisible(true));
                return;
            }

            // 2️⃣ Buscar en tabla estudiante
            String sqlEstudiante = "SELECT CONCAT(nombre_estudiante, ' ', apellido_estudiante) AS nombre FROM estudiante WHERE correo_estudiante = ?";
            PreparedStatement stmtE = conn.prepareStatement(sqlEstudiante);
            stmtE.setString(1, correo);
            ResultSet rsE = stmtE.executeQuery();

            if (rsE.next()) {
                String nombre = rsE.getString("nombre");
                dispose();
                SwingUtilities.invokeLater(() -> new PortalAvisosGUI(nombre, "estudiante").setVisible(true));
                return;
            }

            // 3️⃣ Si no está en ninguna tabla
            JOptionPane.showMessageDialog(this, "Correo no registrado como profesor ni estudiante.", "Error", JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al autenticar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }
}

