package DisplayCards;

import java.util.*;

/**
 * The type Radibautz model.
 */
public class RadibautzModel {

    private static final String[][][] CARDS = {
            {{"6_of_clubs.png", "6", "Clubs", "Six"}, {"7_of_clubs.png", "7", "Clubs", "Seven"}, {"8_of_clubs.png", "8", "Clubs", "Eight"}},
            {{"9_of_clubs.png", "9", "Clubs", "Nine"}, {"10_of_clubs.png", "10", "Clubs", "Ten"}, {"jack_of_clubs.png", "10", "Clubs", "Jack"}},
            {{"queen_of_clubs.png", "10", "Clubs", "Queen"}, {"king_of_clubs.png", "10", "Clubs", "King"}, {"ace_of_clubs.png", "11", "Clubs", "Ace"}},
            {{"6_of_diamonds.png", "6", "Diamonds", "Six"}, {"7_of_diamonds.png", "7", "Diamonds", "Seven"}, {"8_of_diamonds.png", "8", "Diamonds", "Eight"}},
            {{"9_of_diamonds.png", "9", "Diamonds", "Nine"}, {"10_of_diamonds.png", "10", "Diamonds", "Ten"}, {"jack_of_diamonds.png", "10", "Diamonds", "Jack"}},
            {{"queen_of_diamonds.png", "10", "Diamonds", "Queen"}, {"king_of_diamonds.png", "10", "Diamonds", "King"}, {"ace_of_diamonds.png", "11", "Diamonds", "Ace"}},
            {{"6_of_hearts.png", "6", "Hearts", "Six"}, {"7_of_hearts.png", "7", "Hearts", "Seven"}, {"8_of_hearts.png", "8", "Hearts", "Eight"}},
            {{"9_of_hearts.png", "9", "Hearts", "Nine"}, {"10_of_hearts.png", "10", "Hearts", "Ten"}, {"jack_of_hearts.png", "10", "Hearts", "Jack"}},
            {{"queen_of_hearts.png", "10", "Hearts", "Queen"}, {"king_of_hearts.png", "10", "Hearts", "King"}, {"ace_of_hearts.png", "11", "Hearts", "Ace"}},
            {{"6_of_spades.png", "6", "Spades", "Six"}, {"7_of_spades.png", "7", "Spades", "Seven"}, {"8_of_spades.png", "8", "Spades", "Eight"}},
            {{"9_of_spades.png", "9", "Spades", "Nine"}, {"10_of_spades.png", "10", "Spades", "Ten"}, {"jack_of_spades.png", "10", "Spades", "Jack"}},
            {{"queen_of_spades.png", "10", "Spades", "Queen"}, {"king_of_spades.png", "10", "Spades", "King"}, {"ace_of_spades.png", "11", "Spades", "Ace"}}
    };

    private List<String[]> cardList;
    private List<String[]> player1Cards;
    private List<String[]> player2Cards;
    private List<String[]> player3Cards;
    private List<String[]> boardCards;

    /**
     * Instantiates a new Radibautz model.
     */
    public RadibautzModel() {
        cardList = new ArrayList<>();
        player1Cards = new ArrayList<>();
        player2Cards = new ArrayList<>();
        player3Cards = new ArrayList<>();
        boardCards = new ArrayList<>();
        dealNewCards();
    }

    /**
     * Deal new cards.
     */
    public void dealNewCards() {
        // Reset card list and shuffle
        cardList.clear();
        for (String[][] cardSet : CARDS) {
            Collections.addAll(cardList, cardSet);
        }
        Collections.shuffle(cardList);

        // Clear existing card lists
        player1Cards.clear();
        player2Cards.clear();
        player3Cards.clear();
        boardCards.clear();

        // Distribute cards
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
     * Calculate score double.
     *
     * @param cards the cards
     * @return the double
     */
    public double calculateScore(List<String[]> cards) {
        if (cards.size() == 3) {
            String prefix1 = cards.get(0)[0].split("_of_")[0];
            String prefix2 = cards.get(1)[0].split("_of_")[0];
            String prefix3 = cards.get(2)[0].split("_of_")[0];
            String suit1 = cards.get(0)[2];
            String suit2 = cards.get(1)[2];
            String suit3 = cards.get(2)[2];

            if (!suit1.equals(suit2) && !suit1.equals(suit3) && !suit2.equals(suit3)
                    && prefix1.equals(prefix2) && prefix1.equals(prefix3)) {
                return 30.5;
            }
        }

        int highestScore = 0;
        Map<String, Integer> suitScores = new HashMap<>();

        for (String[] card : cards) {
            int cardValue = Integer.parseInt(card[1]);
            String suit = card[2];

            suitScores.put(suit, suitScores.getOrDefault(suit, 0) + cardValue);
            highestScore = Math.max(highestScore, cardValue);
        }

        int maxSuitScore = suitScores.values().stream().max(Integer::compareTo).orElse(0);
        return Math.max(maxSuitScore, highestScore);
    }

    /**
     * Gets player 1 cards.
     *
     * @return the player 1 cards
     */
    public List<String[]> getPlayer1Cards() {
        return player1Cards;
    }

    /**
     * Gets player 2 cards.
     *
     * @return the player 2 cards
     */
    public List<String[]> getPlayer2Cards() {
        return player2Cards;
    }

    /**
     * Gets player 3 cards.
     *
     * @return the player 3 cards
     */
    public List<String[]> getPlayer3Cards() {
        return player3Cards;
    }

    /**
     * Gets board cards.
     *
     * @return the board cards
     */
    public List<String[]> getBoardCards() {
        return boardCards;
    }
}
