public class Card {
    private final int value;
    private final String suit;
    private boolean selected;

    public Card(int value, String suit) {
        this.value = value;
        this.suit = suit;
        this.selected = false;
    }

    public int getValue() {
        return value;
    }

    public String getSuit() {
        return suit;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return switch (value) {
            case 11 -> "J" + suit;
            case 12 -> "Q" + suit;
            case 13 -> "K" + suit;
            case 14 -> "A" + suit;
            default -> value + suit;
        };
    }

    public String getImageName() {
        return switch (value) {
            case 11 -> "J" + suit + ".jpg";
            case 12 -> "Q" + suit + ".jpg";
            case 13 -> "K" + suit + ".jpg";
            case 14 -> "A" + suit + ".jpg";
            default -> value + suit + ".jpg";
        };
    }

    @Override
    public String toString() {
        return getName();
    }
} 