package com.app.server.repository;

import com.app.server.db.DBConnection;
import com.app.server.model.Mahasiswa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaRepository {

    // Create
    public boolean save(Mahasiswa m) {
        String sql = "INSERT INTO mahasiswa (nim, nama, jurusan, angkatan) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getNim());
            ps.setString(2, m.getNama());
            ps.setString(3, m.getJurusan());
            ps.setInt(4, m.getAngkatan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("save error: " + e.getMessage());
            return false;
        }
    }

    // Read all
    public List<Mahasiswa> findAll() {
        List<Mahasiswa> list = new ArrayList<>();
        String sql = "SELECT nim, nama, jurusan, angkatan FROM mahasiswa";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Mahasiswa m = new Mahasiswa(
                        rs.getString("nim"),
                        rs.getString("nama"),
                        rs.getString("jurusan"),
                        rs.getInt("angkatan")
                );
                list.add(m);
            }
        } catch (SQLException e) {
            System.err.println("findAll error: " + e.getMessage());
        }
        return list;
    }

    // Read by NIM
    public Mahasiswa findByNim(String nim) {
        String sql = "SELECT nim, nama, jurusan, angkatan FROM mahasiswa WHERE nim = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nim);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Mahasiswa(
                            rs.getString("nim"),
                            rs.getString("nama"),
                            rs.getString("jurusan"),
                            rs.getInt("angkatan")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("findByNim error: " + e.getMessage());
        }
        return null;
    }

    // Update
    public boolean update(Mahasiswa m) {
        String sql = "UPDATE mahasiswa SET nama = ?, jurusan = ?, angkatan = ? WHERE nim = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getNama());
            ps.setString(2, m.getJurusan());
            ps.setInt(3, m.getAngkatan());
            ps.setString(4, m.getNim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("update error: " + e.getMessage());
            return false;
        }
    }

    // Delete
    public boolean deleteByNim(String nim) {
        String sql = "DELETE FROM mahasiswa WHERE nim = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nim);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("delete error: " + e.getMessage());
            return false;
        }
    }
}
