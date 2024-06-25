package DisplayCards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Radibautz controller.
 */
public class RadibautzController {
    private RadibautzModel model;
    private RadibautzView view;
    private int currentPlayer = 1;
    private int roundCount = 1;
    private boolean[] firstRoundPlayed = {false, false, false};
    private Integer klopfer = null;
    private boolean hidePlayerCards = true;

    private boolean isPlayerCardSelected = false;
    private String[] selectedPlayerCard;
    private JLabel selectedPlayerCardLabel;

    /**
     * Instantiates a new Radibautz controller.
     *
     * @param model the model
     * @param view  the view
     */
    public RadibautzController(RadibautzModel model, RadibautzView view) {
        this.model = model;
        this.view = view;

        initView();
        initController();
    }

    private void initView() {
        view.updateCardPanel(view.getPlayer1Panel(), model.getPlayer1Cards(), false);
        view.updateCardPanel(view.getPlayer2CardsPanel(), model.getPlayer2Cards(), hidePlayerCards);
        view.updateCardPanel(view.getPlayer3CardsPanel(), model.getPlayer3Cards(), hidePlayerCards);
        view.updateCardPanel(view.getBoardCardsPanel(), model.getBoardCards(), false);
        updateScores();
        addCardListeners();
    }

    private void initController() {
        view.getActionButton().addActionListener(e -> {
            if (currentPlayer == 1) {
                model.dealNewCards();
                updateScores();
                nextPlayer();
            }
        });

        view.getSwapAllButton().addActionListener(e -> {
            if (currentPlayer == 1) {
                swapAllCards();
                nextPlayer();
            }
        });

        view.getRadibautzButton().addActionListener(e -> {
            if (currentPlayer == 1) {
                checkRadibautz();
                nextPlayer();
            }
        });

        view.getKlopfenButton().addActionListener(e -> {
            if (currentPlayer == 1) {
                klopfen();
                nextPlayer();
            }
        });

        view.getSkipPlayerButton().addActionListener(e -> skipPlayer());
    }

    private void addCardListeners() {
        addPlayerCardListeners(view.getPlayer1Panel(), model.getPlayer1Cards());
        addBoardCardListeners(view.getBoardCardsPanel(), model.getBoardCards());
    }

