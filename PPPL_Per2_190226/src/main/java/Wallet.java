import java.util.ArrayList;
import java.util.List;

public class Wallet {
    private String owner;
    private List<String> cards;
    private double cash;

    public Wallet() {
        this.cards = new ArrayList<>();
        this.cash = 0;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void addCard(String cardName) {
        cards.add(cardName);
    }

    public boolean removeCard(String cardName) {
        return cards.remove(cardName);
    }

    public void addCash(double amount) {
        if (amount > 0) {
            this.cash += amount;
        }
    }

    public boolean withdrawCash(double amount) {
        if (amount > 0 && this.cash >= amount) {
            this.cash -= amount;
            return true;
        }
        return false;
    }

    public double getCashBalance() {
        return cash;
    }

    public List<String> getCards() {
        return cards;
    }
}