import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.*;
import java.util.List;

public class PokerGame {
    private final JFrame frame;
    private final Deck deck;
    private List<Card> player1Hand;
    private List<Card> player2Hand;
    private List<Card> deskCards;
    private int currentPlayer;
    private int round;
    private int player1RoundsWon;
    private int player2RoundsWon;
    private boolean deskCardsVisible;
    private boolean roundComplete;
    private final Map<String, ImageIcon> cardImages;

    private final JPanel player1Panel;
    private final JPanel player2Panel;
    private final JPanel deskPanel;
    private final List<JCheckBox> player1Checkboxes;
    private final List<JCheckBox> player2Checkboxes;
    private final JButton changeButton1;
    private final JButton changeButton2;
    private final JButton showDeskButton;
    private final JButton nextRoundButton;
    private final JLabel player1PointLabel;
    private final JLabel player2PointLabel;
    private final JLabel scoreLabel;

    public PokerGame() {
        deck = new Deck();
        player1Hand = new ArrayList<>();
        player2Hand = new ArrayList<>();
        deskCards = new ArrayList<>();
        player1Checkboxes = new ArrayList<>();
        player2Checkboxes = new ArrayList<>();
        cardImages = new HashMap<>();
        currentPlayer = 1;
        round = 1;
        player1RoundsWon = 0;
        player2RoundsWon = 0;
        
        // Initialize GUI components
        frame = new JFrame("Poker Game");
        player1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        player2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        deskPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        changeButton1 = new JButton("Change");
        changeButton2 = new JButton("Change");
        showDeskButton = new JButton("Show Desk Cards");
        nextRoundButton = new JButton("Next Round");
        player1PointLabel = new JLabel("Hand's Point: 0");
        player2PointLabel = new JLabel("Hand's Point:");
        scoreLabel = new JLabel("Rounds Won - Player 1: 0 | Player 2: 0");
        
        setupGUI();
        loadCardImages();
        startNewRound();
    }