    private void addPlayerCardListeners(JPanel playerPanel, List<String[]> playerCards) {
        Component[] components = playerPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            JLabel cardLabel = (JLabel) components[i];
            String[] card = playerCards.get(i);
            cardLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (currentPlayer != 1) return;
                    handlePlayerCardSelection(cardLabel, card);
                }
            });
        }
    }

    private void addBoardCardListeners(JPanel boardPanel, List<String[]> boardCards) {
        Component[] components = boardPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            JLabel cardLabel = (JLabel) components[i];
            String[] card = boardCards.get(i);
            cardLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (currentPlayer != 1) return;
                    handleBoardCardSelection(cardLabel, card);
                }
            });
        }
    }

    private void handlePlayerCardSelection(JLabel cardLabel, String[] card) {
        if (isPlayerCardSelected) {
            if (selectedPlayerCardLabel == cardLabel) {
                cardLabel.setBorder(null);
                selectedPlayerCardLabel = null;
                isPlayerCardSelected = false;
            } else {
                selectedPlayerCardLabel.setBorder(null);
                cardLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                selectedPlayerCardLabel = cardLabel;
                selectedPlayerCard = card;
            }
        } else {
            cardLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            selectedPlayerCardLabel = cardLabel;
            selectedPlayerCard = card;
            isPlayerCardSelected = true;
        }
    }

    private void handleBoardCardSelection(JLabel cardLabel, String[] card) {
        if (isPlayerCardSelected) {
            int playerCardIndex = model.getPlayer1Cards().indexOf(selectedPlayerCard);
            int boardCardIndex = model.getBoardCards().indexOf(card);

            String[] oldPlayerCard = model.getPlayer1Cards().get(playerCardIndex);
            String[] oldBoardCard = model.getBoardCards().get(boardCardIndex);

            model.getPlayer1Cards().set(playerCardIndex, oldBoardCard);
            model.getBoardCards().set(boardCardIndex, selectedPlayerCard);

            selectedPlayerCardLabel.setBorder(null);
            selectedPlayerCardLabel = null;
            isPlayerCardSelected = false;

            updateScores();

            view.updateCardPanel(view.getPlayer1Panel(), model.getPlayer1Cards(), false);
            view.updateCardPanel(view.getBoardCardsPanel(), model.getBoardCards(), false);
            addCardListeners();

            view.getStatusLabel().setText("<html>Player 1 tauscht " + oldPlayerCard[3] + " of " + oldPlayerCard[2] + " gegen " + oldBoardCard[3] + " of " + oldBoardCard[2] + "</html>");
            firstRoundPlayed[currentPlayer - 1] = true;
            view.revalidate();
            view.repaint();

            nextPlayer();
        }
    }

    private void swapAllCards() {
        List<String[]> temp = new ArrayList<>(model.getPlayer1Cards());
        model.getPlayer1Cards().clear();
        model.getPlayer1Cards().addAll(model.getBoardCards());
        model.getBoardCards().clear();
        model.getBoardCards().addAll(temp);

        updateScores();

        view.updateCardPanel(view.getPlayer1Panel(), model.getPlayer1Cards(), false);
        view.updateCardPanel(view.getBoardCardsPanel(), model.getBoardCards(), false);
        addCardListeners();

        StringBuilder swapInfo = new StringBuilder("<html>Kompletter Tausch:<br>");
        for (int i = 0; i < model.getPlayer1Cards().size(); i++) {
            swapInfo.append(model.getPlayer1Cards().get(i)[3]).append(" of ").append(model.getPlayer1Cards().get(i)[2])
                    .append(" <--> ").append(model.getBoardCards().get(i)[3]).append(" of ").append(model.getBoardCards().get(i)[2]).append("<br>");
        }
        swapInfo.append("</html>");
        view.getStatusLabel().setText(swapInfo.toString());
        firstRoundPlayed[currentPlayer - 1] = true;
        view.revalidate();
        view.repaint();
    }

    private void checkRadibautz() {
        double player1Score = model.calculateScore(model.getPlayer1Cards());
        String message = (player1Score == 31) ? "Meddl" : "Looser";
        JOptionPane.showMessageDialog(view, message);
        model.dealNewCards();
        initView(); // Add this to reinitialize the view with new cards
        addCardListeners(); // Add this to reinitialize the listeners
    }

    private void klopfen() {
        if (roundCount < 2) {
            JOptionPane.showMessageDialog(view, "Klopfen is only allowed from the second round onwards!");
            return;
        }
        if (klopfer != null) {
            JOptionPane.showMessageDialog(view, "Another player has already knocked!");
            return;
        }
        klopfer = currentPlayer;
        view.getKlopfenLabel().setText("Player who knocked: Player " + klopfer);
        view.getStatusLabel().setText("<html>Player " + currentPlayer + " is satisfied with their cards.<br>Next player's turn.</html>");
    }

    private void skipPlayer() {
        if (!firstRoundPlayed[currentPlayer - 1]) {
            JOptionPane.showMessageDialog(view, "You must make a valid move in the first round!");
            return;
        }
        nextPlayer();
    }

    private void nextPlayer() {
        currentPlayer = (currentPlayer % 3) + 1;
        view.getCurrentPlayerLabel().setText("Current Player: Player " + currentPlayer);
        highlightCurrentPlayer();
        if (klopfer != null && currentPlayer == klopfer) {
            endGame();
            return;
        }

        if (currentPlayer == 2 || currentPlayer == 3) {
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SwingUtilities.invokeLater(this::aiTurn);
            }).start();
        } else {
            if (klopfer == null) {
                roundCount++;
            }
            view.getRoundCountLabel().setText("Runde: " + roundCount);
        }
    }

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

                currentPlayerCards.set(i, tempBoardCard);
                model.getBoardCards().set(j, tempPlayerCard);

                double scoreAfterSwapOne = model.calculateScore(currentPlayerCards);
                if (scoreAfterSwapOne > maxScore) {
                    maxScore = scoreAfterSwapOne;
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
            // Swap all cards
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
            // Swap one card
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

        nextPlayer(); // Move to next player
    }

    private void highlightCurrentPlayer() {
        view.getPlayer1Panel().setBackground(currentPlayer == 1 ? new Color(144, 238, 144, 50) : null);
        view.getPlayer2CardsPanel().setBackground(currentPlayer == 2 ? new Color(144, 238, 144, 50) : null);
        view.getPlayer3CardsPanel().setBackground(currentPlayer == 3 ? new Color(144, 238, 144, 50) : null);

        view.updateCardPanel(view.getPlayer1Panel(), model.getPlayer1Cards(), false);
        view.updateCardPanel(view.getPlayer2CardsPanel(), model.getPlayer2Cards(), hidePlayerCards);
        view.updateCardPanel(view.getPlayer3CardsPanel(), model.getPlayer3Cards(), hidePlayerCards);
        addCardListeners();
    }

    /**
    Berechnung des Scores der Spieer und Auswertung
     */
    private void endGame() {
        double player1Score = model.calculateScore(model.getPlayer1Cards());
        double player2Score = model.calculateScore(model.getPlayer2Cards());
        double player3Score = model.calculateScore(model.getPlayer3Cards());

        double maxScore = Math.max(player1Score, Math.max(player2Score, player3Score));
        double minScore = Math.min(player1Score, Math.min(player2Score, player3Score));

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

        JOptionPane.showMessageDialog(view, message.toString());

        model.dealNewCards();
        initView(); // Add this to reinitialize the view with new cards
        addCardListeners(); // Add this to reinitialize the listeners
    }
/**
 Aktualisierung der Scores
 */
    private void updateScores() {
        double player1Score = model.calculateScore(model.getPlayer1Cards());
        double player2Score = model.calculateScore(model.getPlayer2Cards());
        double player3Score = model.calculateScore(model.getPlayer3Cards());
        double boardScore = model.calculateScore(model.getBoardCards());

        view.getScoreLabel1().setText("Score Player 1: " + player1Score);
        view.getScoreLabel2().setText("Score Player 2: " + player2Score);
        view.getScoreLabel3().setText("Score Player 3: " + player3Score);
        view.getScoreBoardLabel().setText("Score Board: " + boardScore);
    }
}
