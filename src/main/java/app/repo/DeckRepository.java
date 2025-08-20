
package app.repo;

import app.db.Database;
import app.model.Deck;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeckRepository {

    public Deck save(Deck deck) {
        String sql = "INSERT INTO decks(name) VALUES(?)";
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, deck.getName());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) deck.setId(rs.getLong(1));
            }
            return deck;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Deck> findById(long id) {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("SELECT id,name FROM decks WHERE id=?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new Deck(rs.getLong("id"), rs.getString("name")));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Deck> findAll() {
        List<Deck> list = new ArrayList<>();
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("SELECT id,name FROM decks ORDER BY id")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(new Deck(rs.getLong("id"), rs.getString("name")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void delete(long id) {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM decks WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
