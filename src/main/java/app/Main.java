
package app;

import app.db.Database;
import app.model.Deck;
import app.model.Card;
import app.repo.DeckRepository;
import app.repo.CardRepository;
import app.service.DeckService;
import app.service.CardService;
import app.service.QuizService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final DeckService deckService;
    private final CardService cardService;
    private final QuizService quizService;

    public Main() {
        Database.init("data/flashcards.db");
        DeckRepository deckRepo = new DeckRepository();
        CardRepository cardRepo = new CardRepository();
        this.deckService = new DeckService(deckRepo, cardRepo);
        this.cardService = new CardService(cardRepo);
        this.quizService = new QuizService(cardRepo);
    }

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        while (true) {
            System.out.println("\n  Flashcard Quiz");
            System.out.println("1) Почати вивчення");
            System.out.println("2) Управління колодами");
            System.out.println("0) Вихід");
            System.out.print("Ваш вибір: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> startQuiz();
                case "2" -> manageDecks();
                case "0" -> {
                    System.out.println("Бувай!");
                    return;
                }
                default -> System.out.println("Невідомий вибір.");
            }
        }
    }

    private void startQuiz() {
        Optional<Deck> deck = chooseDeck();
        if (deck.isEmpty()) return;
        quizService.start(deck.get(), scanner);
    }

    private Optional<Deck> chooseDeck() {
        List<Deck> decks = deckService.getAllDecks();
        if (decks.isEmpty()) {
            System.out.println("Немає колод. Створіть нову в меню управління");
            return Optional.empty();
        }
        for (int i = 0; i < decks.size(); i++) {
            System.out.printf("%d) %s (id=%d)%n", i + 1, decks.get(i).getName(), decks.get(i).getId());
        }
        System.out.print("Оберіть колоду: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= decks.size()) {
                System.out.println("Неправильний індекс.");
                return Optional.empty();
            }
            return Optional.of(decks.get(idx));
        } catch (NumberFormatException e) {
            System.out.println("Введіть число.");
            return Optional.empty();
        }
    }

    private void manageDecks() {
        while (true) {
            System.out.println("\n--- Управління колодами ---");
            System.out.println("1) Створити колоду");
            System.out.println("2) Переглянути колоди");
            System.out.println("3) Обрати колоду");
            System.out.println("4) Видалити колоду");
            System.out.println("0) Назад");
            System.out.print("Ваш вибір: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    System.out.print("Назва колоди: ");
                    String name = scanner.nextLine().trim();
                    if (name.isBlank()) {
                        System.out.println("Назва не може бути порожньою.");
                        break;
                    }
                    Deck d = deckService.createDeck(name);
                    System.out.println("Створено колоду з id=" + d.getId());
                }
                case "2" -> deckService.getAllDecks().forEach(d ->
                        System.out.printf("- [%d] %s%n", d.getId(), d.getName()));
                case "3" -> manageCardsInDeck();
                case "4" -> {
                    System.out.print("ID колоди для видалення: ");
                    try {
                        long id = Long.parseLong(scanner.nextLine().trim());
                        deckService.deleteDeck(id);
                        System.out.println("Видалено.");
                    } catch (NumberFormatException e) {
                        System.out.println("ID має бути числом.");
                    }
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println("Невідомий вибір.");
            }
        }
    }

    private void manageCardsInDeck() {
        System.out.print("ID колоди: ");
        long deckId;
        try {
            deckId = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID має бути числом.");
            return;
        }
        Optional<Deck> deck = deckService.getDeck(deckId);
        if (deck.isEmpty()) {
            System.out.println("Колоду не знайдено.");
            return;
        }
        while (true) {
            System.out.println("\n--- Картки колоди: " + deck.get().getName() + " ---");
            System.out.println("1) Додати картку");
            System.out.println("2) Переглянути картки");
            System.out.println("3) Редагувати картку");
            System.out.println("4) Видалити картку");
            System.out.println("0) Назад");
            System.out.print("Ваш вибір: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    System.out.print("Питання: ");
                    String q = scanner.nextLine().trim();
                    System.out.print("Відповідь: ");
                    String a = scanner.nextLine().trim();
                    if (q.isBlank() || a.isBlank()) {
                        System.out.println("Питання/відповідь не можуть бути порожніми.");
                        break;
                    }
                    Card c = cardService.createCard(deckId, q, a);
                    System.out.println("Додано картку id=" + c.getId());
                }
                case "2" -> {
                    List<Card> cards = cardService.getByDeck(deckId);
                    if (cards.isEmpty()) System.out.println("Немає карток");
                    else cards.forEach(c -> System.out.printf("[%d] Q: %s | A: %s%n",
                            c.getId(), c.getQuestion(), c.getAnswer()));
                }
                case "3" -> {
                    System.out.print("ID картки: ");
                    try {
                        long id = Long.parseLong(scanner.nextLine().trim());
                        System.out.print("Нове питання (порожнє — без змін): ");
                        String q = scanner.nextLine();
                        System.out.print("Нова відповідь (порожнє — без змін): ");
                        String a = scanner.nextLine();
                        cardService.updateCard(id, q.isBlank() ? null : q, a.isBlank() ? null : a);
                        System.out.println("Оновлено");
                    } catch (NumberFormatException e) {
                        System.out.println("ID має бути числом");
                    }
                }
                case "4" -> {
                    System.out.print("ID картки для видалення: ");
                    try {
                        long id = Long.parseLong(scanner.nextLine().trim());
                        cardService.deleteCard(id);
                        System.out.println("Видалено");
                    } catch (NumberFormatException e) {
                        System.out.println("ID має бути числом");
                    }
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println("Невідомий вибір.");
            }
        }
    }
}
