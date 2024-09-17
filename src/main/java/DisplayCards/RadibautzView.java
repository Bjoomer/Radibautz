package DisplayCards;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.io.FileInputStream;
import java.util.Properties;
import java.io.IOException;

/**
 * The view class for the Radibautz game, responsible for the graphical user interface.
 */
public class RadibautzView extends JFrame {

    public static final String JOKER_CARD = "ChreguCard.png"; // Image for hidden cards

    public static String configPath = "";

    static {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(input);
            configPath = properties.getProperty("card_path", "src/Cards/"); // Fallback Pfad, falls der Eintrag nicht existiert
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Fehler beim Laden der Konfigurationsdatei.", e);
        }
    }

    public static final String CARD_PATH = configPath; // Path to the card images


    private JPanel player1Panel; // Panel for Player 1's cards
    private JPanel player2CardsPanel; // Panel for Player 2's cards
    private JPanel player3CardsPanel; // Panel for Player 3's cards
    private JPanel boardCardsPanel; // Panel for the board's cards
    private JLabel scoreLabel1; // Label showing Player 1's score
    private JLabel scoreLabel2; // Label showing Player 2's score
    private JLabel scoreLabel3; // Label showing Player 3's score
    private JLabel scoreBoardLabel; // Label showing the board's score
    private JLabel statusLabel; // Label showing the game status messages
    private JLabel currentPlayerLabel; // Label showing the current player
    private JLabel roundCountLabel; // Label showing the current round number
    private JLabel klopfenLabel; // Label showing which player knocked
    private JButton actionButton; // Button for dealing new cards
    private JButton swapAllButton; // Button for swapping all cards
    private JButton radibautzButton; // Button for checking Radibautz
    private JButton klopfenButton; // Button for knocking
    private JButton skipPlayerButton; // Button for skipping player's turn

    /**
     * Instantiates a new Radibautz view, setting up the GUI layout.
     */
    public RadibautzView() {
        setTitle("Radibautz"); // Set window title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1500, 1000); // Set window size

        // Set up the board panel for the cards and score
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new BorderLayout());
        boardPanel.setBackground(new Color(205, 133, 63)); // Light brown background
        scoreBoardLabel = new JLabel("Score Board: 0", SwingConstants.CENTER);
        boardPanel.add(scoreBoardLabel, BorderLayout.NORTH);

        boardCardsPanel = new JPanel();
        boardCardsPanel.setLayout(new FlowLayout());
        boardCardsPanel.setBackground(new Color(205, 133, 63));
        boardPanel.add(boardCardsPanel, BorderLayout.CENTER);

        // Set up Player 1's panel
        player1Panel = new JPanel();
        player1Panel.setLayout(new FlowLayout());

        JPanel player1Container = new JPanel();
        player1Container.setLayout(new BorderLayout());
        player1Container.add(player1Panel, BorderLayout.CENTER);

        scoreLabel1 = new JLabel("Score Player 1: 0", SwingConstants.CENTER);
        player1Container.add(scoreLabel1, BorderLayout.NORTH);

        // Add buttons for Player 1's actions
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

        // Set up Player 2's panel
        player2CardsPanel = new JPanel();
        player2CardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel player2Container = new JPanel();
        player2Container.setLayout(new BorderLayout());
        scoreLabel2 = new JLabel("Score Player 2: 0", SwingConstants.CENTER);
        player2Container.add(scoreLabel2, BorderLayout.NORTH);
        player2Container.add(player2CardsPanel, BorderLayout.CENTER);

        // Set up Player 3's panel
        player3CardsPanel = new JPanel();
        player3CardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel player3Container = new JPanel();
        player3Container.setLayout(new BorderLayout());
        scoreLabel3 = new JLabel("Score Player 3: 0", SwingConstants.CENTER);
        player3Container.add(scoreLabel3, BorderLayout.NORTH);
        player3Container.add(player3CardsPanel, BorderLayout.CENTER);

        // Set up labels for game status
        statusLabel = new JLabel("<html>Welcome to Radibautz!</html>", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Serif", Font.BOLD, 20));

        currentPlayerLabel = new JLabel("Current Player: Player 1", SwingConstants.CENTER);
        currentPlayerLabel.setFont(new Font("Serif", Font.BOLD, 20));

        roundCountLabel = new JLabel("Runde: 1", SwingConstants.CENTER);
        roundCountLabel.setFont(new Font("Serif", Font.BOLD, 20));

        klopfenLabel = new JLabel("", SwingConstants.CENTER);
        klopfenLabel.setFont(new Font("Serif", Font.BOLD, 20));

        // Set up center and klopfen panels
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(currentPlayerLabel, BorderLayout.NORTH);
        centerPanel.add(statusLabel, BorderLayout.CENTER);
        centerPanel.add(roundCountLabel, BorderLayout.SOUTH);

        JPanel klopfenPanel = new JPanel();
        klopfenPanel.setLayout(new BorderLayout());
        klopfenPanel.add(klopfenLabel, BorderLayout.CENTER);

        // Add all components to the main frame
        add(centerPanel, BorderLayout.CENTER);
        add(klopfenPanel, BorderLayout.WEST);
        add(boardPanel, BorderLayout.NORTH);
        add(player1Container, BorderLayout.SOUTH);
        add(player2Container, BorderLayout.WEST);
        add(player3Container, BorderLayout.EAST);

        setVisible(true); // Make the window visible
    }

    /**
     * Updates the specified card panel with the given cards.
     *
     * @param panel     the panel to update
     * @param cards     the list of cards to display
     * @param hideCards whether to hide the cards (show back side)
     */
    public void updateCardPanel(JPanel panel, List<String[]> cards, boolean hideCards) {
        panel.removeAll(); // Clear existing components
        for (String[] card : cards) {
            JLabel cardLabel = createCardLabel(card[0], hideCards); // Create a label for each card
            panel.add(cardLabel); // Add the label to the panel
        }
    }

    /**
     * Creates a JLabel for a card, showing either the front or back.
     *
     * @param cardFilename the filename of the card image
     * @param hideCard     whether to hide the card (show back side)
     * @return the JLabel representing the card
     */
    private JLabel createCardLabel(String cardFilename, boolean hideCard) {
        String filename = hideCard ? JOKER_CARD : cardFilename; // Show front or back of the card
        ImageIcon imageIcon = new ImageIcon(CARD_PATH + filename);
       //int width = imageIcon.getIconWidth() * 30 / 100;
       //int height = imageIcon.getIconHeight() * 30 / 100;

        int width = 150;
        int height = 217;

        Image scaledImage = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(scaledImage));
        label.setOpaque(true);
        label.setBackground(new Color(144, 238, 144, 50)); // Light green background
        return label;
    }

    // Getter methods for various UI components
    public JPanel getPlayer1Panel() { return player1Panel; }
    public JPanel getPlayer2CardsPanel() { return player2CardsPanel; }
    public JPanel getPlayer3CardsPanel() { return player3CardsPanel; }
    public JPanel getBoardCardsPanel() { return boardCardsPanel; }
    public JLabel getScoreLabel1() { return scoreLabel1; }
    public JLabel getScoreLabel2() { return scoreLabel2; }
    public JLabel getScoreLabel3() { return scoreLabel3; }
    public JLabel getScoreBoardLabel() { return scoreBoardLabel; }
    public JLabel getStatusLabel() { return statusLabel; }
    public JLabel getCurrentPlayerLabel() { return currentPlayerLabel; }
    public JLabel getRoundCountLabel() { return roundCountLabel; }
    public JLabel getKlopfenLabel() { return klopfenLabel; }
    public JButton getActionButton() { return actionButton; }
    public JButton getSwapAllButton() { return swapAllButton; }
    public JButton getRadibautzButton() { return radibautzButton; }
    public JButton getKlopfenButton() { return klopfenButton; }
    public JButton getSkipPlayerButton() { return skipPlayerButton; }
}