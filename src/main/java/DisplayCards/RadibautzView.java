package DisplayCards;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The type Radibautz view.
 */
public class RadibautzView extends JFrame {
    /**
     * The constant CARD_PATH.
     */
    public static final String CARD_PATH = "src/Cards/";
    /**
     * The constant JOKER_CARD.
     */
    public static final String JOKER_CARD = "ChreguCard.png";

    private JPanel player1Panel;
    private JPanel player2CardsPanel;
    private JPanel player3CardsPanel;
    private JPanel boardCardsPanel;
    private JLabel scoreLabel1;
    private JLabel scoreLabel2;
    private JLabel scoreLabel3;
    private JLabel scoreBoardLabel;
    private JLabel statusLabel;
    private JLabel currentPlayerLabel;
    private JLabel roundCountLabel;
    private JLabel klopfenLabel;
    private JButton actionButton;
    private JButton swapAllButton;
    private JButton radibautzButton;
    private JButton klopfenButton;
    private JButton skipPlayerButton;

    /**
     * Instantiates a new Radibautz view.
     */
    public RadibautzView() {
        setTitle("Radibautz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1500, 1000);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new BorderLayout());
        boardPanel.setBackground(new Color(205, 133, 63));
        scoreBoardLabel = new JLabel("Score Board: 0", SwingConstants.CENTER);
        boardPanel.add(scoreBoardLabel, BorderLayout.NORTH);

        boardCardsPanel = new JPanel();
        boardCardsPanel.setLayout(new FlowLayout());
        boardCardsPanel.setBackground(new Color(205, 133, 63));
        boardPanel.add(boardCardsPanel, BorderLayout.CENTER);

        player1Panel = new JPanel();
        player1Panel.setLayout(new FlowLayout());

        JPanel player1Container = new JPanel();
        player1Container.setLayout(new BorderLayout());
        player1Container.add(player1Panel, BorderLayout.CENTER);

        scoreLabel1 = new JLabel("Score Player 1: 0", SwingConstants.CENTER);
        player1Container.add(scoreLabel1, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        actionButton = new JButton("Action");
        Dimension buttonSize = new Dimension(actionButton.getPreferredSize().width, actionButton.getPreferredSize().height * 2);
        actionButton.setPreferredSize(buttonSize);
        buttonPanel.add(actionButton);

        swapAllButton = new JButton("Swap All");
        swapAllButton.setPreferredSize(buttonSize);
        buttonPanel.add(swapAllButton);

        radibautzButton = new JButton("Radibautz");
        radibautzButton.setPreferredSize(buttonSize);
        buttonPanel.add(radibautzButton);

        klopfenButton = new JButton("Klopfen");
        klopfenButton.setPreferredSize(buttonSize);
        buttonPanel.add(klopfenButton);

        skipPlayerButton = new JButton("SkipPlayer");
        skipPlayerButton.setPreferredSize(buttonSize);
        buttonPanel.add(skipPlayerButton);

        player1Container.add(buttonPanel, BorderLayout.SOUTH);

        player2CardsPanel = new JPanel();
        player2CardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel player2Container = new JPanel();
        player2Container.setLayout(new BorderLayout());
        scoreLabel2 = new JLabel("Score Player 2: 0", SwingConstants.CENTER);
        player2Container.add(scoreLabel2, BorderLayout.NORTH);
        player2Container.add(player2CardsPanel, BorderLayout.CENTER);

        player3CardsPanel = new JPanel();
        player3CardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel player3Container = new JPanel();
        player3Container.setLayout(new BorderLayout());
        scoreLabel3 = new JLabel("Score Player 3: 0", SwingConstants.CENTER);
        player3Container.add(scoreLabel3, BorderLayout.NORTH);
        player3Container.add(player3CardsPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("<html>Welcome to Radibautz!</html>", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Serif", Font.BOLD, 20));

        currentPlayerLabel = new JLabel("Current Player: Player 1", SwingConstants.CENTER);
        currentPlayerLabel.setFont(new Font("Serif", Font.BOLD, 20));

        roundCountLabel = new JLabel("Runde: 1", SwingConstants.CENTER);
        roundCountLabel.setFont(new Font("Serif", Font.BOLD, 20));

        klopfenLabel = new JLabel("", SwingConstants.CENTER);
        klopfenLabel.setFont(new Font("Serif", Font.BOLD, 20));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(currentPlayerLabel, BorderLayout.NORTH);
        centerPanel.add(statusLabel, BorderLayout.CENTER);
        centerPanel.add(roundCountLabel, BorderLayout.SOUTH);

        JPanel klopfenPanel = new JPanel();
        klopfenPanel.setLayout(new BorderLayout());
        klopfenPanel.add(klopfenLabel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        add(klopfenPanel, BorderLayout.WEST);
        add(boardPanel, BorderLayout.NORTH);
        add(player1Container, BorderLayout.SOUTH);
        add(player2Container, BorderLayout.WEST);
        add(player3Container, BorderLayout.EAST);

        setVisible(true);
    }

    /**
     * Update card panel.
     *
     * @param panel     the panel
     * @param cards     the cards
     * @param hideCards the hide cards
     */
    public void updateCardPanel(JPanel panel, List<String[]> cards, boolean hideCards) {
        panel.removeAll();
        for (String[] card : cards) {
            JLabel cardLabel = createCardLabel(card[0], hideCards);
            panel.add(cardLabel);
        }
    }

    private JLabel createCardLabel(String cardFilename, boolean hideCard) {
        String filename = hideCard ? JOKER_CARD : cardFilename;
        ImageIcon imageIcon = new ImageIcon(CARD_PATH + filename);
        int width = imageIcon.getIconWidth() * 30 / 100;
        int height = imageIcon.getIconHeight() * 30 / 100;
        Image scaledImage = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(scaledImage));
        label.setOpaque(true);
        label.setBackground(new Color(144, 238, 144, 50));
        return label;
    }

    /**
     * Gets player 1 panel.
     *
     * @return the player 1 panel
     */
    public JPanel getPlayer1Panel() {
        return player1Panel;
    }

    /**
     * Gets player 2 cards panel.
     *
     * @return the player 2 cards panel
     */
    public JPanel getPlayer2CardsPanel() {
        return player2CardsPanel;
    }

    /**
     * Gets player 3 cards panel.
     *
     * @return the player 3 cards panel
     */
    public JPanel getPlayer3CardsPanel() {
        return player3CardsPanel;
    }

    /**
     * Gets board cards panel.
     *
     * @return the board cards panel
     */
    public JPanel getBoardCardsPanel() {
        return boardCardsPanel;
    }

    /**
     * Gets score label 1.
     *
     * @return the score label 1
     */
    public JLabel getScoreLabel1() {
        return scoreLabel1;
    }

    /**
     * Gets score label 2.
     *
     * @return the score label 2
     */
    public JLabel getScoreLabel2() {
        return scoreLabel2;
    }

    /**
     * Gets score label 3.
     *
     * @return the score label 3
     */
    public JLabel getScoreLabel3() {
        return scoreLabel3;
    }

    /**
     * Gets score board label.
     *
     * @return the score board label
     */
    public JLabel getScoreBoardLabel() {
        return scoreBoardLabel;
    }

    /**
     * Gets status label.
     *
     * @return the status label
     */
    public JLabel getStatusLabel() {
        return statusLabel;
    }

    /**
     * Gets current player label.
     *
     * @return the current player label
     */
    public JLabel getCurrentPlayerLabel() {
        return currentPlayerLabel;
    }

    /**
     * Gets round count label.
     *
     * @return the round count label
     */
    public JLabel getRoundCountLabel() {
        return roundCountLabel;
    }

    /**
     * Gets klopfen label.
     *
     * @return the klopfen label
     */
    public JLabel getKlopfenLabel() {
        return klopfenLabel;
    }

    /**
     * Gets action button.
     *
     * @return the action button
     */
    public JButton getActionButton() {
        return actionButton;
    }

    /**
     * Gets swap all button.
     *
     * @return the swap all button
     */
    public JButton getSwapAllButton() {
        return swapAllButton;
    }

    /**
     * Gets radibautz button.
     *
     * @return the radibautz button
     */
    public JButton getRadibautzButton() {
        return radibautzButton;
    }

    /**
     * Gets klopfen button.
     *
     * @return the klopfen button
     */
    public JButton getKlopfenButton() {
        return klopfenButton;
    }

    /**
     * Gets skip player button.
     *
     * @return the skip player button
     */
    public JButton getSkipPlayerButton() {
        return skipPlayerButton;
    }
}
