package app.service;

import app.model.Deck;
import app.repo.CardRepository;
import app.repo.DeckRepository;

import java.util.List;
import java.util.Optional;

public class DeckService {
    private final DeckRepository deckRepo;
    private final CardRepository cardRepo;

    public DeckService(DeckRepository deckRepo, CardRepository cardRepo) {
        this.deckRepo = deckRepo;
        this.cardRepo = cardRepo;
    }

    public Deck createDeck(String name) {
        return deckRepo.save(new Deck(name));
    }

    public List<Deck> getAllDecks() {
        return deckRepo.findAll();
    }

    public Optional<Deck> getDeck(long id) {
        return deckRepo.findById(id);
    }

    public void deleteDeck(long id) {
        deckRepo.delete(id);
    }
}
