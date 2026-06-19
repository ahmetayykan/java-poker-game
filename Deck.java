import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        reset();
    }

    public final void reset() {
        cards = new ArrayList<>();
        var suits = List.of("♥", "♦", "♣", "♠");
        
        for (int value = 2; value <= 14; value++) {
            for (String suit : suits) {
                cards.add(new Card(value, suit));
            }
        }
        Collections.shuffle(cards);
    }

    public List<Card> draw(int count) {
        var drawn = new ArrayList<Card>();
        for (int i = 0; i < count && !cards.isEmpty(); i++) {
            drawn.add(cards.remove(cards.size() - 1));
        }
        return drawn;
    }
} 