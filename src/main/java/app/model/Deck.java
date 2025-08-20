
package app.model;

public class Deck {
    private long id;
    private String name;

    public Deck() {
    }

    public Deck(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Deck(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Deck{id=" + id + ", name='" + name + "'}";
    }
}
