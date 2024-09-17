package DisplayCards;

import java.util.*;

/**
 * The model class for the Radibautz game, managing the game's state and logic.
 */
public class RadibautzModel {

    // Array defining all the cards in the game with their images, values, suits, and names
    private static final String[][][] CARDS = {
            {{"6_of_clubs.png", "6", "Clubs", "Six", "6_of_Eichel.png"}, {"7_of_clubs.png", "7", "Clubs", "Seven", "7_of_Eichel.png"}, {"8_of_clubs.png", "8", "Clubs", "Eight", "8_of_Eichel.png"}},
            {{"9_of_clubs.png", "9", "Clubs", "Nine", "9_of_Eichel.png"}, {"10_of_clubs.png", "10", "Clubs", "Ten", "10_of_Eichel.png"}, {"jack_of_clubs.png", "10", "Clubs", "Jack", "Ober_of_Eichel.png"}},
            {{"queen_of_clubs.png", "10", "Clubs", "Queen", "Under_of_Eichel.png"}, {"king_of_clubs.png", "10", "Clubs", "King", "König_of_Eichel.png"}, {"ace_of_clubs.png", "11", "Clubs", "Ace", "Ass_of_Eichel.png"}},
            {{"6_of_diamonds.png", "6", "Diamonds", "Six", "6_of_Rosen.png"}, {"7_of_diamonds.png", "7", "Diamonds", "Seven", "7_of_Rosen.png"}, {"8_of_diamonds.png", "8", "Diamonds", "Eight", "8_of_Rosen.png"}},
            {{"9_of_diamonds.png", "9", "Diamonds", "Nine", "9_of_Rosen.png"}, {"10_of_diamonds.png", "10", "Diamonds", "Ten", "10_of_Rosen.png"}, {"jack_of_diamonds.png", "10", "Diamonds", "Jack", "Ober_of_Rosen.png"}},
            {{"queen_of_diamonds.png", "10", "Diamonds", "Queen", "Under_of_Rosen.png"}, {"king_of_diamonds.png", "10", "Diamonds", "King", "König_of_Rosen.png"}, {"ace_of_diamonds.png", "11", "Diamonds", "Ace", "Ass_of_Rosen.png"}},
            {{"6_of_hearts.png", "6", "Hearts", "Six", "6_of_Schellen.png"}, {"7_of_hearts.png", "7", "Hearts", "Seven", "7_of_Schellen.png"}, {"8_of_hearts.png", "8", "Hearts", "Eight", "8_of_Schellen.png"}},
            {{"9_of_hearts.png", "9", "Hearts", "Nine", "9_of_Schellen.png"}, {"10_of_hearts.png", "10", "Hearts", "Ten", "10_of_Schellen.png"}, {"jack_of_hearts.png", "10", "Hearts", "Jack", "Ober_of_Schellen.png"}},
            {{"queen_of_hearts.png", "10", "Hearts", "Queen", "Under_of_Schellen.png"}, {"king_of_hearts.png", "10", "Hearts", "King", "König_of_Schellen.png"}, {"ace_of_hearts.png", "11", "Hearts", "Ace", "Ass_of_Schellen.png"}},
            {{"6_of_spades.png", "6", "Spades", "Six", "6_of_Schilten.png"}, {"7_of_spades.png", "7", "Spades", "Seven", "7_of_Schilten.png"}, {"8_of_spades.png", "8", "Spades", "Eight", "8_of_Schilten.png"}},
            {{"9_of_spades.png", "9", "Spades", "Nine", "9_of_Schilten.png"}, {"10_of_spades.png", "10", "Spades", "Ten", "10_of_Schilten.png"}, {"jack_of_spades.png", "10", "Spades", "Jack", "Ober_of_Schilten.png"}},
            {{"queen_of_spades.png", "10", "Spades", "Queen", "Under_of_Schilten.png"}, {"king_of_spades.png", "10", "Spades", "King", "König_of_Schilten.png"}, {"ace_of_spades.png", "11", "Spades", "Ace", "Ass_of_Schilten.png"}}
    };


    private List<String[]> cardList;       // List of all cards in the game
    private List<String[]> player1Cards;   // Cards held by Player 1
    private List<String[]> player2Cards;   // Cards held by Player 2
    private List<String[]> player3Cards;   // Cards held by Player 3
    private List<String[]> boardCards;     // Cards on the board

    /**
     * Instantiates a new Radibautz model, initializing the card lists and dealing new cards.
     */
    public RadibautzModel() {
        cardList = new ArrayList<>();
        player1Cards = new ArrayList<>();
        player2Cards = new ArrayList<>();
        player3Cards = new ArrayList<>();
        boardCards = new ArrayList<>();
        dealNewCards(); // Deal cards when the game starts
    }

    /**
     * Deals new cards to all players and the board by shuffling and distributing the card list.
     */
    public void dealNewCards() {
        // Reset card list and shuffle
        cardList.clear();
        for (String[][] cardSet : CARDS) {
            Collections.addAll(cardList, cardSet); // Add all cards to the list
        }
        Collections.shuffle(cardList); // Shuffle the cards

        // Clear existing card lists
        player1Cards.clear();
        player2Cards.clear();
        player3Cards.clear();
        boardCards.clear();

        // Distribute 3 cards to each player and 3 cards to the board
        for (int i = 0; i < 3; i++) {
            player1Cards.add(cardList.get(i));
            player2Cards.add(cardList.get(i + 3));
            player3Cards.add(cardList.get(i + 6));
        }

        for (int i = 9; i < 12; i++) {
            boardCards.add(cardList.get(i));
        }
    }

    /**
     * Calculates the score of a set of cards, considering special cases.
     *
     * @param cards the list of cards to calculate the score for
     * @return the calculated score
     */
    public double calculateScore(List<String[]> cards) {
        // Special case: all cards have the same value but different suits
        if (cards.size() == 3) {
            String prefix1 = cards.get(0)[0].split("_of_")[0];
            String prefix2 = cards.get(1)[0].split("_of_")[0];
            String prefix3 = cards.get(2)[0].split("_of_")[0];
            String suit1 = cards.get(0)[2];
            String suit2 = cards.get(1)[2];
            String suit3 = cards.get(2)[2];

            // Check if all three cards have the same value but different suits
            if (!suit1.equals(suit2) && !suit1.equals(suit3) && !suit2.equals(suit3)
                    && prefix1.equals(prefix2) && prefix1.equals(prefix3)) {
                return 30.5; // Special score for this condition
            }
        }

        int highestScore = 0; // Track the highest single card value
        Map<String, Integer> suitScores = new HashMap<>(); // Map to accumulate scores by suit

        for (String[] card : cards) {
            int cardValue = Integer.parseInt(card[1]); // Get card value
            String suit = card[2]; // Get card suit

            // Accumulate scores by suit
            suitScores.put(suit, suitScores.getOrDefault(suit, 0) + cardValue);
            highestScore = Math.max(highestScore, cardValue); // Update highest single card value
        }

        int maxSuitScore = suitScores.values().stream().max(Integer::compareTo).orElse(0); // Find the max score by suit
        return Math.max(maxSuitScore, highestScore); // Return the higher of the suit score or single card value
    }

    // Getter methods for the lists of cards held by each player and on the board
    public List<String[]> getPlayer1Cards() { return player1Cards; }
    public List<String[]> getPlayer2Cards() { return player2Cards; }
    public List<String[]> getPlayer3Cards() { return player3Cards; }
    public List<String[]> getBoardCards() { return boardCards; }
}