    private void loadCardImages() {
        var cardsDir = new File("Cards");
        if (!cardsDir.exists()) {
            System.out.println("Cards folder not found!");
            return;
        }

        try {
            // Load card back
            var backImage = ImageIO.read(new File(cardsDir, "back.jpg"));
            var scaledBack = backImage.getScaledInstance(70, 100, Image.SCALE_SMOOTH);
            cardImages.put("back", new ImageIcon(scaledBack));

            // Load all card images
            var suits = List.of("♥", "♦", "♣", "♠");
            var values = List.of("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K");

            for (var suit : suits) {
                for (var value : values) {
                    var imageName = value + suit + ".jpg";
                    // Special case for 8 of spades
                    if (value.equals("8") && suit.equals("♠")) {
                        imageName = "8s.jpg";
                    }
                    var cardImage = ImageIO.read(new File(cardsDir, imageName));
                    var scaledCard = cardImage.getScaledInstance(70, 100, Image.SCALE_SMOOTH);
                    cardImages.put(imageName, new ImageIcon(scaledCard));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        var mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Player 1 section
        var player1Section = new JPanel(new FlowLayout(FlowLayout.CENTER));
        var player1Info = new JPanel();
        player1Info.setLayout(new BoxLayout(player1Info, BoxLayout.Y_AXIS));

        var player1Label = new JLabel("Player 1");
        player1Label.setFont(new Font("Arial", Font.BOLD, 14));
        player1PointLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        changeButton1.addActionListener(e -> changeCards());

        player1Info.add(player1Label);
        player1Info.add(Box.createVerticalStrut(5));
        player1Info.add(player1PointLabel);
        player1Info.add(Box.createVerticalStrut(5));
        player1Info.add(changeButton1);

        player1Section.add(player1Panel);
        player1Section.add(Box.createHorizontalStrut(20));
        player1Section.add(player1Info);

        // Player 2 section
        var player2Section = new JPanel(new FlowLayout(FlowLayout.CENTER));
        var player2Info = new JPanel();
        player2Info.setLayout(new BoxLayout(player2Info, BoxLayout.Y_AXIS));

        var player2Label = new JLabel("Player 2");
        player2Label.setFont(new Font("Arial", Font.BOLD, 14));
        player2PointLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        changeButton2.addActionListener(e -> changeCards());

        player2Info.add(player2Label);
        player2Info.add(Box.createVerticalStrut(5));
        player2Info.add(player2PointLabel);
        player2Info.add(Box.createVerticalStrut(5));
        player2Info.add(changeButton2);

        player2Section.add(player2Panel);
        player2Section.add(Box.createHorizontalStrut(20));
        player2Section.add(player2Info);

        // Control buttons
        var buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        showDeskButton.addActionListener(e -> showDeskCards());
        nextRoundButton.addActionListener(e -> nextRound());
        nextRoundButton.setEnabled(false);

        buttonPanel.add(showDeskButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(nextRoundButton);

        // Score display
        var scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        scorePanel.add(scoreLabel);

        // Add all sections to main panel
        mainPanel.add(player1Section);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(deskPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(player2Section);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(scorePanel);

        frame.add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private void startNewRound() {
        deck.reset();
        player1Hand = deck.draw(5);
        player2Hand = deck.draw(5);
        deskCards = deck.draw(3);
        deskCardsVisible = false;
        currentPlayer = 1;
        updateDisplay();
    }

    private void changeCards() {
        var currentHand = (currentPlayer == 1) ? player1Hand : player2Hand;
        var currentCheckboxes = (currentPlayer == 1) ? player1Checkboxes : player2Checkboxes;

        // Get selected cards
        var selectedIndices = new ArrayList<Integer>();
        for (int i = 0; i < currentCheckboxes.size(); i++) {
            if (currentCheckboxes.get(i).isSelected()) {
                selectedIndices.add(i);
            }
        }

        // Draw new cards
        var newCards = deck.draw(selectedIndices.size());

        // Replace selected cards
        for (int i = 0; i < selectedIndices.size(); i++) {
            currentHand.set(selectedIndices.get(i), newCards.get(i));
        }

        // Switch player
        currentPlayer = 3 - currentPlayer; // Switches between 1 and 2
        updateDisplay();

        // If both players have played, show desk cards
        if (player1Checkboxes.isEmpty() && player2Checkboxes.isEmpty()) {
            showDeskCards();
        }
    }

    private void showDeskCards() {
        deskCardsVisible = true;
        roundComplete = true;
        updateDisplay();

        // Calculate and display points
        var p1Points = calculateHandPoints(player1Hand, deskCards);
        var p2Points = calculateHandPoints(player2Hand, deskCards);

        player1PointLabel.setText("Hand's Point: " + p1Points);
        player2PointLabel.setText("Hand's Point: " + p2Points);

        // Determine round winner
        String winner = switch (Integer.compare(p1Points, p2Points)) {
            case 1 -> {
                player1RoundsWon++;
                yield "Player 1";
            }
            case -1 -> {
                player2RoundsWon++;
                yield "Player 2";
            }
            default -> "Tie";
        };

        scoreLabel.setText("Rounds Won - Player 1: " + player1RoundsWon + " | Player 2: " + player2RoundsWon);

        // Check if game is over
        if (player1RoundsWon >= 3 || player2RoundsWon >= 3) {
            var gameWinner = (player1RoundsWon >= 3) ? "Player 1" : "Player 2";
            JOptionPane.showMessageDialog(frame, gameWinner + " wins the game!");
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Round " + round + " - " + winner + " wins!");
            nextRoundButton.setEnabled(true);
            changeButton1.setEnabled(false);
            changeButton2.setEnabled(false);
            showDeskButton.setEnabled(false);
        }
    }

    private void nextRound() {
        round++;
        deskCardsVisible = false;
        roundComplete = false;
        currentPlayer = (round % 2 == 0) ? 2 : 1; // Alternate starting player

        nextRoundButton.setEnabled(false);
        changeButton1.setEnabled(true);
        changeButton2.setEnabled(true);
        showDeskButton.setEnabled(true);

        startNewRound();
    }

    private void updateDisplay() {
        // Clear existing display
        player1Panel.removeAll();
        player2Panel.removeAll();
        deskPanel.removeAll();

        // Reset checkboxes
        player1Checkboxes.clear();
        player2Checkboxes.clear();

        // Display Player 1's cards
        for (var card : player1Hand) {
            var cardPanel = new JPanel();
            cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));

            JLabel cardLabel;
            if (currentPlayer == 1 || roundComplete) {
                var cardImage = cardImages.get(card.getImageName());
                cardLabel = cardImage != null ? new JLabel(cardImage) : new JLabel(card.getName());
            } else {
                var backImage = cardImages.get("back");
                cardLabel = backImage != null ? new JLabel(backImage) : new JLabel("🂠");
            }
            cardPanel.add(cardLabel);

            if (currentPlayer == 1 && !roundComplete) {
                var checkbox = new JCheckBox();
                player1Checkboxes.add(checkbox);
                cardPanel.add(checkbox);
            }

            player1Panel.add(cardPanel);
        }

        // Display Player 2's cards
        for (var card : player2Hand) {
            var cardPanel = new JPanel();
            cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));

            JLabel cardLabel;
            if (currentPlayer == 2 || roundComplete) {
                var cardImage = cardImages.get(card.getImageName());
                cardLabel = cardImage != null ? new JLabel(cardImage) : new JLabel(card.getName());
            } else {
                var backImage = cardImages.get("back");
                cardLabel = backImage != null ? new JLabel(backImage) : new JLabel("🂠");
            }
            cardPanel.add(cardLabel);

            if (currentPlayer == 2 && !roundComplete) {
                var checkbox = new JCheckBox();
                player2Checkboxes.add(checkbox);
                cardPanel.add(checkbox);
            }

            player2Panel.add(cardPanel);
        }

        // Display desk cards
        for (var card : deskCards) {
            JLabel cardLabel;
            if (deskCardsVisible) {
                var cardImage = cardImages.get(card.getImageName());
                cardLabel = cardImage != null ? new JLabel(cardImage) : new JLabel(card.getName());
            } else {
                var backImage = cardImages.get("back");
                cardLabel = backImage != null ? new JLabel(backImage) : new JLabel("🂠");
            }
            deskPanel.add(cardLabel);
        }

        // Update point labels
        if (deskCardsVisible) {
            var p1Points = calculateHandPoints(player1Hand, deskCards);
            var p2Points = calculateHandPoints(player2Hand, deskCards);
            player1PointLabel.setText("Hand's Point: " + p1Points);
            player2PointLabel.setText("Hand's Point: " + p2Points);
        } else {
            if (currentPlayer == 1) {
                var p1Points = calculateHandPoints(player1Hand, new ArrayList<>());
                player1PointLabel.setText("Hand's Point: " + p1Points);
                player2PointLabel.setText("Hand's Point:");
            } else {
                var p2Points = calculateHandPoints(player2Hand, new ArrayList<>());
                player1PointLabel.setText("Hand's Point:");
                player2PointLabel.setText("Hand's Point: " + p2Points);
            }
        }

        // Update change buttons state
        changeButton1.setEnabled(currentPlayer == 1 && !roundComplete);
        changeButton2.setEnabled(currentPlayer == 2 && !roundComplete);

        // Refresh the display
        frame.revalidate();
        frame.repaint();
    }

    private int calculateHandPoints(List<Card> hand, List<Card> deskCards) {
        var allCards = new ArrayList<>(hand);
        allCards.addAll(deskCards);
        var totalPoints = 0;

        // Count occurrences of each value
        var valueCount = new HashMap<Integer, Integer>();
        for (var card : allCards) {
            valueCount.merge(card.getValue(), 1, Integer::sum);
            totalPoints += card.getValue();
        }

        // Count suits
        var suitCount = new HashMap<String, List<Integer>>();
        for (var card : allCards) {
            suitCount.computeIfAbsent(card.getSuit(), k -> new ArrayList<>()).add(card.getValue());
        }

        // Check for straight
        var values = allCards.stream()
                .mapToInt(Card::getValue)
                .distinct()
                .sorted()
                .toArray();
        
        var maxStraight = 1;
        var currentStraight = 1;
        for (var i = 1; i < values.length; i++) {
            if (values[i] == values[i-1] + 1) {
                currentStraight++;
                maxStraight = Math.max(maxStraight, currentStraight);
            } else {
                currentStraight = 1;
            }
        }

        // Four of a kind (x4)
        if (valueCount.containsValue(4)) {
            var value = valueCount.entrySet().stream()
                    .filter(e -> e.getValue() == 4)
                    .mapToInt(Map.Entry::getKey)
                    .findFirst()
                    .orElse(0);
            var remainingPoints = valueCount.entrySet().stream()
                    .filter(e -> e.getValue() == 1)
                    .mapToInt(Map.Entry::getKey)
                    .sum();
            return value * 4 * 4 + remainingPoints;
        }

        // Three of a kind (x3)
        if (valueCount.containsValue(3)) {
            var value = valueCount.entrySet().stream()
                    .filter(e -> e.getValue() == 3)
                    .mapToInt(Map.Entry::getKey)
                    .findFirst()
                    .orElse(0);
            var remainingPoints = valueCount.entrySet().stream()
                    .filter(e -> e.getValue() == 1)
                    .mapToInt(Map.Entry::getKey)
                    .sum();
            return value * 3 * 3 + remainingPoints;
        }

        // Pairs (x2)
        var pairs = valueCount.entrySet().stream()
                .filter(e -> e.getValue() == 2)
                .map(Map.Entry::getKey)
                .toList();
        if (!pairs.isEmpty()) {
            var pairPoints = pairs.stream()
                    .mapToInt(value -> value * 2 * 2)
                    .sum();
            var remainingPoints = valueCount.entrySet().stream()
                    .filter(e -> e.getValue() == 1)
                    .mapToInt(Map.Entry::getKey)
                    .sum();
            return pairPoints + remainingPoints;
        }

        // Straight of 5 or more (x5)
        if (maxStraight >= 5) {
            return totalPoints * 5;
        }

        // Flush - 5 or more cards of same suit (x6)
        if (suitCount.values().stream().anyMatch(list -> list.size() >= 5)) {
            return totalPoints * 6;
        }

        return totalPoints;
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var game = new PokerGame();
            game.show();
        });
    }
} 