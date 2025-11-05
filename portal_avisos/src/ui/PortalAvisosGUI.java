package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.AvisoDAO;
import database.Conexion;
import models.Aviso;
import java.awt.*;
import java.sql.*;
import java.util.List;

public class PortalAvisosGUI extends JFrame {

    private JTextField txtProfesor, txtTitulo;
    private JTextArea txtContenido;
    private JTable tabla;
    private DefaultTableModel modelo;
    private AvisoDAO dao = new AvisoDAO();

    private String usuarioNombre;
    private String usuarioRol;

    // 🔹 Constructor con parámetros
    public PortalAvisosGUI(String nombre, String rol) {
        this.usuarioNombre = nombre;
        this.usuarioRol = rol;

        setTitle("📢 Portal de Avisos Universitario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel de formulario
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 8, 8));
        panelForm.setBorder(BorderFactory.createTitledBorder("Nuevo aviso"));

        panelForm.add(new JLabel("Profesor:"));
        txtProfesor = new JTextField();
        panelForm.add(txtProfesor);

        panelForm.add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        panelForm.add(txtTitulo);

        panelForm.add(new JLabel("Contenido:"));
        txtContenido = new JTextArea(3, 20);
        panelForm.add(new JScrollPane(txtContenido));

        JButton btnAgregar = new JButton("Agregar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        // Panel de tabla
        modelo = new DefaultTableModel(new Object[]{"ID", "Profesor", "Título", "Contenido", "Fecha"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        add(panelForm, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        cargarAvisos();

        // Acciones de botones
        btnAgregar.addActionListener(e -> agregarAviso());
        btnActualizar.addActionListener(e -> actualizarAviso());
        btnEliminar.addActionListener(e -> eliminarAviso());

        // 🔹 Configuración según el rol
        if (rol.equals("estudiante")) {
            txtProfesor.setText(nombre);
            txtProfesor.setEnabled(false);
            btnAgregar.setEnabled(false);
            btnActualizar.setEnabled(false);
            btnEliminar.setEnabled(false);
            setTitle("👩‍🎓 Portal de Avisos - Estudiante: " + nombre);
        } else {
            txtProfesor.setText(nombre);
            setTitle("👨‍🏫 Portal de Avisos - Profesor: " + nombre);
        }
    }

    // 🔹 Cargar avisos desde la BD
    private void cargarAvisos() {
        try {
            modelo.setRowCount(0);
            String sql = """
                SELECT a.id_aviso, u.nombre, a.titulo, a.contenido, a.fecha_publicacion
                FROM aviso a
                JOIN usuario u ON a.id_profesor = u.id_usuario
                ORDER BY a.fecha_publicacion DESC
            """;
            try (Connection conn = Conexion.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        rs.getInt("id_aviso"),
                        rs.getString("nombre"),
                        rs.getString("titulo"),
                        rs.getString("contenido"),
                        rs.getString("fecha_publicacion")
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar avisos: " + e.getMessage());
        }
    }

    // 🔹 Obtener ID de profesor según nombre
    private int obtenerIdProfesor(String nombre) throws SQLException {
        String sql = "SELECT id_usuario FROM usuario WHERE nombre = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_usuario");
            } else {
                throw new SQLException("No existe un profesor con ese nombre.");
            }
        }
    }

    // 🔹 Agregar aviso
    private void agregarAviso() {
        try {
            String profesor = txtProfesor.getText();
            String titulo = txtTitulo.getText();
            String contenido = txtContenido.getText();

            if (profesor.isEmpty() || titulo.isEmpty() || contenido.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor completa todos los campos.");
                return;
            }

            int idProfesor = obtenerIdProfesor(profesor);
            Aviso aviso = new Aviso(0, titulo, contenido, null, idProfesor);
            dao.agregarAviso(aviso);
            JOptionPane.showMessageDialog(this, "✅ Aviso agregado correctamente.");
            cargarAvisos();
            limpiarCampos();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error SQL: " + ex.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar: " + e.getMessage());
        }
    }

    // 🔹 Actualizar aviso
    private void actualizarAviso() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un aviso para actualizar.");
            return;
        }

        try {
            int id = (int) modelo.getValueAt(fila, 0);
            String profesor = txtProfesor.getText();
            String titulo = txtTitulo.getText();
            String contenido = txtContenido.getText();
            int idProfesor = obtenerIdProfesor(profesor);

            Aviso aviso = new Aviso(id, titulo, contenido, null, idProfesor);
            dao.actualizarAviso(aviso);
            JOptionPane.showMessageDialog(this, "♻️ Aviso actualizado.");
            cargarAvisos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
        }
    }

    // 🔹 Eliminar aviso
    private void eliminarAviso() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un aviso para eliminar.");
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este aviso?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dao.eliminarAviso(id);
                JOptionPane.showMessageDialog(this, "🗑 Aviso eliminado.");
                cargarAvisos();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            }
        }
    }

    // 🔹 Limpiar campos del formulario
    private void limpiarCampos() {
        txtProfesor.setText("");
        txtTitulo.setText("");
        txtContenido.setText("");
    }

    // 🔹 Punto de entrada
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }
}


