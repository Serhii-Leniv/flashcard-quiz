
package app.service;

import app.model.Card;
import app.repo.CardRepository;

import java.util.List;

public class CardService {
    private final CardRepository repo;

    public CardService(CardRepository repo) { this.repo = repo; }

    public Card createCard(long deckId, String question, String answer) {
        return repo.save(new Card(deckId, question, answer));
    }

    public List<Card> getByDeck(long deckId) {
        return repo.findByDeck(deckId);
    }

    public void updateCard(long id, String question, String answer) {
        repo.update(id, question, answer);
    }

    public void deleteCard(long id) {
        repo.delete(id);
    }
}
