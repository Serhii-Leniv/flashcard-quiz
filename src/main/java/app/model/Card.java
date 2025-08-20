
package app.model;

public class Card {
    private long id;
    private long deckId;
    private String question;
    private String answer;

    public Card() {
    }

    public Card(long id, long deckId, String question, String answer) {
        this.id = id;
        this.deckId = deckId;
        this.question = question;
        this.answer = answer;
    }

    public Card(long deckId, String question, String answer) {
        this.deckId = deckId;
        this.question = question;
        this.answer = answer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDeckId() {
        return deckId;
    }

    public void setDeckId(long deckId) {
        this.deckId = deckId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Card{id=" + id + ", deckId=" + deckId + ", q='" + question + "', a='" + answer + "'}";
    }
}
