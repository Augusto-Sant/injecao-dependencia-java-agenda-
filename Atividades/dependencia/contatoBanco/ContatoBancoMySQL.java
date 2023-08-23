package Atividades.dependencia.contatoBanco;

import Atividades.dependencia.Contato;

import java.sql.*;
import java.util.ArrayList;

public class ContatoBancoMySQL implements ContatoBanco {

    // Conexão com o banco de dados MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/dependencia";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Consultas SQL
    private static final String SELECT_ALL = "SELECT * FROM contatos";
    private static final String SELECT_BY_ID = "SELECT * FROM contatos WHERE id = ?";
    private static final String SELECT_BY_EMAIL = "SELECT * FROM contatos WHERE email = ?";
    private static final String INSERT = "INSERT INTO contatos (name, birth_date, phone, email) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE contatos SET name = ?, birth_date = ?, phone = ?, email = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM contatos WHERE id = ?";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Override
    public ArrayList<Contato> getTodosContatos() {
        ArrayList<Contato> contatos = new ArrayList<>();
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Contato contato = new Contato(rs.getInt("id"), rs.getString("name"),
                        rs.getString("birth_date"), rs.getString("phone"), rs.getString("email"));
                contatos.add(contato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contatos;
    }

    @Override
    public Contato getContatoId(int id) {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Contato(rs.getInt("id"), rs.getString("name"),
                            rs.getString("birth_date"), rs.getString("phone"), rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Contato getContatoEmail(String email) {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_BY_EMAIL)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Contato(rs.getInt("id"), rs.getString("name"),
                            rs.getString("birth_date"), rs.getString("phone"), rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean inserir(Contato contato) {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(INSERT)) {
            stmt.setString(1, contato.getName());
            stmt.setString(2, contato.getBirthDate());
            stmt.setString(3, contato.getPhone());
            stmt.setString(4, contato.getEmail());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean alterar(Contato contato) {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            stmt.setString(1, contato.getName());
            stmt.setString(2, contato.getBirthDate());
            stmt.setString(3, contato.getPhone());
            stmt.setString(4, contato.getEmail());
            stmt.setInt(5, contato.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean excluir(int id) {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
