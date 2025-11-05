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
        setTitle("🔐 Ingreso al Portal de Avisos");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10));

        JLabel lblCorreo = new JLabel("Correo institucional:");
        txtCorreo = new JTextField();
        btnEntrar = new JButton("Entrar");

        add(lblCorreo);
        add(txtCorreo);
        add(btnEntrar);

        btnEntrar.addActionListener(e -> autenticar());
    }

    private void autenticar() {
        String correo = txtCorreo.getText();
        if (correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa tu correo.");
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

                JOptionPane.showMessageDialog(this, "Bienvenido, " + nombre + " (" + rol + ")");
                dispose();

                if (rol.equals("profesor")) {
                    new PortalAvisosGUI(nombre, rol).setVisible(true);
                } else {
                    new PortalAvisosGUI(nombre, rol).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al autenticar: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }
}
