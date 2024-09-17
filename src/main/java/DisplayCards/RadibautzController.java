package DisplayCards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
/**
 * The controller for the Radibautz game, handling game logic and user interactions.
 */
public class RadibautzController {
    private RadibautzModel model; // The game model containing game data
    private RadibautzView view;   // The game view containing UI components
    private int currentPlayer = 1; // Tracks the current player (1: Player 1, 2: Player 2, 3: Player 3)
    private int roundCount = 1;   // Tracks the current round number
    private boolean[] firstRoundPlayed = {false, false, false}; // Tracks if each player has played their first round
    private Integer klopfer = null; // The player who knocked (null if no one has knocked)
    private boolean hidePlayerCards = true; // Whether to hide the cards of player 2 and 3

    private boolean isPlayerCardSelected = false; // Tracks if a player card is selected for swapping
    private String[] selectedPlayerCard; // The selected player card for swapping
    private JLabel selectedPlayerCardLabel; // The label of the selected player card for swapping

    /**
     * Instantiates a new Radibautz controller.
     *
     * @param model the game model
     * @param view  the game view
     */
    public RadibautzController(RadibautzModel model, RadibautzView view) {
        this.model = model;
        this.view = view;

        initView();       // Initialize the view with the current game state
        initController(); // Set up event handlers and game logic
    }

    /**
     * Initializes the view by updating card panels and setting scores.
     */
    private void initView() {
        // Update card panels for all players and the board
        view.updateCardPanel(view.getPlayer1Panel(), model.getPlayer1Cards(), false);
        view.updateCardPanel(view.getPlayer2CardsPanel(), model.getPlayer2Cards(), hidePlayerCards);
        view.updateCardPanel(view.getPlayer3CardsPanel(), model.getPlayer3Cards(), hidePlayerCards);
        view.updateCardPanel(view.getBoardCardsPanel(), model.getBoardCards(), false);
        updateScores(); // Display initial scores
        addCardListeners(); // Add click listeners to cards for interaction
    }

    /**
     * Initializes the controller by setting up event listeners for buttons.
     */
    private void initController() {
        // Button listeners for player 1's actions
        view.getActionButton().addActionListener(e -> {
            if (currentPlayer == 1) {
                model.dealNewCards(); // Deal new cards
                updateScores();       // Update scores after dealing new cards
                nextPlayer();         // Proceed to the next player
            }
        });

        view.getSwapAllButton().addActionListener(e -> {
            if (currentPlayer == 1) {
                swapAllCards();       // Swap all cards between player 1 and the board
                nextPlayer();         // Proceed to the next player
            }
        });

        view.getRadibautzButton().addActionListener(e -> {
            if (currentPlayer == 1) {
                checkRadibautz();     // Check if player 1 has Radibautz (score of 31)
                nextPlayer();         // Proceed to the next player
            }
        });

        view.getKlopfenButton().addActionListener(e -> {
            if (currentPlayer == 1) {
                klopfen();            // Player 1 knocks, ending the game soon
                playSound("src/main/resources/Knocking.wav"); // Path to your .wav file
                nextPlayer();         // Proceed to the next player
            }
        });

        view.getSkipPlayerButton().addActionListener(e -> skipPlayer()); // Skip player 1's turn
    }

    /**
     * Adds mouse listeners to player and board cards for swapping interactions.
     */
    private void addCardListeners() {
        // Add listeners to player 1's cards
        addPlayerCardListeners(view.getPlayer1Panel(), model.getPlayer1Cards());
        // Add listeners to board cards
        addBoardCardListeners(view.getBoardCardsPanel(), model.getBoardCards());
    }

