package dao;
import database.Conexion;
import models.Aviso;
import java.sql.*;
import java.util.*;

public class AvisoDAO {

    // CREATE
    public void agregarAviso(Aviso aviso) throws SQLException {
        String sql = "INSERT INTO aviso (titulo, contenido, id_profesor) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aviso.getTitulo());
            stmt.setString(2, aviso.getContenido());
            stmt.setInt(3, aviso.getIdProfesor());
            stmt.executeUpdate();
        }
    }

    // READ
    public List<Aviso> listarAvisos() throws SQLException {
        List<Aviso> lista = new ArrayList<>();
        String sql = "SELECT * FROM aviso ORDER BY fecha_publicacion DESC";
        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Aviso(
                    rs.getInt("id_aviso"),
                    rs.getString("titulo"),
                    rs.getString("contenido"),
                    rs.getString("fecha_publicacion"),
                    rs.getInt("id_profesor")
                ));
            }
        }
        return lista;
    }

    // UPDATE
    public void actualizarAviso(Aviso aviso) throws SQLException {
        String sql = "UPDATE aviso SET titulo=?, contenido=? WHERE id_aviso=?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aviso.getTitulo());
            stmt.setString(2, aviso.getContenido());
            stmt.setInt(3, aviso.getId());
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void eliminarAviso(int id) throws SQLException {
        String sql = "DELETE FROM aviso WHERE id_aviso=?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
