
package app.service;

import app.model.Card;
import app.model.Deck;
import app.repo.CardRepository;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class QuizService {
    private final CardRepository repo;

    public QuizService(CardRepository repo) { this.repo = repo; }

    public void start(Deck deck, Scanner scanner) {
        List<Card> cards = repo.findByDeck(deck.getId());
        if (cards.isEmpty()) {
            System.out.println("У колоді немає карток");
            return;
        }
        Collections.shuffle(cards);
        int correct = 0;
        for (Card c : cards) {
            System.out.println("Питання: " + c.getQuestion());
            System.out.print("Ваша відповідь: ");
            String ans = scanner.nextLine().trim();
            if (ans.equalsIgnoreCase(c.getAnswer().trim())) {
                System.out.println("Правильно");
                correct++;
            } else {
                System.out.println("Неправильно. Правильна відповідь: " + c.getAnswer());
            }
        }
        System.out.printf("%nРезультат: %d/%d правильних%n", correct, cards.size());
    }
}
