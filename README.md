# Java Poker Game

A two-player poker game with a graphical interface, built using Java Swing.

## Features

- Graphical card display with real card images
- Two-player turn-based gameplay
- Each player can select and exchange cards from their hand
- Hand scoring system that recognizes pairs, three/four of a kind, straights, and flushes
- Round-based scoring to track wins across multiple rounds

## How It Works

- Each player is dealt 5 cards, with 3 shared "desk" cards drawn from the deck
- Players take turns selecting cards to exchange for new ones
- After both players have played, the desk cards are revealed
- Hand points are calculated based on poker-style combinations (pairs, three of a kind, straights, flushes, etc.)

## Tech Stack

- Java
- Java Swing (GUI)

## How to Run

1. Make sure you have a Java Development Kit (JDK) installed.
2. Compile the files:

   javac Card.java Deck.java PokerGame.java

3. Run the game:

   java PokerGame

Make sure the Cards folder (containing the card images) is in the same directory as the compiled files.