    /**
     * Adds mouse listeners to the player's cards for interaction.
     *
     * @param playerPanel the panel containing player cards
     * @param playerCards the list of player cards
     */
    private void addPlayerCardListeners(JPanel playerPanel, List<String[]> playerCards) {
        Component[] components = playerPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            JLabel cardLabel = (JLabel) components[i];
            String[] card = playerCards.get(i);
            cardLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (currentPlayer != 1) return; // Only allow Player 1 to interact
                    handlePlayerCardSelection(cardLabel, card); // Handle card selection
                }
            });
        }
    }

    /**
     * Adds mouse listeners to the board's cards for interaction.
     *
     * @param boardPanel the panel containing board cards
     * @param boardCards the list of board cards
     */
    private void addBoardCardListeners(JPanel boardPanel, List<String[]> boardCards) {
        Component[] components = boardPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            JLabel cardLabel = (JLabel) components[i];
            String[] card = boardCards.get(i);
            cardLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (currentPlayer != 1) return; // Only allow Player 1 to interact
                    handleBoardCardSelection(cardLabel, card); // Handle card selection and swapping
                }
            });
        }
    }

    /**
     * Handles the selection of a player's card for swapping.
     *
     * @param cardLabel the label of the selected card
     * @param card      the selected card data
     */
    private void handlePlayerCardSelection(JLabel cardLabel, String[] card) {
        if (isPlayerCardSelected) {
            if (selectedPlayerCardLabel == cardLabel) {
                // Deselect the card if clicked again
                cardLabel.setBorder(null);
                selectedPlayerCardLabel = null;
                isPlayerCardSelected = false;
            } else {
                // Select a different player card
                selectedPlayerCardLabel.setBorder(null);
                cardLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                selectedPlayerCardLabel = cardLabel;
                selectedPlayerCard = card;
            }
        } else {
            // Select the player card
            cardLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            selectedPlayerCardLabel = cardLabel;
            selectedPlayerCard = card;
            isPlayerCardSelected = true;
        }
    }

    /**
     * Handles the selection of a board card for swapping with a player's card.
     *
     * @param cardLabel the label of the selected board card
     * @param card      the selected board card data
     */
    private void handleBoardCardSelection(JLabel cardLabel, String[] card) {
        if (isPlayerCardSelected) {
            // Perform the swap between the selected player card and the selected board card
            int playerCardIndex = model.getPlayer1Cards().indexOf(selectedPlayerCard);
            int boardCardIndex = model.getBoardCards().indexOf(card);

            String[] oldPlayerCard = model.getPlayer1Cards().get(playerCardIndex);
            String[] oldBoardCard = model.getBoardCards().get(boardCardIndex);

            model.getPlayer1Cards().set(playerCardIndex, oldBoardCard);
            model.getBoardCards().set(boardCardIndex, selectedPlayerCard);

            // Deselect the selected card
            selectedPlayerCardLabel.setBorder(null);
            selectedPlayerCardLabel = null;
            isPlayerCardSelected = false;

            updateScores(); // Update the scores after the swap

            // Refresh the panels with updated cards
            view.updateCardPanel(view.getPlayer1Panel(), model.getPlayer1Cards(), false);
            view.updateCardPanel(view.getBoardCardsPanel(), model.getBoardCards(), false);
            addCardListeners(); // Re-add listeners after the update

            view.getStatusLabel().setText("<html>Player 1 tauscht " + oldPlayerCard[3] + " of " + oldPlayerCard[2] + " gegen " + oldBoardCard[3] + " of " + oldBoardCard[2] + "</html>");
            firstRoundPlayed[currentPlayer - 1] = true; // Mark that the player has played their first round
            view.revalidate();
            view.repaint();

            nextPlayer(); // Move to the next player after swapping
        }
    }

    /**
     * Swaps all cards between player 1 and the board.
     */
    private void swapAllCards() {
        // Swap all cards between player 1 and the board
        List<String[]> temp = new ArrayList<>(model.getPlayer1Cards());
        model.getPlayer1Cards().clear();
        model.getPlayer1Cards().addAll(model.getBoardCards());
        model.getBoardCards().clear();
        model.getBoardCards().addAll(temp);

        updateScores(); // Update the scores after swapping

        // Refresh the panels with updated cards
        view.updateCardPanel(view.getPlayer1Panel(), model.getPlayer1Cards(), false);
        view.updateCardPanel(view.getBoardCardsPanel(), model.getBoardCards(), false);
        addCardListeners(); // Re-add listeners after the update

        StringBuilder swapInfo = new StringBuilder("<html>Kompletter Tausch:<br>");
        for (int i = 0; i < model.getPlayer1Cards().size(); i++) {
            swapInfo.append(model.getPlayer1Cards().get(i)[3]).append(" of ").append(model.getPlayer1Cards().get(i)[2])
                    .append(" <--> ").append(model.getBoardCards().get(i)[3]).append(" of ").append(model.getBoardCards().get(i)[2]).append("<br>");
        }
        swapInfo.append("</html>");
        view.getStatusLabel().setText(swapInfo.toString());
        firstRoundPlayed[currentPlayer - 1] = true; // Mark that the player has played their first round
        view.revalidate();
        view.repaint();
    }

    /**
     * Checks if player 1 has Radibautz (a score of 31) and displays a message.
     */
    private void checkRadibautz() {
        double player1Score = model.calculateScore(model.getPlayer1Cards());
        String message = (player1Score == 31) ? "Meddl" : "Looser"; // Check if the score is exactly 31
        JOptionPane.showMessageDialog(view, message); // Show result message
        model.dealNewCards(); // Deal new cards for the next game
        initView();           // Reinitialize the view with the new cards
        addCardListeners();   // Re-add listeners after dealing new cards
    }

    /**
     * Handles the action when a player knocks (Klopfen), indicating satisfaction with their cards.
     */
    private void klopfen() {
        if (roundCount < 2) {
            JOptionPane.showMessageDialog(view, "Klopfen is only allowed from the second round onwards!"); // Warn if knocking too early
            return;
        }
        if (klopfer != null) {
            JOptionPane.showMessageDialog(view, "Another player has already knocked!"); // Warn if another player has already knocked
            return;
        }
        klopfer = currentPlayer; // Mark the current player as the one who knocked
        view.getKlopfenLabel().setText("Player who knocked: Player " + klopfer);
        view.getStatusLabel().setText("<html>Player " + currentPlayer + " is satisfied with their cards.<br>Next player's turn.</html>");
    }

    /**
     * Skips the current player's turn if they have already played a valid move in the first round.
     */
    private void skipPlayer() {
        if (!firstRoundPlayed[currentPlayer - 1]) {
            JOptionPane.showMessageDialog(view, "You must make a valid move in the first round!"); // Prevent skipping without a valid move
            return;
        }
        nextPlayer(); // Move to the next player
    }

    /**
     * Advances the turn to the next player and handles the end of the game if necessary.
     */
    private void nextPlayer() {
        currentPlayer = (currentPlayer % 3) + 1; // Cycle through players 1, 2, 3
        view.getCurrentPlayerLabel().setText("Current Player: Player " + currentPlayer); // Update the current player display
        highlightCurrentPlayer(); // Highlight the current player's section
        if (klopfer != null && currentPlayer == klopfer) {
            endGame(); // End the game if the player who knocked is up again
            return;
        }

        // Handle AI players (Player 2 and 3)
        if (currentPlayer == 2 || currentPlayer == 3) {
            new Thread(() -> {
                try {
                    Thread.sleep(3000); // Simulate AI thinking time
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SwingUtilities.invokeLater(this::aiTurn); // AI takes its turn
            }).start();
        } else {
            if (klopfer == null) {
                roundCount++; // Increment round count only if no one has knocked
            }
            view.getRoundCountLabel().setText("Runde: " + roundCount); // Update the round count display
        }
    }

    /**
     * Simulates the AI player's turn, deciding on optimal moves.
     */
    private void aiTurn() {
        List<String[]> currentPlayerCards = (currentPlayer == 2) ? model.getPlayer2Cards() : model.getPlayer3Cards();
        String player = (currentPlayer == 2) ? "Player 2" : "Player 3";

        // Calculate current score
        double currentScore = model.calculateScore(currentPlayerCards);

        // Check if swapping all cards increases score
        List<String[]> temp = new ArrayList<>(currentPlayerCards);
        currentPlayerCards.clear();
        currentPlayerCards.addAll(model.getBoardCards());
        double scoreAfterSwapAll = model.calculateScore(currentPlayerCards);
        currentPlayerCards.clear();
        currentPlayerCards.addAll(temp);

        double maxScore = currentScore;
        int swapIndexPlayer = -1;
        int swapIndexBoard = -1;

        // Check if swapping one card increases score
        for (int i = 0; i < currentPlayerCards.size(); i++) {
            for (int j = 0; j < model.getBoardCards().size(); j++) {
                String[] tempPlayerCard = currentPlayerCards.get(i);
                String[] tempBoardCard = model.getBoardCards().get(j);

                // Swap the cards and calculate the new score
                currentPlayerCards.set(i, tempBoardCard);
                model.getBoardCards().set(j, tempPlayerCard);

                double scoreAfterSwapOne = model.calculateScore(currentPlayerCards);
                if (scoreAfterSwapOne > maxScore) {
                    maxScore = scoreAfterSwapOne; // Update max score if swap improves score
                    swapIndexPlayer = i;
                    swapIndexBoard = j;
                }

                // Swap back to original
                currentPlayerCards.set(i, tempPlayerCard);
                model.getBoardCards().set(j, tempBoardCard);
            }
        }

        if (roundCount >= 2 && currentScore >= maxScore && klopfer == null) {
            // AI decides to knock if it can't improve its score and no one else has knocked
            klopfen();
        } else if (scoreAfterSwapAll > maxScore) {
            // Swap all cards if it improves score
            currentPlayerCards.clear();
            currentPlayerCards.addAll(model.getBoardCards());
            model.getBoardCards().clear();
            model.getBoardCards().addAll(temp);

            StringBuilder swapInfo = new StringBuilder("<html>" + player + " Kompletter Tausch:<br>");
            for (int i = 0; i < currentPlayerCards.size(); i++) {
                swapInfo.append(currentPlayerCards.get(i)[3]).append(" of ").append(currentPlayerCards.get(i)[2])
                        .append(" <--> ").append(model.getBoardCards().get(i)[3]).append(" of ").append(model.getBoardCards().get(i)[2]).append("<br>");
            }
            swapInfo.append("</html>");
            view.getStatusLabel().setText(swapInfo.toString());
        } else if (swapIndexPlayer != -1) {
            // Swap one card if it improves score
            String[] oldPlayerCard = currentPlayerCards.get(swapIndexPlayer);
            String[] oldBoardCard = model.getBoardCards().get(swapIndexBoard);

            currentPlayerCards.set(swapIndexPlayer, oldBoardCard);
            model.getBoardCards().set(swapIndexBoard, oldPlayerCard);

            view.getStatusLabel().setText("<html>" + player + " tauscht " + oldPlayerCard[3] + " of " + oldPlayerCard[2] + " gegen " + oldBoardCard[3] + " of " + oldBoardCard[2] + "</html>");
        }

        // Update card panels and scores for AI
        if (currentPlayer == 2) {
            view.updateCardPanel(view.getPlayer2CardsPanel(), model.getPlayer2Cards(), hidePlayerCards);
            double player2Score = model.calculateScore(model.getPlayer2Cards());
            view.getScoreLabel2().setText("Score Player 2: " + player2Score);
        } else {
            view.updateCardPanel(view.getPlayer3CardsPanel(), model.getPlayer3Cards(), hidePlayerCards);
            double player3Score = model.calculateScore(model.getPlayer3Cards());
            view.getScoreLabel3().setText("Score Player 3: " + player3Score);
        }

        view.updateCardPanel(view.getBoardCardsPanel(), model.getBoardCards(), false);
        double boardScore = model.calculateScore(model.getBoardCards());
        view.getScoreBoardLabel().setText("Score Board: " + boardScore);

        view.revalidate();
        view.repaint();

        nextPlayer(); // Move to the next player after AI completes turn
    }

    /**
     * Highlights the current player panel to indicate whose turn it is.
     */
    private void highlightCurrentPlayer() {
        // Highlight the current player's panel
        view.getPlayer1Panel().setBackground(currentPlayer == 1 ? new Color(144, 238, 144, 50) : null);
        view.getPlayer2CardsPanel().setBackground(currentPlayer == 2 ? new Color(144, 238, 144, 50) : null);
        view.getPlayer3CardsPanel().setBackground(currentPlayer == 3 ? new Color(144, 238, 144, 50) : null);

        // Update the card panels
        view.updateCardPanel(view.getPlayer1Panel(), model.getPlayer1Cards(), false);
        view.updateCardPanel(view.getPlayer2CardsPanel(), model.getPlayer2Cards(), hidePlayerCards);
        view.updateCardPanel(view.getPlayer3CardsPanel(), model.getPlayer3Cards(), hidePlayerCards);
        addCardListeners();
    }

    /**
     * Ends the game, calculates scores, and displays the winner(s) and the player with the lowest score.
     */
    private void endGame() {
        // Calculate scores for all players
        double player1Score = model.calculateScore(model.getPlayer1Cards());
        double player2Score = model.calculateScore(model.getPlayer2Cards());
        double player3Score = model.calculateScore(model.getPlayer3Cards());

        // Determine the highest and lowest score
        double maxScore = Math.max(player1Score, Math.max(player2Score, player3Score));
        double minScore = Math.min(player1Score, Math.min(player2Score, player3Score));

        // Collect the winners
        List<String> winners = new ArrayList<>();
        if (player1Score == maxScore) {
            winners.add("Player 1");
        }
        if (player2Score == maxScore) {
            winners.add("Player 2");
        }
        if (player3Score == maxScore) {
            winners.add("Player 3");
        }

        // Display the winners and the player with the lowest score
        StringBuilder message = new StringBuilder("Winner(s): " + String.join(", ", winners) + " with a score of " + maxScore);

        if (player1Score == minScore) {
            message.append("\nPlayer with the lowest score: Player 1");
        }
        if (player2Score == minScore) {
            message.append("\nPlayer with the lowest score: Player 2");
        }
        if (player3Score == minScore) {
            message.append("\nPlayer with the lowest score: Player 3");
        }

        JOptionPane.showMessageDialog(view, message.toString()); // Show the end game message

        model.dealNewCards(); // Deal new cards for the next game
        initView();           // Reinitialize the view with the new cards
        addCardListeners();   // Re-add listeners after dealing new cards
    }

    /**
     * Updates the score displays for all players and the board.
     */
    private void updateScores() {
        // Calculate scores for all players and the board
        double player1Score = model.calculateScore(model.getPlayer1Cards());
        double player2Score = model.calculateScore(model.getPlayer2Cards());
        double player3Score = model.calculateScore(model.getPlayer3Cards());
        double boardScore = model.calculateScore(model.getBoardCards());

        // Update score labels
        view.getScoreLabel1().setText("Score Player 1: " + player1Score);
        view.getScoreLabel2().setText("Score Player 2: " + player2Score);
        view.getScoreLabel3().setText("Score Player 3: " + player3Score);
        view.getScoreBoardLabel().setText("Score Board: " + boardScore);
    }

    /**
     * Plays a sound from a specified file path.
     *
     * @param soundFilePath the path to the sound file
     */
    private void playSound(String soundFilePath) {
        try {
            // Load the audio file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFilePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start(); // Play the sound
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}