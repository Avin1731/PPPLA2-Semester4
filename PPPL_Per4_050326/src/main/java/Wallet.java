import org.example.InsufficientFundsException;
import org.example.Owner;

import java.util.ArrayList;
import java.util.List;

public class Wallet {
    private String owner;
    private Owner ownerObject; // Req 3: Tambahan Object Owner
    private List<String> cards;
    private double cash;

    public Wallet() {
        this.cards = new ArrayList<>();
        this.cash = 0;
    }

    // Method lama tetap ada (String)
    public void setOwner(String owner) { this.owner = owner; }
    public String getOwner() { return owner; }

    // Req 3: Method baru untuk Object Owner
    public void setOwner(Owner owner) { this.ownerObject = owner; }
    public Owner getOwnerObject() { return ownerObject; }

    public void addCard(String cardName) { cards.add(cardName); }
    public boolean removeCard(String cardName) { return cards.remove(cardName); }

    // Diperbarui melempar exception untuk angka tidak valid
    public void addCash(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Nominal harus positif");
        }
        this.cash += amount;
    }

    // Req 2: Diperbarui agar melempar exception saat saldo kurang / tidak valid
    public void withdrawCash(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Nominal tarik tidak valid");
        }
        if (this.cash < amount) {
            throw new InsufficientFundsException("Saldo tidak cukup");
        }
        this.cash -= amount;
    }

    public double getCashBalance() { return cash; }
    public List<String> getCards() { return cards; }
}