
package app.repo;

import app.db.Database;
import app.model.Card;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardRepository {

    public Card save(Card card) {
        String sql = "INSERT INTO cards(deck_id,question,answer) VALUES(?,?,?)";
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, card.getDeckId());
            ps.setString(2, card.getQuestion());
            ps.setString(3, card.getAnswer());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) card.setId(rs.getLong(1));
            }
            return card;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Card> findByDeck(long deckId) {
        List<Card> list = new ArrayList<>();
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("SELECT id,deck_id,question,answer FROM cards WHERE deck_id=? ORDER BY id")) {
            ps.setLong(1, deckId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(new Card(
                    rs.getLong("id"),
                    rs.getLong("deck_id"),
                    rs.getString("question"),
                    rs.getString("answer")
            ));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public Optional<Card> findById(long id) {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("SELECT id,deck_id,question,answer FROM cards WHERE id=?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new Card(
                        rs.getLong("id"),
                        rs.getLong("deck_id"),
                        rs.getString("question"),
                        rs.getString("answer")));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(long id, String question, String answer) {
        StringBuilder sb = new StringBuilder("UPDATE cards SET ");
        boolean set = false;
        if (question != null) {
            sb.append("question=?");
            set = true;
        }
        if (answer != null) {
            sb.append(set ? ", " : "").append("answer=?");
            set = true;
        }
        if (!set) return;
        sb.append(" WHERE id=?");
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement(sb.toString())) {
            int idx = 1;
            if (question != null) ps.setString(idx++, question);
            if (answer != null) ps.setString(idx++, answer);
            ps.setLong(idx, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(long id) {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM cards WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
